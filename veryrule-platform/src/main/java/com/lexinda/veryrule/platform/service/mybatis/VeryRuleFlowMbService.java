package com.lexinda.veryrule.platform.service.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lexinda.veryrule.platform.mapper.VeryRuleFlowMapper;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:50:40
* 类说明
*/
@Service
public class VeryRuleFlowMbService extends ServiceImpl<VeryRuleFlowMapper, VeryRuleFlowModel>{
    
	public Page<VeryRuleFlowModel> selectVeryRuleFlowList(Page<VeryRuleFlowModel> page,Map<String, Object> param) {
        page.setRecords(baseMapper.selectVeryRuleFlowList(page,param));
        return page;
    }
	
	public List<VeryRuleFlowModel> selectVeryRuleFlowList(Map<String, Object> param) {
        return baseMapper.selectVeryRuleFlowList(param);
    }
    
    public int updateById(Map<String, Object> param) {
        return baseMapper.updateById(param);
    }
    
    public int deleteById(@Param("pm") Map<String, Object> param) {
    	return baseMapper.deleteById(param);
    }
}
