package com.lexinda.veryrule;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.common.RuleResult;
import com.lexinda.veryrule.core.RuleInvoker;
import com.lexinda.veryrule.core.RuleInvokerAbst;
import com.lexinda.veryrule.core.RuleProxyHandler;
import com.lexinda.veryrule.core.interfaces.IRuleAction;
import com.lexinda.veryrule.core.interfaces.IRuleCondation;
import com.lexinda.veryrule.core.interfaces.IRuleListener;
import com.lexinda.veryrule.core.interfaces.IRuleResultCondation;

public class RuleEngine<R extends RuleBo> {

	protected Map<String, IRuleAction> ruleActionMap;
	protected Map<String, IRuleResultCondation> ruleResultCondationMap;
	protected Map<String, IRuleCondation> ruleCondationMap;
	protected IRuleListener ruleListener;
	protected RuleInvokerAbst ruleInvoker;
	protected RuleProxyHandler ruleProxyHandler = null;

	protected RuleResult getResult(Map<String, Object> param, Map<R, Object> condations,
			Map<R, IRuleResultCondation> ruleResultCondations, Map<R, IRuleAction> ruleActions, boolean isTest,
			ThreadPoolExecutor threadPoolExecutor) throws Exception {
		RuleInvoker invoke = (RuleInvoker)((RuleInvoker) ruleInvoker).clone();
		
		if(ruleProxyHandler.getRuleListener()!=null) {
			ruleProxyHandler.getRuleListener().ruleFlowStart(param);
		}
		
		invoke.doRuleCondation(param, condations,ruleProxyHandler,isTest);
		
		invoke.doRuleResultCondation(param, ruleResultCondations,ruleProxyHandler,isTest,threadPoolExecutor);
		
		invoke.doRuleAction(param, ruleActions,ruleProxyHandler,isTest);
		
		if(ruleProxyHandler.getRuleListener()!=null) {
			ruleProxyHandler.getRuleListener().ruleFlowEnd(param,invoke.getRuleResult());
		}
		
		return invoke.getRuleResult();
	}

	protected static void getClassByPath(String path, List<Class<?>> classList) throws ClassNotFoundException {
		if (path != null) {
			File file = new File(path);
			if (file.isDirectory() && file.listFiles().length > 0) {
				for (File childFile : file.listFiles()) {
					if (childFile.isDirectory() && file.listFiles().length > 0) {
						getClassByPath(childFile.getAbsolutePath(), classList);
					} else {
						String childFilePath = childFile.getPath();
						if (childFilePath.endsWith(".class")) {
							if (childFilePath.contains("test-classes")) {
								childFilePath = childFilePath.substring(childFilePath.indexOf("\\test-classes") + 14,
										childFilePath.lastIndexOf("."));
							} else {
								childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
										childFilePath.lastIndexOf("."));
							}

							childFilePath = childFilePath.replace("\\", ".");
							classList.add(Class.forName(childFilePath));
						}
					}
				}
			}
		}
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

	public void setRuleInvoker(RuleInvokerAbst ruleInvoker) {
		this.ruleInvoker = ruleInvoker;
	}

	public RuleInvokerAbst getRuleInvoker() {
		return ruleInvoker;
	}

	public RuleProxyHandler getRuleProxyHandler() {
		return ruleProxyHandler;
	}

	public void setRuleProxyHandler(RuleProxyHandler ruleProxyHandler) {
		this.ruleProxyHandler = ruleProxyHandler;
	}
}
