package com.lexinda.veryrule.platform.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.annotation.VeryRuleSingle;
import com.lexinda.veryrule.base.key.RuleCode;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletLogMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletMbService;
import com.lexinda.veryrule.vo.RestApiResponse;

/**
 * 
 * @author lexinda
 *
 */
@RestController
@RequestMapping("/veryrule")
public class VeryRuleFlowTempletController {
	
	private static final Logger logger = LoggerFactory.getLogger(VeryRuleFlowTempletController.class);

	private final int PAGE_SIZE = 10;
	
	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletService;
	
	@Autowired
	private VeryRuleFlowTempletLogMbService veryRuleFlowTempletLogService;

	@Autowired
	private VeryRule veryRule;

	@RequestMapping(value = "/getVeryRuleFlowTempletPage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public RestApiResponse getVeryRuleFlowTempletPage(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Integer currentPage = param.getInteger("currentPage");
			Integer pageSize = param.getInteger("pageSize");
			Page<VeryRuleFlowTempletModel> page = new Page<VeryRuleFlowTempletModel>(currentPage==null?1:currentPage, pageSize==null?PAGE_SIZE:pageSize);
			
			Page<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletService.selectVeryRuleFlowTempletList(page, param);
			res.setErrorCode(0);
			res.setBody(veryRuleFlowTempletList);
		} catch (Exception e) {
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}
	
	@RequestMapping(value = "/getVeryRuleFlowTempletList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public RestApiResponse getVeryRuleFlowTempletList(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			String ruleFlowTempletCode = param.getString("ruleFlowTempletCode");
			if(StringUtils.isNotBlank(ruleFlowTempletCode)) {
				dataParam.put("ruleFlowTempletCode", ruleFlowTempletCode);
			}
			List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletService.selectVeryRuleFlowTempletList(dataParam);
			res.setErrorCode(0);
			res.setBody(veryRuleFlowTempletList);
		} catch (Exception e) {
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	// {"ruleFlowCode":"test","ruleFlowName":"test","ruleFlowTemplet":"root"}
	@RequestMapping(value = "/addVeryRuleFlowTemplet", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowTempletCode,ruleFlowTemplet", ruleErrMsg = "不能为空")
	public RestApiResponse addVeryRuleFlowTemplet(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			VeryRuleFlowTempletModel veryRuleFlowTempletData = new VeryRuleFlowTempletModel();
			veryRuleFlowTempletData.setRuleFlowTemplet(param.getString("ruleFlowTemplet"));
			veryRuleFlowTempletData.setRuleFlowTempletCode(param.getString("ruleFlowTempletCode"));
			String createId = param.getString("createId");
			Date time = new Date();
			if (StringUtils.isNotBlank(createId)) {
				veryRuleFlowTempletData.setCreateId(createId);
				veryRuleFlowTempletData.setUpdateId(createId);
			} else {
				veryRuleFlowTempletData.setCreateId("");
				veryRuleFlowTempletData.setUpdateId("");
			}
			veryRuleFlowTempletData.setCreateTime(time);
			veryRuleFlowTempletData.setUpdateTime(time);
			veryRuleFlowTempletData.setVersion(1);
			Page<VeryRuleFlowTempletModel> page = new Page<VeryRuleFlowTempletModel>(1, 9999);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleFlowTempletCode", veryRuleFlowTempletData.getRuleFlowTempletCode());
			Page<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletService.selectVeryRuleFlowTempletList(page, param);
			if (veryRuleFlowTempletList.getTotal() > 0) {
				res.setErrorDesc("规则模板标识已存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			veryRuleFlowTempletService.save(veryRuleFlowTempletData);
			res.setErrorCode(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			;
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	// {"id":1,"ruleFlowTemplet":"test1"}
	@RequestMapping(value = "/updateVeryRuleFlowTemplet", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id,ruleFlowTemplet", ruleErrMsg = "不能为空")
	public RestApiResponse updateVeryRuleFlowTemplet(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			Integer id = param.getInteger("id");
			dataParam.put("id", id);
			dataParam.put("ruleFlowTemplet", param.getString("ruleFlowTemplet"));
			VeryRuleFlowTempletModel veryRuleFlowTemplet = veryRuleFlowTempletService.getById(id);
			if (veryRuleFlowTemplet != null && veryRuleFlowTemplet.getId() > 0) {
				dataParam.put("version", veryRuleFlowTemplet.getVersion());
				dataParam.put("versionTo", veryRuleFlowTemplet.getVersion() + 1);
			} else {
				res.setErrorDesc("规则流不存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			veryRuleFlowTempletService.updateById(dataParam);
			veryRuleFlowTempletLogService.save(veryRuleFlowTemplet);
			res.setErrorCode(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}
}
