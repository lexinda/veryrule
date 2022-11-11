package com.lexinda.veryrule.core.interfaces;

import java.util.HashMap;
import java.util.Map;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;

/**
 * 
 * @author lexinda
 *	仅检测是否触达
 */
public interface IRuleTest{
	public default <R extends RuleBo> Map<String,Object> ruleTest(R rule){
		Map<String,Object> result = new HashMap<String,Object>();
		Rule ruleAnnotation = this.getClass().getAnnotation(Rule.class);
		if(rule!=null&&rule.getRuleCode().equals(ruleAnnotation.code())) {
			result.put(rule.getRuleCode(), ruleAnnotation.name());
		}
		return result;
	}
}
