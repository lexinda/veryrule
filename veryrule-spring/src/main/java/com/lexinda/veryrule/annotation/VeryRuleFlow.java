package com.lexinda.veryrule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import com.lexinda.veryrule.bo.RuleBo;

/**
* @author zhumengle
* @version 创建时间：2022-3-4 9:18:32
* 规则流校验
*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface VeryRuleFlow {
	String ruleFlowCode() default "";
}
