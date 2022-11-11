package com.lexinda.veryrule.platform.veryrule;

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
import com.lexinda.veryrule.platform.model.RuleCodeModel;

@Rule(code = RuleCodeModel.REGEXREPLACE,name = "指定key匹配正则表达式替换", desc = "指定key匹配正则表达式替换", type = RuleType.RESULT_CONDATION)
public class RegexReplaceRuleAction implements IRuleResultCondation {

	@Override
	public <R extends RuleBo> Map<String, Object> contation(Map<String, Object> param,R rule) throws Exception {
		String info = (String) param.get(rule.getRuleKey());
		if (info == null || "".equals(info)) {
			throw new Exception(rule.getRuleKey() + rule.getRuleErrMsg());
		}
		String[] regexArr = rule.getRuleValue().split("}}");
		String pattern = regexArr[0].substring(2);
		String replaceInfo = regexArr[1].substring(2);
		info = info.replaceAll(pattern, replaceInfo);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(rule.getRuleKey(), info);
		return result;
	}

}
