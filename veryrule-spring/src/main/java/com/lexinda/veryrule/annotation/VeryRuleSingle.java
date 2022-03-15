package com.lexinda.veryrule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lexinda.veryrule.bo.RuleBo;

/**
* @author zhumengle
* @version 创建时间：2022-3-4 9:18:32
* 单个规则校验
*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface VeryRuleSingle {
	String ruleCode() default "";
	String ruleValue() default "";
	String ruleKey() default "";
	String ruleErrMsg() default "";
	String[] ruleCondations() default "";
}
