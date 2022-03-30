package com.lexinda.veryrule.platform.model;

import java.util.List;


/**
 * 
 * @author lexinda
 *
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
