package com.lexinda.veryrule.springboot;
/**
 * 
 * @author lexinda
 *
 */
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import com.lexinda.veryrule.VeryRule;
import com.lexinda.veryrule.annotation.Rule;
import com.lexinda.veryrule.aspect.VeryRuleAspect;
import com.lexinda.veryrule.core.IRuleAction;
import com.lexinda.veryrule.core.IRuleCondation;
import com.lexinda.veryrule.core.IRuleListener;
import com.lexinda.veryrule.core.IRuleResultAction;
import com.lexinda.veryrule.spring.VeryRuleClassPathDefinitionScanner;

@Configuration
@EnableConfigurationProperties(VeryRuleProperties.class)
public class VeryRuleAutoConfiguration implements BeanPostProcessor, ApplicationContextAware {

	@Autowired
	private GenericApplicationContext context;

	@Autowired
	private VeryRuleProperties veryRuleProperties;

	@Bean("veryRuleAspect")
	public <aspect extends VeryRuleAspect> aspect initVeryRuleAspect() {
		if (veryRuleProperties.isUseAspect()) {
			if (veryRuleProperties.getAspectBean() != null && !veryRuleProperties.getAspectBean().equals("")) {
				Class clazz = null;
				try {
					clazz = Class.forName(veryRuleProperties.getAspectBean());
					return (aspect) clazz.getDeclaredConstructor().newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return (aspect) new VeryRuleAspect();
			}
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 扫描ruleBean
		VeryRuleClassPathDefinitionScanner veryRuleClassPathDefinitionScanner = new VeryRuleClassPathDefinitionScanner(
				context, Rule.class);
		veryRuleClassPathDefinitionScanner.registerTypeFilter();
		if (veryRuleProperties.getRulePackage() != null && !veryRuleProperties.getRulePackage().equals("")) {
			veryRuleClassPathDefinitionScanner.scan(veryRuleProperties.getRulePackage());
		}

		if (veryRuleProperties.isLoadDefaultRule()) {
			veryRuleClassPathDefinitionScanner.scan("com.lexinda.veryrule.base");
		}
		VeryRule veryRule = VeryRule.builder();
		Map<String, Object> ruleBeanMap = context.getBeansWithAnnotation(Rule.class);
		ruleBeanMap.entrySet().forEach(bean -> {
			try {
				if (bean.getValue() instanceof IRuleCondation) {
					veryRule.condation(((IRuleCondation) bean.getValue()).getClass());
				}
				if (bean.getValue() instanceof IRuleAction) {
					veryRule.action(((IRuleAction) bean.getValue()).getClass());
				}
				if (bean.getValue() instanceof IRuleResultAction) {
					veryRule.resultAction(((IRuleResultAction) bean.getValue()).getClass());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		if (veryRuleProperties.getListenerBean() != null && !veryRuleProperties.getListenerBean().equals("")) {
			ClassLoader classLoader = this.getClass().getClassLoader();
			Class<IRuleListener> listener;
			try {
				listener = (Class<IRuleListener>) classLoader.loadClass(veryRuleProperties.getListenerBean());
				veryRule.listener(listener);
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
		veryruleDefinitionBuilder.addPropertyValue("ruleCondationMap", veryRule.getRuleCondationMap());
		veryruleDefinitionBuilder.addPropertyValue("ruleActionMap", veryRule.getRuleActionMap());
		veryruleDefinitionBuilder.addPropertyValue("ruleResultActionMap", veryRule.getRuleResultActionMap());
		veryruleDefinitionBuilder.addPropertyValue("ruleListener", veryRule.getRuleListener());
		veryruleDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		
		// 注册bean
		context.registerBeanDefinition("veryrule", veryruleDefinitionBuilder.getRawBeanDefinition());
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

}
