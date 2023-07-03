package com.lexinda.veryrule.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.bo.RuleBo;

import ognl.DefaultClassResolver;
import ognl.DefaultTypeConverter;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class VeryRuleOgnlUtil {
	
	private static volatile VeryRuleOgnlUtil instanse;
	
	public static VeryRuleOgnlUtil create() {
		if (instanse == null) {
			synchronized (VeryRule.class) {
				if (instanse == null) {
					instanse = new VeryRuleOgnlUtil();
				}
			}
		}
		return instanse;
	}

	// #{ 'foo' : 'foo value', 'bar' : 'bar value' }
	public Map<String, Object> getExprMap(String expr) throws OgnlException {
		Object tree = Ognl.parseExpression(expr.substring(2, expr.length() - 2));
		Map<String, Object> result = (Map<String, Object>) Ognl.getValue(tree, new HashMap<String, Object>());
		return result;
	}

	// {'1','2','3'}
	public List<Object> getExprList(String expr) throws OgnlException {
		Object tree = Ognl.parseExpression(expr);
		List<Object> result = (List) Ognl.getValue(tree, new ArrayList<Object>());
		return result;
	}
	
	public List<Object> getExprObjList(String expr) throws OgnlException {
		Object tree = Ognl.parseExpression(expr);
		List<Object> result = (List) Ognl.getValue(tree, new ArrayList<Object>());
		return result;
	}
	
	//"ruleCode='123fsdfd',ruleType=1"
	public <T extends RuleBo> T getRule(String expr, T root) throws OgnlException {
		OgnlContext context = (OgnlContext) Ognl.createDefaultContext(root, new DefaultClassResolver(), new DefaultTypeConverter());
		Object tree = Ognl.parseExpression(expr);
		Ognl.getValue(tree, context,context.getRoot());
		return root;
	}
	
}
