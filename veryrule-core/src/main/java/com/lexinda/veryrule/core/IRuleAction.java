package com.lexinda.veryrule.core;

/**
 * 
 * @author zhumengle
 *
 */
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

public interface IRuleAction {
	<R extends RuleBo> void action(Map<String, Object> param, Map<String, Object> condation, R rule)
			throws Exception;
}
