package com.lexinda.veryrule.platform.model;

import java.util.List;


/**
 * 
 * @author lexinda
 *
 */
public class VeryRuleElementMenuModel extends VeryRuleElementModel {
	
	private boolean arrow;
	
	private String path;

	private List<VeryRuleElementMenuModel> children;

	public List<VeryRuleElementMenuModel> getChildren() {
		return children;
	}

	public void setChildren(List<VeryRuleElementMenuModel> children) {
		this.children = children;
	}

	public boolean isArrow() {
		return arrow;
	}

	public void setArrow(boolean arrow) {
		this.arrow = arrow;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
