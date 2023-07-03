package com.lexinda.veryrule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.util.VeryRuleOgnlUtil;

import ognl.OgnlException;

public class RuleFlow {
	private String scene;
	private List<RuleBo> ruleBoList;
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public List<RuleBo> getRuleBoList() {
		return ruleBoList;
	}
	public void setRuleBoList(List<String> ruleBoList) {
		if(!ruleBoList.isEmpty()) {
			RuleBo ruleBo = new RuleBo();
			this.ruleBoList = ruleBoList.stream().map(ruleStr -> {
				try {
					return VeryRuleOgnlUtil.create().getRule(ruleStr, ruleBo);
				} catch (OgnlException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}).collect(Collectors.toList());
		}else {
			this.ruleBoList = new ArrayList<RuleBo>();
		}
	}
}
