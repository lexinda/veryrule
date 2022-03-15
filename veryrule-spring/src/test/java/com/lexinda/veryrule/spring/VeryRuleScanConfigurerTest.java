package com.lexinda.veryrule.spring;

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

//指定在单元测试启动的时候创建spring的工厂类对象
@ContextConfiguration(locations = {"classpath:./applicationContext.xml"})
//RunWith的value属性指定以spring test的SpringJUnit4ClassRunner作为启动类
//如果不指定启动类，默认启用的junit中的默认启动类
@RunWith(value = SpringJUnit4ClassRunner.class)
public class VeryRuleScanConfigurerTest {

	@Autowired
	private VeryRule veryRule;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Map<String,Object> param = new HashMap<>();
		param.put("a", "123a");
		List<String> resCondation = new ArrayList<String>();
		resCondation.add("regexRuleCondation");
		List<RuleBo> res= new ArrayList<RuleBo>();
		res.add(new RuleBo("regex","a","(\\D*)(\\d+)(.*)","regex",resCondation));
		try {
			veryRule.fire(param,res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}

}
