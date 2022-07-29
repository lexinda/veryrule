package com.lexinda.veryrule.base.action;

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
import com.lexinda.veryrule.core.IRuleCondation;

@Rule(code = "testRuleAction",name = "测试规则执行", desc = "测试规则执行", type = RuleType.ACTION)
public class TestRuleAction implements IRuleAction {


	@Override
	public <R extends RuleBo> Map<String, Object> action(Map<String, Object> param, Map<String, Object> condation,
			R rule) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(condation);
		return null;
	}

}
