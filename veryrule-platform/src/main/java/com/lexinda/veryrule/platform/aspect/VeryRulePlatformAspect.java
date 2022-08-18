package com.lexinda.veryrule.platform.aspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson2.JSON;
import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.aspect.VeryRuleAspect;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.platform.model.VeryRuleFlowModel;
import com.lexinda.veryrule.platform.model.VeryRuleFlowTempletModel;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowMbService;
import com.lexinda.veryrule.platform.service.mybatis.VeryRuleFlowTempletMbService;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRulePlatformAspect extends VeryRuleAspect{

	@Autowired
	private VeryRule veryRule;
	
	@Autowired
	private VeryRuleFlowTempletMbService veryRuleFlowTempletMbService;
	
	@Autowired
	private VeryRuleFlowMbService veryRuleFlowMbService;
	
	@Override
	public Map<String,Object> getParam(MethodSignature methodSignature,Object[] args){
		String[] parameterNames = methodSignature.getParameterNames();
		Map<String,Object> param = new HashMap<String,Object>();
		for(int i=0;i<parameterNames.length;i++) {
			param.put(parameterNames[i], args[i]);
			if(parameterNames[i].equals("data")) {
				if(args[i]!=null) {
					param.putAll(JSON.parseObject((String) args[i]));
				}
			}
		}
		return param;
	}
	
	//可写入cache
	@Override
	public <R extends RuleBo> List<R> getRuleFlow(String ruleFlowTempletCode) {
		// TODO Auto-generated method stub
		Map<String, Object> dataParam = new HashMap<String, Object>();
		dataParam.put("ruleFlowTempletCode", ruleFlowTempletCode);
		dataParam.put("status", 1);
		List<VeryRuleFlowModel> veryRuleFlowList = veryRuleFlowMbService.selectVeryRuleFlowList(dataParam);
		List<RuleBo> ruleModelList = new ArrayList<RuleBo>();
		if(veryRuleFlowList.size()>0) {
			List<VeryRuleFlowTempletModel> veryRuleFlowTemplet = veryRuleFlowTempletMbService.selectVeryRuleFlowTempletList(dataParam);
			if(veryRuleFlowTemplet.size()>0) {
				ruleModelList = JSON.parseArray(veryRuleFlowTemplet.get(0).getRuleFlowTemplet(), RuleBo.class);
			}
		}
		return (List<R>) ruleModelList;
	}
	
}
