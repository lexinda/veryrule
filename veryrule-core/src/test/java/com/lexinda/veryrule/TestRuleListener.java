package com.lexinda.veryrule;

import com.lexinda.veryrule.core.IRuleListener;

/**
* @author zhumengle
* @version 创建时间：2022-3-14 10:49:16
* 类说明
*/
public class TestRuleListener implements IRuleListener {

	@Override
	public void ruleListener(Object proxy, Object[] args) {
		System.out.println(proxy);
		System.out.println(args);
	}

}
