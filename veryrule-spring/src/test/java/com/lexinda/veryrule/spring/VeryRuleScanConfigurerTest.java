package com.lexinda.veryrule.spring;
/**
 * 
 * @author lexinda
 *
 */
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.common.RuleType;

//指定在单元测试启动的时候创建spring的工厂类对象
@ContextConfiguration(locations = {"classpath:./applicationContext.xml"})
//RunWith的value属性指定以spring test的SpringJUnit4ClassRunner作为启动类
//如果不指定启动类，默认启用的junit中的默认启动类
@RunWith(value = SpringJUnit4ClassRunner.class)
public class VeryRuleScanConfigurerTest {

	@Autowired
	private VeryRule veryRule;
	
	private Map<String,Object> param = null;
	
	private List<RuleBo> res = null;
	
	@Before
	public void setUp() throws Exception {
		param = new HashMap<>();
		param.put("a", "123a");
		res = new ArrayList<RuleBo>();
		res.add(new RuleBo("regexRuleCondation","a","(\\D*)(\\d+)(.*)","regex",RuleType.CONDATION,2));
	}

	@Test
	public void testFire() {
		
		try {
			veryRule.fire(param,res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	public void testFireTest() {
		
		try {
			RuleResult result = veryRule.fireTest(res);
			System.out.println(result.getResult());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}

}
