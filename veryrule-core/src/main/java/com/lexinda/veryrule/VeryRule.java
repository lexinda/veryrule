package com.lexinda.veryrule;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleListener;
import com.lexinda.veryrule.core.IRuleResultAction;
import com.lexinda.veryrule.core.RuleResult;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRule extends RuleEngine {

	private static volatile VeryRule builder;

	public static VeryRule builder() {
		if (builder == null) {
			synchronized (VeryRule.class) {
				if (builder == null) {
					builder = new VeryRule();
					init();
				}
			}
		}
		return builder;
	}
	
	public boolean isEmpty() {
		if(builder.ruleActionMap.isEmpty()&&builder.ruleResultActionMap.isEmpty()&&builder.ruleCondationMap.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isInit() {
		if(actionInit||resultActionInit||condationInit) {
			return true;
		}else {
			return false;
		}
	}
	
	public Map<String,Class<?>> allRule() {
		Map<String,Class<?>> allRule = new HashMap<String,Class<?>>();
		allRule.putAll(builder.ruleActionMap);
		allRule.putAll(builder.ruleResultActionMap);
		allRule.putAll(builder.ruleCondationMap);
		return allRule;
	}
	
	/**
	 * 
	 * @param <R>
	 * @param param 入参
	 * @param rule  需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, R rule) throws Exception {
		return fireWithCondation(param, Arrays.asList(rule), false);
	}

	/**
	 * 
	 * @param <R>
	 * @param param 入参
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, List<R> rules) throws Exception {
		return fireWithCondation(param, rules, false);
	}

	/**
	 * 
	 * @param <R>
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fireTest(List<R> rules) throws Exception {
		return fireWithCondation(new HashMap<String, Object>(), rules, true);
	}

	/**
	 * 
	 * @param <R>
	 * @param isTest    是否测试
	 * @param condation 前置条件
	 * @param rules     需要执行的规则
	 * @return
	 * @throws Exception
	 */
	private <R extends RuleBo> RuleResult fireWithCondation(Map<String, Object> param, List<R> rules, boolean isTest)
			throws Exception {
		List<IRuleCondation> ruleCondations = new ArrayList<IRuleCondation>();
		Map<R, IRuleAction> ruleActions = new LinkedHashMap<R, IRuleAction>();
		Map<R, IRuleResultAction> ruleResultActions = new LinkedHashMap<R, IRuleResultAction>();
		if (rules != null) {
			Set<String> condationAnnotationList = new HashSet<String>();
			rules.stream().forEach(rule -> {
				if (rule.getRuleCondations() != null && rule.getRuleCondations().size() > 0) {
					rule.getRuleCondations().stream().filter(condation -> condation != null && !"".equals(condation))
							.forEach(condation -> condationAnnotationList.add(condation));
				}
			});
			rules.stream().forEach(rule -> {
				IRuleAction ruleAction = (IRuleAction) builder.ruleActionMap.get(rule.getRuleCode());
				if (ruleAction != null) {
					ruleActions.put(rule, ruleAction);
					List<String> condationAnnotations = getCondationList(ruleAction.getClass());
					if (condationAnnotations != null && condationAnnotations.size() > 0) {
						condationAnnotationList.addAll(condationAnnotations);
					}
				}
				IRuleResultAction ruleResultAction = (IRuleResultAction) builder.ruleResultActionMap
						.get(rule.getRuleCode());
				if (ruleResultAction != null) {
					ruleResultActions.put(rule, ruleResultAction);
					List<String> condationAnnotations = getCondationList(ruleResultAction.getClass());
					if (condationAnnotations != null) {
						condationAnnotationList.addAll(condationAnnotations);
					}
				}
			});
			condationAnnotationList.stream().filter(condation -> condation != null && !"".equals(condation))
					.forEach(condation -> ruleCondations.add((IRuleCondation) builder.ruleCondationMap.get(condation)));
			return getResult(param, ruleCondations, ruleActions, ruleResultActions, isTest);
		} else {
			return null;
		}
	}
	
	public VeryRule action(Class<? extends IRuleAction> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Rule.class)) {
			Rule rule = clazz.getAnnotation(Rule.class);
			if (builder.ruleActionMap.get(rule.code()) == null) {
				builder.ruleActionMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
			}
		} else {
			throw new Exception(clazz.getName() + " is not annotation present rule ");
		}
		actionInit = true;
		return builder;
	}
	
	public VeryRule resultAction(Class<? extends IRuleResultAction> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Rule.class)) {
			Rule rule = clazz.getAnnotation(Rule.class);
			if (builder.ruleResultActionMap.get(rule.code()) == null) {
				builder.ruleResultActionMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
			}
		} else {
			throw new Exception(clazz.getName() + " is not annotation present rule ");
		}
		resultActionInit = true;
		return builder;
	}

	public VeryRule condation(Class<? extends IRuleCondation> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Rule.class)) {
			Rule rule = clazz.getAnnotation(Rule.class);
			if (builder.ruleCondationMap.get(rule.code()) == null) {
				builder.ruleCondationMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
			}
		} else {
			throw new Exception(clazz.getName() + " is not annotation present rule ");
		}
		condationInit = true;
		return builder;
	}
	
	public VeryRule rulePackage(String rulePackage) throws Exception {
		if(!"".equals(rulePackage)&&rulePackage!=null) {
			String path = rulePackage.replace(".", "/");
			Enumeration<URL> urls =Thread.currentThread().getContextClassLoader().getResources(path);
			List<Class<?>> classList = new ArrayList<Class<?>>();
			while(urls.hasMoreElements()) {
				URL url= urls.nextElement();
				String protol = url.getProtocol();
				if("file".equals(protol)) {
					String pathStr = URLDecoder.decode(url.getFile(),"UTF-8");
					getClassByPath(pathStr,classList);
				}
			}
			if(classList.size()==0) {
				throw new Exception(" builder rulePackage have no ruleClass ");
			}
			classList.stream().forEach(clazz->{
				if (clazz.isAnnotationPresent(Rule.class)) {
					Rule rule = clazz.getAnnotation(Rule.class);
					if(RuleType.CONDATION.equals(rule.type())) {
						try {
							builder.condation((Class<? extends IRuleCondation>) clazz);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(RuleType.ACTION.equals(rule.type())) {
						try {
							builder.action((Class<? extends IRuleAction>) clazz);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(RuleType.RESULT_ACTION.equals(rule.type())) {
						try {
							builder.resultAction((Class<? extends IRuleResultAction>) clazz);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						try {
							builder.action((Class<? extends IRuleAction>) clazz);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
		actionInit = true;
		resultActionInit = true;
		condationInit = true;
		return builder;
	}
	
	public VeryRule listener(Class<? extends IRuleListener> clazz) throws Exception {
		builder.ruleListener = clazz.getDeclaredConstructor().newInstance();
		return builder;
	}
	
	private static void init() {
		builder.ruleActionMap = new HashMap<String, IRuleAction>();
		builder.ruleResultActionMap = new HashMap<String, IRuleResultAction>();
		builder.ruleCondationMap = new HashMap<String, IRuleCondation>();
	}
	
}
