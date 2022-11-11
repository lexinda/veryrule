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

@Rule(code = "ruleResultCondationOne",name = "测试带返回值条件1", desc = "测试带返回值条件1", type = RuleType.RESULT_CONDATION)
public class RuleResultCondationOne implements IRuleResultCondation {


	@Override
	public <R extends RuleBo> Map<String, Object> contation(Map<String, Object> param, R rule) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("contation");
		Map<String, Object> result = new HashMap<String,Object>();
//		Thread.sleep(5000);
		result.put("one", 1);
		return result;
	}

}
