package com.lexinda.veryrule.base.condation;

import java.util.HashMap;
/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.interfaces.IRuleResultCondation;

@Rule(code = "ruleResultCondationThree",name = "测试带返回值条件3", desc = "测试带返回值条件3", type = RuleType.RESULT_CONDATION)
public class RuleResultCondationThree implements IRuleResultCondation {


	@Override
	public <R extends RuleBo> Map<String, Object> contation(Map<String, Object> param, R rule) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("contation");
		Map<String, Object> result = new HashMap<String,Object>();
//		Thread.sleep(1000);
		result.put("Three", 3);
		return result;
	}

}

