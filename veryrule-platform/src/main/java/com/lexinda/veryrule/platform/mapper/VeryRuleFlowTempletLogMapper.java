package com.lexinda.veryrule.platform.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:46:00
* 类说明
*/

@Mapper
public interface VeryRuleFlowTempletLogMapper extends BaseMapper<VeryRuleFlowTempletModel> {
	
    List<VeryRuleFlowTempletModel> selectVeryRuleFlowTempletLogList(Page<VeryRuleFlowTempletModel> page,@Param("pm") Map<String, Object> param);
	
}


