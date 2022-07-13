package com.lexinda.veryrule;
import java.io.File;
/**
 * 
 * @author lexinda
 *
 */
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.core.IRuleResultCondation;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.core.RuleInvoker;
import com.lexinda.veryrule.core.IRuleListener;
import com.lexinda.veryrule.core.RuleResult;

public class RuleEngine<R extends RuleBo> {
	
	protected Map<String, IRuleAction> ruleActionMap;
	protected Map<String, IRuleResultCondation> ruleResultCondationMap;
	protected Map<String, IRuleCondation> ruleCondationMap;
	protected IRuleListener ruleListener;
	
	public RuleResult getResult(Map<String, Object> param,Map<R, List<IRuleCondation>> ruleCondations,Map<R, List<IRuleResultCondation>> ruleResultCondations,Map<R, IRuleAction> ruleActions,boolean isTest) throws Exception {
		RuleInvoker invoke = new RuleInvoker();
		invoke.doRuleCondation(param, ruleCondations,this.ruleListener,isTest);
		invoke.doRuleResultCondation(param, ruleResultCondations,this.ruleListener,isTest);
		invoke.doRuleAction(param, ruleActions,this.ruleListener,isTest);
		return invoke.getRuleResult();
	}
	
	public static String getClassByPath(String path,List<Class<?>> classList) throws ClassNotFoundException {
		if(path!=null) {
			File file = new File(path);
			if(file.isDirectory()&&file.listFiles().length>0) {
				for(File childFile:file.listFiles()) {
					if(childFile.isDirectory()&&file.listFiles().length>0) {
						return getClassByPath(childFile.getAbsolutePath(), classList);
					}else {
						String childFilePath = childFile.getPath();
						if (childFilePath.endsWith(".class")) { 
							if(childFilePath.contains("test-classes")) {
								childFilePath = childFilePath.substring(childFilePath.indexOf("\\test-classes") + 14, childFilePath.lastIndexOf(".")); 
							}else {
								childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf(".")); 
							}
							
							childFilePath = childFilePath.replace("\\", ".");
							classList.add(Class.forName(childFilePath));
		                } 
					}
				}
			}
		}
		return path;
	}

	public Map<String, IRuleAction> getRuleActionMap() {
		return ruleActionMap;
	}

	public void setRuleActionMap(Map<String, IRuleAction> ruleActionMap) {
		this.ruleActionMap = ruleActionMap;
	}

	public Map<String, IRuleResultCondation> getRuleResultCondationMap() {
		return ruleResultCondationMap;
	}

	public void setRuleResultCondationMap(Map<String, IRuleResultCondation> ruleResultCondationMap) {
		this.ruleResultCondationMap = ruleResultCondationMap;
	}

	public Map<String, IRuleCondation> getRuleCondationMap() {
		return ruleCondationMap;
	}

	public void setRuleCondationMap(Map<String, IRuleCondation> ruleCondationMap) {
		this.ruleCondationMap = ruleCondationMap;
	}

	public IRuleListener getRuleListener() {
		return ruleListener;
	}

	public void setRuleListener(IRuleListener ruleListener) {
		this.ruleListener = ruleListener;
	}
}
