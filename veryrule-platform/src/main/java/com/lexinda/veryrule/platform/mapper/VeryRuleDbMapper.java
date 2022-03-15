package com.lexinda.veryrule.platform.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:46:00
* 类说明
*/

@Mapper
public interface VeryRuleDbMapper extends BaseMapper<VeryRuleFlowModel> {
	
    void createVeryRuleFlow();
	
	void createVeryRuleElement();
	
	void createVeryRuleFlowTemplet();
	
	void createVeryRuleFlowTempletLog();
}


