package com.lexinda.veryrule.core;

/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

public interface IRuleAction extends IRuleTest{
	<R extends RuleBo> Map<String, Object> action(Map<String, Object> param,
			Map<String, Object> condation, R rule) throws Exception;
}
