package com.lexinda.veryrule.base.reduce;

import java.util.Map;

import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.interfaces.IRuleReduce;

public class OgnlRuleReduce implements IRuleReduce{
	
	private String expr;

	public OgnlRuleReduce(String expr) {
		super();
		this.expr = expr;
	}

	@Override
	public Map<String, Object> reduce(RuleResult ruleResult) throws Exception {
		return null;
	}

}
