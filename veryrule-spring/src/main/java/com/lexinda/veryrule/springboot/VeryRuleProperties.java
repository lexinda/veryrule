package com.lexinda.veryrule.springboot;
/**
 * 
 * @author lexinda
 *
 */
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* @author zhumengle
* @version 创建时间：2022-3-2 13:51:01
* 类说明
*/
@ConfigurationProperties(prefix = VeryRuleProperties.VERY_RULE_PREFIX)
public class VeryRuleProperties {
	public static final String VERY_RULE_PREFIX="veryrule";
	private boolean loadDefaultRule;
	private String rulePackage;
	private boolean useAspect;
	private String aspectBean;
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
