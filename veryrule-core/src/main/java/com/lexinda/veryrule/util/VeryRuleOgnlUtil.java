package com.lexinda.veryrule.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	
	//"ruleCode='123fsdfd',ruleType=1"
	public <T extends RuleBo> T getRule(String expr, T root) throws OgnlException {
		OgnlContext context = (OgnlContext) Ognl.createDefaultContext(root, new DefaultClassResolver(), new DefaultTypeConverter());
		Object tree = Ognl.parseExpression(expr);
		Ognl.getValue(tree, context,context.getRoot());
		return root;
	}
	
	//scene='123fsdfd',ruleBoList={"ruleCode='123fsdfd',ruleType=1","ruleCode='123fsdfd',ruleType=1"}
	public RuleFlow getRuleFlow(String expr) throws OgnlException {
		RuleFlow ruleFlow = new RuleFlow();
		OgnlContext context = (OgnlContext) Ognl.createDefaultContext(ruleFlow, new DefaultClassResolver(), new DefaultTypeConverter());
		Object tree = Ognl.parseExpression(expr);
		Ognl.getValue(tree, context,context.getRoot());
		return ruleFlow;
	}
	
	public static class RuleFlow {
		private String scene;
		private List<RuleBo> ruleBoList;
		public String getScene() {
			return scene;
		}
		public void setScene(String scene) {
			this.scene = scene;
		}
		public List<RuleBo> getRuleBoList() {
			return ruleBoList;
		}
		public void setRuleBoList(List<String> ruleBoList) {
			if(!ruleBoList.isEmpty()) {
				RuleBo ruleBo = new RuleBo();
				this.ruleBoList = ruleBoList.stream().map(ruleStr -> {
					try {
						return VeryRuleOgnlUtil.create().getRule(ruleStr, ruleBo);
					} catch (OgnlException e) {
						// TODO Auto-generated catch block
						return null;
					}
				}).collect(Collectors.toList());
			}else {
				this.ruleBoList = new ArrayList<RuleBo>();
			}
		}
	}
	
}
