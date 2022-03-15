package com.lexinda.veryrule.core;

import java.lang.reflect.Method;
import java.util.Map;

/**
* @author zhumengle
* @version 创建时间：2022-3-14 10:14:38
* 类说明
*/
public interface IRuleListener {
	void ruleListener(Object proxy, Object[] args);
}
