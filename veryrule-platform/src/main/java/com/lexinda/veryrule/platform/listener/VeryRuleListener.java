package com.lexinda.veryrule.platform.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSON;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.interfaces.IRuleListener;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleListener implements IRuleListener {
	private static final Logger logger = LoggerFactory.getLogger(VeryRuleListener.class);

	@Override
	public void initRule() {
		logger.info("------veryrule inited------");
	}

	@Override
	public void ruleFlowStart(Map<String, Object> param) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info("------veryrule ruleFlowStart------");
				logger.info(JSON.toJSONString(param));
			}
		}).start();
	}

	@Override
	public void ruleEnd(Object[] args, Object result) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info("------veryrule ruleEnd------");
				logger.info(JSON.toJSONString(args));
				logger.info(JSON.toJSONString(result));
			}
		}).start();
	}

	@Override
	public void ruleFlowEnd(Map<String, Object> param, RuleResult result) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info("------veryrule ruleFlowEnd------");
				logger.info(JSON.toJSONString(param));
				logger.info(JSON.toJSONString(result));
			}
		}).start();
	}

}
