package com.lexinda.veryrule.platform.model;

import java.util.ArrayList;
import java.util.List;


/**
* @author zhumengle
* @version 创建时间：2022-3-9 15:11:30
* 类说明
*/
public class VeryRuleElementMenuModel extends VeryRuleElementModel {

	private List<VeryRuleElementMenuModel> children;

	public List<VeryRuleElementMenuModel> getChildren() {
		return children;
	}

	public void setChildren(List<VeryRuleElementMenuModel> children) {
		this.children = children;
	}
}
