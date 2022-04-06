package com.lexinda.veryrule.base.action;

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
import com.lexinda.veryrule.core.IRuleAction;

@Rule(code = RuleCode.NOTNULL,name = "指定key不可为空", desc = "指定key不可为空", type = RuleType.ACTION)
public class NotNullRuleAction implements IRuleAction {

	@Override
	public <R extends RuleBo> void action(Map<String, Object> param,
			Map<String, Object> condation, R rule) throws Exception {
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

}
