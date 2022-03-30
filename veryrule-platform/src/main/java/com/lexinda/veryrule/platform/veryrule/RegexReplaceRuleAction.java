package com.lexinda.veryrule.platform.veryrule;

/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.platform.model.RuleCodeModel;

@Rule(code = RuleCodeModel.REGEXREPLACE,name = "指定key匹配正则表达式替换", desc = "指定key匹配正则表达式替换", type = RuleType.ACTION)
public class RegexReplaceRuleAction implements IRuleAction {

	@Override
	public <R extends RuleBo> void action(Map<String, Object> param,
			Map<String, Object> condation, R rule) throws Exception {
		String info = (String) param.get(rule.getRuleKey());
		if (info == null || "".equals(info)) {
			throw new Exception(rule.getRuleKey() + rule.getRuleErrMsg());
		}
		String[] regexArr = rule.getRuleValue().split("}}");
		String pattern = regexArr[0].substring(2);
		String replaceInfo = regexArr[1].substring(2);
		info = info.replaceAll(pattern, replaceInfo);
		param.put(rule.getRuleKey(), info);
	}

}
