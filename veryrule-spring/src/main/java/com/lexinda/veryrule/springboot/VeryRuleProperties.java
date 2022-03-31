package com.lexinda.veryrule.springboot;
/**
 * 
 * @author lexinda
 *
 */
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = VeryRuleProperties.VERY_RULE_PREFIX)
public class VeryRuleProperties {
	public static final String VERY_RULE_PREFIX="veryrule";
	/**
	 * 加载默认规则
	 */
	private boolean loadDefaultRule;
	/**
	 * 待读取规则路径
	 */
	private String rulePackage;
	/**
	 * 使用规则切面
	 */
	private boolean useAspect;
	/**
	 * 规则切面实现
	 */
	private String aspectBean;
	/**
	 * 规则监听实现
	 */
	private String listenerBean;
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
