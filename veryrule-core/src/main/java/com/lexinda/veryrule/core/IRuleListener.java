package com.lexinda.veryrule.core;

/**
 * 
 * @author lexinda
 *
 */
public interface IRuleListener {
	/**
	 * 初始化完成
	 * 
	 * @param proxy
	 */
	void initRule(Object proxy);

	/**
	 * 单个规则执行完成
	 * 
	 * @param proxy
	 */
	void ruleListener(Object proxy, Object[] args, Object result);
}
