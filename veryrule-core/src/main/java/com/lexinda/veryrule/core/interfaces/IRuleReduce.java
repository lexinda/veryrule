package com.lexinda.veryrule.core.interfaces;

import java.util.Map;

import com.lexinda.veryrule.common.RuleResult;

public interface IRuleReduce extends IRuleTest{
	Map<String, Object> reduce(RuleResult ruleResult) throws Exception;
}
