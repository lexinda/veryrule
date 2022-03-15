package com.lexinda.veryrule.base.condation;

/**
 * 
 * @author zhumengle
 *
 */
import java.util.Map;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.core.IRuleCondation;

@Rule(code = "regexRuleCondation",name = "匹配正则表达式", desc = "匹配正则表达式", type = RuleType.CONDATION)
public class RegexRuleCondation implements IRuleCondation {


	@Override
	public Map<String, Object> contation(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("contation");
		return null;
	}

}
