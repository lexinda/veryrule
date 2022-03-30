package com.lexinda.veryrule;

import com.lexinda.veryrule.core.IRuleListener;

/**
 * 
 * @author lexinda
 *
 */
public class TestRuleListener implements IRuleListener {

	@Override
	public void ruleListener(Object proxy, Object[] args) {
		System.out.println(proxy);
		System.out.println(args);
	}

}
