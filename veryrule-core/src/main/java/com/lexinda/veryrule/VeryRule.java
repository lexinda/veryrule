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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleListener;
import com.lexinda.veryrule.core.IRuleResultCondation;
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
	 * @param <R>
	 * @param param 入参
	 * @param rule  需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fire(Map<String, Object> param, R rule) throws Exception {
		return fireWithCondation(param, Arrays.asList(rule), false,null);
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
		return fireWithCondation(param, rules, false,null);
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
		return fireWithCondation(param, rules, false,threadPoolExecutor);
	}

	/**
	 * 
	 * @param <R>
	 * @param rules 需要执行的规则
	 * @return
	 * @throws Exception
	 */
	public <R extends RuleBo> RuleResult fireTest(List<R> rules) throws Exception {
		return fireWithCondation(new HashMap<String, Object>(), rules, true,null);
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
	private <R extends RuleBo> RuleResult fireWithCondation(Map<String, Object> param, List<R> rules, boolean isTest,ThreadPoolExecutor threadPoolExecutor)
			throws Exception {
		Map<R, IRuleCondation> ruleCondations = new LinkedHashMap<R, IRuleCondation>();
		Map<R, IRuleResultCondation> ruleResultCondations = new LinkedHashMap<R, IRuleResultCondation>();
		Map<R, IRuleAction> ruleActions = new LinkedHashMap<R, IRuleAction>();
		if (rules != null) {
			rules.stream().forEach(rule -> {
				int ruleType = rule.getRuleType()==null?1:rule.getRuleType();
				if(ruleType==1) {
					IRuleCondation condationAction = (IRuleCondation) builder.ruleCondationMap.get(rule.getRuleCode());
					if (condationAction != null) {
						ruleCondations.put(rule, condationAction);
					}
				}else if(ruleType==2) {
					IRuleResultCondation resultCondationAction = (IRuleResultCondation) builder.ruleResultCondationMap.get(rule.getRuleCode());
					if (resultCondationAction != null) {
						ruleResultCondations.put(rule, resultCondationAction);
					}
				}else if(ruleType==3) {
					IRuleAction ruleAction = (IRuleAction) builder.ruleActionMap.get(rule.getRuleCode());
					if (ruleAction != null) {
						ruleActions.put(rule, ruleAction);
					}
				}
			});
			return getResult(param, ruleCondations, ruleResultCondations, ruleActions, isTest,threadPoolExecutor);
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
	
	public VeryRule listener(Class<? extends IRuleListener> clazz) throws Exception {
		builder.ruleListener = clazz.getDeclaredConstructor().newInstance();
		if(builder.getRuleListener()!=null) {
			builder.getRuleListener().initRule(builder);
		}
		return builder;
	}
	
	private static void init() {
		builder.ruleActionMap = new HashMap<String, IRuleResultCondation>();
		builder.ruleResultCondationMap = new HashMap<String, IRuleAction>();
		builder.ruleCondationMap = new HashMap<String, IRuleCondation>();
	}
	
}
