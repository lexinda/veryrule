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
import com.lexinda.veryrule.base.key.RuleCode;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.platform.enums.VeryRuleElementGroupEnum;
import com.lexinda.veryrule.platform.model.VeryRuleElementMenuModel;
import com.lexinda.veryrule.platform.model.VeryRuleElementModel;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.model.VeryRuleSceneModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleElementMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleSceneMbService;
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
	
	@Autowired
	private VeryRuleSceneMbService veryRuleSceneMbService;
	
	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletMbService;

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
			Integer ruleType = param.getInteger("ruleType");
			if(ruleType==12) {
				dataParam.put("ruleTypes", new int[] {1,2});
			}else {
				dataParam.put("ruleType", ruleType);
			}
			
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
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleMenu", ruleErrMsg = "不能为空")
	public RestApiResponse getVeryRuleElementMenu(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		Map<String,Object> body = new HashMap<String,Object>();
		body.put("haveScene", 0);
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(param.getString("ruleType"))) {
				int ruleType = param.getIntValue("ruleType");
				if(ruleType==12) {
					dataParam.put("ruleTypes", new int[] {1,2});
				}else {
					dataParam.put("ruleType", ruleType);
				}
			}
			dataParam.put("orderRuleType",1);
			List<VeryRuleElementModel> veryRuleElementList = veryRuleElementService.selectVeryRuleElementList(dataParam);
			List<VeryRuleSceneModel> veryRuleSceneList = new ArrayList<VeryRuleSceneModel>();
			if(StringUtils.isNotBlank(param.getString("ruleSceneId"))) {
				Map<String, Object> dataSceneParam = new HashMap<String, Object>();
				dataSceneParam.put("pid",param.getString("ruleSceneId"));
				veryRuleSceneList = veryRuleSceneMbService.selectVeryRuleSceneList(dataSceneParam);
			}
			//展示树形
			List<VeryRuleElementMenuModel> veryRuleElementMenuList = new ArrayList<VeryRuleElementMenuModel>();
			if (param.getInteger("ruleMenu") == 1) {
				Set<String> groupNameSet = new HashSet<String>();
				veryRuleElementList.stream().forEach(vgr -> {
					VeryRuleElementMenuModel veryRuleElementMenu = JSON.parseObject(JSON.toJSONString(vgr),
							VeryRuleElementMenuModel.class);
					if (StringUtils.isNotBlank(vgr.getGroupName())) {
						veryRuleElementMenu.setRuleName(veryRuleElementMenu.getGroupName());
						veryRuleElementMenu.setRuleCode(VeryRuleElementGroupEnum.getCode(vgr.getGroupName()));
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
				if(veryRuleSceneList.size()>0) {
					List<VeryRuleElementMenuModel> veryRuleSceneMenuList = new ArrayList<VeryRuleElementMenuModel>();
					veryRuleSceneList.stream().forEach(vrs->{
						VeryRuleElementMenuModel veryRuleElementModel = new VeryRuleElementMenuModel();
						veryRuleElementModel.setId(vrs.getId());
						veryRuleElementModel.setRuleCode(vrs.getRuleSceneCode());
						veryRuleElementModel.setRuleName(vrs.getRuleSceneName());
						veryRuleElementModel.setGroupName(vrs.getRuleSceneName());
						veryRuleElementModel.setChildren(veryRuleElementMenuList);
						veryRuleSceneMenuList.add(veryRuleElementModel);
					});
					body.put("haveScene", 1);
					body.put("ruleScene", veryRuleSceneList);
					body.put("ruleMenu", veryRuleSceneMenuList);
				}else {
					body.put("ruleMenu", veryRuleElementMenuList);
				}
				
			} else {
				if(veryRuleSceneList.size()>0) {
					veryRuleElementList.stream().forEach(vgr -> {
						VeryRuleElementMenuModel veryRuleElementMenu = JSON.parseObject(JSON.toJSONString(vgr),
								VeryRuleElementMenuModel.class);
						veryRuleElementMenuList.add(veryRuleElementMenu);
					});
					List<VeryRuleElementMenuModel> veryRuleSceneMenuList = new ArrayList<VeryRuleElementMenuModel>();
					veryRuleSceneList.stream().forEach(vrs->{
						VeryRuleElementMenuModel veryRuleElementModel = new VeryRuleElementMenuModel();
						veryRuleElementModel.setId(vrs.getId());
						veryRuleElementModel.setRuleCode(vrs.getRuleSceneCode());
						veryRuleElementModel.setRuleName(vrs.getRuleSceneName());
						veryRuleElementModel.setGroupName(vrs.getRuleSceneName());
						veryRuleElementModel.setChildren(veryRuleElementMenuList);
						veryRuleSceneMenuList.add(veryRuleElementModel);
					});
					body.put("haveScene", 1);
					body.put("ruleScene", veryRuleSceneList);
					body.put("ruleMenu", veryRuleSceneMenuList);
				}else {
					body.put("ruleMenu", veryRuleElementMenuList);
				}
				
			}
			res.setBody(body);
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
			String ruleExpr = param.getString("ruleExpr");
			if(StringUtils.isNotBlank(ruleExpr)) {
				veryRuleElementData.setRuleExpr(ruleExpr);
			}
			String ruleKey = param.getString("ruleKey");
			if(StringUtils.isNotBlank(ruleKey)) {
				veryRuleElementData.setRuleKey(ruleKey);
			}
			String ruleValue = param.getString("ruleValue");
			if (StringUtils.isNotBlank(ruleValue)) {
				veryRuleElementData.setRuleValue(ruleValue);
			} else {
				veryRuleElementData.setRuleValue("");
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
			List<VeryRuleElementModel> veryRuleElementList = veryRuleElementService.selectVeryRuleElementList(dataParam);
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
			dataParam.put("ruleType", param.getInteger("ruleType"));
			dataParam.put("groupName", param.getString("groupName"));
			String ruleExpr = param.getString("ruleExpr");
			if(StringUtils.isNotBlank(ruleExpr)) {
				dataParam.put("ruleExpr", ruleExpr);
			}
			String ruleKey = param.getString("ruleKey");
			if(StringUtils.isNotBlank(ruleKey)) {
				dataParam.put("ruleKey", ruleKey);
			}
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
			Map<String, Object> dataIdParam = new HashMap<String, Object>();
			dataIdParam.put("id", id);
			List<VeryRuleElementModel> veryRuleElement = veryRuleElementService.selectVeryRuleElementList(dataIdParam);
			List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletMbService.selectVeryRuleFlowTempletList(new HashMap<String,Object>());
			StringBuffer ruleTempStr = new StringBuffer();
			veryRuleFlowTempletList.stream().forEach(rft->{
				if(StringUtils.isNotBlank(rft.getRuleFlowTemplet())) {
					try {
						List<RuleBo> ruleList = JSON.parseArray(rft.getRuleFlowTemplet(), RuleBo.class);
						for(RuleBo rl:ruleList) {
							if(rl.getRuleCode().equals(veryRuleElement.get(0).getRuleCode())) {
								ruleTempStr.append(rft.getRuleFlowTempletCode()).append(",");
								break;
							}
						}
					}catch(Exception e) {
						JSONObject ruleData = JSON.parseObject(rft.getRuleFlowTemplet());
						ruleData.entrySet().forEach(rd->{
							if(!ruleTempStr.toString().contains(rft.getRuleFlowTempletCode())) {
								List<RuleBo> ruleList = JSON.parseArray(rd.getValue().toString(), RuleBo.class);
								for(RuleBo rl:ruleList) {
									if(rl.getRuleCode().equals(veryRuleElement.get(0).getRuleCode())) {
										ruleTempStr.append(rft.getRuleFlowTempletCode()).append(",");
										break;
									}
								}
							}
						});
					}
				}
			});
			if(ruleTempStr.length()>0) {
				res.setErrorDesc(ruleTempStr.substring(0,ruleTempStr.length()-1)+"正在使用此规则！");
				return res;
			}
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
	
	@RequestMapping(value = "/getVeryRuleElementGroup", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public RestApiResponse getVeryRuleElementGroup(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(0);
		res.setElapsedTime(System.currentTimeMillis());
		res.setBody(VeryRuleElementGroupEnum.toList());
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

}
