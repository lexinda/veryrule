package com.lexinda.veryrule.platform.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;

/**
 * 
 * @author lexinda
 *
 */

@Mapper
public interface VeryRuleDbMapper extends BaseMapper<VeryRuleFlowModel> {
	
    void createVeryRuleFlow();
	
	void createVeryRuleElement();
	
	void createVeryRuleFlowTemplet();
	
	void createVeryRuleFlowTempletLog();
	
	void createVeryRuleScene();
}


