package com.lexinda.veryrule.platform.controller;

import java.util.ArrayList;
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
import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.base.key.RuleCode;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.platform.model.VeryRuleElementModel;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleElementMbService;
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

	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletMbService;

	@Autowired
	private VeryRule veryRule;
	
	@Autowired
	private VeryRuleElementMbService veryRuleElementMbService;

	/**
	 * 获取所有flow用到的rule
	 * 比较所有服务器里初始化的rule（启动时可将所有初始化的规则写入redis/数据库）
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/compareRuleActive", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public RestApiResponse compareRuleActive(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			Set<String> diffRuleInfo = new HashSet<String>();
			List<VeryRuleElementModel> ruleElementList = veryRuleElementMbService.selectVeryRuleElementList(new HashMap<>());
			Map<String,Class<?>> allRule = veryRule.allRule();
			//有实现规则但是没配
			allRule.entrySet().stream().forEach(ar->{
				if(ruleElementList.stream().filter(re->re.getRuleCode().equals(ar.getKey())).count()==0) {
					diffRuleInfo.add(ar.getKey()+"_"+1);
				}
			});
			//有配规则但是没有实现规则
			if(ruleElementList.size()>0) {
				ruleElementList.stream().forEach(re->{
					if(allRule.get(re.getRuleCode())==null) {
						diffRuleInfo.add(re.getRuleCode()+"_"+2);
					}
				});
			}else {
				allRule.entrySet().stream().forEach(ar->{
					diffRuleInfo.add(ar.getKey()+"_"+2);
				});
			}
			
			List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletMbService.selectVeryRuleFlowTempletList(new HashMap<String,Object>());
			Map<String,Object> ruleFlowTempletMap = new HashMap<String,Object>();
			if(veryRuleFlowTempletList.size()>0) {
				veryRuleFlowTempletList.stream().forEach(rft->{
					if(StringUtils.isNotBlank(rft.getRuleFlowTemplet())) {
						try {
							List<RuleBo> ruleList = JSON.parseArray(rft.getRuleFlowTemplet(), RuleBo.class);
							ruleList.stream().forEach(rl->ruleFlowTempletMap.put(rl.getRuleCode(), rl.getRuleCode()));
						}catch(Exception e) {
							JSONObject ruleData = JSON.parseObject(rft.getRuleFlowTemplet());
							ruleData.entrySet().forEach(rd->{
								List<RuleBo> ruleList = JSON.parseArray(rd.getValue().toString(), RuleBo.class);
								ruleList.stream().forEach(rl->ruleFlowTempletMap.put(rl.getRuleCode(), rl.getRuleCode()));
							});
						}
					}
				});
			}
			//规则没配但是规则流用到
			ruleFlowTempletMap.entrySet().stream().forEach(rf->{
				if(!rf.getKey().equals(RuleCode.NOTNULL)&&ruleElementList.stream().filter(re->re.getRuleCode().equals(rf.getKey())).count()==0) {
					diffRuleInfo.add(rf.getKey()+"_"+3);
				}
			});
			res.setErrorCode(0);
			res.setBody(diffRuleInfo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}
	
}
