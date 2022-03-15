package com.lexinda.veryrule.platform.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lexinda.veryrule.core.IRuleListener;

/**
 * @author zhumengle
 * @version 创建时间：2022-3-14 10:49:16 类说明
 */
public class VeryRuleListener implements IRuleListener {
	private static final Logger logger = LoggerFactory.getLogger(VeryRuleListener.class);

	@Override
	public void ruleListener(Object proxy, Object[] args) {
		logger.debug(JSON.toJSONString(proxy));
		logger.debug(JSON.toJSONString(args));
	}

}
