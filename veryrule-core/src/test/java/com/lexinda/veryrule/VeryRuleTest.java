package com.lexinda.veryrule;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.common.RuleType;
import com.lexinda.veryrule.util.VeryRuleOgnlUtil;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleTest {
	
	VeryRule veryRule = null;
	
	/**
	 * corePoolSize： 线程池维护线程的最少数量 
	 * maximumPoolSize：线程池维护线程的最大数量 
	 * keepAliveTime： 线程池维护线程所允许的空闲时间 
	 * unit： 线程池维护线程所允许的空闲时间的单位 
	 * workQueue： 线程池所使用的缓冲队列 
	 * handler： 线程池对拒绝任务的处理策略 
	 *	 new ThreadPoolExecutor.AbortPolicy() //队列满了，还有线程进来，不处理，抛出异常
	 *	 new ThreadPoolExecutor.CallerRunsPolicy() //哪来的去哪里！
	 *	 new ThreadPoolExecutor.DiscardPolicy() //队列满了，丢掉线程，不会抛出异常！
	 *	 new ThreadPoolExecutor.DiscardOldestPolicy() //队列满了，尝试和最早的竞争，也不会抛出异常
	 */
	ThreadPoolExecutor threadPoolExecutor = null;
	
	private Map<String, Object> param = null;
	
	private List<RuleBo> res = null;

	@Before
	public void setUp() throws Exception {
//		veryRule = VeryRule.builder().condation(RegexRuleCondation.class).condation(NotNullRuleCondation.class)
//				.resultCondation(RuleResultCondationOne.class)
//				.resultCondation(RuleResultCondationTwo.class)
//				.resultCondation(RuleResultCondationThree.class)
//				.action(TestRuleAction.class)
//				.listener(TestRuleListener.class);
		veryRule = VeryRule.builder().rulePackage("com.lexinda.veryrule.base").listener(TestRuleListener.class);
		threadPoolExecutor = new ThreadPoolExecutor(2, 20, 20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.CallerRunsPolicy());
		param = new HashMap<String, Object>();
		param.put("a", "abc123");
		res = new ArrayList<RuleBo>();
		RuleBo nn = new RuleBo("notNull", "", "a", "不可为空",RuleType.CONDATION);
		res.add(nn);
		RuleBo one = new RuleBo(RuleTestCode.RULERESULTCONDATIONONE, "", "", "测试",RuleType.RESULT_CONDATION,2);
		RuleBo two = new RuleBo(RuleTestCode.RULERESULTCONDATIONTWO, "", "", "测试",RuleType.RESULT_CONDATION);
		RuleBo three = new RuleBo(RuleTestCode.RULERESULTCONDATIONTHREE, "", "", "测试",RuleType.RESULT_CONDATION);
		RuleBo action = new RuleBo(RuleTestCode.TESTRULEACTION, "", "", "测试",RuleType.ACTION,2);
		res.add(one);
		res.add(two);
		res.add(three);
		res.add(action);
	}

	@Test
	public void testFire() {
		
		try {
			Long time = System.currentTimeMillis();
//			for(int i=0;i<1000000;i++) {
//				veryRule.fire(param, res);
//			}
//			veryRule.fire(param, res);
			RuleResult ruleResult = veryRule.fireTest(res);
			System.out.println(System.currentTimeMillis()-time);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}
	
	@Test
	public void testFireThreadPool() {
		try {
			Long time1 = System.currentTimeMillis();
			veryRule.fire(param, res,threadPoolExecutor);
			System.out.println(System.currentTimeMillis()-time1);
			threadPoolExecutor.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}
	
	@Test
	public void testFireTest() {
		try {
			veryRule.fireTest(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}
	
	@Test
	public void testOgnl() {
		try {
			RuleBo ruleBo = new RuleBo();
			System.out.println(ruleBo.getRuleCode());
			String expr = "ruleCode='123fsdfd',ruleType=1";
			String exprList = "{#{'ruleCode':'123fsdfd','ruleType':1},#{'ruleCode':'234','ruleType':2}}";
			VeryRuleOgnlUtil.create().getRule(expr, ruleBo);
			System.out.println(ruleBo.getRuleCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}

}
