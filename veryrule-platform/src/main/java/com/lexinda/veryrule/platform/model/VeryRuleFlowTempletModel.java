package com.lexinda.veryrule.platform.model;

import java.util.Date;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:46:32
* 类说明
*/
public class VeryRuleFlowTempletModel{
	private Integer id;
	private String ruleFlowTempletCode;
	private String ruleFlowTemplet;
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
	public String getRuleFlowTempletCode() {
		return ruleFlowTempletCode;
	}
	public void setRuleFlowTempletCode(String ruleFlowTempletCode) {
		this.ruleFlowTempletCode = ruleFlowTempletCode;
	}
	public String getRuleFlowTemplet() {
		return ruleFlowTemplet;
	}
	public void setRuleFlowTemplet(String ruleFlowTemplet) {
		this.ruleFlowTemplet = ruleFlowTemplet;
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
}
