package com.lexinda.veryrule.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author lexinda
 *
 */
public class RuleProxyHandler implements InvocationHandler {

	Object target;
	IRuleListener ruleListener;

	public RuleProxyHandler(Object target, IRuleListener ruleListener) {
		this.target = target;
		this.ruleListener = ruleListener;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object result = method.invoke(target, args);
		if (ruleListener != null) {
			ruleListener.ruleListener(target, args, result);
		}
		return result;
	}

}
