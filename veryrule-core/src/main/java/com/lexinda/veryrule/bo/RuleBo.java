package com.lexinda.veryrule.bo;

import java.util.List;

/**
 * 
 * @author lexinda
 *
 */
public class RuleBo {
	/**
	 * 规则编码
	 */
	protected String ruleCode;
	/**
	 * 规则默认值/表达式
	 */
	protected String ruleValue;
	/**
	 * 规则指定入参关键字
	 */
	protected String ruleKey;
	/**
	 * 自定义错误信息
	 */
	protected String ruleErrMsg;
	/**
	 * 自定义条件
	 */
	protected List<String> ruleCondations;

	public RuleBo() {
	}

	public RuleBo(String ruleCode, String ruleValue, String ruleKey, String ruleErrMsg,
			List<String> ruleCondations) {
		this.ruleCode = ruleCode;
		this.ruleValue = ruleValue;
		this.ruleKey = ruleKey;
		this.ruleErrMsg = ruleErrMsg;
		this.ruleCondations = ruleCondations;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleValue() {
		return ruleValue;
	}

	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}

	public String getRuleKey() {
		return ruleKey;
	}

	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}

	public String getRuleErrMsg() {
		return ruleErrMsg;
	}

	public void setRuleErrMsg(String ruleErrMsg) {
		this.ruleErrMsg = ruleErrMsg;
	}

	public List<String> getRuleCondations() {
		return ruleCondations;
	}

	public void setRuleCondations(List<String> ruleCondations) {
		this.ruleCondations = ruleCondations;
	}

	@Override
	public String toString() {
		return "RuleModel [ruleCode=" + ruleCode  + ", ruleValue=" + ruleValue + ", ruleKey="
				+ ruleKey + ", ruleErrMsg=" + ruleErrMsg + ", ruleCondations=" + ruleCondations + "]";
	}


}
