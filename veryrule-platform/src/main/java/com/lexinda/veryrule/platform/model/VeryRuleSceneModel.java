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
public class VeryRuleSceneModel{
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	private Integer pid;
	private String ruleSceneCode;
	private String ruleSceneName;
	private String ruleSceneType;
	private String ruleSceneDesc;
	private boolean hasChildren;
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
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getRuleSceneCode() {
		return ruleSceneCode;
	}
	public void setRuleSceneCode(String ruleSceneCode) {
		this.ruleSceneCode = ruleSceneCode;
	}
	public String getRuleSceneName() {
		return ruleSceneName;
	}
	public void setRuleSceneName(String ruleSceneName) {
		this.ruleSceneName = ruleSceneName;
	}
	public String getRuleSceneType() {
		return ruleSceneType;
	}
	public void setRuleSceneType(String ruleSceneType) {
		this.ruleSceneType = ruleSceneType;
	}
	public String getRuleSceneDesc() {
		return ruleSceneDesc;
	}
	public void setRuleSceneDesc(String ruleSceneDesc) {
		this.ruleSceneDesc = ruleSceneDesc;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
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
