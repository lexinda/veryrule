package com.lexinda.veryrule.core;
/**
 * 
 * @author lexinda
 *
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;

public class RuleInvoker extends RuleInvokerAbst{
	
	@Override
	public <R extends RuleBo> void doRuleCondation(Map<String, Object> param, Map<R, List<IRuleCondation>> ruleCondations,IRuleListener ruleListener,boolean isTest) {
		ruleCondations.entrySet().stream().forEach(condation->{
			try {
				condation.getValue().stream().forEach(condationAction->{
					try {
						RuleProxyHandler ruleHandler = new RuleProxyHandler(condationAction,ruleListener);
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
						throw new RuntimeException(e.getMessage());
					}
				});
			}catch(Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	@Override
	public <R extends RuleBo> void doRuleResultCondation(Map<String, Object> param, Map<R, List<IRuleResultCondation>> ruleCondations,IRuleListener ruleListener,boolean isTest) {
		Map<String, Object> condationResultMap = new HashMap<String, Object>();
		ruleCondations.entrySet().stream().forEach(condation->{
			try {
				condation.getValue().stream().forEach(condationAction->{
					try {
						RuleProxyHandler ruleHandler = new RuleProxyHandler(condationAction,ruleListener);
						if(isTest) {
							Map<String, Object> testResult = getTestResult(ruleHandler,condation.getKey());
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
			}catch(Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		});
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
