package com.lexinda.veryrule.platform.service.mybatis;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lexinda.veryrule.platform.mapper.VeryRuleDbMapper;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;

/**
 * 
 * @author lexinda
 *
 */
@Service
public class VeryRuleDbMbService extends ServiceImpl<VeryRuleDbMapper, VeryRuleFlowModel>{
    public void createVeryRuleFlow() {
        baseMapper.createVeryRuleFlow();
    }
    
    public void createVeryRuleElement() {
        baseMapper.createVeryRuleElement();
    }
    
    public void createVeryRuleFlowTemplet() {
        baseMapper.createVeryRuleFlowTemplet();
    }
    
    public void createVeryRuleFlowTempletLog() {
        baseMapper.createVeryRuleFlowTempletLog();
    }
}
