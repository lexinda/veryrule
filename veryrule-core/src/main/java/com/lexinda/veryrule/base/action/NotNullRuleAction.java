package com.lexinda.veryrule.base.action;

import java.util.HashMap;

/**
 * 
 * @author lexinda
 *
 */

import java.util.Map;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleCode;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleTest;

@Rule(code = RuleCode.NOTNULL,name = "指定key不可为空", desc = "指定key不可为空", type = RuleType.CONDATION)
public class NotNullRuleAction implements IRuleCondation,IRuleTest {

	@Override
	public <R extends RuleBo> void contation(Map<String, Object> param, R rule) throws Exception {
		String ruleKey = rule.getRuleKey();
		String[] keys = null;
		if(ruleKey.indexOf(",")>0) {
			keys = ruleKey.split(",");
		}else {
			keys = new String[] {ruleKey};
		}
		for(String key :keys) {
			Object data = param.get(key);
			if(data==null) {
				throw new Exception(key+rule.getRuleErrMsg());
			}else {
				if("".equals(data.toString().trim())) {
					throw new Exception(key+rule.getRuleErrMsg());
				}
			}
		}
	}

	@Override
	public <R extends RuleBo> Map<String, Object> ruleTest(R rule) {
		Map<String,Object> result = new HashMap<String,Object>();
		Rule ruleAnnotation = this.getClass().getAnnotation(Rule.class);
		if(rule!=null&&rule.getRuleCode().equals(ruleAnnotation.code())) {
			result.put(rule.getRuleCode(), ruleAnnotation.name());
		}
		return result;
	}

}
