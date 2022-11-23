package com.lexinda.veryrule.core;

import java.util.Map;
import java.util.concurrent.Callable;

import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.core.interfaces.IRuleResultCondation;

public class RuleResultCondationCallable<R extends RuleBo> implements Callable<Object>{

	private IRuleResultCondation ruleResultCondation;
	
	private Map<String, Object> param;
	
	private R rule;
	
	public RuleResultCondationCallable(IRuleResultCondation ruleResultCondation,Map<String, Object> param, R rule) {
		super();
		this.ruleResultCondation = ruleResultCondation;
		this.param = param;
		this.rule = rule;
	}

	@Override
	public Object call() throws Exception {
		return this.ruleResultCondation.contation(param, rule);
	}

}
