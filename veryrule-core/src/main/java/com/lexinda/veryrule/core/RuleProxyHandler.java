package com.lexinda.veryrule.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.lexinda.veryrule.core.interfaces.IRuleAction;
import com.lexinda.veryrule.core.interfaces.IRuleListener;

/**
 * 
 * @author lexinda
 *
 */
public class RuleProxyHandler implements InvocationHandler,Cloneable {

	Object target;
	IRuleListener ruleListener;

	public RuleProxyHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object result = method.invoke(target, args);
		if (ruleListener != null) {
			ruleListener.ruleEnd(args, result);
		}
		return result;
	}

	public IRuleListener getRuleListener() {
		return ruleListener;
	}

	public void setRuleListener(IRuleListener ruleListener) {
		this.ruleListener = ruleListener;
	}
	
	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	@Override
	public RuleProxyHandler clone(){
		RuleProxyHandler obj = null;
        try {
            obj = (RuleProxyHandler)super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return obj;
    }

}
