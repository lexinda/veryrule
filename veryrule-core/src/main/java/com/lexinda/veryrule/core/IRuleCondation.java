package com.lexinda.veryrule.core;

/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

public interface IRuleCondation {
	<R extends RuleBo> void contation(Map<String, Object> param, R rule)
			throws Exception;
}
