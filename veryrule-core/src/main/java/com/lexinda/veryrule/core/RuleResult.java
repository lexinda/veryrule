package com.lexinda.veryrule.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lexinda
 *
 */
public class RuleResult{
	
	private Map<String,Object> result = new HashMap<String,Object>();
	
	private Map<String,Object> condationResult = new HashMap<String,Object>();

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	
	public void addResult(String code,String name) {
		this.result.put(code,name);
	}

	public Map<String, Object> getCondationResult() {
		return condationResult;
	}

	public void setCondationResult(Map<String, Object> condationResult) {
		this.condationResult = condationResult;
	}

}
