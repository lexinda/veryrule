# veryrule ![image](https://portrait.gitee.com/uploads/avatars/user/40/122663_lexindasoft_1647328799.png!avatar30)
## 特性
- 可设置单个规则或配置规则包路径


- 可单行代码调用


- 可自定义监听器


- 可测试规则是否触达


- 可注解调用（结合spring开启切面）


- 可自定义注解配置和取值

- 可使用线程池处理带返回值的条件规则

- 可使用内置规则流工具创建

## 规则管理工具(veryrule-platform)
- 规则管理：新增、编辑、删除。


- 规则流管理：新增、编辑、删除、禁用、复制、
			查看规则模板、编辑规则模板、删除规则模板、
			测试、场景
			
			
- 规则工具：检测创建未使用的规则

## 使用

```
	//定义不带返回值条件
	@Rule(code = RuleCode.NOTNULL,name = "指定key不可为空", desc = "指定key不可为空", type = RuleType.CONDATION)
	public class NotNullRuleCondation implements IRuleCondation {
	
		@Override
		public <R extends RuleBo> void contation(Map<String, Object> param, R rule) throws Exception {
			String ruleKey = rule.getRuleKey();
			String[] keys = null;
			if(ruleKey.indexOf(",")>0) {
				keys = ruleKey.split(",");
			}else {
				keys = new String[] {ruleKey};
			}
			for(String key :keys) {
				Object data = param.get(key);
				if(data==null) {
					throw new Exception(key+rule.getRuleErrMsg());
				}else {
					if("".equals(data.toString().trim())) {
						throw new Exception(key+rule.getRuleErrMsg());
					}
				}
			}
		}
	
		//可覆盖，自定义
		//@Override
		//public <R extends RuleBo> Map<String, Object> ruleTest(R rule) {
		//	Map<String,Object> result = new HashMap<String,Object>();
		//	Rule ruleAnnotation = this.getClass().getAnnotation(Rule.class);
		//	if(rule!=null&&rule.getRuleCode().equals(ruleAnnotation.code())) {
		//		result.put(rule.getRuleCode(), ruleAnnotation.name());
		//	}
		//	return result;
		//}
	
	}
	//初始化
	//可指定规则类
	VeryRule veryRule = VeryRule.builder().condation(NotNullRuleAction.class).resultCondation(...).action(...).listener(...);
	//可指定规则包路径
	VeryRule veryRule = VeryRule.builder().rulePackage("com.lexinda.veryrule.base").listener(TestRuleListener.class)
	//单独使用
	Map<String, Object> param = new HashMap<>();
	param.put("a", "123a");
	RuleBo nn = new RuleBo("notNull", "a,c", "", "不可为空",RuleType.CONDATION);
	veryRule.fire(param, nn);
	
	//匹配spring
	<bean class="com.lexinda.veryrule.spring.VeryRuleScanConfigurer">
		<property name="rulePackage"
			value="com.lexinda.veryrule.base" />
		<property name="loadDefaultRule"
			value="true" />
		<property name="useAspect"
           	value="true" />
		<property name="aspectBean"
           	value="com.lexinda.veryrule.aspect.VeryRuleAspect" />
           <property name="listenerBean"
           	value="com.lexinda.veryrule.platform.listener.VeryRuleListener" />
			
	</bean>
	//匹配spring-boot
	//veryrule包路径
	veryrule.rulePackage=com.lexinda.veryrule.base
	//读取集成规则
	veryrule.loadDefaultRule=true
	//开启注解
	veryrule.useAspect=true
	//自定义切面(取值，校验...)
	veryrule.aspectBean=com.lexinda.veryrule.aspect.VeryRuleAspect
	//自定义监听器
	veryrule.listenerBean=com.lexinda.veryrule.platform.listener.VeryRuleListener
	
	//springAOP-单个规则，规则类型缺省为不带返回值的条件
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "currentPage,ruleType", ruleErrMsg = "不能为空")
	//springAOP-编排规则流
	@VeryRuleFlow(ruleFlowCode = "test")
```