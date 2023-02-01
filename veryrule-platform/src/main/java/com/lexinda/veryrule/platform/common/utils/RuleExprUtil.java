package com.lexinda.veryrule.platform.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

public class RuleExprUtil {

	// #{ 'foo' : 'foo value', 'bar' : 'bar value' }
	public Map<String, Object> getExprMap(String expr) {
		Map<String, Object> result = null;
		try {
			Object tree = Ognl.parseExpression(expr);
			result = (Map<String, Object>) Ognl.getValue(tree, new HashMap<String, Object>());
		} catch (OgnlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// {'1','2','3'}
	public List<Object> getExprList(String expr) {
		List<Object> result = null;
		try {
			Object tree = Ognl.parseExpression(expr);
			result = (List) Ognl.getValue(tree, new ArrayList<Object>());
		} catch (OgnlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public <T> void getExprObject(String expr, T root) {
		try {
			Object tree = Ognl.parseExpression(expr);
			Ognl.getValue(tree, root);
		} catch (OgnlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
