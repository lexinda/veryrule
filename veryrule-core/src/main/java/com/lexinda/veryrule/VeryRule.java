package com.lexinda.veryrule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleResultAction;
import com.lexinda.veryrule.core.IRuleListener;
import com.lexinda.veryrule.core.RuleResult;

/**
 * 
 * @author zhumengle
 *
 */
public class VeryRule extends RuleEngine {

	

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * 
	 * @param <R>
	 * @param param        入参
	 * @param rule 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String,Object> param, R rule) throws Exception {
		return fireWithCondation(param, Arrays.asList(rule),false);
	}
	
	/**
	 * 
	 * @param <R>
	 * @param param        入参
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String,Object> param, List<R> rules) throws Exception {
		return fireWithCondation(param, rules,false);
	}
	
	/**
	 * 
	 * @param <R>
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fireTest(List<R> rules) throws Exception {
		return fireWithCondation(new HashMap<String,Object>(), rules,true);
	}
	
	/**
	 * 
	 * @param <R>
	 * @param isTest 是否测试
	 * @param condation 前置条件
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	private <R extends RuleBo> RuleResult fireWithCondation(Map<String,Object> param, List<R> rules,boolean isTest)
			throws Exception {
		List<IRuleCondation> ruleCondations = new ArrayList<IRuleCondation>();
		Map<R, IRuleAction> ruleActions = new LinkedHashMap<R, IRuleAction>();
		Map<R, IRuleResultAction> ruleResultActions = new LinkedHashMap<R, IRuleResultAction>();
		if(rules!=null) {
			Set<String> condationAnnotationList = new HashSet<String>();
			rules.stream().forEach(rule->{
				if(rule.getRuleCondations()!=null&&rule.getRuleCondations().size()>0) {
					rule.getRuleCondations().stream().filter(condation->condation!=null&&!"".equals(condation)).forEach(condation->condationAnnotationList.add(condation));
				}
			});
			rules.stream().forEach(rule->{
				IRuleAction ruleAction = (IRuleAction) this.ruleActionMap.get(rule.getRuleCode());
				if(ruleAction!=null) {
					ruleActions.put(rule, ruleAction);
					List<String> condationAnnotations = getCondationList(ruleAction.getClass());
					if(condationAnnotations!=null&&condationAnnotations.size()>0) {
						condationAnnotationList.addAll(condationAnnotations);
					}
				}
				IRuleResultAction ruleResultAction = (IRuleResultAction) this.ruleResultActionMap.get(rule.getRuleCode());
				if(ruleResultAction!=null) {
					ruleResultActions.put(rule, ruleResultAction);
					List<String> condationAnnotations = getCondationList(ruleResultAction.getClass());
					if(condationAnnotations!=null) {
						condationAnnotationList.addAll(condationAnnotations);
					}
				}
			});
			condationAnnotationList.stream().filter(condation->condation!=null&&!"".equals(condation)).forEach(condation->ruleCondations.add((IRuleCondation) this.ruleCondationMap.get(condation)));
			return getResult(param,ruleCondations,ruleActions,ruleResultActions,isTest);
		}else {
			return null;
		}
	}

	public static class Builder {

		private Map<String, IRuleAction> ruleActionMap = new HashMap<String, IRuleAction>();
		private Map<String, IRuleResultAction> ruleResultActionMap = new HashMap<String, IRuleResultAction>();
		private Map<String, IRuleCondation> ruleCondationMap = new HashMap<String, IRuleCondation>();
		private IRuleListener ruleListener;
		public Builder action(Class<? extends IRuleAction> clazz) throws Exception {
			if (clazz.isAnnotationPresent(Rule.class)) {
				Rule rule = clazz.getAnnotation(Rule.class);
				if (ruleActionMap.get(rule.code()) == null) {
					ruleActionMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
				}
			} else {
				throw new Exception(clazz.getName() + " is not annotation present rule ");
			}
			return this;
		}
		
		public Builder listener(Class<? extends IRuleListener> clazz) throws Exception{
			ruleListener = clazz.getDeclaredConstructor().newInstance();
			return this;
		}
		
		public Builder resultAction(Class<? extends IRuleResultAction> clazz) throws Exception {
			if (clazz.isAnnotationPresent(Rule.class)) {
				Rule rule = clazz.getAnnotation(Rule.class);
				if (ruleResultActionMap.get(rule.code()) == null) {
					ruleResultActionMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
				}
			} else {
				throw new Exception(clazz.getName() + " is not annotation present rule ");
			}
			return this;
		}

		public Builder condation(Class<? extends IRuleCondation> clazz) throws Exception {
			if (clazz.isAnnotationPresent(Rule.class)) {
				Rule rule = clazz.getAnnotation(Rule.class);
				if (ruleCondationMap.get(rule.code()) == null) {
					ruleCondationMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
				}
			} else {
				throw new Exception(clazz.getName() + " is not annotation present rule ");
			}
			return this;
		}

		public VeryRule build() {
			VeryRule vertRuleFactory = new VeryRule();
			vertRuleFactory.setRuleActionMap(this.ruleActionMap);
			vertRuleFactory.setRuleResultActionMap(this.ruleResultActionMap);
			vertRuleFactory.setRuleCondationMap(this.ruleCondationMap);
			vertRuleFactory.setRuleListener(ruleListener);
			return vertRuleFactory;
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

}
