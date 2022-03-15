<template>
	<el-tabs type="border-card" style="margin-bottom: 10px;" @tab-click="tabClick">
		<el-tab-pane label="规则流">
		</el-tab-pane>
		<el-tab-pane label="规则集">
		</el-tab-pane>
	</el-tabs>
	<router-view></router-view>
</template>

<script setup lang="ts">
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	const route = useRoute()
	const router = useRouter()
	router.push({
		name: "ruleFlow"
	})
	const tabClick = (target: string)=>{
		if(target.index == 0){
			router.push({
				name: "ruleFlow"
			})
		}else{
			router.push({
				name: "ruleSet"
			})
		}
	}
</script>

<style>
	.el-tabs__content {
		display: none;
	}
	.el-tabs__nav-scroll{
		border-left: 1px solid var(--el-border-color-base);
		border-top: 1px solid var(--el-border-color-base);
		box-shadow: 0 2px 4px 0 rgba(0,0,0,.12),0 0 6px 0 rgba(0,0,0,.04);
	}
	.el-tabs--border-card{
		border:none;
		box-shadow:none;
	}
</style>
