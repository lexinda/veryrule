package com.lexinda.veryrule.platform.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.annotation.VeryRuleSingle;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleCode;
import com.lexinda.veryrule.core.RuleResult;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.service.VeryRuleFlowService;
import com.lexinda.veryrule.platform.service.VeryRuleFlowTempletService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletMbService;
import com.lexinda.veryrule.vo.RestApiResponse;

/**
 * 
 * @author lexinda
 *
 */
@RestController
@RequestMapping("/veryrule")
public class VeryRuleFlowController {

	private static final Logger logger = LoggerFactory.getLogger(VeryRuleFlowController.class);

	private final int PAGE_SIZE = 10;
	
	@Autowired
	private VeryRuleFlowMbService veryRuleFlowMbService;

	@Autowired
	private VeryRuleFlowService veryRuleFlowService;

	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletMbService;

	@Autowired
	private VeryRuleFlowTempletService veryRuleFlowTempletService;
	
	@Autowired
	private VeryRule veryRule;

	@RequestMapping(value = "/getVeryRuleFlowPage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//	@VeryRuleFlow(ruleFlowCode = "test")
//	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "parentRuleFlowCode,currentPage", ruleErrMsg = "不能为空")
	public RestApiResponse getVeryRuleFlowPage(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Integer currentPage = param.getInteger("currentPage");
			Integer pageSize = param.getInteger("pageSize");
			Page<VeryRuleFlowModel> page = new Page<VeryRuleFlowModel>(currentPage == null ? 1 : currentPage,
					pageSize == null ? PAGE_SIZE : pageSize);

			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("parentRuleFlowCode", param.getString("parentRuleFlowCode"));
			if (StringUtils.isNotBlank(param.getString("ruleFlowCode"))) {
				dataParam.put("ruleFlowCode", param.getString("ruleFlowCode"));
			}
			if (StringUtils.isNotBlank(param.getString("ruleFlowName"))) {
				dataParam.put("ruleFlowName", param.getString("ruleFlowName"));
			}
			if (StringUtils.isNotBlank(param.getString("groupName"))) {
				dataParam.put("groupName", param.getString("groupName"));
			}
			Page<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(page, dataParam);
			Set<String> ruleFlowCodeSet = new HashSet<String>();
			veryRuleFlowList.getRecords().stream().forEach(vf -> ruleFlowCodeSet.add(vf.getRuleFlowCode()));
			if (ruleFlowCodeSet.size() > 0) {
				dataParam = new HashMap<String, Object>();
				dataParam.put("parentRuleFlowCodes", ruleFlowCodeSet);
				List<VeryRuleFlowModel> childVeryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
				veryRuleFlowList.getRecords().stream().forEach(vf -> {
					if (childVeryRuleFlowList.stream()
							.filter(cr -> cr.getParentRuleFlowCode().equals(vf.getRuleFlowCode())).count() > 0) {
						vf.setHasChildren(true);
					}
				});
			}
			res.setErrorCode(0);
			res.setBody(veryRuleFlowList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}
	
	@RequestMapping(value = "/getVeryRuleFlowList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public RestApiResponse getVeryRuleFlowList(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			Map<String, Object> dataParam = new HashMap<String, Object>();
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			VeryRuleFlowModel veryRuleFlowModel = new VeryRuleFlowModel();
			veryRuleFlowModel.setRuleFlowCode("root");
			veryRuleFlowModel.setParentRuleFlowCode("root");
			veryRuleFlowModel.setRuleFlowName("root");
			veryRuleFlowList.add(0, veryRuleFlowModel);
			res.setErrorCode(0);
			res.setBody(veryRuleFlowList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	// {"ruleFlowCode":"test","ruleFlowName":"test","parentRuleFlowCode":"root","groupName":"test","ruleFlowDesc":"test"}
	@RequestMapping(value = "/addVeryRuleFlow", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowCode,ruleFlowName,parentRuleFlowCode,", ruleErrMsg = "不能为空")
	public RestApiResponse addVeryRuleFlow(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			VeryRuleFlowModel veryRuleFlowData = new VeryRuleFlowModel();
			veryRuleFlowData.setParentRuleFlowCode(param.getString("parentRuleFlowCode"));
			veryRuleFlowData.setRuleFlowCode(param.getString("ruleFlowCode"));
			veryRuleFlowData.setRuleFlowName(param.getString("ruleFlowName"));
			String groupName = param.getString("groupName");
			if (StringUtils.isNotBlank(groupName)) {
				veryRuleFlowData.setGroupName(groupName);
			} else {
				veryRuleFlowData.setGroupName("");
			}
			String ruleFlowDesc = param.getString("ruleFlowDesc");
			if (StringUtils.isNotBlank(ruleFlowDesc)) {
				veryRuleFlowData.setRuleFlowDesc(ruleFlowDesc);
			} else {
				veryRuleFlowData.setRuleFlowDesc("");
			}
			String createId = param.getString("createId");
			Date time = new Date();
			if (StringUtils.isNotBlank(createId)) {
				veryRuleFlowData.setCreateId(createId);
				veryRuleFlowData.setUpdateId(createId);
			} else {
				veryRuleFlowData.setCreateId("");
				veryRuleFlowData.setUpdateId("");
			}
			veryRuleFlowData.setCreateTime(time);
			veryRuleFlowData.setUpdateTime(time);
			veryRuleFlowData.setStatus(1);
			veryRuleFlowData.setVersion(1);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleFlowCode", veryRuleFlowData.getRuleFlowCode());
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			if (veryRuleFlowList.size() > 0) {
				res.setErrorDesc("规则流标识已存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			veryRuleFlowMbService.save(veryRuleFlowData);
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

	// {"id":1,"ruleFlowName":"test1","groupName":"test1","ruleFlowDesc":"test1","status":"0"}
	@RequestMapping(value = "/updateVeryRuleFlow", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id", ruleErrMsg = "不能为空")
	public RestApiResponse updateVeryRuleFlow(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			Page<VeryRuleFlowModel> page = new Page<VeryRuleFlowModel>(1, 3);
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			Integer id = param.getInteger("id");
			dataParam.put("id", id);
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			if (veryRuleFlowList != null && veryRuleFlowList.size() > 0 && veryRuleFlowList.get(0).getId() > 0) {
				dataParam.put("version", veryRuleFlowList.get(0).getVersion());
				dataParam.put("versionTo", veryRuleFlowList.get(0).getVersion() + 1);
			} else {
				res.setErrorDesc("规则流不存在");
				res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
				res.setServerTime(System.currentTimeMillis());
				return res;
			}
			dataParam.put("ruleFlowName", param.getString("ruleFlowName"));
			dataParam.put("groupName", param.getString("groupName"));
			dataParam.put("ruleFlowDesc", param.getString("ruleFlowDesc"));
			veryRuleFlowMbService.updateById(dataParam);
			res.setErrorCode(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}
	
	// {"id":1,"status":"0"}
		@RequestMapping(value = "/updateVeryRuleFlowStatus", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
		@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id,status", ruleErrMsg = "不能为空")
		public RestApiResponse updateVeryRuleFlowStatus(String data) throws Exception {
			RestApiResponse res = new RestApiResponse();
			res.setErrorCode(1);
			res.setElapsedTime(System.currentTimeMillis());
			try {
				Page<VeryRuleFlowModel> page = new Page<VeryRuleFlowModel>(1, 3);
				JSONObject param = JSON.parseObject(data);
				Map<String, Object> dataParam = new HashMap<String, Object>();
				Integer id = param.getInteger("id");
				dataParam.put("id", id);
				List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
				if (veryRuleFlowList != null && veryRuleFlowList.size() > 0 && veryRuleFlowList.get(0).getId() > 0) {
					Map<String, Object> dataAllParam = new HashMap<String, Object>();
					List<VeryRuleFlowModel> veryRuleFlowAllList = veryRuleFlowMbService.selectVeryRuleFlowList(dataAllParam);
					List<VeryRuleFlowModel> veryRuleFlowSubList = new ArrayList<VeryRuleFlowModel>();
					getSubVeryRuleFlow(veryRuleFlowList.get(0).getRuleFlowCode(), veryRuleFlowAllList, veryRuleFlowSubList);
					veryRuleFlowSubList.add(0,veryRuleFlowList.get(0));
					Map<String, Object> dataStatusParam = new HashMap<String, Object>();
					veryRuleFlowSubList.stream().forEach(vrf->{
						dataStatusParam.put("id", vrf.getId());
						dataStatusParam.put("status", param.getInteger("status"));
						dataStatusParam.put("version", vrf.getVersion());
						dataStatusParam.put("versionTo", vrf.getVersion() + 1);
						veryRuleFlowMbService.updateById(dataStatusParam);
					});
				} else {
					res.setErrorDesc("规则流不存在");
					res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
					res.setServerTime(System.currentTimeMillis());
					return res;
				}
				res.setErrorCode(0);
			} catch (Exception e) {
				logger.error(e.getMessage());
				res.setErrorDesc(e.getMessage());
			}
			res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
			res.setServerTime(System.currentTimeMillis());
			return res;
		}

	// {"id":1,"ruleFlowName":"test1","groupName":"test1","ruleFlowDesc":"test1","status":"0"}
	@RequestMapping(value = "/updateVeryRuleFlowAndTemplet", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowTempletCode,ruleFlowTemplet,id", ruleErrMsg = "不能为空")
	public RestApiResponse updateVeryRuleFlowTemplet(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleFlowTempletCode", param.getString("ruleFlowTempletCode"));
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			VeryRuleFlowTempletModel veryRuleFlowTempletData = new VeryRuleFlowTempletModel();
			veryRuleFlowTempletData.setRuleFlowTemplet(param.getString("ruleFlowTemplet"));
			veryRuleFlowTempletData.setRuleFlowTempletCode(param.getString("ruleFlowTempletCode"));
			Date time = new Date();
			String createId = param.getString("createId");
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
			if (veryRuleFlowList.size() > 0) {
				boolean isHave = false;
				if (veryRuleFlowList.stream().filter(rf -> (rf.getId() - param.getIntValue("id")) != 0).count() > 0) {
					isHave = true;
				}
				if (isHave) {
					res.setErrorDesc("该规则模板标识已存在");
					res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
					res.setServerTime(System.currentTimeMillis());
					return res;
				}
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletMbService
						.selectVeryRuleFlowTempletList(dataParam);
				if (veryRuleFlowTempletList.size() > 0) {
					dataParam.put("id", veryRuleFlowTempletList.get(0).getId());
					dataParam.put("ruleFlowTemplet", param.getString("ruleFlowTemplet"));
					dataParam.put("version", veryRuleFlowTempletList.get(0).getVersion());
					dataParam.put("versionTo", veryRuleFlowTempletList.get(0).getVersion() + 1);
					veryRuleFlowTempletService.updateRuleFlowTemplet(dataParam, veryRuleFlowTempletData);
				} else {
					veryRuleFlowTempletMbService.save(veryRuleFlowTempletData);
				}
			} else {
				Map<String, Object> dataFlowParam = new HashMap<String, Object>();
				dataFlowParam.put("id", param.getIntValue("id"));
				veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataFlowParam);
				dataParam.put("version", veryRuleFlowList.get(0).getVersion());
				dataParam.put("versionTo", veryRuleFlowList.get(0).getVersion() + 1);
				dataParam.put("id", param.getIntValue("id"));
				dataParam.put("ruleFlowTempletCode", param.getString("ruleFlowTempletCode"));
				veryRuleFlowService.addRuleFlowTemplet(dataParam, veryRuleFlowTempletData);
			}
			res.setErrorCode(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	// {"ruleFlowTempletCode":"test1"}
	@RequestMapping(value = "/delVeryRuleFlowAndTemplet", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowTempletCode", ruleErrMsg = "不能为空")
	public RestApiResponse delVeryRuleFlowAndTemplet(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleFlowTempletCode", param.getString("ruleFlowTempletCode"));
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			if (veryRuleFlowList.size() > 0) {
				Map<String, Object> dataDelParam = new HashMap<String, Object>();
				dataDelParam.put("id", veryRuleFlowList.get(0).getId());
				dataDelParam.put("ruleFlowTempletCode", "");
				dataDelParam.put("version", veryRuleFlowList.get(0).getVersion());
				dataDelParam.put("versionTo", veryRuleFlowList.get(0).getVersion() + 1);
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletMbService
						.selectVeryRuleFlowTempletList(dataParam);
				if (veryRuleFlowTempletList.size() > 0) {
					veryRuleFlowService.delRuleFlowTemplet(dataDelParam, veryRuleFlowTempletList.get(0));
				} else {
					veryRuleFlowService.delRuleFlowTemplet(dataDelParam, null);
				}
			}
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
	@RequestMapping(value = "/delVeryRuleFlow", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "id", ruleErrMsg = "不能为空")
	public RestApiResponse delVeryRuleFlow(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Integer id = param.getInteger("id");
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("id", 111);
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			if (veryRuleFlowList.size() > 0) {
				Map<String, Object> dataAllParam = new HashMap<String, Object>();
				List<VeryRuleFlowModel> veryRuleFlowAllList = veryRuleFlowMbService.selectVeryRuleFlowList(dataAllParam);
				List<VeryRuleFlowModel> veryRuleFlowSubList = new ArrayList<VeryRuleFlowModel>();
				getSubVeryRuleFlow(veryRuleFlowList.get(0).getRuleFlowCode(), veryRuleFlowAllList, veryRuleFlowSubList);
				veryRuleFlowSubList.add(0,veryRuleFlowList.get(0));
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletModelList = new ArrayList<VeryRuleFlowTempletModel>();
				if(veryRuleFlowSubList.size()>0) {
					Set<String> veryRuleFlowTempletSet = new HashSet<String>();
					veryRuleFlowSubList.stream().forEach(vrfs->{
						if(StringUtils.isNotBlank(vrfs.getRuleFlowTempletCode())) {
							veryRuleFlowTempletSet.add(vrfs.getRuleFlowTempletCode());
						}
					});
					if(veryRuleFlowTempletSet.size()>0) {
						Map<String, Object> dataTempletParam = new HashMap<String, Object>();
						dataTempletParam.put("ruleFlowTempletCodes", veryRuleFlowTempletSet);
						veryRuleFlowTempletModelList = veryRuleFlowTempletMbService.selectVeryRuleFlowTempletList(dataTempletParam);
					}
					
				}
				veryRuleFlowService.deleteRuleFlow(veryRuleFlowSubList, veryRuleFlowTempletModelList);
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
	
	@RequestMapping(value = "/copyVeryRuleFlow", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowCode,ruleFlowName,parentRuleFlowCode,id", ruleErrMsg = "不能为空")
	public RestApiResponse copyVeryRuleFlow(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			VeryRuleFlowModel firstRuleFlow = veryRuleFlowList.stream().filter(vrf->vrf.getId() == param.getInteger("id")).collect(Collectors.toList()).get(0);
			String parentRuleFlowCode = firstRuleFlow.getRuleFlowCode();
			firstRuleFlow.setId(0);
			firstRuleFlow.setParentRuleFlowCode(param.getString("parentRuleFlowCode"));
			firstRuleFlow.setRuleFlowCode(param.getString("ruleFlowCode"));
			firstRuleFlow.setRuleFlowName(param.getString("ruleFlowName"));
			firstRuleFlow.setRuleFlowTempletCode(firstRuleFlow.getRuleFlowTempletCode()+"-"+System.currentTimeMillis());
			List<VeryRuleFlowModel> veryRuleFlowResultList = new ArrayList<VeryRuleFlowModel>();
			getVeryRuleFlowByParent(firstRuleFlow,parentRuleFlowCode,veryRuleFlowList,veryRuleFlowResultList);
			veryRuleFlowResultList.add(0,firstRuleFlow);
			if(veryRuleFlowResultList.size()>0) {
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = new ArrayList<VeryRuleFlowTempletModel>();
				Set<String> ruleFlowTempletCodeSet = new HashSet<String>();
				veryRuleFlowResultList.stream().forEach(vrf->{
					if(StringUtils.isNotBlank(vrf.getRuleFlowTempletCode())) {
						ruleFlowTempletCodeSet.add(vrf.getRuleFlowTempletCode().split("-")[0]);
					}
				});
				dataParam.put("ruleFlowTempletCodes", ruleFlowTempletCodeSet);
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletModelList = veryRuleFlowTempletMbService.selectVeryRuleFlowTempletList(dataParam);
				veryRuleFlowResultList.stream().forEach(vrf->{
					if(StringUtils.isNotBlank(vrf.getRuleFlowTempletCode())) {
						String ruleFlowTempletCode = vrf.getRuleFlowTempletCode().split("-")[0];
						if(veryRuleFlowTempletModelList.stream().filter(vrft->ruleFlowTempletCode.equals(vrft.getRuleFlowTempletCode())).count()>0) {
							VeryRuleFlowTempletModel veryRuleFlowTempletModel = veryRuleFlowTempletModelList.stream().filter(vrft->ruleFlowTempletCode.equals(vrft.getRuleFlowTempletCode())).collect(Collectors.toList()).get(0);
							veryRuleFlowTempletModel.setId(0);
							veryRuleFlowTempletModel.setVersion(1);
							veryRuleFlowTempletModel.setRuleFlowTempletCode(vrf.getRuleFlowTempletCode());
							veryRuleFlowTempletList.add(veryRuleFlowTempletModel);
						}
					}
				});
				veryRuleFlowService.copyRuleFlow(veryRuleFlowResultList, veryRuleFlowTempletList);
			}
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
	@RequestMapping(value = "/testVeryRuleFlow", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowTemplet", ruleErrMsg = "不能为空")
	public RestApiResponse testVeryRuleFlow(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			List<RuleBo> ruleList = JSON.parseArray(param.getString("ruleFlowTemplet"),RuleBo.class);
			RuleResult result = veryRule.fireTest(Arrays.asList(ruleList.get(0)));
			res.setBody(result);
			res.setErrorCode(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}
	
	public List<VeryRuleFlowModel> getSubVeryRuleFlow(String parentRuleFlowCode,List<VeryRuleFlowModel> veryRuleFlowList,List<VeryRuleFlowModel> veryRuleFlowResultList){
		List<VeryRuleFlowModel> subVeryRuleFlowList = veryRuleFlowList.stream().filter(vrf->vrf.getParentRuleFlowCode().equals(parentRuleFlowCode)).collect(Collectors.toList());
		if(subVeryRuleFlowList.size()>0) {
			subVeryRuleFlowList.stream().forEach(srf->{
				veryRuleFlowResultList.add(srf);
			});
			for(VeryRuleFlowModel srf:subVeryRuleFlowList) {
				return getSubVeryRuleFlow(srf.getRuleFlowCode(),veryRuleFlowList,veryRuleFlowResultList);
			}
		}
		return veryRuleFlowResultList;
	}
	
	public List<VeryRuleFlowModel> getVeryRuleFlowByParent(VeryRuleFlowModel prevRuleFlow,String parentRuleFlowCode,List<VeryRuleFlowModel> veryRuleFlowList,List<VeryRuleFlowModel> veryRuleFlowResultList){
		List<VeryRuleFlowModel> subVeryRuleFlowList = veryRuleFlowList.stream().filter(vrf->vrf.getParentRuleFlowCode().equals(parentRuleFlowCode)).collect(Collectors.toList());
		if(subVeryRuleFlowList.size()>0) {
			subVeryRuleFlowList.stream().forEach(srf->{
				VeryRuleFlowModel ruleFlow = JSON.parseObject(JSON.toJSONString(srf),VeryRuleFlowModel.class);
				ruleFlow.setId(0);
				ruleFlow.setRuleFlowCode(prevRuleFlow.getRuleFlowCode()+srf.getRuleFlowCode());
				ruleFlow.setRuleFlowName(srf.getRuleFlowName()+"副本");
				ruleFlow.setParentRuleFlowCode(prevRuleFlow.getRuleFlowCode());
				ruleFlow.setVersion(1);
				if(StringUtils.isNotBlank(srf.getRuleFlowTempletCode())) {
					ruleFlow.setRuleFlowTempletCode(srf.getRuleFlowTempletCode()+"-"+System.currentTimeMillis());
				}
				veryRuleFlowResultList.add(ruleFlow);
			});
			for(VeryRuleFlowModel srf:subVeryRuleFlowList) {
				VeryRuleFlowModel prevRule = new VeryRuleFlowModel();
				prevRule.setRuleFlowCode(prevRuleFlow.getRuleFlowCode()+srf.getRuleFlowCode());
				prevRule.setRuleFlowName(srf.getRuleFlowName()+"副本");
				prevRule.setParentRuleFlowCode(prevRuleFlow.getRuleFlowCode());
				return getVeryRuleFlowByParent(prevRule,srf.getRuleFlowCode(),veryRuleFlowList,veryRuleFlowResultList);
			}
		}
		return veryRuleFlowResultList;
	}

}
