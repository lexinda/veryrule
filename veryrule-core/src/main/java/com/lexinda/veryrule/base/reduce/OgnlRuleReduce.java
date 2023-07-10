package com.lexinda.veryrule.base.reduce;

import java.util.Map;

import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.interfaces.IRuleReduce;

import ognl.Ognl;
import ognl.OgnlContext;

public class OgnlRuleReduce implements IRuleReduce{
	
	private String expr;

	public OgnlRuleReduce(String expr) {
		super();
		this.expr = expr;
	}

	@Override
	public Map<String, Object> reduce(RuleResult ruleResult) throws Exception {
		OgnlContext context = (OgnlContext) Ognl.createDefaultContext(this);
		context.put("condation", ruleResult.getCondationResult());
		Ognl.getValue(expr, context, context.getRoot());
		return ruleResult.getCondationResult();
	}

}
