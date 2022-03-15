package com.lexinda.veryrule.platform.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:46:00
* 类说明
*/

@Mapper
public interface VeryRuleFlowMapper extends BaseMapper<VeryRuleFlowModel> {
	
    List<VeryRuleFlowModel> selectVeryRuleFlowList(Page<VeryRuleFlowModel> page,@Param("pm") Map<String, Object> param);
	
    List<VeryRuleFlowModel> selectVeryRuleFlowList(@Param("pm") Map<String, Object> param);
	
    int updateById(@Param("pm") Map<String, Object> param);
	
	int deleteById(@Param("pm") Map<String, Object> param);
	
}


