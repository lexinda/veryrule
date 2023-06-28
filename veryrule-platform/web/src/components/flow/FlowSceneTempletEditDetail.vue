<template>
	<el-row class="tac">
		<el-col :span="17">
			<i class="rule-tips">
				<el-icon>
				    <Bell />
				  </el-icon>:条件(无返回值)
				  <el-icon>
				      <BellFilled />
				    </el-icon>:条件(有返回值)
					<el-icon>
					    <Promotion />
					  </el-icon>:动作
			</i>
				<div style="height:85vh;overflow-y:scroll;">
					<div v-for="(scene,index) in ruleScene">
						<fieldset style="margin-top: 10px;min-width: 90%;border: 1px solid #dcdfe6;" v-if="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode] && ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].length>0">
							<legend style="font-size: 16px;">{{scene.ruleSceneName}}({{scene.ruleSceneCode}})</legend>
							<el-table :data="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType != 3)" v-if="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType != 3).length>0" style="width: 100%;" border @row-click="tableRowClick">
								<el-table-column label="(条件)规则名称" align="center">
									<template #default="scope">
										<div>{{ scope.row.ruleCode }}
										<el-icon v-if="scope.row.ruleType == 1">
											<Bell />
										  </el-icon>
										  <el-icon v-if="scope.row.ruleType == 2">
											  <BellFilled />
											</el-icon>
										</div>
										<div v-if="scope.row.ruleAsyn == 1">{{ scope.row.ruleName }}(<font color="red">异步</font>)</div>
										<div v-else>{{ scope.row.ruleName }}</div>
									</template>
								</el-table-column>
								<el-table-column label="指定入参key" prop="ruleKey">
								</el-table-column>
								<el-table-column label="默认值" prop="ruleValue">
								</el-table-column>
								<el-table-column label="表达式" prop="ruleExpr">
								</el-table-column>
								<el-table-column label="异常提示" prop="ruleErrMsg">
								</el-table-column>
							</el-table>
							<el-table :data="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 3)" v-if="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 3).length>0" style="width: 100%;" border @row-click="tableRowClick">
								<el-table-column label="(执行动作)规则名称" align="center">
									<template #default="scope">
										<div>{{ scope.row.ruleCode }}
											<el-icon>
											    <Promotion />
											  </el-icon>
										</div>
										<div>{{ scope.row.ruleName }}</div>
									</template>
								</el-table-column>
								<el-table-column label="指定入参key" prop="ruleKey">
								</el-table-column>
								<el-table-column label="默认值" prop="ruleValue">
								</el-table-column>
								<el-table-column label="表达式" prop="ruleExpr">
								</el-table-column>
								<el-table-column label="异常提示" prop="ruleErrMsg">
								</el-table-column>
							</el-table>
						</fieldset>
					</div>
				</div>
			</el-col>
		<el-col :span="7">
			<div style="height:80vh;">
				<el-row class="mb-4">
					<el-button style="margin-left:5px;" type="primary" @click="handleTempletTest">测试</el-button>
				</el-row>
				<div style="height:80vh;overflow-y:scroll;margin-top: 10px;text-align: left;">
					<json-viewer :value="jsonData" box sort :expandDepth="9" />
				</div>
			</div>
		</el-col>
	</el-row>
	<el-dialog v-model="templetTestVisible" destroy-on-close width="500px" @close="templetTestClose">
		<div style="max-height: 500px;overflow-y: scroll;">
			<div v-for="(item,index) in templetTestResult" style="width: 460px;border-bottom: 1px dotted #aaaaaa;height: 40px;padding:5px 0px;">
				<div style=" text-align: center;width: 200px;float: left;">
					<span >{{item.ruleCode}}</span><br>
					<span >{{item.ruleName}}</span>
				</div>
				<div style=" text-align: center;width: 200px;float: left;line-height: 40px;">
					<span style="" v-if="item.success">成功</span>
					<span style="color:red;" v-else>失败</span>
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
	import {
		ElMessage
	} from 'element-plus'
	import {
			Bell,
			BellFilled,
			Promotion
		} from '@element-plus/icons-vue'
	import post from "../../axios/post.js";
	
	onMounted(() => {
		getRuleSceneList(props.ruleFlowTempletData.ruleSceneId)
	})
	
	const props = defineProps < {
		ruleFlowTempletData: Object
	} > ()
	
	const templetTestVisible = ref(false)
	const templetTestResult = ref([])
	const handleTempletTest = ()=>{
		const param = {
			"ruleFlowTemplet": jsonData.value
		}
		post("/api/testVeryRuleFlow", param, (data) => {
			templetTestResult.value = []
			if (data.errorCode == 0) {
				templetTestVisible.value = true
				const result = data.body;
				for(var ruleCode in result){
					const ruleName = result[ruleCode]
					var resultData = {
						"ruleCode":ruleCode,
						"ruleName":ruleName,
						"success":true
					}
					if(ruleName == ''){
						resultData.success = false
					}
					
					templetTestResult.value.push(resultData)
				}
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

	const jsonData = ref(props.ruleFlowTempletData.ruleFlowTemplet)
	const ruleScene = ref([])
	const getRuleSceneList = (pid) => {
		const param = {
			"pid": pid,
		}
		post("/api/getVeryRuleSceneList", param, (data) => {
			if (data.errorCode == 0) {
				console.log(data)
				ruleScene.value = data.body
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}
</script>
<style scoped>
	.rule-tips{
		position: absolute;
		top:-35px;
		left:100px;
		font-size:10px;
		color:#aaaaaa;
	}
</style>
