package com.lexinda.veryrule.core;

/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

public interface IRuleCondation {
	Map<String, Object> contation(Map<String, Object> param) throws Exception;
}
