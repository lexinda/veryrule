package com.lexinda.veryrule.platform.controller;

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
@RequestMapping("/veryrule")
public class VeryRuleSceneController {

	private static final Logger logger = LoggerFactory.getLogger(VeryRuleSceneController.class);

	private final int PAGE_SIZE = 10;

	@Autowired
	private VeryRuleSceneMbService veryRuleSceneMbService;

	@Autowired
	private VeryRule veryRule;
	
	@RequestMapping(value = "/getVeryRuleScenePage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//	@VeryRuleScene(RuleSceneCode = "test")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "pid,currentPage", ruleErrMsg = "不能为空")
	public RestApiResponse getVeryRuleScenePage(String data,RuleResult ruleResult) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Integer currentPage = param.getInteger("currentPage");
			Integer pageSize = param.getInteger("pageSize");
			Page<VeryRuleSceneModel> page = new Page<VeryRuleSceneModel>(currentPage == null ? 1 : currentPage,
					pageSize == null ? PAGE_SIZE : pageSize);

			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("pid", param.getString("pid"));
			if(StringUtils.isNotBlank(param.getString("ruleSceneNameL"))) {
				dataParam.put("ruleSceneNameL", param.getString("ruleSceneNameL"));
			}
			if(StringUtils.isNotBlank(param.getString("ruleSceneCode"))) {
				dataParam.put("ruleSceneCode", param.getString("ruleSceneCode"));
			}
			Page<VeryRuleSceneModel> veryRuleSceneList = veryRuleSceneMbService.selectVeryRuleSceneList(page, dataParam);
			Set<Integer> RuleSceneCodeSet = new HashSet<Integer>();
			veryRuleSceneList.getRecords().stream().forEach(vs -> RuleSceneCodeSet.add(vs.getId()));
			if (RuleSceneCodeSet.size() > 0) {
				dataParam = new HashMap<String, Object>();
				dataParam.put("pids", RuleSceneCodeSet);
				List<VeryRuleSceneModel> childVeryRuleSceneList = veryRuleSceneMbService.selectVeryRuleSceneList(dataParam);
				veryRuleSceneList.getRecords().stream().forEach(vs -> {
					if (childVeryRuleSceneList.stream()
							.filter(cr -> cr.getPid().equals(vs.getId())).count() > 0) {
						vs.setHasChildren(true);
					}
				});
			}
			res.setErrorCode(0);
			res.setBody(veryRuleSceneList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	@RequestMapping(value = "/getVeryRuleSceneList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "pid", ruleErrMsg = "不能为空")
	public RestApiResponse getVeryRuleSceneList(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("pid", param.getString("pid"));
			List<VeryRuleSceneModel> veryRuleSceneList = veryRuleSceneMbService.selectVeryRuleSceneList(dataParam);
			res.setErrorCode(0);
			res.setBody(veryRuleSceneList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	// {"RuleSceneCode":"test","RuleSceneName":"test","parentRuleSceneCode":"root","groupName":"test","RuleSceneDesc":"test"}
	@RequestMapping(value = "/addVeryRuleScene", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "pid,ruleSceneCode,ruleSceneName,ruleSceneType", ruleErrMsg = "不能为空")
	public RestApiResponse addVeryRuleScene(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			VeryRuleSceneModel veryRuleSceneData = new VeryRuleSceneModel();
			veryRuleSceneData.setPid(param.getInteger("pid"));
			veryRuleSceneData.setRuleSceneCode(param.getString("ruleSceneCode"));
			veryRuleSceneData.setRuleSceneName(param.getString("ruleSceneName"));
			veryRuleSceneData.setRuleSceneType(param.getString("ruleSceneType"));
			String RuleSceneDesc = param.getString("ruleSceneDesc");
			if (StringUtils.isNotBlank(RuleSceneDesc)) {
				veryRuleSceneData.setRuleSceneDesc(RuleSceneDesc);
			} else {
				veryRuleSceneData.setRuleSceneDesc("");
			}
			String createId = param.getString("createId");
			Date time = new Date();
			if (StringUtils.isNotBlank(createId)) {
				veryRuleSceneData.setCreateId(createId);
				veryRuleSceneData.setUpdateId(createId);
			} else {
				veryRuleSceneData.setCreateId("");
				veryRuleSceneData.setUpdateId("");
			}
			veryRuleSceneData.setCreateTime(time);
			veryRuleSceneData.setUpdateTime(time);
			veryRuleSceneData.setVersion(1);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleSceneNameCode", "V");
			dataParam.put("ruleSceneNameV", veryRuleSceneData.getRuleSceneName());
			dataParam.put("ruleSceneCodeV", veryRuleSceneData.getRuleSceneCode());
			List<VeryRuleSceneModel> veryRuleSceneList = veryRuleSceneMbService.selectVeryRuleSceneList(dataParam);
			if (veryRuleSceneList.size() > 0) {
				res.setErrorDesc("规则场景编码或名称已存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			veryRuleSceneMbService.save(veryRuleSceneData);
			res.setErrorCode(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	// {"id":1,"RuleSceneName":"test1","groupName":"test1","RuleSceneDesc":"test1","status":"0"}
	@RequestMapping(value = "/updateVeryRuleScene", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id", ruleErrMsg = "不能为空")
	public RestApiResponse updateVeryRuleScene(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			Page<VeryRuleSceneModel> page = new Page<VeryRuleSceneModel>(1, 3);
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			Integer id = param.getInteger("id");
			dataParam.put("id", id);
			List<VeryRuleSceneModel> veryRuleSceneList = veryRuleSceneMbService.selectVeryRuleSceneList(dataParam);
			if (veryRuleSceneList != null && veryRuleSceneList.size() > 0 && veryRuleSceneList.get(0).getId() > 0) {
				dataParam.put("version", veryRuleSceneList.get(0).getVersion());
				dataParam.put("versionTo", veryRuleSceneList.get(0).getVersion() + 1);
			} else {
				res.setErrorDesc("规则场景不存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			dataParam.put("ruleSceneCode", param.getString("ruleSceneCode"));
			dataParam.put("ruleSceneName", param.getString("ruleSceneName"));
			dataParam.put("ruleSceneType", param.getString("ruleSceneType"));
			dataParam.put("ruleSceneDesc", param.getString("ruleSceneDesc"));
			veryRuleSceneMbService.updateById(dataParam);
			res.setErrorCode(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}


	// {"id":1}
	@RequestMapping(value = "/delVeryRuleScene", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id", ruleErrMsg = "不能为空")
	public RestApiResponse delVeryRuleScene(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Integer id = param.getInteger("id");
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("id", id);
			List<VeryRuleSceneModel> veryRuleSceneList = veryRuleSceneMbService.selectVeryRuleSceneList(dataParam);
			if (veryRuleSceneList.size() > 0) {
				veryRuleSceneMbService.deleteById(dataParam);
				res.setErrorCode(0);
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
