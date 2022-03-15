package com.lexinda.veryrule.core;
/**
* @author zhumengle
* @version 创建时间：2022-2-23 11:32:18
* 类说明
*/

import java.util.List;
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

public abstract class RuleInvokerAbst{

	protected RuleResult ruleResult = new RuleResult();
	
	public abstract void doRuleCondation(Map<String, Object> param,List<IRuleCondation> ruleCondations,IRuleListener ruleListener,boolean isTest);
	
	public abstract <R extends RuleBo> void doRuleAction(Map<String, Object> param,Map<R, IRuleAction> ruleActions,IRuleListener ruleListener,boolean isTest);
	
	public abstract <R extends RuleBo> void doRuleResultAction(Map<String, Object> param,Map<R, IRuleResultAction> ruleResultActions,IRuleListener ruleListener,boolean isTest);

	public RuleResult getRuleResult() {
		return ruleResult;
	}

}
