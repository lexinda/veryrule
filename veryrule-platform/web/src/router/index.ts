import { createRouter, createWebHistory,createWebHashHistory } from 'vue-router'
import RuleFlow from "../components/RuleFlow.vue"
import RuleSet from "../components/RuleSet.vue"
import RuleTool from "../components/RuleTool.vue"
const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
        path: "/ruleFlow",
        name: "ruleFlow",
        component: RuleFlow
      },
      {
        path: "/ruleSet",
        name: "ruleSet",
        component: RuleSet
      },
	  {
	    path: "/ruleTool",
	    name: "ruleTool",
	    component: RuleTool
	  }
  ],
})

export default router
