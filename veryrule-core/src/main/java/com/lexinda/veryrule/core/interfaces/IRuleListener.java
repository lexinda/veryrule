package com.lexinda.veryrule.core.interfaces;

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
	 */
	void initRule();

	/**
	 * 规则执行完成
	 * 
	 * @param args
	 * @param result
	 */
	void ruleListener(Object[] args, Object result);

}
