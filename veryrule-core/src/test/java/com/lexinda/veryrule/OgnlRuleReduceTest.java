package com.lexinda.veryrule;

import java.util.Map;
import java.lang.*;

import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.interfaces.IRuleReduce;

import ognl.Ognl;
import ognl.OgnlContext;

public class OgnlRuleReduceTest implements IRuleReduce{
	
	private String expr;

	public OgnlRuleReduceTest(String expr) {
		super();
		this.expr = expr;
	}

	@Override
	public Map<String, Object> reduce(RuleResult ruleResult) throws Exception {
		OgnlContext context = (OgnlContext) Ognl.createDefaultContext(this);
		context.put("condation", ruleResult.getCondationResult());
		Object res = Ognl.getValue("#condation.one > 2", context, context.getRoot());
		Ognl.getValue("#condation.one = null", context, context.getRoot());
		Ognl.getValue("#condation.one = 2", context, context.getRoot());
		System.out.println(res);
		System.out.println(ruleResult);
		return null;
	}

}
