package com.lexinda.veryrule.platform.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lexinda.veryrule.platform.model.VeryRuleSceneModel;

/**
 * 
 * @author lexinda
 *
 */

@Mapper
public interface VeryRuleSceneMapper extends BaseMapper<VeryRuleSceneModel> {
	
    List<VeryRuleSceneModel> selectVeryRuleSceneList(Page<VeryRuleSceneModel> page,@Param("pm") Map<String, Object> param);
	
    List<VeryRuleSceneModel> selectVeryRuleSceneList(@Param("pm") Map<String, Object> param);
	
    int updateById(@Param("pm") Map<String, Object> param);
	
	int deleteById(@Param("pm") Map<String, Object> param);
	
}


