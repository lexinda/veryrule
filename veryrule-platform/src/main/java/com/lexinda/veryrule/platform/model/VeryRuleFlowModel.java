package com.lexinda.veryrule.platform.model;

import java.util.Date;

import com.lexinda.veryrule.bo.VeryRuleFlowBo;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 15:46:32
* 类说明
*/
public class VeryRuleFlowModel extends VeryRuleFlowBo{
	private Integer id;
	private String ruleFlowDesc;
	private String ruleFlowTempletCode;
	private Integer version;
	private boolean hasChildren;
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
	public String getRuleFlowDesc() {
		return ruleFlowDesc;
	}
	public void setRuleFlowDesc(String ruleFlowDesc) {
		this.ruleFlowDesc = ruleFlowDesc;
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
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public String getRuleFlowTempletCode() {
		return ruleFlowTempletCode;
	}
	public void setRuleFlowTempletCode(String ruleFlowTempletCode) {
		this.ruleFlowTempletCode = ruleFlowTempletCode;
	}
	
}
