package com.lexinda.veryrule.platform.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletLogMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletMbService;

/**
 * 
 * @author lexinda
 *
 */
@Service
public class VeryRuleFlowTempletService{

	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletMbService;
	
	@Autowired
	private VeryRuleFlowTempletLogMbService veryRuleFlowTempletLogMbService;
	
	@Transactional(rollbackFor = {RuntimeException.class, Error.class})
	@Commit
	public void updateRuleFlowTemplet(Map<String, Object> updateParam,VeryRuleFlowTempletModel veryRuleFlowTempletData) {
		veryRuleFlowTempletMbService.updateById(updateParam);
		veryRuleFlowTempletLogMbService.save(veryRuleFlowTempletData);
	}

}
