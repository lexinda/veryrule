package com.lexinda.veryrule.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lexinda
 *
 */
public class RuleResult implements Cloneable{
	
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
	
	public void addResultAll(Map<String,Object> result) {
		this.result.putAll(result);
	}

	public Map<String, Object> getCondationResult() {
		return condationResult;
	}

	public void setCondationResult(Map<String, Object> condationResult) {
		this.condationResult = condationResult;
	}
	
	public void setCondationResultAll(Map<String,Object> condationResult) {
		this.condationResult.putAll(condationResult);
	}
	
	@Override
	public RuleResult clone(){
		RuleResult obj = null;
        try {
            obj = (RuleResult)super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return obj;
    }

}
