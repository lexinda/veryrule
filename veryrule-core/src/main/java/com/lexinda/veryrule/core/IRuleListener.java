package com.lexinda.veryrule.core;

import java.util.EventListener;

/**
 * 
 * @author lexinda
 *
 */
public interface IRuleListener extends EventListener{
	/**
	 * 初始化完成
	 * 
	 * @param proxy
	 */
	void initRule();

	/**
	 * 规则执行完成
	 * 
	 * @param proxy
	 */
	void ruleListener(Object[] args, Object result);

}
