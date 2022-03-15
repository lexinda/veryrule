<template>
	<el-row class="tac">
		<el-col :span="17">
			<el-table :data="tableData" style="width: 100%;height:80vh" border>
				<el-table-column label="规则名称" align="center">
					<template #default="scope">
						<div>{{ scope.row.ruleCode }}</div>
						<el-popover effect="light" trigger="hover" placement="top" width="auto">
							<template #default>
								<div>{{ scope.row.ruleDesc }}</div>
							</template>
							<template #reference>
								<el-tag>({{ scope.row.ruleName }})</el-tag>
							</template>
						</el-popover>
					</template>
				</el-table-column>
				<el-table-column label="指定入参key" prop="ruleKey">
				</el-table-column>
				<el-table-column label="默认值" prop="ruleValue">
				</el-table-column>
				<el-table-column label="执行条件">
					<template #default="scope">
			  	<div v-for="(item,index) in scope.row.ruleCondations">{{ item }}</div>
					</template>
				</el-table-column>
				<el-table-column label="异常提示" prop="ruleErrMsg">
				</el-table-column>
			</el-table>
		</el-col>
		<el-col :span="7">
			<div style="height:80vh;">
				<el-row class="mb-4">
					<el-button type="primary" @click="handleTempletTest">测试</el-button>
				</el-row>
				<div style="height:80vh;overflow-y:scroll;margin-top: 10px;">
					<json-viewer :value="jsonData" box sort :expandDepth="9" />
				</div>
			</div>
		</el-col>
	</el-row>
	<el-dialog v-model="templetTestVisible" destroy-on-close width="500px" @close="templetTestClose">
		<div style="max-height: 500px;overflow-y: scroll;">
			<div v-for="(item,index) in templetTestResult" style="width: 400px;">
				<div style=" text-align: center;width: 200px;height: 40px;float: left;">
					<span >{{item.ruleCode}}</span><br>
					<span >{{item.ruleName}}</span>
				</div>
				<div style=" text-align: center;width: 200px;height: 40px;float: left;">
					<span style="line-height: 40px;height: 40px;" v-if="item.success">成功</span>
					<span style="line-height: 40px;height: 40px;color:red;" v-else>失败</span>
				</div>
			</div>
		</div>
	</el-dialog>
</template>

<script setup lang="ts">
	import {
		ref,
		onMounted,
		nextTick
	} from 'vue'
	
	import post from "../../axios/post.js";
	
	const props = defineProps < {
		ruleFlowTemplet: Object
	} > ()
	
	const templetTestVisible = ref(false)
	const templetTestResult = ref([])
	const handleTempletTest = ()=>{
		const param = {
			"ruleFlowTemplet": tableData.value
		}
		post("/api/testVeryRuleFlow", param, (data) => {
			templetTestResult.value = []
			if (data.errorCode == 0) {
				templetTestVisible.value = true
				for(var index in tableData.value){
					var success = false
					if(data.body.result[tableData.value[index].ruleCode]!=null){
						success = true
					}
					const result = {
						"ruleCode":tableData.value[index].ruleCode,
						"ruleName":tableData.value[index].ruleName,
						"success":success
					}
					templetTestResult.value.push(result)
				}
				console.log(templetTestResult)
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}

	import "vue3-json-viewer/dist/index.css";

	interface RuleData {
		id: number
		ruleCode: string
		ruleName: string
		ruleValue: string
		ruleKey: string
		ruleCondation: string
		ruleCondations: Array
		ruleErrMsg: string
		ruleDesc: string
	}

	const tableData: RuleData[] = ref(props.ruleFlowTemplet.ruleFlowTemplet)

	const jsonData = ref(tableData.value)
</script>
