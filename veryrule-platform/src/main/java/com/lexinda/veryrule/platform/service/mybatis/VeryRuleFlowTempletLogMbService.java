package com.lexinda.veryrule.platform.service.mybatis;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lexinda.veryrule.platform.mapper.VeryRuleFlowTempletLogMapper;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:50:40
* 类说明
*/
@Service
public class VeryRuleFlowTempletLogMbService extends ServiceImpl<VeryRuleFlowTempletLogMapper, VeryRuleFlowTempletModel>{
    public Page<VeryRuleFlowTempletModel> selectVeryRuleFlowList(Page<VeryRuleFlowTempletModel> page,Map<String, Object> param) {
        page.setRecords(baseMapper.selectVeryRuleFlowTempletLogList(page,param));
        return page;
    }
}
