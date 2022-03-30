package com.lexinda.veryrule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lexinda
 *
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
