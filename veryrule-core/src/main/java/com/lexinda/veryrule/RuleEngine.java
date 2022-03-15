package com.lexinda.veryrule;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleResultAction;
import com.lexinda.veryrule.core.RuleInvoker;
import com.lexinda.veryrule.core.IRuleListener;
import com.lexinda.veryrule.core.RuleResult;

public class RuleEngine<R extends RuleBo> {
	
	protected Map<String, IRuleAction> ruleActionMap;
	protected Map<String, IRuleResultAction> ruleResultActionMap;
	protected Map<String, IRuleCondation> ruleCondationMap;
	protected IRuleListener ruleListener;
	
	public List<String> getCondationList(Class<?> clazz){
		Annotation[]  annotations = clazz.getAnnotations();
		for(Annotation ruleAnnotation:annotations) {
			if(ruleAnnotation!=null&&ruleAnnotation instanceof Rule) {
				String[] ruleAnnotationCondations = ((Rule)ruleAnnotation).condations();
				return Arrays.asList(ruleAnnotationCondations).stream().filter(str->!str.equals("")&&str!=null).collect(Collectors.toList());
			}
		}
		return null;
	}

	public RuleResult getResult(Map<String, Object> param,List<IRuleCondation> ruleCondations,Map<R, IRuleAction> ruleActions,Map<R, IRuleResultAction> ruleResultActions,boolean isTest) throws Exception {
		RuleInvoker invoke = new RuleInvoker();
		invoke.doRuleCondation(param, ruleCondations,this.ruleListener,isTest);
		invoke.doRuleAction(param, ruleActions,this.ruleListener,isTest);
		invoke.doRuleResultAction(param, ruleResultActions,this.ruleListener,isTest);
		return invoke.getRuleResult();
	}
	
	public void action(Class<? extends IRuleAction> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Rule.class)) {
			Rule rule = clazz.getAnnotation(Rule.class);
			if (ruleActionMap.get(rule.code()) == null) {
				ruleActionMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
			}
		} else {
			throw new Exception(clazz.getName() + " is not annotation present rule ");
		}
	}
	
	public void resultAction(Class<? extends IRuleResultAction> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Rule.class)) {
			Rule rule = clazz.getAnnotation(Rule.class);
			if (ruleResultActionMap.get(rule.code()) == null) {
				ruleResultActionMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
			}
		} else {
			throw new Exception(clazz.getName() + " is not annotation present rule ");
		}
	}

	public void condation(Class<? extends IRuleCondation> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Rule.class)) {
			Rule rule = clazz.getAnnotation(Rule.class);
			if (ruleCondationMap.get(rule.code()) == null) {
				ruleCondationMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
			}
		} else {
			throw new Exception(clazz.getName() + " is not annotation present rule ");
		}
	}
	
	public Map<String, IRuleAction> getRuleActionMap() {
		return ruleActionMap;
	}

	public void setRuleActionMap(Map<String, IRuleAction> ruleActionMap) {
		this.ruleActionMap = ruleActionMap;
	}

	public Map<String, IRuleResultAction> getRuleResultActionMap() {
		return ruleResultActionMap;
	}

	public void setRuleResultActionMap(Map<String, IRuleResultAction> ruleResultActionMap) {
		this.ruleResultActionMap = ruleResultActionMap;
	}

	public Map<String, IRuleCondation> getRuleCondationMap() {
		return ruleCondationMap;
	}

	public void setRuleCondationMap(Map<String, IRuleCondation> ruleCondationMap) {
		this.ruleCondationMap = ruleCondationMap;
	}

	public IRuleListener getRuleListener() {
		return ruleListener;
	}

	public void setRuleListener(IRuleListener ruleListener) {
		this.ruleListener = ruleListener;
	}
}
