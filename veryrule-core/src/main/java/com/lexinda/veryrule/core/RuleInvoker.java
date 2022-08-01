package com.lexinda.veryrule.core;
/**
 * 
 * @author lexinda
 *
 */

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.lexinda.veryrule.bo.RuleBo;

public class RuleInvoker extends RuleInvokerAbst{
	
	@Override
	public <R extends RuleBo> void doRuleCondation(Map<String, Object> param, Map<R, IRuleCondation> ruleCondations,IRuleListener ruleListener,boolean isTest) {
		ruleCondations.entrySet().stream().forEach(condation->{
			try {
				RuleProxyHandler ruleHandler = new RuleProxyHandler(condation.getValue(),ruleListener);
				if(isTest) {
					Map<String, Object> testResult = getTestResult(ruleHandler,condation.getKey());
					if(testResult!=null) {
						ruleResult.addResultAll(testResult);
					}
				}else {
					IRuleCondation subject = (IRuleCondation) Proxy.newProxyInstance(IRuleCondation.class.getClassLoader(), new Class<?>[] {IRuleCondation.class}, ruleHandler);
					subject.contation(param,condation.getKey());
				}
			}catch(Exception e) {
				String msg = e.getCause().getMessage();
				if(msg.equals("")||msg==null) {
					msg = e.getMessage();
				}
				throw new RuntimeException(msg);
			}
		});
	}

	@Override
	public <R extends RuleBo> void doRuleResultCondation(Map<String, Object> param, Map<R, IRuleResultCondation> ruleCondations,IRuleListener ruleListener,boolean isTest,ThreadPoolExecutor threadPoolExecutor) {
		Map<String, Object> condationResultMap = new HashMap<String, Object>();
		List<Future> list = new ArrayList<Future>();
		ruleCondations.entrySet().stream().forEach(condation->{
			try {
				RuleProxyHandler ruleHandler = new RuleProxyHandler(condation.getValue(),ruleListener);
				if(isTest) {
					Map<String, Object> testResult = getTestResult(ruleHandler,condation.getKey());
					if(testResult!=null) {
						ruleResult.addResultAll(testResult);
					}
				}else {
					IRuleResultCondation subject = (IRuleResultCondation) Proxy.newProxyInstance(IRuleResultCondation.class.getClassLoader(), new Class<?>[] {IRuleResultCondation.class}, ruleHandler);
					if(threadPoolExecutor!=null) {
						Callable c = new RuleResultCondationCallable(subject,param,condation.getKey());
						Future f = threadPoolExecutor.submit(c);
						list.add(f);
					}else {
						Map<String, Object> condationResult = (Map<String, Object>) subject.contation(param,condation.getKey());
						if (condationResult != null) {
							condationResultMap.putAll(condationResult);
						}
					}
				}
			}catch(Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		});
		if(threadPoolExecutor!=null) {
			for (Future f : list) {
				try {
					if(f.get()!=null) {
						condationResultMap.putAll((Map<String, Object>)f.get());
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		ruleResult.setCondationResult(condationResultMap);
	}

	@Override
	public <R extends RuleBo> void doRuleAction(Map<String, Object> param,
			Map<R, IRuleAction> ruleResultActions,IRuleListener ruleListener,boolean isTest) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ruleResultActions.entrySet().stream().forEach(action->{
			try {
				RuleProxyHandler ruleHandler = new RuleProxyHandler(action.getValue(),ruleListener);
				if(isTest) {
					Map<String, Object> testResult = getTestResult(ruleHandler, action.getKey());
					if(testResult!=null) {
						ruleResult.addResultAll(testResult);
					}
				}else {
					IRuleAction subject = (IRuleAction) Proxy.newProxyInstance(IRuleAction.class.getClassLoader(), new Class<?>[] {IRuleAction.class}, ruleHandler);
					Map<String, Object> resultData = (Map<String, Object>)subject.action(param, ruleResult.getCondationResult(), action.getKey());
					if (resultData != null) {
						resultMap.putAll(resultData);
					}
				}
			}catch(Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		});
		if(!resultMap.isEmpty()) {
			ruleResult.setResult(resultMap);
		}
	}
	
	public <R extends RuleBo> Map<String,Object> getTestResult(RuleProxyHandler ruleHandler,R rule) {
		Map<String, Object> testResult = new HashMap<String, Object>();
		try {
			IRuleTest subject = (IRuleTest) Proxy.newProxyInstance(IRuleTest.class.getClassLoader(), new Class<?>[] {IRuleTest.class}, ruleHandler);
			testResult = (Map<String, Object>) subject.ruleTest(rule);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return testResult;
	}

}
