package com.lexinda.veryrule.bo;

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
	 * 规则默认值
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
	 * 规则类型
	 * 1:条件
	 * 2:带返回值的条件
	 * 3:执行动作
	 */
	protected Integer ruleType;
	
	/**
	 * 带返回值的条件-是否异步
	 * 1:异步
	 * 非1：同步
	 */
	protected Integer ruleAsyn;
	/**
	 * 表达式
	 */
	protected String ruleExpr;
	
	public RuleBo() {
	}
	
	public RuleBo(String ruleCode, String ruleValue, String ruleKey, String ruleErrMsg,Integer ruleType) {
		this.ruleCode = ruleCode;
		this.ruleValue = ruleValue;
		this.ruleKey = ruleKey;
		this.ruleErrMsg = ruleErrMsg;
		this.ruleType = ruleType;
		this.ruleAsyn = 1;
		this.ruleExpr = "";
	}
	
	public RuleBo(String ruleCode, String ruleValue, String ruleKey, String ruleErrMsg,Integer ruleType,Integer ruleAsyn) {
		this.ruleCode = ruleCode;
		this.ruleValue = ruleValue;
		this.ruleKey = ruleKey;
		this.ruleErrMsg = ruleErrMsg;
		this.ruleType = ruleType;
		this.ruleAsyn = ruleAsyn;
		this.ruleExpr = "";
	}
	
	public RuleBo(String ruleCode, String ruleValue, String ruleKey, String ruleErrMsg,Integer ruleType,String ruleExpr) {
		this.ruleCode = ruleCode;
		this.ruleValue = ruleValue;
		this.ruleKey = ruleKey;
		this.ruleErrMsg = ruleErrMsg;
		this.ruleType = ruleType;
		this.ruleAsyn = 1;
		this.ruleExpr = ruleExpr;
	}
	
	public RuleBo(String ruleCode, String ruleValue, String ruleKey, String ruleErrMsg,Integer ruleType,Integer ruleAsyn,String ruleExpr) {
		this.ruleCode = ruleCode;
		this.ruleValue = ruleValue;
		this.ruleKey = ruleKey;
		this.ruleErrMsg = ruleErrMsg;
		this.ruleType = ruleType;
		this.ruleAsyn = ruleAsyn;
		this.ruleExpr = ruleExpr;
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

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public Integer getRuleAsyn() {
		return ruleAsyn;
	}

	public void setRuleAsyn(Integer ruleAsyn) {
		this.ruleAsyn = ruleAsyn;
	}

	public String getRuleExpr() {
		return ruleExpr;
	}

	public void setRuleExpr(String ruleExpr) {
		this.ruleExpr = ruleExpr;
	}

	@Override
	public String toString() {
		return "RuleBo [ruleCode=" + ruleCode + ", ruleValue=" + ruleValue + ", ruleKey=" + ruleKey + ", ruleErrMsg="
				+ ruleErrMsg + ", ruleType=" + ruleType + ", ruleAsyn=" + ruleAsyn+ ", ruleExpr=" + ruleExpr + "]";
	}

}
