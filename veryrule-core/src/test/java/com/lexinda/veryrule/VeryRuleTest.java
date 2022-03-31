package com.lexinda.veryrule;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.lexinda.veryrule.base.action.NotNullRuleAction;
import com.lexinda.veryrule.base.condation.RegexRuleCondation;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleCode;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleTest {
	VeryRule veryRule = null;

	@Before
	public void setUp() throws Exception {
		veryRule = VeryRule.builder().condation(RegexRuleCondation.class).action(NotNullRuleAction.class).listener(TestRuleListener.class);
	}

	@Test
	public void test() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("a", "abc123");
		List<String> resCondation = new ArrayList<String>();
		resCondation.add("regexRuleCondation");
		List<RuleBo> res = new ArrayList<RuleBo>();
		RuleBo nn = new RuleBo(RuleCode.NOTNULL, "", "a", "不可为空",null);
		res.add(nn);
		try {
//			RuleResult RuleResult = veryRuleFactory.fireTest(param, re);
//			System.out.println(RuleResult);
			veryRule.fire(param, res);
//			veryRuleFactory.fire(param, nn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}

}
