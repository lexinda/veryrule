package com.lexinda.veryrule.platform.service.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lexinda.veryrule.platform.mapper.VeryRuleFlowTempletMapper;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:50:40
* 类说明
*/
@Service
public class VeryRuleFlowTempletMbService extends ServiceImpl<VeryRuleFlowTempletMapper, VeryRuleFlowTempletModel>{
    public Page<VeryRuleFlowTempletModel> selectVeryRuleFlowTempletList(Page<VeryRuleFlowTempletModel> page,Map<String, Object> param) {
        page.setRecords(baseMapper.selectVeryRuleFlowTempletList(page,param));
        return page;
    }
    
    public List<VeryRuleFlowTempletModel> selectVeryRuleFlowTempletList(Map<String, Object> param) {
        return baseMapper.selectVeryRuleFlowTempletList(param);
    }
    
    public int updateById(Map<String, Object> param) {
        return baseMapper.updateById(param);
    }
    
    public int deleteById(@Param("pm") Map<String, Object> param) {
    	return baseMapper.deleteById(param);
    }
    
}
