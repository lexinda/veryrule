package com.lexinda.veryrule;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.RuleInvoker;
import com.lexinda.veryrule.core.RuleProxyHandler;
import com.lexinda.veryrule.core.interfaces.IRuleAction;
import com.lexinda.veryrule.core.interfaces.IRuleCondation;
import com.lexinda.veryrule.core.interfaces.IRuleListener;
import com.lexinda.veryrule.core.interfaces.IRuleReduce;
import com.lexinda.veryrule.core.interfaces.IRuleResultCondation;

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
		if(builder.ruleActionMap.isEmpty()&&builder.ruleResultCondationMap.isEmpty()&&builder.ruleCondationMap.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	public Map<String,Class<?>> allRule() {
		Map<String,Class<?>> allRule = new HashMap<String,Class<?>>();
		allRule.putAll(builder.ruleActionMap);
		allRule.putAll(builder.ruleResultCondationMap);
		allRule.putAll(builder.ruleCondationMap);
		return allRule;
	}
	
	/**
	 * 
	 * @param param 入参
	 * @param rule  需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, R rule) throws Exception {
		return fireWithCondation(param, Arrays.asList(rule), false,null,null);
	}

	/**
	 * 
	 * @param param 入参
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, List<R> rules) throws Exception {
		return fireWithCondation(param, rules, false,null,null);
	}
	
	/**
	 * 
	 * @param param 入参
	 * @param rules 需要执行的规则
	 * @param ruleReduce 聚合处理
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, List<R> rules,IRuleReduce ruleReduce) throws Exception {
		return fireWithCondation(param, rules, false,null,ruleReduce);
	}
	
	/**
	 * 
	 * @param <R>
	 * @param param 入参
	 * @param rules 需要执行的规则
	 * @param threadPoolExecutor 线程池
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, List<R> rules,ThreadPoolExecutor threadPoolExecutor) throws Exception {
		return fireWithCondation(param, rules, false,threadPoolExecutor,null);
	}
	
	/**
	 * 
	 * @param <R>
	 * @param param 入参
	 * @param rules 需要执行的规则
	 * @param threadPoolExecutor 线程池
	 * @param ruleReduce 聚合处理
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, List<R> rules,ThreadPoolExecutor threadPoolExecutor,IRuleReduce ruleReduce) throws Exception {
		return fireWithCondation(param, rules, false,threadPoolExecutor,ruleReduce);
	}
	
	/**
	 * 
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fireTest(List<R> rules) throws Exception {
		return fireWithCondation(new HashMap<String, Object>(), rules, true,null,null);
	}
	
	/**
	 * 
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fireTest(List<R> rules,IRuleReduce ruleReduce) throws Exception {
		return fireWithCondation(new HashMap<String, Object>(), rules, true,null,ruleReduce);
	}

	/**
	 * 
	 * @param param 入参
	 * @param rules     需要执行的规则
	 * @param isTest    是否测试
	 * @param threadPoolExecutor    线程池
	 * @return
	 * @throws Exception
	 */
	private <R extends RuleBo> RuleResult fireWithCondation(Map<String, Object> param, List<R> rules, boolean isTest,ThreadPoolExecutor threadPoolExecutor,IRuleReduce ruleReduce)
			throws Exception {
		Map<R, Object> condations = new LinkedHashMap<R, Object>();
		Map<R, IRuleResultCondation> ruleResultCondations = new LinkedHashMap<R, IRuleResultCondation>();
		Map<R, IRuleAction> ruleActions = new LinkedHashMap<R, IRuleAction>();
		if (rules != null) {
			rules.stream().forEach(rule -> {
				int ruleType = rule.getRuleType()==null?1:rule.getRuleType();
				if(ruleType==1) {
					IRuleCondation condationAction = (IRuleCondation) builder.ruleCondationMap.get(rule.getRuleCode());
					if(condationAction == null) {
						throw new RuntimeException(rule.getRuleCode()+" rule class not found");
					}
					condations.put(rule, condationAction);
				}else if(ruleType==2) {
					IRuleResultCondation resultCondationAction = (IRuleResultCondation) builder.ruleResultCondationMap.get(rule.getRuleCode());
					if(resultCondationAction == null) {
						throw new RuntimeException(rule.getRuleCode()+" rule class not found");
					}
					/*
					 * 有返回值的规则条件默认并行执行
					 */
					if(rule.getRuleAsyn() == 1) {
						ruleResultCondations.put(rule, resultCondationAction);
					}else {
						condations.put(rule, resultCondationAction);
					}
				}else if(ruleType==3) {
					IRuleAction ruleAction = (IRuleAction) builder.ruleActionMap.get(rule.getRuleCode());
					if(ruleAction == null) {
						throw new RuntimeException(rule.getRuleCode()+" rule class not found");
					}
					ruleActions.put(rule, ruleAction);
				}
			});
			return getResult(param, condations, ruleResultCondations, ruleActions, isTest,threadPoolExecutor,ruleReduce);
		} else {
			return null;
		}
	}
	
	public VeryRule listener(Class<? extends IRuleListener> clazz) throws Exception {
		IRuleListener ruleListener = clazz.getDeclaredConstructor().newInstance();
		builder.ruleListener = ruleListener;
		builder.ruleProxyHandler.setRuleListener(builder.ruleListener);
		builder.ruleListener.initRule();
		return builder;
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
		return builder;
	}
	
	public VeryRule resultCondation(Class<? extends IRuleResultCondation> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Rule.class)) {
			Rule rule = clazz.getAnnotation(Rule.class);
			if (builder.ruleResultCondationMap.get(rule.code()) == null) {
				builder.ruleResultCondationMap.put(rule.code(), clazz.getDeclaredConstructor().newInstance());
			}
		} else {
			throw new Exception(clazz.getName() + " is not annotation present rule ");
		}
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
					if(RuleType.CONDATION == rule.type()) {
						try {
							builder.condation((Class<? extends IRuleCondation>) clazz);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(RuleType.RESULT_CONDATION == rule.type()) {
						try {
							builder.resultCondation((Class<? extends IRuleResultCondation>) clazz);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(RuleType.ACTION == rule.type()) {
						try {
							builder.action((Class<? extends IRuleAction>) clazz);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						try {
							builder.condation((Class<? extends IRuleCondation>) clazz);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
		return builder;
	}
	
	private static void init() {
		builder.ruleActionMap = new HashMap<String, IRuleResultCondation>();
		builder.ruleResultCondationMap = new HashMap<String, IRuleAction>();
		builder.ruleCondationMap = new HashMap<String, IRuleCondation>();
		builder.ruleInvoker = new RuleInvoker();
		RuleProxyHandler ruleProxyHandler = new RuleProxyHandler(null);
		builder.ruleProxyHandler = ruleProxyHandler;
	}
	
}
