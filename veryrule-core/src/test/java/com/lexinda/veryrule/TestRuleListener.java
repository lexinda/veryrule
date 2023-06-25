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
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("ruleFlowStart");
				System.out.println(param);
			}
		}).start();
	}

	@Override
	public void ruleEnd(Object[] args, Object result) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("ruleEnd");
				System.out.println(args);
				System.out.println(result);
			}
		}).start();
	}

	@Override
	public void ruleFlowEnd(Map<String, Object> param, RuleResult result) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("ruleFlowEnd");
				System.out.println(param);
				System.out.println(result);
			}
		}).start();
	}

}
