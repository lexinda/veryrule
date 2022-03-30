package com.lexinda.veryrule.platform.service.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lexinda.veryrule.platform.mapper.VeryRuleElementMapper;
import com.lexinda.veryrule.platform.model.VeryRuleElementModel;

/**
 * 
 * @author lexinda
 *
 */
@Service
public class VeryRuleElementMbService extends ServiceImpl<VeryRuleElementMapper, VeryRuleElementModel>{
    public Page<VeryRuleElementModel> selectVeryRuleElementList(Page<VeryRuleElementModel> page,Map<String, Object> param) {
        page.setRecords(baseMapper.selectVeryRuleElementList(page,param));
        return page;
    }
    
    public List<VeryRuleElementModel> selectVeryRuleElementList(Map<String, Object> param) {
        return baseMapper.selectVeryRuleElementList(param);
    }
    
    public int updateById(Map<String, Object> param) {
        return baseMapper.updateById(param);
    }
    
    public int deleteById(@Param("pm") Map<String, Object> param) {
    	return baseMapper.deleteById(param);
    }
}
