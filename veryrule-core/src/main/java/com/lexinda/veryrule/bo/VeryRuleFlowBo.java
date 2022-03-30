package com.lexinda.veryrule.bo;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleFlowBo{
	private String ruleFlowCode;
	private String ruleFlowName;
	private String groupName;
	private String parentRuleFlowCode;
	private int status;
	public String getRuleFlowCode() {
		return ruleFlowCode;
	}
	public void setRuleFlowCode(String ruleFlowCode) {
		this.ruleFlowCode = ruleFlowCode;
	}
	public String getRuleFlowName() {
		return ruleFlowName;
	}
	public void setRuleFlowName(String ruleFlowName) {
		this.ruleFlowName = ruleFlowName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getParentRuleFlowCode() {
		return parentRuleFlowCode;
	}
	public void setParentRuleFlowCode(String parentRuleFlowCode) {
		this.parentRuleFlowCode = parentRuleFlowCode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
