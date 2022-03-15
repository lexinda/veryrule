package com.lexinda.veryrule.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.lexinda.veryrule.VeryRule;

/**
* @author zhumengle
* @version 创建时间：2022-3-14 9:31:44
* 类说明
*/
public class RuleProxyHandler implements InvocationHandler {
	
	Object target;
	IRuleListener ruleListener;

	public RuleProxyHandler(Object target,IRuleListener ruleListener) {
		this.target = target;
		this.ruleListener = ruleListener;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// TODO Auto-generated method stub
		Object result = method.invoke(target, args);
		if(ruleListener!=null) {
			ruleListener.ruleListener(target, args);
		}
		return result;
	}

}
