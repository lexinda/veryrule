package com.lexinda.veryrule;

import com.lexinda.veryrule.core.IRuleListener;

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
	public void ruleListener(Object[] args, Object result) {
		// TODO Auto-generated method stub
//		System.out.println(args);
//		System.out.println(result);
	}

}
