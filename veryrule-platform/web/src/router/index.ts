import { createRouter, createWebHistory,createWebHashHistory } from 'vue-router'
import RuleFlow from "../components/RuleFlow.vue"
import RuleSet from "../components/RuleSet.vue"
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
      }
  ],
})

export default router
