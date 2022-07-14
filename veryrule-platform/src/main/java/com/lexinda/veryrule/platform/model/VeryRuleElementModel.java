package com.lexinda.veryrule.platform.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lexinda.veryrule.bo.RuleBo;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleElementModel extends RuleBo{
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	private String ruleName;
	private String ruleDesc;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
