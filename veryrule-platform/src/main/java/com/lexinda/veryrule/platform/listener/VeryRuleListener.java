package com.lexinda.veryrule.platform.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSON;
import com.lexinda.veryrule.core.IRuleListener;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleListener implements IRuleListener {
	private static final Logger logger = LoggerFactory.getLogger(VeryRuleListener.class);

	@Override
	public void initRule() {
		// TODO Auto-generated method stub
		logger.info("------veryrule inited------");
	}

	@Override
	public void ruleListener(Object[] args, Object result) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				logger.info(JSON.toJSONString(args));
				logger.info(JSON.toJSONString(result));
			}
		}).start();
	}

}
