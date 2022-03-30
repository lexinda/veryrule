package com.lexinda.veryrule.platform.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;

/**
 * 
 * @author lexinda
 *
 */

@Mapper
public interface VeryRuleFlowMapper extends BaseMapper<VeryRuleFlowModel> {
	
    List<VeryRuleFlowModel> selectVeryRuleFlowList(Page<VeryRuleFlowModel> page,@Param("pm") Map<String, Object> param);
	
    List<VeryRuleFlowModel> selectVeryRuleFlowList(@Param("pm") Map<String, Object> param);
	
    int updateById(@Param("pm") Map<String, Object> param);
	
	int deleteById(@Param("pm") Map<String, Object> param);
	
}


