package com.lexinda.veryrule;

import java.util.Map;

import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.interfaces.IRuleListener;

/**
 * 
 * @author lexinda
 *
 */
public class TestRuleListener implements IRuleListener {

	@Override
	public void initRule() {
		System.out.println("init");
	}
	
	@Override
	public void ruleFlowStart(Map<String, Object> param) {
		// TODO Auto-generated method stub
		System.out.println(param);
	}

	@Override
	public void ruleListener(Object[] args, Object result) {
		// TODO Auto-generated method stub
		System.out.println(args);
		System.out.println(result);
	}

	@Override
	public void ruleFlowEnd(Map<String, Object> param, RuleResult result) {
		// TODO Auto-generated method stub
		System.out.println(param);
		System.out.println(result);
	}

}
