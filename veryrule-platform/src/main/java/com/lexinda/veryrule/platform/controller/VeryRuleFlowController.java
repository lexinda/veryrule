package com.lexinda.veryrule.platform.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.annotation.VeryRuleSingle;
import com.lexinda.veryrule.base.key.RuleCode;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.platform.model.VeryRuleDocumentModel;
import com.lexinda.veryrule.platform.model.VeryRuleDocumentModel.Param;
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

	@Value("${veryrule.scene.path}")
	private String scenePath;

	@Value("${server.name}")
	private String serverName;

	@RequestMapping(value = "/getVeryRuleFlowPage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//	@VeryRuleFlow(ruleFlowCode = "test")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "parentRuleFlowCode,currentPage", ruleErrMsg = "不能为空")
	public RestApiResponse getVeryRuleFlowPage(String data, RuleResult ruleResult) throws Exception {
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
			String ruleFlowDocument = param.getString("ruleFlowDocument");
			if (StringUtils.isNotBlank(ruleFlowDocument)) {
				veryRuleFlowData.setRuleFlowDocument(ruleFlowDocument);
			} else {
				veryRuleFlowData.setRuleFlowDocument("");
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
			dataParam.put("ruleSceneId", param.getString("ruleSceneId"));
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
				List<VeryRuleFlowModel> veryRuleFlowAllList = veryRuleFlowMbService
						.selectVeryRuleFlowList(dataAllParam);
				List<VeryRuleFlowModel> veryRuleFlowSubList = new ArrayList<VeryRuleFlowModel>();
				getSubVeryRuleFlow(veryRuleFlowList.get(0).getRuleFlowCode(), veryRuleFlowAllList, veryRuleFlowSubList);
				veryRuleFlowSubList.add(0, veryRuleFlowList.get(0));
				Map<String, Object> dataStatusParam = new HashMap<String, Object>();
				veryRuleFlowSubList.stream().forEach(vrf -> {
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
			dataParam.put("id", id);
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			if (veryRuleFlowList.size() > 0) {
				Map<String, Object> dataAllParam = new HashMap<String, Object>();
				List<VeryRuleFlowModel> veryRuleFlowAllList = veryRuleFlowMbService
						.selectVeryRuleFlowList(dataAllParam);
				List<VeryRuleFlowModel> veryRuleFlowSubList = new ArrayList<VeryRuleFlowModel>();
				getSubVeryRuleFlow(veryRuleFlowList.get(0).getRuleFlowCode(), veryRuleFlowAllList, veryRuleFlowSubList);
				veryRuleFlowSubList.add(0, veryRuleFlowList.get(0));
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletModelList = new ArrayList<VeryRuleFlowTempletModel>();
				if (veryRuleFlowSubList.size() > 0) {
					Set<String> veryRuleFlowTempletSet = new HashSet<String>();
					veryRuleFlowSubList.stream().forEach(vrfs -> {
						if (StringUtils.isNotBlank(vrfs.getRuleFlowTempletCode())) {
							veryRuleFlowTempletSet.add(vrfs.getRuleFlowTempletCode());
						}
					});
					if (veryRuleFlowTempletSet.size() > 0) {
						Map<String, Object> dataTempletParam = new HashMap<String, Object>();
						dataTempletParam.put("ruleFlowTempletCodes", veryRuleFlowTempletSet);
						veryRuleFlowTempletModelList = veryRuleFlowTempletMbService
								.selectVeryRuleFlowTempletList(dataTempletParam);
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
			VeryRuleFlowModel firstRuleFlow = veryRuleFlowList.stream()
					.filter(vrf -> vrf.getId() == param.getInteger("id")).collect(Collectors.toList()).get(0);
			String parentRuleFlowCode = firstRuleFlow.getRuleFlowCode();
			firstRuleFlow.setId(0);
			firstRuleFlow.setParentRuleFlowCode(param.getString("parentRuleFlowCode"));
			firstRuleFlow.setRuleFlowCode(param.getString("ruleFlowCode"));
			firstRuleFlow.setRuleFlowName(param.getString("ruleFlowName"));
			firstRuleFlow
					.setRuleFlowTempletCode(firstRuleFlow.getRuleFlowTempletCode() + "-" + System.currentTimeMillis());
			List<VeryRuleFlowModel> veryRuleFlowResultList = new ArrayList<VeryRuleFlowModel>();
			getVeryRuleFlowByParent(firstRuleFlow, parentRuleFlowCode, veryRuleFlowList, veryRuleFlowResultList);
			veryRuleFlowResultList.add(0, firstRuleFlow);
			if (veryRuleFlowResultList.size() > 0) {
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = new ArrayList<VeryRuleFlowTempletModel>();
				Set<String> ruleFlowTempletCodeSet = new HashSet<String>();
				veryRuleFlowResultList.stream().forEach(vrf -> {
					if (StringUtils.isNotBlank(vrf.getRuleFlowTempletCode())) {
						ruleFlowTempletCodeSet.add(vrf.getRuleFlowTempletCode().split("-")[0]);
					}
				});
				dataParam.put("ruleFlowTempletCodes", ruleFlowTempletCodeSet);
				List<VeryRuleFlowTempletModel> veryRuleFlowTempletModelList = veryRuleFlowTempletMbService
						.selectVeryRuleFlowTempletList(dataParam);
				veryRuleFlowResultList.stream().forEach(vrf -> {
					if (StringUtils.isNotBlank(vrf.getRuleFlowTempletCode())) {
						String ruleFlowTempletCode = vrf.getRuleFlowTempletCode().split("-")[0];
						if (veryRuleFlowTempletModelList.stream()
								.filter(vrft -> ruleFlowTempletCode.equals(vrft.getRuleFlowTempletCode()))
								.count() > 0) {
							VeryRuleFlowTempletModel veryRuleFlowTempletModel = veryRuleFlowTempletModelList.stream()
									.filter(vrft -> ruleFlowTempletCode.equals(vrft.getRuleFlowTempletCode()))
									.collect(Collectors.toList()).get(0);
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
	public RestApiResponse testVeryRuleFlow(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			String ruleFlowTemplet = param.getString("ruleFlowTemplet");
			String ruleFlowTempletCode = param.getString("ruleFlowTempletCode");
			if (StringUtils.isNotBlank(ruleFlowTemplet) || StringUtils.isNotBlank(ruleFlowTempletCode)) {
				List<RuleBo> ruleList = new ArrayList<RuleBo>();
				if (StringUtils.isNotBlank(ruleFlowTemplet)) {
					ruleList = JSON.parseArray(ruleFlowTemplet, RuleBo.class);
				} else {
					Map<String, Object> dataParam = new HashMap<String, Object>();
					dataParam.put("ruleFlowTempletCode", ruleFlowTempletCode);
					List<VeryRuleFlowTempletModel> veryRuleFlowTempletList = veryRuleFlowTempletMbService
							.selectVeryRuleFlowTempletList(dataParam);
					if (veryRuleFlowTempletList.size() > 0) {
						ruleList = JSON.parseArray(veryRuleFlowTempletList.get(0).getRuleFlowTemplet(), RuleBo.class);
					}
				}
				if (ruleList.size() > 0) {
					RuleResult ruleResult = veryRule.fireTest(ruleList);
					Map<String, String> result = new LinkedHashMap<String, String>();
					ruleList.forEach(rule -> {
						if (ruleResult.getResult().get(rule.getRuleCode()) != null) {
							result.put(rule.getRuleCode(), ruleResult.getResult().get(rule.getRuleCode()).toString());
						} else {
							result.put(rule.getRuleCode(), "");
						}
					});
					res.setBody(result);
					res.setErrorCode(0);
				} else {
					res.setErrorDesc("无此规则");
				}
			} else {
				res.setErrorDesc("无效规则");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setErrorDesc(e.getMessage());
		}
		res.setElapsedTime(System.currentTimeMillis() - res.getElapsedTime());
		res.setServerTime(System.currentTimeMillis());
		return res;
	}

	@RequestMapping(value = "/getDocumentInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowCode", ruleErrMsg = "不能为空")
	public RestApiResponse getDocumentInfo(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			String ruleFlowCode = param.getString("ruleFlowCode");
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleFlowCode", ruleFlowCode);
			List<VeryRuleFlowModel> veryRuleFlowTempletList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			String ruleFlowDocument = "";
			if (veryRuleFlowTempletList.size() > 0) {
				ruleFlowDocument = veryRuleFlowTempletList.get(0).getRuleFlowDocument();
			}
			Map<String, String> result = new LinkedHashMap<String, String>();
			result.put("ruleFlowDocument", ruleFlowDocument);
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

	@RequestMapping(value = "/updateVeryRuleFlowDocument", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowCode,ruleFlowDocument", ruleErrMsg = "不能为空")
	public RestApiResponse updateVeryRuleFlowDocument(String data) throws Exception {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(1);
		res.setElapsedTime(System.currentTimeMillis());
		try {
			JSONObject param = JSON.parseObject(data);
			Map<String, Object> dataParam = new HashMap<String, Object>();
			dataParam.put("ruleFlowCode", param.getString("ruleFlowCode"));
			List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
			if (veryRuleFlowList.size() > 0) {
				Map<String, Object> dataUpdateParam = new HashMap<String, Object>();
				dataUpdateParam.put("id", veryRuleFlowList.get(0).getId());
				dataUpdateParam.put("ruleFlowDocument", param.getString("ruleFlowDocument"));
				dataUpdateParam.put("versionTo", veryRuleFlowList.get(0).getVersion() + 1);
				dataUpdateParam.put("version", veryRuleFlowList.get(0).getVersion());
				veryRuleFlowMbService.updateById(dataUpdateParam);
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

	public List<VeryRuleFlowModel> getSubVeryRuleFlow(String parentRuleFlowCode,
			List<VeryRuleFlowModel> veryRuleFlowList, List<VeryRuleFlowModel> veryRuleFlowResultList) {
		List<VeryRuleFlowModel> subVeryRuleFlowList = veryRuleFlowList.stream()
				.filter(vrf -> vrf.getParentRuleFlowCode().equals(parentRuleFlowCode)).collect(Collectors.toList());
		if (subVeryRuleFlowList.size() > 0) {
			subVeryRuleFlowList.stream().forEach(srf -> {
				veryRuleFlowResultList.add(srf);
			});
			for (VeryRuleFlowModel srf : subVeryRuleFlowList) {
				return getSubVeryRuleFlow(srf.getRuleFlowCode(), veryRuleFlowList, veryRuleFlowResultList);
			}
		}
		return veryRuleFlowResultList;
	}

	public List<VeryRuleFlowModel> getVeryRuleFlowByParent(VeryRuleFlowModel prevRuleFlow, String parentRuleFlowCode,
			List<VeryRuleFlowModel> veryRuleFlowList, List<VeryRuleFlowModel> veryRuleFlowResultList) {
		List<VeryRuleFlowModel> subVeryRuleFlowList = veryRuleFlowList.stream()
				.filter(vrf -> vrf.getParentRuleFlowCode().equals(parentRuleFlowCode)).collect(Collectors.toList());
		if (subVeryRuleFlowList.size() > 0) {
			subVeryRuleFlowList.stream().forEach(srf -> {
				VeryRuleFlowModel ruleFlow = JSON.parseObject(JSON.toJSONString(srf), VeryRuleFlowModel.class);
				ruleFlow.setId(0);
				ruleFlow.setRuleFlowCode(prevRuleFlow.getRuleFlowCode() + srf.getRuleFlowCode());
				ruleFlow.setRuleFlowName(srf.getRuleFlowName() + "副本");
				ruleFlow.setParentRuleFlowCode(prevRuleFlow.getRuleFlowCode());
				ruleFlow.setVersion(1);
				if (StringUtils.isNotBlank(srf.getRuleFlowTempletCode())) {
					ruleFlow.setRuleFlowTempletCode(srf.getRuleFlowTempletCode() + "-" + System.currentTimeMillis());
				}
				veryRuleFlowResultList.add(ruleFlow);
			});
			for (VeryRuleFlowModel srf : subVeryRuleFlowList) {
				VeryRuleFlowModel prevRule = new VeryRuleFlowModel();
				prevRule.setRuleFlowCode(prevRuleFlow.getRuleFlowCode() + srf.getRuleFlowCode());
				prevRule.setRuleFlowName(srf.getRuleFlowName() + "副本");
				prevRule.setParentRuleFlowCode(prevRuleFlow.getRuleFlowCode());
				return getVeryRuleFlowByParent(prevRule, srf.getRuleFlowCode(), veryRuleFlowList,
						veryRuleFlowResultList);
			}
		}
		return veryRuleFlowResultList;
	}

	// 业务场景详情页面
	@RequestMapping(value = "/showSceneInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "ruleFlowTempletCode", ruleErrMsg = "不能为空")
	public RestApiResponse showSceneInfo(String data) {
		RestApiResponse res = new RestApiResponse();
		res.setErrorCode(0);
		List<String> imageList = new ArrayList<String>();
		try {
			JSONObject param = JSON.parseObject(data);
			String sceneImgPath = scenePath + File.separator + param.getString("ruleFlowTempletCode");
			File file = new File(sceneImgPath);
			if (file.exists()) {
				File[] files = file.listFiles();
				for (File fileItem : files) {
					imageList.add(serverName + "/veryrule/sceneImage/" + param.getString("ruleFlowTempletCode") + "/"
							+ fileItem.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.setBody(imageList);
		return res;
	}

	@RequestMapping(value = "/sceneImage/{path}/{img}", method = RequestMethod.GET)
	public void sceneImage(HttpServletRequest request, HttpServletResponse response, @PathVariable("path") String path,
			@PathVariable("img") String img) throws IOException {
		response.setContentType("image/png");
		try (OutputStream out = response.getOutputStream()) {
			// 获取图片存放路径
			FileInputStream ips = null;
			try {
				String sceneImage = scenePath + File.separator + path + File.separator + img;
				ips = new FileInputStream(new File(sceneImage));
				// 读取文件流

				int len = 0;

				byte[] buffer = new byte[1024 * 10];

				while ((len = ips.read(buffer)) != -1) {

					out.write(buffer, 0, len);

				}

				out.flush();

			}

			catch (Exception e) {

				e.printStackTrace();

			}

			finally {

				out.close();

				ips.close();

			}
		}
	}

	static String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
			+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>" + "<w:lvl w:ilvl=\"0\">" + "<w:start w:val=\"1\"/>"
			+ "<w:numFmt w:val=\"decimal\"/>" + "<w:lvlText w:val=\"%1、\"/>" + "<w:lvlJc w:val=\"left\"/>" + "<w:pPr>"
			+ "<w:ind w:left=\"720\" w:hanging=\"360\"/>" + "</w:pPr>" + "</w:lvl>"
			+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\">" + "<w:start w:val=\"1\"/>" + "<w:numFmt w:val=\"decimal\"/>"
			+ "<w:lvlText w:val=\"%1.%2\"/>" + "<w:lvlJc w:val=\"left\"/>" + "<w:pPr>"
			+ "<w:ind w:left=\"1440\" w:hanging=\"360\"/>" + "</w:pPr>" + "</w:lvl>"
			+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\">" + "<w:start w:val=\"1\"/>" + "<w:numFmt w:val=\"decimal\"/>"
			+ "<w:lvlText w:val=\"%1.%2.%3\"/>" + "<w:lvlJc w:val=\"left\"/>" + "<w:pPr>"
			+ "<w:ind w:left=\"2160\" w:hanging=\"360\"/>" + "</w:pPr>" + "</w:lvl>" + "</w:abstractNum>";

	public static void createMergeHCell(XWPFTableRow row, int from, int to, String text, boolean showColor) {
		for (int colIndex = from; colIndex < to; colIndex++) {
			XWPFTableCell cell = row.getCell(colIndex);
			if (cell == null) {
				cell = row.createCell();
			}
			cell.setText(text);
			if (showColor) {
				cell.getCTTc().addNewTcPr().addNewShd().setFill("6495ED");
			}
			if (colIndex == from) {
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			} else {
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
	}
	
	@RequestMapping(value = "/downVeryRuleFlowDocument", method = RequestMethod.GET)
	public void downVeryRuleFlowDocument(HttpServletRequest request, HttpServletResponse response) throws IOException, XmlException {
		Map<String,Object> dataParam = new HashMap<String,Object>();
		dataParam.put("ruleFlowCode", request.getParameter("ruleFlowCode"));
		List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
		String ruleFlowName = "",ruleDocument = "";
		if(veryRuleFlowList.size()>0) {
			VeryRuleFlowModel veryRuleFlowModel = veryRuleFlowList.get(0);
			ruleFlowName = veryRuleFlowModel.getRuleFlowName();
			ruleDocument = veryRuleFlowModel.getRuleFlowDocument();
		}
//		String str = "[{\"id\":2,\"domains\":[{\"key\":\"beta\",\"value\":\"2\"}],\"name\":\"2\",\"author\":\"2\",\"key\":\"2\",\"request\":{\"type\":\"post\",\"contentType\":\"application/json\"},\"paramIn\":[{\"id\":0,\"field\":\"\",\"require\":\"是\",\"dtype\":\"String\",\"desc\":\"\"}],\"paramInExample\":\"2\",\"paramOut\":[{\"id\":0,\"field\":\"\",\"require\":\"是\",\"dtype\":\"String\",\"desc\":\"\"}],\"paramOutExample\":\"2\",\"desc\":\"2\"},{\"id\":2,\"domains\":[{\"key\":\"beta\",\"value\":\"1\"}],\"name\":\"1\",\"author\":\"1\",\"key\":\"1\",\"request\":{\"type\":\"post\",\"contentType\":\"application/json\"},\"paramIn\":[{\"id\":0,\"field\":\"\",\"require\":\"是\",\"dtype\":\"String\",\"desc\":\"\"}],\"paramInExample\":\"\",\"paramOut\":[{\"id\":0,\"field\":\"\",\"require\":\"是\",\"dtype\":\"String\",\"desc\":\"\"}],\"paramOutExample\":\"\",\"desc\":\"\"}]";
		List<VeryRuleDocumentModel> veryRuleDocumentModelList = JSON.parseArray(ruleDocument, VeryRuleDocumentModel.class);
		XWPFDocument document = new XWPFDocument();

		{

			XWPFParagraph paragraph = document.createParagraph();

			XWPFRun run = paragraph.createRun();

			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);

			CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);

			XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);

			XWPFNumbering numbering = document.createNumbering();

			BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);

			BigInteger numID = numbering.addNum(abstractNumID);

			System.out.println("numID: " + numID);

			for (VeryRuleDocumentModel veryRuleDocumentModel : veryRuleDocumentModelList) {

				paragraph = document.createParagraph();

				paragraph.setNumID(numID);

				run = paragraph.createRun();

				run.setText(veryRuleDocumentModel.getName());

				XWPFTable table = document.createTable();
				table.setWidth(5 * 1300);
				CTTblPr tblPr =  table.getCTTbl().getTblPr();
				CTTblLayoutType t = tblPr.isSetTblLayout()?tblPr.getTblLayout():tblPr.addNewTblLayout();
				t.setType(STTblLayoutType.FIXED);

				// 接口地址
				XWPFTableRow row = table.getRow(0);
				List<VeryRuleDocumentModel.Domain> domains = veryRuleDocumentModel.getDomains();
				for (VeryRuleDocumentModel.Domain domain : domains) {
					createMergeHCell(row, 0, 5, "接口地址" + domain.getKey() + "：" + domain.getValue(), true);
				}
				// 负责人
				XWPFTableRow authorRow = table.createRow();
				createMergeHCell(authorRow, 0, 5, "负责人：" + veryRuleDocumentModel.getAuthor(), true);
				// Key
				XWPFTableRow keyRow = table.createRow();
				createMergeHCell(keyRow, 0, 5, "Key：" + veryRuleDocumentModel.getAuthor(), true);

				XWPFTableRow timeRow = table.createRow();
				createMergeHCell(timeRow, 0, 5, "接口对接时间（假接口）：", true);

				XWPFTableRow devTimeRow = table.createRow();
				createMergeHCell(devTimeRow, 0, 5, "接口Dev环境时间：", true);

				XWPFTableRow bodyRow = table.createRow();
				createMergeHCell(bodyRow, 0, 5, "业务入参(body部分)：", true);

				XWPFTableRow paramInTitle = table.createRow();
				XWPFTableCell cell = paramInTitle.getCell(0);
				cell.setText("入参字段");
				CTTcPr ctTcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
				CTTblWidth ctTblWidth = ctTcPr.addNewTcW();
				ctTblWidth.setW(BigInteger.valueOf(150));
				ctTblWidth.setType(STTblWidth.DXA);
				XWPFTableCell cell1 = paramInTitle.getCell(1);
				cell1.setText("字段名称");
				CTTcPr ctTcPr1 = cell1.getCTTc().isSetTcPr() ? cell1.getCTTc().getTcPr() : cell1.getCTTc().addNewTcPr();
				CTTblWidth ctTblWidth1 = ctTcPr1.addNewTcW();
				ctTblWidth1.setW(BigInteger.valueOf(100));
				ctTblWidth1.setType(STTblWidth.DXA);
				XWPFTableCell cell2 = paramInTitle.getCell(2);
				cell2.setText("类型");
				CTTcPr ctTcPr2 = cell2.getCTTc().isSetTcPr() ? cell2.getCTTc().getTcPr() : cell2.getCTTc().addNewTcPr();
				CTTblWidth ctTblWidth2 = ctTcPr2.addNewTcW();
				ctTblWidth2.setW(BigInteger.valueOf(50));
				ctTblWidth2.setType(STTblWidth.DXA);
				XWPFTableCell cell3 = paramInTitle.getCell(3);
				cell3.setText("必填");
				CTTcPr ctTcPr3 = cell3.getCTTc().isSetTcPr() ? cell3.getCTTc().getTcPr() : cell3.getCTTc().addNewTcPr();
				CTTblWidth ctTblWidth3 = ctTcPr3.addNewTcW();
				ctTblWidth3.setW(BigInteger.valueOf(50));
				ctTblWidth3.setType(STTblWidth.DXA);
				XWPFTableCell cell4 = paramInTitle.getCell(4);
				cell4.setText("说明");
				CTTcPr ctTcPr4 = cell4.getCTTc().isSetTcPr() ? cell4.getCTTc().getTcPr() : cell4.getCTTc().addNewTcPr();
				CTTblWidth ctTblWidth4 = ctTcPr4.addNewTcW();
				ctTblWidth4.setW(BigInteger.valueOf(150));
				ctTblWidth4.setType(STTblWidth.DXA);

				List<Param> paramIn = veryRuleDocumentModel.getParamIn();
				for (Param param : paramIn) {
					XWPFTableRow rowIn = table.createRow();
					for (int col = 0; col < 5; col++) {
						XWPFTableCell cellIn = rowIn.getCell(col);
						if (cellIn == null) {
							cellIn = rowIn.createCell();
						}
						switch (col) {
						case 0:
							cellIn.setText(param.getField());
							break;
						case 1:
							cellIn.setText(param.getName());
							break;
						case 2:
							cellIn.setText(param.getDtype());
							break;
						case 3:
							cellIn.setText(param.getRequire());
							break;
						case 4:
							cellIn.setText(param.getDesc());
							break;
						default:
							break;
						}
					}
				}
				// 举例
				XWPFTableRow exampleRow = table.createRow();
				createMergeHCell(exampleRow, 0, 5, "举例：", true);
				XWPFTableRow exampleDataRow = table.createRow();
				createMergeHCell(exampleDataRow, 0, 5, veryRuleDocumentModel.getParamInExample(), false);

				// 业务出参(状态部分)：
				XWPFTableRow paramStatusRow = table.createRow();
				createMergeHCell(paramStatusRow, 0, 5, "业务出参(状态部分)：", true);
				XWPFTableRow paramStatusTitle = table.createRow();
				XWPFTableCell cellStatusOut = paramStatusTitle.getCell(0);
				cellStatusOut.setText("出参字段");
				XWPFTableCell cellStatusOut1 = paramStatusTitle.getCell(1);
				cellStatusOut1.setText("字段名称");
				XWPFTableCell cellStatusOut2 = paramStatusTitle.getCell(2);
				cellStatusOut2.setText("类型");
				createMergeHCell(paramStatusTitle, 3, 5, "说明", false);

				for (int statusIndex = 0; statusIndex < 5; statusIndex++) {
					XWPFTableRow paramStatusDataRow = table.createRow();
					XWPFTableCell cellStatusOutData = paramStatusDataRow.getCell(0);
					XWPFTableCell cellStatusOutData1 = paramStatusDataRow.getCell(1);
					XWPFTableCell cellStatusOutData2 = paramStatusDataRow.getCell(2);
					switch (statusIndex) {
					case 0:
						cellStatusOutData.setText("body/data");
						cellStatusOutData1.setText("返回结果");
						cellStatusOutData2.setText("Object");
						createMergeHCell(paramStatusDataRow, 3, 5, "data:接口转发app专用", false);
						break;
					case 1:
						cellStatusOutData.setText("errorCode");
						cellStatusOutData1.setText("相应代码");
						cellStatusOutData2.setText("Integer");
						createMergeHCell(paramStatusDataRow, 3, 5, "成功0失败其他", false);
						break;
					case 2:
						cellStatusOutData.setText("errorDesc");
						cellStatusOutData1.setText("错误描述");
						cellStatusOutData2.setText("String");
						createMergeHCell(paramStatusDataRow, 3, 5, "", false);
						break;
					case 3:
						cellStatusOutData.setText("elapsedTime");
						cellStatusOutData1.setText("执行时间");
						cellStatusOutData2.setText("Long");
						createMergeHCell(paramStatusDataRow, 3, 5, "", false);
						break;
					case 4:
						cellStatusOutData.setText("serverTime");
						cellStatusOutData1.setText("服务器时间");
						cellStatusOutData2.setText("Long");
						createMergeHCell(paramStatusDataRow, 3, 5, "", false);
						break;
					default:
						break;
					}
				}

				// 出参
				XWPFTableRow paramOutRow = table.createRow();
				createMergeHCell(paramOutRow, 0, 5, "业务出参(body/data部分)：", true);
				XWPFTableRow paramOutTitle = table.createRow();
				XWPFTableCell cellOut = paramOutTitle.getCell(0);
				cellOut.setText("出参字段");
				XWPFTableCell cellOut1 = paramOutTitle.getCell(1);
				cellOut1.setText("字段名称");
				XWPFTableCell cellOut2 = paramOutTitle.getCell(2);
				cellOut2.setText("类型");
				createMergeHCell(paramOutTitle, 3, 5, "说明", false);

				List<Param> paramOut = veryRuleDocumentModel.getParamOut();
				for (Param param : paramOut) {
					XWPFTableRow rowOut = table.createRow();
					for (int col = 0; col < 5; col++) {
						XWPFTableCell outCell = rowOut.getCell(col);
						if (outCell == null) {
							outCell = rowOut.createCell();
						}
						switch (col) {
						case 0:
							outCell.setText(param.getField());
							break;
						case 1:
							outCell.setText(param.getName());
							break;
						case 2:
							outCell.setText(param.getDtype());
							break;
						case 3:
							createMergeHCell(rowOut, 3, 5, param.getDesc(), false);
							break;
						default:
							break;
						}
					}
				}
				// 举例
				XWPFTableRow exampleOutRow = table.createRow();
				createMergeHCell(exampleOutRow, 0, 5, "举例：", true);
				XWPFTableRow exampleOutDataRow = table.createRow();
				createMergeHCell(exampleOutDataRow, 0, 5, veryRuleDocumentModel.getParamOutExample(), false);
			}

			String fileName = ruleFlowName+"_"+System.currentTimeMillis()+".docx";
			response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "utf-8"));
            OutputStream out = response.getOutputStream();
            document.write(out);
            out.close();
		}
	
	}

	public static void main(String[] args) throws Exception {}
}
