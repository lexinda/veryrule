# veryrule
```
	//定义规则
	@Rule(name = "notNull", desc = "指定key不可为空", type = RuleType.ACTION)
	public class NotNullRuleAction implements IRuleAction {
	
		@Override
		public <R extends RuleModel> void action(Map<String, Object> param,
				Map<String, Object> condation, R rule) throws Exception {
			// TODO Auto-generated method stub
			String ruleKey = ((RuleKeyModel)rule).getKey();
			String[] keys = null;
			if(ruleKey.indexOf(",")>0) {
				keys = ruleKey.split(",");
			}else {
				keys = new String[] {ruleKey};
			}
			for(String key :keys) {
				Object data = param.get(key);
				if(data==null) {
					throw new Exception(key+rule.getErrorMsg());
				}else {
					if("".equals(data.toString().trim())) {
						throw new Exception(key+rule.getErrorMsg());
					}
				}
			}
		}
	
	}

	//单独使用
	VeryRule veryRuleFactory = VeryRule.builder().condation(...).action(NotNullRuleAction.class).resultAction(...).build();
	Map<String, Object> param = new HashMap<>();
	param.put("a", "123a");
	List<String> resCondation = new ArrayList<String>();
	resCondation.add("regexRuleCondation");
	RuleKeyModel nn = new RuleKeyModel("notNull", "a,c", "", "不可为空",resCondation);
	veryRuleFactory.fire(param, nn);
	
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
	veryrule.rulePackage=com.lexinda.veryrule.base
	veryrule.loadDefaultRule=true
	veryrule.useAspect=true
	veryrule.aspectBean=com.lexinda.veryrule.aspect.VeryRuleAspect
	veryrule.listenerBean=com.lexinda.veryrule.platform.listener.VeryRuleListener
	
	//springAOP
	@VeryRuleSingle(ruleCode = RuleCode.NOTNULL, ruleKey = "currentPage,ruleType", ruleErrMsg = "不能为空")
	//springAOP-编排规则流
	@VeryRuleFlow(ruleFlowCode = "test")
