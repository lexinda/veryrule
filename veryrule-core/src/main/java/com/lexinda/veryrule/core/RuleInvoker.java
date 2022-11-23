package com.lexinda.veryrule.core;

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
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.interfaces.IRuleAction;
import com.lexinda.veryrule.core.interfaces.IRuleCondation;
import com.lexinda.veryrule.core.interfaces.IRuleResultCondation;
import com.lexinda.veryrule.core.interfaces.IRuleTest;

public class RuleInvoker extends RuleInvokerAbst implements Cloneable {
	
	@Override
	public <R extends RuleBo> void doRuleCondation(Map<String, Object> param, Map<R, IRuleCondation> ruleCondations,RuleProxyHandler ruleProxyHandler,boolean isTest) {
		ruleCondations.entrySet().stream().forEach(condation->{
			try {
				RuleProxyHandler ruleHandler = ruleProxyHandler.clone();
				ruleHandler.setTarget(condation.getValue());
				if(isTest) {
					Map<String, Object> testResult = getTestResult(ruleHandler, condation.getKey());
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
	public <R extends RuleBo> void doRuleResultCondation(Map<String, Object> param, Map<R, IRuleResultCondation> ruleCondations,RuleProxyHandler ruleProxyHandler,boolean isTest,ThreadPoolExecutor threadPoolExecutor) {
		Map<String, Object> condationResultMap = new HashMap<String, Object>();
		if(threadPoolExecutor!=null) {
			List<Future> futurelist = new ArrayList<Future>();
			ruleCondations.entrySet().stream().forEach(condation->{
				try {
					RuleProxyHandler ruleHandler = ruleProxyHandler.clone();
					ruleHandler.setTarget(condation.getValue());
					if(isTest) {
						Map<String, Object> testResult = getTestResult(ruleHandler, condation.getKey());
						if(testResult!=null) {
							ruleResult.addResultAll(testResult);
						}
					}else {
						IRuleResultCondation subject = (IRuleResultCondation) Proxy.newProxyInstance(IRuleResultCondation.class.getClassLoader(), new Class<?>[] {IRuleResultCondation.class}, ruleHandler);
						Callable c = new RuleResultCondationCallable(subject,param,condation.getKey());
						Future f = threadPoolExecutor.submit(c);
						futurelist.add(f);
					}
				}catch(Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			});
			if(futurelist.size()>0) {
				for (Future f : futurelist) {
					try {
						if(f.get()!=null) {
							condationResultMap.putAll((Map<String, Object>)f.get());
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			ruleCondations.entrySet().stream().parallel().forEach(condation->{
				try {
					RuleProxyHandler ruleHandler = ruleProxyHandler.clone();
					ruleHandler.setTarget(condation.getValue());
					if(isTest) {
						Map<String, Object> testResult = getTestResult(ruleHandler, condation.getKey());
						if(testResult!=null) {
							ruleResult.addResultAll(testResult);
						}
					}else {
						IRuleResultCondation subject = (IRuleResultCondation) Proxy.newProxyInstance(IRuleResultCondation.class.getClassLoader(), new Class<?>[] {IRuleResultCondation.class}, ruleHandler);
						Map<String, Object> condationResult = (Map<String, Object>) subject.contation(param,condation.getKey());
						if (condationResult != null) {
							condationResultMap.putAll(condationResult);
						}
					}
				}catch(Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			});
		}
		ruleResult.setCondationResult(condationResultMap);
	}

	@Override
	public <R extends RuleBo> void doRuleAction(Map<String, Object> param,
			Map<R, IRuleAction> ruleResultActions,RuleProxyHandler ruleProxyHandler,boolean isTest) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ruleResultActions.entrySet().stream().forEach(action->{
			try {
				RuleProxyHandler ruleHandler = ruleProxyHandler.clone();
				ruleHandler.setTarget(action.getValue());
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
	
	@Override
	public Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		RuleInvoker ruleInvoker = (RuleInvoker) obj;
		ruleInvoker.ruleResult = (RuleResult) ruleInvoker.getRuleResult().clone();
		return obj;
	}
	
}
