package com.lexinda.veryrule;

import com.lexinda.veryrule.core.IRuleListener;

/**
 * 
 * @author lexinda
 *
 */
public class TestRuleListener implements IRuleListener {

	@Override
	public void initRule(Object proxy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ruleListener(Object proxy, Object[] args, Object result) {
		System.out.println(proxy);
		System.out.println(args);
		System.out.println(result);
	}

}
