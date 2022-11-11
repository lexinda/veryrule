package com.lexinda.veryrule.core.interfaces;

/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

public interface IRuleCondation extends IRuleTest{
	<R extends RuleBo> void contation(Map<String, Object> param, R rule)
			throws Exception;
}
