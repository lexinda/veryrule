package com.lexinda.veryrule.core;
/**
 * 
 * @author lexinda
 *
 */

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.lexinda.veryrule.bo.RuleBo;

public abstract class RuleInvokerAbst{

	protected RuleResult ruleResult = new RuleResult();
	
	public abstract <R extends RuleBo> void doRuleCondation(Map<String, Object> param,Map<R, IRuleCondation> ruleCondations,IRuleListener ruleListener,boolean isTest);
	
	public abstract <R extends RuleBo> void doRuleResultCondation(Map<String, Object> param,Map<R, IRuleResultCondation> ruleCondations,IRuleListener ruleListener,boolean isTest,ThreadPoolExecutor threadPoolExecutor);
	
	public abstract <R extends RuleBo> void doRuleAction(Map<String, Object> param,Map<R, IRuleAction> ruleResultActions,IRuleListener ruleListener,boolean isTest);

	public RuleResult getRuleResult() {
		return ruleResult;
	}

}
