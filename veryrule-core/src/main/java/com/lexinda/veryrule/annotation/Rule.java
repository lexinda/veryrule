package com.lexinda.veryrule.annotation;

/**
 * 
 * @author lexinda
 *
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lexinda.veryrule.common.RuleType;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Rule {
	
	String code() default "";
	
	String name() default "";

	String desc() default "";

	String type() default RuleType.ACTION;
	
	String[] condations() default "";
}
