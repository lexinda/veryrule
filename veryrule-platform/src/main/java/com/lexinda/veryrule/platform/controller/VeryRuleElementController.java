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
import com.lexinda.veryrule.annotation.VeryRuleSingle;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleCode;
import com.lexinda.veryrule.platform.model.VeryRuleElementMenuModel;
import com.lexinda.veryrule.platform.model.VeryRuleElementModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleElementMbService;
import com.lexinda.veryrule.vo.RestApiResponse;

/**
 * 
 * @author lexinda
 *
 */
@RestController
@RequestMapping("/veryrule")
public class VeryRuleElementController {

	private static final Logger logger = LoggerFactory.getLogger(VeryRuleElementController.class);

	private final int PAGE_SIZE = 10;

	@Autowired
	private VeryRuleElementMbService veryRuleElementService;

	@RequestMapping(value = "/getVeryRuleElementList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "currentPage,ruleType", ruleErrMsg = "不能为空")
	public RestApiResponse getVeryRuleElementList(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Integer currentPage = param.getInteger("currentPage");
			Integer pageSize = param.getInteger("pageSize");
			Page<VeryRuleElementModel> page = new Page<VeryRuleElementModel>(currentPage == null ? 1 : currentPage,
					pageSize == null ? PAGE_SIZE : pageSize);

			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleType", param.getString("ruleType"));
			String ruleCode = param.getString("ruleCode");
			if (StringUtils.isNotBlank(ruleCode)) {
				dataParam.put("ruleCode", param.getString("ruleCode"));
			}
			String ruleName = param.getString("ruleName");
			if (StringUtils.isNotBlank(ruleName)) {
				dataParam.put("ruleName", param.getString("ruleName"));
			}
			String groupName = param.getString("groupName");
			if (StringUtils.isNotBlank(groupName)) {
				dataParam.put("groupName", param.getString("groupName"));
			}
			Page<VeryRuleElementModel> veryRuleElementList = veryRuleElementService.selectVeryRuleElementList(page,
					dataParam);
			res.setErrorCode(0);
			res.setBody(veryRuleElementList);
		} catch (Exception e) {
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	@RequestMapping(value = "/getVeryRuleElementMenu", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleType", ruleErrMsg = "不能为空")
	public RestApiResponse getVeryRuleElementMenu(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			List<VeryRuleElementModel> veryRuleElementList = veryRuleElementService.selectVeryRuleElementList(dataParam);
			if (param.getInteger("ruleType") == 1) {
				Set<String> groupNameSet = new HashSet<String>();
				List<VeryRuleElementMenuModel> veryRuleElementMenuList = new ArrayList<VeryRuleElementMenuModel>();
				veryRuleElementList.stream().forEach(vgr -> {
					VeryRuleElementMenuModel veryRuleElementMenu = JSON.parseObject(JSON.toJSONString(vgr),
							VeryRuleElementMenuModel.class);
					if (StringUtils.isNotBlank(vgr.getGroupName())) {
						veryRuleElementMenu.setRuleName(veryRuleElementMenu.getGroupName());
						if (!groupNameSet.contains(vgr.getGroupName())) {
							groupNameSet.add(vgr.getGroupName());
							veryRuleElementMenuList.add(veryRuleElementMenu);
						}
					}else {
						veryRuleElementMenuList.add(veryRuleElementMenu);
					}
					
				});
				if (veryRuleElementMenuList.size() > 0) {
					veryRuleElementMenuList.stream().forEach(vrm -> {
						if (StringUtils.isNotBlank(vrm.getGroupName())) {
							List<VeryRuleElementMenuModel> children = new ArrayList<VeryRuleElementMenuModel>();
							veryRuleElementList.stream().forEach(vr -> {
								if (vr.getGroupName().equals(vrm.getGroupName())) {
									VeryRuleElementMenuModel veryRuleElementMenu = JSON.parseObject(JSON.toJSONString(vr),
											VeryRuleElementMenuModel.class);
									children.add(veryRuleElementMenu);
								}
							});
							if (children.size() > 0) {
								vrm.setChildren(children);
							}
						}
					});
				}
				res.setBody(veryRuleElementMenuList);
			} else {
				res.setBody(veryRuleElementList);
			}
			res.setErrorCode(0);

		} catch (Exception e) {
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	// {"ruleCode":"test","ruleName":"test","ruleKey":"root","ruleErrMsg":"test","ruleDesc":"test","ruleCondation":"test"}
	@RequestMapping(value = "/addVeryRuleElement", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleCode,ruleName,ruleType", ruleErrMsg = "不能为空")
	public RestApiResponse addVeryRuleElement(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			VeryRuleElementModel veryRuleElementData = new VeryRuleElementModel();
			veryRuleElementData.setRuleCode(param.getString("ruleCode"));
			veryRuleElementData.setRuleName(param.getString("ruleName"));
			veryRuleElementData.setRuleType(param.getInteger("ruleType"));
			String ruleValue = param.getString("ruleValue");
			if (StringUtils.isNotBlank(ruleValue)) {
				veryRuleElementData.setRuleValue(ruleValue);
			} else {
				veryRuleElementData.setRuleValue("");
			}
			String ruleCondation = param.getString("ruleCondation");
			List<RuleBo> ruleBoList = new ArrayList<RuleBo>();
			if (StringUtils.isNotBlank(ruleCondation)) {
				veryRuleElementData.setRuleCondations(ruleBoList);
			} else {
				veryRuleElementData.setRuleCondations(ruleBoList);
			}
			String ruleDesc = param.getString("ruleDesc");
			if (StringUtils.isNotBlank(ruleDesc)) {
				veryRuleElementData.setRuleDesc(ruleDesc);
			} else {
				veryRuleElementData.setRuleDesc("");
			}
			String groupName = param.getString("groupName");
			if (StringUtils.isNotBlank(groupName)) {
				veryRuleElementData.setGroupName(groupName);
			} else {
				veryRuleElementData.setGroupName("");
			}
			String createId = param.getString("createId");
			Date time = new Date();
			if (StringUtils.isNotBlank(createId)) {
				veryRuleElementData.setCreateId(createId);
				veryRuleElementData.setUpdateId(createId);
			} else {
				veryRuleElementData.setCreateId("");
				veryRuleElementData.setUpdateId("");
			}
			veryRuleElementData.setCreateTime(time);
			veryRuleElementData.setUpdateTime(time);
			veryRuleElementData.setVersion(1);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleCode", veryRuleElementData.getRuleCode());
			List<VeryRuleElementModel> veryRuleElementList = veryRuleElementService.selectVeryRuleElementList(param);
			if (veryRuleElementList.size() > 0) {
				res.setErrorDesc("规则标识已存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			veryRuleElementService.save(veryRuleElementData);
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

	// {"id":1,"ruleName":"test1","ruleKey":"test1","ruleDesc":"test1","ruleErrMsg":"0","ruleCondation":"0"}
	@RequestMapping(value = "/updateVeryRuleElement", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id", ruleErrMsg = "不能为空")
	public RestApiResponse updateVeryRuleElement(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			Integer id = param.getInteger("id");
			dataParam.put("id", id);
			dataParam.put("ruleName", param.getString("ruleName"));
			dataParam.put("ruleValue", param.getString("ruleValue"));
			dataParam.put("ruleCondation", param.getString("ruleCondation"));
			dataParam.put("ruleDesc", param.getString("ruleDesc"));
			dataParam.put("groupName", param.getString("groupName"));
			Map<String, Object> dataIdParam = new HashMap<String, Object>();
			dataIdParam.put("id", id);
			List<VeryRuleElementModel> veryRuleElement = veryRuleElementService.selectVeryRuleElementList(dataIdParam);
			if (veryRuleElement != null && veryRuleElement.size() > 0 && veryRuleElement.get(0).getId() > 0) {
				dataParam.put("version", veryRuleElement.get(0).getVersion());
				dataParam.put("versionTo", veryRuleElement.get(0).getVersion() + 1);
			} else {
				res.setErrorDesc("规则不存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			veryRuleElementService.updateById(dataParam);
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
	@RequestMapping(value = "/delVeryRuleElement", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id", ruleErrMsg = "不能为空")
	public RestApiResponse delVeryRuleElement(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Integer id = param.getInteger("id");
			veryRuleElementService.removeById(id);
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
