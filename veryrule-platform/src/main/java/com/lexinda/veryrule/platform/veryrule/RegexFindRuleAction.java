package com.lexinda.veryrule.platform.veryrule;

/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.interfaces.IRuleCondation;
import com.lexinda.veryrule.platform.model.RuleCodeModel;

@Rule(code = RuleCodeModel.REGEXFIND,name = "指定key匹配正则表达式查找", desc = "指定key匹配正则表达式查找", type = RuleType.CONDATION)
public class RegexFindRuleAction implements IRuleCondation {

	@Override
	public <R extends RuleBo> void contation(Map<String, Object> param, R rule) throws Exception {
		String info = (String) param.get(rule.getRuleKey());
		if (StringUtils.isBlank(info)) {
			throw new Exception("指定入参key不能为空");
		}
		String pattern = rule.getRuleValue();
		if (StringUtils.isBlank(pattern)) {
			throw new Exception("规则值不能为空");
		}
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 创建 matcher 对象
		Matcher m = r.matcher(info);
		if (!m.find()) {
			throw new Exception(rule.getRuleErrMsg());
		}
	}
}
