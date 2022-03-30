package com.lexinda.veryrule.spring;

/**
 * 
 * @author lexinda
 *
 */

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.aspect.VeryRuleAspect;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleListener;
import com.lexinda.veryrule.core.IRuleResultAction;

@Component
public class VeryRuleScanConfigurer implements ApplicationContextAware {

	private boolean loadDefaultRule;

	private String rulePackage;
	
	private boolean useAspect;
	
	private String aspectBean;
	
	private String listenerBean;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
		DefaultListableBeanFactory context = (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();
		// 扫描ruleBean
		VeryRuleClassPathDefinitionScanner veryRuleClassPathDefinitionScanner = new VeryRuleClassPathDefinitionScanner(
				context, Rule.class);
		veryRuleClassPathDefinitionScanner.registerTypeFilter();
		if (this.rulePackage != null && !this.rulePackage.equals("")) {
			veryRuleClassPathDefinitionScanner.scan(this.rulePackage);
		}

		if (this.loadDefaultRule) {
			veryRuleClassPathDefinitionScanner.scan("com.lexinda.veryrule.base");
		}
		VeryRule.Builder builder = VeryRule.builder();
		Map<String, Object> ruleBeanMap = context.getBeansWithAnnotation(Rule.class);
		ruleBeanMap.entrySet().forEach(beanItem -> {
			try {
				if (beanItem.getValue() instanceof IRuleCondation) {
					builder.condation(((IRuleCondation) beanItem.getValue()).getClass());
				}
				if (beanItem.getValue() instanceof IRuleAction) {
					builder.action(((IRuleAction) beanItem.getValue()).getClass());
				}
				if (beanItem.getValue() instanceof IRuleResultAction) {
					builder.resultAction(((IRuleResultAction) beanItem.getValue()).getClass());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		if (this.listenerBean != null && !this.listenerBean.equals("")) {
			ClassLoader classLoader = this.getClass().getClassLoader();
			Class<IRuleListener> listener;
			try {
				listener = (Class<IRuleListener>) classLoader.loadClass(this.listenerBean);
				builder.listener(listener);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 通过BeanDefinitionBuilder创建bean定义
		BeanDefinitionBuilder veryruleDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(VeryRule.class);
		veryruleDefinitionBuilder.addPropertyValue("ruleCondationMap", builder.getRuleCondationMap());
		veryruleDefinitionBuilder.addPropertyValue("ruleActionMap", builder.getRuleActionMap());
		veryruleDefinitionBuilder.addPropertyValue("ruleResultActionMap", builder.getRuleResultActionMap());
		veryruleDefinitionBuilder.addPropertyValue("ruleListener", builder.getRuleListener());
		// 注册bean
		context.registerBeanDefinition("veryrule", veryruleDefinitionBuilder.getRawBeanDefinition());
	}
	
	public void initAspect(DefaultListableBeanFactory context) {
		if(this.useAspect = true) {
			BeanDefinitionBuilder beanDefinitionBuilder = null;
			if(this.aspectBean!=null&&!this.aspectBean.equals("")) {
				Class clazz = null;
		        try {
		            clazz = Class.forName(this.aspectBean);
		            beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}else {
				beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(VeryRuleAspect.class);
			}
			context.registerBeanDefinition("veryRuleAspect", beanDefinitionBuilder.getRawBeanDefinition());
		}
		
	}
	
	
	public boolean isLoadDefaultRule() {
		return loadDefaultRule;
	}

	public void setLoadDefaultRule(boolean loadDefaultRule) {
		this.loadDefaultRule = loadDefaultRule;
	}

	public String getRulePackage() {
		return rulePackage;
	}

	public void setRulePackage(String rulePackage) {
		this.rulePackage = rulePackage;
	}
	
	public boolean isUseAspect() {
		return useAspect;
	}
	public void setUseAspect(boolean useAspect) {
		this.useAspect = useAspect;
	}
	public String getAspectBean() {
		return aspectBean;
	}
	public void setAspectBean(String aspectBean) {
		this.aspectBean = aspectBean;
	}

	public String getListenerBean() {
		return listenerBean;
	}

	public void setListenerBean(String listenerBean) {
		this.listenerBean = listenerBean;
	}
}
