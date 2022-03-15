package com.lexinda.veryrule.core;
/**
* @author zhumengle
* @version 创建时间：2022-2-23 11:32:18
* 类说明
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
	public void doRuleCondation(Map<String, Object> param, List<IRuleCondation> ruleCondations,IRuleListener ruleListener,boolean isTest) {
		Map<String, Object> condationResultMap = new HashMap<String, Object>();
		ruleCondations.stream().forEach(condation->{
			try {
				if(isTest) {
					Rule rule = condation.getClass().getAnnotation(Rule.class);
					ruleResult.addResult(rule.code(),rule.name());
				}else {
					RuleProxyHandler ruleTestHandler = new RuleProxyHandler(condation,ruleListener);
					IRuleCondation subject = (IRuleCondation) Proxy.newProxyInstance(IRuleCondation.class.getClassLoader(), new Class<?>[] {IRuleCondation.class}, ruleTestHandler);
					Map<String, Object> condationResult = (Map<String, Object>) subject.contation(param);
					if (condationResult != null) {
						condationResultMap.putAll(condationResult);
					}
				}
				
			}catch(Exception e) {
				throw new RuntimeException(e.getMessage());
			}
			
		});
		ruleResult.setCondationResult(condationResultMap);
	}

	@Override
	public <R extends RuleBo> void doRuleAction(Map<String, Object> param, Map<R, IRuleAction> ruleActions,IRuleListener ruleListener,boolean isTest) {
		ruleActions.entrySet().stream().forEach(action->{
			try {
				if(isTest) {
					Rule rule = action.getValue().getClass().getAnnotation(Rule.class);
					ruleResult.addResult(rule.code(),rule.name());
				}else {
					RuleProxyHandler ruleTestHandler = new RuleProxyHandler(action.getValue(),ruleListener);
					IRuleAction subject = (IRuleAction) Proxy.newProxyInstance(IRuleAction.class.getClassLoader(), new Class<?>[] {IRuleAction.class}, ruleTestHandler);
					subject.action(param, ruleResult.getCondationResult(), action.getKey());
				}
			}catch(InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException().getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	@Override
	public <R extends RuleBo> void doRuleResultAction(Map<String, Object> param,
			Map<R, IRuleResultAction> ruleResultActions,IRuleListener ruleListener,boolean isTest) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ruleResultActions.entrySet().stream().forEach(action->{
			try {
				if(isTest) {
					Rule rule = action.getValue().getClass().getAnnotation(Rule.class);
					ruleResult.addResult(rule.code(),rule.name());
				}else {
					RuleProxyHandler ruleTestHandler = new RuleProxyHandler(action.getValue(),ruleListener);
					IRuleResultAction subject = (IRuleResultAction) Proxy.newProxyInstance(IRuleResultAction.class.getClassLoader(), new Class<?>[] {IRuleResultAction.class}, ruleTestHandler);
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


}
