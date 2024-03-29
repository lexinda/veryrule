package com.lexinda.veryrule.platform.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lexinda.veryrule.bo.RuleFlowBo;

/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleFlowModel extends RuleFlowBo{
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	private String ruleFlowDesc;
	private String ruleFlowTempletCode;
	private String ruleFlowDocument;
	private Integer ruleSceneId;
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
	public Integer getRuleSceneId() {
		return ruleSceneId;
	}
	public void setRuleSceneId(Integer ruleSceneId) {
		this.ruleSceneId = ruleSceneId;
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
	public String getRuleFlowDocument() {
		return ruleFlowDocument;
	}
	public void setRuleFlowDocument(String ruleFlowDocument) {
		this.ruleFlowDocument = ruleFlowDocument;
	}
	
}
