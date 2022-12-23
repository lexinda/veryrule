package com.lexinda.veryrule.platform.service.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lexinda.veryrule.platform.mapper.VeryRuleSceneMapper;
import com.lexinda.veryrule.platform.model.VeryRuleSceneModel;

/**
 * 
 * @author lexinda
 *
 */
@Service
public class VeryRuleSceneMbService extends ServiceImpl<VeryRuleSceneMapper, VeryRuleSceneModel>{
    
	public Page<VeryRuleSceneModel> selectVeryRuleSceneList(Page<VeryRuleSceneModel> page,Map<String, Object> param) {
        page.setRecords(baseMapper.selectVeryRuleSceneList(page,param));
        return page;
    }
	
	public List<VeryRuleSceneModel> selectVeryRuleSceneList(Map<String, Object> param) {
        return baseMapper.selectVeryRuleSceneList(param);
    }
    
    public int updateById(Map<String, Object> param) {
        return baseMapper.updateById(param);
    }
    
    public int deleteById(@Param("pm") Map<String, Object> param) {
    	return baseMapper.deleteById(param);
    }
}
