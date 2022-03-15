package com.lexinda.veryrule.platform.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.lexinda.veryrule.bo.RuleBo;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:46:32
* 类说明
*/
public class VeryRuleElementModel extends RuleBo{
	private Integer id;
	private String ruleName;
	private String ruleCondation;
	private String ruleDesc;
	private Integer ruleType;
	private String groupName;
	private Integer version;
	private String createId;
	private Date createTime;
	private String updateId;
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	public String getRuleCondation() {
		return ruleCondation;
	}
	//String字符串，多个,隔开
	public void setRuleCondation(String ruleCondation) {
		this.ruleCondation = ruleCondation;
		if(ruleCondation.indexOf(",")>0) {
			this.ruleCondations = Arrays.asList(ruleCondation.split(","));
		}else {
			this.ruleCondations = new ArrayList<String>();
			this.ruleCondations.add(ruleCondation);
		}
		
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
	public String getRuleDesc() {
		return ruleDesc;
	}
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
