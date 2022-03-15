package com.lexinda.veryrule.spring;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class VeryRuleClassPathDefinitionScanner extends ClassPathBeanDefinitionScanner{
	private Class type;
	public VeryRuleClassPathDefinitionScanner(BeanDefinitionRegistry registry,Class<? extends Annotation> type) {
		super(registry,false);
		this.type = type;
	}
	/**
     * 注册 过滤器
     */
    public void registerTypeFilter(){
       addIncludeFilter(new AnnotationTypeFilter(type));
    }
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
}
