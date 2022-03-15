package com.lexinda.veryrule.platform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletLogMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletMbService;

/**
 * @author zhumengle
 * @version 创建时间：2022-3-8 18:56:27 类说明
 */
@Service
public class VeryRuleFlowService{

	@Autowired
	private VeryRuleFlowMbService veryRuleFlowMbService;

	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletMbService;

	@Autowired
	private VeryRuleFlowTempletLogMbService veryRuleFlowTempletLogMbService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class })
	public void addRuleFlowTemplet(Map<String, Object> updateParam, VeryRuleFlowTempletModel veryRuleFlowTempletData) {
		veryRuleFlowMbService.updateById(updateParam);
		veryRuleFlowTempletMbService.save(veryRuleFlowTempletData);
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class })
	public void delRuleFlowTemplet(Map<String, Object> updateParam, VeryRuleFlowTempletModel veryRuleFlowTempletData) {
		veryRuleFlowMbService.updateById(updateParam);
		if (veryRuleFlowTempletData != null) {
			veryRuleFlowTempletMbService.removeById(veryRuleFlowTempletData.getId());
			veryRuleFlowTempletData.setId(0);
			veryRuleFlowTempletLogMbService.save(veryRuleFlowTempletData);
		}
	}
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class })
	public void copyRuleFlow(List<VeryRuleFlowModel> veryRuleFlowList, List<VeryRuleFlowTempletModel> veryRuleFlowTempletList) {
		if (veryRuleFlowList.size()>0) {
			veryRuleFlowList.forEach(vrf->{
				veryRuleFlowMbService.save(vrf);
			});
		}
		if (veryRuleFlowTempletList.size()>0) {
			veryRuleFlowTempletList.forEach(vrft->{
				veryRuleFlowTempletMbService.save(vrft);
			});
		}
	}
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class })
	public void deleteRuleFlow(List<VeryRuleFlowModel> veryRuleFlowSubList, List<VeryRuleFlowTempletModel> veryRuleFlowTempletList) {
		Map<String, Object> dataFlowParam = new HashMap<String, Object>();
		if(veryRuleFlowSubList.size()>0) {
			veryRuleFlowSubList.forEach(vrf->{
				dataFlowParam.put("id", vrf.getId());
				veryRuleFlowMbService.deleteById(dataFlowParam);
			});
			
		}
		Map<String, Object> dataTempletParam = new HashMap<String, Object>();
		if (veryRuleFlowTempletList.size()>0) {
			veryRuleFlowTempletList.forEach(vrft->{
				dataTempletParam.put("id", vrft.getId());
				veryRuleFlowTempletMbService.deleteById(dataTempletParam);
				vrft.setId(0);
				veryRuleFlowTempletLogMbService.save(vrft);
			});
			
		}
	}

}
