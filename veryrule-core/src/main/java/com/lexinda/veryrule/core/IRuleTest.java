package com.lexinda.veryrule.core;

import java.util.Map;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;

/**
 * 
 * @author lexinda
 *	仅检测是否触达
 */
public interface IRuleTest{
	<R extends RuleBo> Map<String,Object> ruleTest(R rule);
}
