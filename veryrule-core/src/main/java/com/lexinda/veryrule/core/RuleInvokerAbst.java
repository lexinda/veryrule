package com.lexinda.veryrule.core;

/**
 * 
 * @author lexinda
 *
 */

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.interfaces.IRuleAction;
import com.lexinda.veryrule.core.interfaces.IRuleCondation;
import com.lexinda.veryrule.core.interfaces.IRuleResultCondation;

public abstract class RuleInvokerAbst{

	protected RuleResult ruleResult = new RuleResult();
	
	public abstract <R extends RuleBo> void doRuleCondation(Map<String, Object> param,Map<R, IRuleCondation> ruleCondations,RuleProxyHandler ruleProxyHandler,boolean isTest);
	
	public abstract <R extends RuleBo> void doRuleResultCondation(Map<String, Object> param,Map<R, IRuleResultCondation> ruleCondations,RuleProxyHandler ruleProxyHandler,boolean isTest,ThreadPoolExecutor threadPoolExecutor);
	
	public abstract <R extends RuleBo> void doRuleAction(Map<String, Object> param,Map<R, IRuleAction> ruleResultActions,RuleProxyHandler ruleProxyHandler,boolean isTest);

	public RuleResult getRuleResult() {
		return ruleResult;
	}
	
}
