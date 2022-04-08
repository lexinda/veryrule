package com.lexinda.veryrule.platform.controller;

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

import com.alibaba.fastjson.JSON;
import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletMbService;
import com.lexinda.veryrule.vo.RestApiResponse;

/**
 * 
 * @author lexinda
 *
 */
@RestController
@RequestMapping("/veryrule")
public class VeryRuleFlowToolsController {

	private static final Logger logger = LoggerFactory.getLogger(VeryRuleFlowToolsController.class);

	private final int PAGE_SIZE = 10;
	
	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletMbService;

	@Autowired
	private VeryRule veryRule;

	/**
	 * 获取所有flow用到的rule
	 * 比较所有服务器里初始化的rule（启动时可将所有初始化的规则写入redis/数据库）
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/compareRuleActive", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public RestApiResponse compareRuleActive(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			//初始化的rule
			Map<String,Class<?>> allRule = veryRule.allRule();
			List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletMbService.selectVeryRuleFlowTempletList(new HashMap<String,Object>());
			Map<String,Object> ruleFlowTempletMap = new HashMap<String,Object>();
			if(veryRuleFlowTempletList.size()>0) {
				veryRuleFlowTempletList.stream().forEach(rft->{
					if(StringUtils.isNotBlank(rft.getRuleFlowTemplet())) {
						List<RuleBo> ruleList = JSON.parseArray(rft.getRuleFlowTemplet(), RuleBo.class);
						ruleList.stream().forEach(rl->ruleFlowTempletMap.put(rl.getRuleCode(), rl.getRuleCode()));
					}
				});
			}
			Set<String> diffRuleInfo = new HashSet<String>();
			if(allRule!=null&&!allRule.isEmpty()) {
				allRule.entrySet().stream().forEach(ar->{
					if(ruleFlowTempletMap.get(ar.getKey())==null) {
						diffRuleInfo.add(ar.getKey()+"_"+1);
					}
				});
				ruleFlowTempletMap.entrySet().stream().forEach(rf->{
					if(allRule.get(rf.getKey())==null) {
						diffRuleInfo.add(rf.getKey()+"_"+1);
					}
				});
				res.setErrorCode(0);
				res.setBody(diffRuleInfo);
			}else {
				res.setErrorCode(1);
				res.setErrorDesc("无规则信息");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}
	
}
