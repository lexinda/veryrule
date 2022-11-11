package com.lexinda.veryrule.core.interfaces;

/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

public interface IRuleResultCondation extends IRuleTest{
	<R extends RuleBo> Map<String, Object> contation(Map<String, Object> param, R rule) throws Exception;
}
