<template>
	<el-row>
		<el-col :span="2">
			<el-tabs :tab-position="tabPosition" style="height: 90vh;" type="border-card" @tab-click="tabClick">
				<el-tab-pane label="规则使用详情">
				</el-tab-pane>
			</el-tabs>
		</el-col>
		<el-col :span="22">
			<el-row>
				<el-form ref="ruleActionFormRef" :inline="true" :model="rule" class="demo-form-inline">
					<el-form-item>
						<el-button type="primary" @click="searchDiffRule">查询</el-button>
					</el-form-item>
				</el-form>
			</el-row>
			<em style="font-size: 10px;color:red;">黄色:规则存在规则流没用到，红色:规则不存在规则流用到</em>
			<div style="border: #eeeeee solid 1px;">
				<span v-for="(rule,index) in diffRule" style="margin-left: 5px;padding-bottom: 5px;">
					<el-tag class="ml-2" type="danger"   effect="dark" v-if="rule.split('_')[1] == 2">{{rule.split("_")[0]}}</el-tag>
				</span>
			</div>
			<div style="border: #eeeeee solid 1px;margin-top: 10px;padding-bottom: 5px;">
				<span v-for="(rule,index) in diffRule" style="margin-left: 5px;">
					<el-tag class="ml-2" type="warning"  effect="dark" v-if="rule.split('_')[1] == 1">{{rule.split("_")[0]}}</el-tag>
				</span>
			</div>
			<div style="border: #eeeeee solid 1px;margin-top: 10px;padding-bottom: 5px;">
				<span v-for="(rule,index) in diffRule" style="margin-left: 5px;margin-bottom: 5px;">
					<el-tag class="ml-2" type="success"   effect="dark" v-if="rule.split('_')[1] == 0">{{rule.split("_")[0]}}</el-tag>
				</span>
			</div>
			
		</el-col>
	</el-row>
</template>

<script lang="ts" setup>
	import {
		ElMessage
	} from 'element-plus'
	import post from "../axios/post.js";
	import {
		ref,
		onMounted
	} from 'vue'

	/* tab */
	const tabPosition = ref('left')
	const ruleType = ref(1)
	const tabClick = (target: string) => {
		if (target.index == 1) {
			ruleType.value = 2
		} else {
			ruleType.value = 1
		}
	}

	const diffRule = ref([])
	const searchDiffRule = () => {
		diffRule.value = []
		const param = {
		}
		post("/api/compareRuleActive", param, (data) => {
			if (data.errorCode == 0) {
				diffRule.value = data.body
			}
		});
	}
</script>

