package com.lexinda.veryrule.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.annotation.VeryRuleFlow;
import com.lexinda.veryrule.annotation.VeryRuleSingle;
import com.lexinda.veryrule.bo.RuleBo;
import com.lexinda.veryrule.vo.RestApiResponse;

/**
* @author zhumengle
* @version 创建时间：2022-3-4 9:30:19
* 类说明
*/
@Component
@Aspect
public class VeryRuleAspect {
	
	@Autowired
	private VeryRule veryRule;
	
	@Around("@annotation(veryRuleSingle)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, VeryRuleSingle veryRuleSingle) throws Throwable {
		Object result= null;
		Signature signature = proceedingJoinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		RuleBo ruleModel = new RuleBo(veryRuleSingle.ruleCode(),veryRuleSingle.ruleValue(),veryRuleSingle.ruleKey(),veryRuleSingle.ruleErrMsg(),Arrays.asList(veryRuleSingle.ruleCondations()));
		try{
			veryRule.fire(getParam(methodSignature,proceedingJoinPoint.getArgs()),ruleModel);
			result=(Object) proceedingJoinPoint.proceed();
		}catch(Exception e) {
			RestApiResponse res = new RestApiResponse();
			res.setErrorCode(1);
			res.setErrorDesc(e.getMessage());
			result = res;
		}
		return result;
		
	}
	
	@Around("@annotation(veryRuleFlow)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, VeryRuleFlow veryRuleFlow) throws Throwable {
		Object result= null;
		Signature signature = proceedingJoinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		try{
			veryRule.fire(getParam(methodSignature,proceedingJoinPoint.getArgs()),getRuleFlow(veryRuleFlow.ruleFlowCode()));
			result=(Object) proceedingJoinPoint.proceed();
		}catch(Exception e) {
			RestApiResponse res = new RestApiResponse();
			res.setErrorCode(1);
			res.setErrorDesc(e.getMessage());
			result = res;
		}
		return result;
		
	}
	
	public Map<String,Object> getParam(MethodSignature methodSignature,Object[] args){
		String[] parameterNames = methodSignature.getParameterNames();
		Map<String,Object> param = new HashMap<String,Object>();
		for(int i=0;i<parameterNames.length;i++) {
			param.put(parameterNames[i], args[i]);
		}
		return param;
	}
	
	public <R extends RuleBo> List<R> getRuleFlow(String ruleFlowTempletCode){
		return new ArrayList<R>();
	}

}
