package com.lexinda.veryrule.core.interfaces;

import java.util.EventListener;
import java.util.Map;

import com.lexinda.veryrule.common.RuleResult;

/**
 * 
 * @author lexinda
 *
 */
public interface IRuleListener extends EventListener{
	/**
	 * 初始化完成
	 * 
	 */
	void initRule();
	
	/**
	 * 规则流开始执行
	 * 
	 */
	void ruleFlowStart(Map<String, Object> param);

	/**
	 * 单个规则执行完成
	 * 
	 * @param args
	 * @param result
	 */
	void ruleEnd(Object[] args, Object result);
	
	/**
	 * 规则流执行结束
	 * 
	 */
	void ruleFlowEnd(Map<String, Object> param,RuleResult result);

}
