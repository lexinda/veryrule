package com.lexinda.veryrule.core;

/**
 * 
 * @author zhumengle
 *
 */
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

public interface IRuleResultAction{

	<R extends RuleBo> Map<String, Object> action(Map<String, Object> param,
			Map<String, Object> condation, R rule) throws Exception;
}
