package com.lexinda.veryrule.platform.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.annotation.VeryRuleFlow;
import com.lexinda.veryrule.annotation.VeryRuleSingle;
import com.lexinda.veryrule.base.key.RuleCode;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.platform.model.VeryRuleSceneModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleSceneMbService;
import com.lexinda.veryrule.vo.RestApiResponse;

/**
 * 
 * @author lexinda
 *
 */
@RestController
@RequestMapping("/veryruletest")
public class VeryRuleTestController {

	private static final Logger logger = LoggerFactory.getLogger(VeryRuleTestController.class);

	@RequestMapping(value = "/getVeryRuleScenePage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleFlow(ruleFlowCode = "wenku_reset",ruleFlowScene="checkUser")
	public RestApiResponse getVeryRuleScenePage(String data,RuleResult ruleResult) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			res.setErrorCode(0);
			res.setBody(new ArrayList<String>());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

}
