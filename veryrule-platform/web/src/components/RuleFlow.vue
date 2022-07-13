<template>
	<el-row>
		<el-form ref="ruleFlowForm" :inline="true" :model="ruleFlow" class="demo-form-inline">
			<el-form-item label="规则流" prop="ruleFlowCode">
				<el-input v-model="ruleFlow.ruleFlowCode" placeholder="请输入规则流标识"></el-input>
			</el-form-item>
			<el-form-item label="规则名" prop="ruleFlowName">
				<el-input v-model="ruleFlow.ruleFlowName" placeholder="请输入规则名"></el-input>
			</el-form-item>
			<el-form-item label="组织" prop="groupName">
				<el-input v-model="ruleFlow.groupName" placeholder="请输入规则名"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="queryFlow">查询</el-button>
				<el-button type="primary" @click="addFlow">新增</el-button>
			</el-form-item>
		</el-form>
	</el-row>
	<el-table :data="tableData" ref="ruleFlowTable" style="width: 100%;" row-key="id" border lazy :load="load"
		:tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
		<el-table-column prop="ruleFlowCode" label="规则流">
		</el-table-column>
		<el-table-column prop="ruleFlowName" label="规则流名称">
		</el-table-column>
		<el-table-column prop="ruleFlowTempletCode" label="规则模板">
		</el-table-column>
		<el-table-column prop="groupName" label="组织">
		</el-table-column>
		<el-table-column label="状态">
			<template #default="scope">
				<el-tag type="success" v-if="scope.row.status == 1">启用</el-tag>
				<el-tag type="danger" v-else>禁用</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="描述">
			<template #default="scope">
				<el-popover effect="light" trigger="hover" placement="top" width="auto">
					<template #default>
						<div>{{ scope.row.ruleFlowDesc }}</div>
					</template>
					<template #reference>
						<el-tag>详</el-tag>
					</template>
				</el-popover>
			</template>
		</el-table-column>
		<el-table-column label="操作" width="400px">
			<template #default="scope">
				<el-button size="small" type="primary" @click="handleAdd(scope.$index, scope.row)">新增</el-button>
				<el-button size="small" type="warning" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
				<el-button size="small" type="warning" @click="handleCopy(scope.$index, scope.row)">复制</el-button>
				<el-button size="small" type="danger" @click="handleStatusEdit(scope.$index, scope.row,0)"
					v-if="scope.row.status == 1">禁用</el-button>
				<el-button size="small" type="danger" @click="handleStatusEdit(scope.$index, scope.row,1)" v-else>启用
				</el-button>
				<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row)">
					<template #reference>
						<el-button size="small" type="danger">删除</el-button>
					</template>
				</el-popconfirm>
				<div style="margin-top: 5px;" v-if="scope.row.ruleFlowTempletCode!=''">
					<el-button size="small" type="primary" @click="handleTempletDetail(scope.$index, scope.row)">查看规则模板
					</el-button>
					<el-button size="small" type="warning" @click="handleTempletEdit(scope.$index, scope.row)">编辑规则模板
					</el-button>
					<el-popconfirm title="是否删除规则模板?" @confirm="handleTempletDel(scope.$index, scope.row)">
						<template #reference>
							<el-button size="small" type="danger">
								删除规则模板
							</el-button>
						</template>
					</el-popconfirm>
				</div>
				<div style="margin-top: 5px;" v-else>
					<el-button size="small" type="primary" @click="handleTempletEdit(scope.$index, scope.row)">新增规则模板
					</el-button>
				</div>
				
				<div style="margin-top: 5px;">
					<el-button size="small" type="primary" @click="showTest(scope.$index, scope.row)">测试</el-button>
					<el-button size="small" type="primary" @click="showScene(scope.$index, scope.row)">场景</el-button>
				</div>
				
			</template>
		</el-table-column>
	</el-table>
	<div class="demo-pagination-block" style="float: right;margin-right: 20px;">
		<el-pagination v-model:currentPage="pageCurrent" v-model:page-size="pageSize"
			:page-sizes="[10, 20, 40, 60, 80, 100]" :small="small" :disabled="disabled" :background="background"
			layout="total, sizes, prev, pager, next, jumper" :total="pageTotal" @size-change="handleSizeChange"
			@current-change="handleCurrentChange">
		</el-pagination>
	</div>
	<el-dialog v-model="flowEditVisible" :title="flowEditTitle" destroy-on-close width="30%">
		<FlowEdit :ruleFlowData="currentRuleFlowData" @cancelFlowEdit="cancelFlowEdit"
			@successFlowUpdate="successFlowUpdate" @successFlowAdd="successFlowAdd"></FlowEdit>
	</el-dialog>

	<el-dialog v-model="flowTempletEditVisible" :title="currentRuleFlowData.ruleFlowName" destroy-on-close
		:fullscreen="true">
		<FlowTempletEdit @updateVeryRuleFlow="updateVeryRuleFlow" @cancelFlowTempletEdit="cancelFlowTempletEdit"
			:ruleFlowTemplet="ruleFlowTemplet"></FlowTempletEdit>
	</el-dialog>

	<el-dialog v-model="flowTempletDetailVisible" :title="currentRuleFlowData.ruleFlowName" destroy-on-close
		:fullscreen="true">
		<FlowTempletEditDetail @cancelFlowTempletDetailEdit="cancelFlowTempletDetailEdit"
			:ruleFlowTemplet="ruleFlowTemplet"></FlowTempletEditDetail>
	</el-dialog>
	<el-dialog v-model="veryFlowtempletCopyVisible" destroy-on-close width="500px">
		<FlowCopy :veryFlowAllData="veryFlowAllData" :veryFlowtempletCopyData = "veryFlowtempletCopyData"
		 @cancelFlowCopy="cancelFlowCopy" @successFlowCopy="successFlowCopy"></FlowCopy>
	</el-dialog>
	
	<el-dialog v-model="templetTestVisible" destroy-on-close width="500px">
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
	
	<el-dialog v-model="veryFlowtempletSceneVisible" destroy-on-close width="800px">
		<FlowScene :veryFlowSceneData="veryFlowSceneData" @cancelFlowScene="cancelFlowScene"></FlowScene>
	</el-dialog>
</template>

<script lang="ts" setup>
	import FlowEdit from "./flow/FlowEdit.vue";
	import FlowCopy from "./flow/FlowCopy.vue";
	import FlowTempletEdit from "./flow/FlowTempletEdit.vue";
	import FlowTempletEditDetail from "./flow/FlowTempletEditDetail.vue";
	import FlowScene from "./flow/FlowScene.vue";
	import {
		ElMessage
	} from 'element-plus'
	import post from "../axios/post.js";
	import {
		ref,
		onMounted,
		nextTick
	} from 'vue'

	onMounted(() => {
		pageCurrent.value = 1
		getRuleFlowPage("root")
	})

	/* ruleFlowForm  */

	const ruleFlow = ref({
		ruleFlowCode: '',
		ruleName: '',
		groupName: [],
	})

	const addFlow = () => {
		currentRuleFlowData.value = {
			"id": 0,
			"ruleFlowCode": "",
			"ruleFlowName": "",
			"parentRuleFlowCode": "root",
			"groupName": "",
			"ruleFlowDesc": "",
		}
		flowEditTitle.value = '新增规则流'
		flowEditVisible.value = true
	}

	const queryFlow = () => {
		pageCurrent.value = 1
		getRuleFlowPage("root")
	}

	/* ruleFlowTable */

	const tableData = ref(null)

	const currentRuleFlowData = ref({})

	interface RuleFlow {
		id: number
		ruleFlowCode: string
		ruleFlowName: string
		parentRuleFlowCode: String
		groupName: string
		status: number
		ruleFlowDesc: string
		ruleFlowTempletCode: string
		hasChildren ? : boolean
		children ? : RuleFlow[]
	}

	const ruleFlowForm = ref()

	const resolveFunc = ref()
	const load = (
		row: RuleFlow,
		treeNode: unknown,
		resolve: (ruleFlowCode: RuleFlow[]) => void
	) => {
		const param = {
			"parentRuleFlowCode": row.ruleFlowCode,
			"currentPage": 1,
			"pageSize": pageSize.value,
			"ruleFlowCode": ruleFlowForm.value["model"].ruleFlowCode,
			"ruleFlowName": ruleFlowForm.value["model"].ruleFlowName,
			"groupName": ruleFlowForm.value["model"].groupName,
		}
		post("/api/getVeryRuleFlowPage", param, (data) => {
			if (data.errorCode == 0) {
				if (data.body.records.length > 0) {
					if (typeof resolve === "function") {
						resolve(data.body.records)
					} else {
						var ruleFlowData = ruleFlowTable.value.store.states.lazyTreeNodeMap.value[row.id]
						if (ruleFlowData != undefined) {
							ruleFlowData = []
							for (var index in data.body.records) {
								ruleFlowData.push(data.body.records[index])
							}
							ruleFlowTable.value.store.states.lazyTreeNodeMap.value[row.id] = ruleFlowData
						}
					}
				}
			}
		});
	}


	const getRuleFlowPage = (parentRuleFlowCode) => {
		const param = {
			"parentRuleFlowCode": parentRuleFlowCode,
			"currentPage": pageCurrent.value,
			"pageSize": pageSize.value,
			"ruleFlowCode": ruleFlowForm.value["model"].ruleFlowCode,
			"ruleFlowName": ruleFlowForm.value["model"].ruleFlowName,
			"groupName": ruleFlowForm.value["model"].groupName,
		}
		post("/api/getVeryRuleFlowPage", param, (data) => {
			if (data.errorCode == 0) {
				pageCurrent.value = data.body.current
				pageSize.value = data.body.size
				pageTotal.value = data.body.total
				tableData.value = data.body.records
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}

	const handleStatusEdit = (index: number, row: RuleFlow, status: number) => {
		const param = {
			"id": row.id,
			"status": status,
		}
		post("/api/updateVeryRuleFlowStatus", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
					message: '保存成功',
					type: 'success',
				})
				location.href='/'
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}

	const handleEdit = (index: number, row: RuleFlow) => {
		flowEditTitle.value = row.ruleFlowName
		flowEditVisible.value = true
		currentRuleFlowData.value = row
	}

	const handleAdd = (index: number, row: RuleFlow) => {
		ruleFlowTableRow.value = row
		currentRuleFlowData.value = {
			"id": 0,
			"ruleFlowCode": "",
			"ruleFlowName": "",
			"parentRuleFlowCode": row.ruleFlowCode,
			"groupName": "",
			"ruleFlowDesc": "",
		}
		flowEditTitle.value = row.ruleFlowName + '-新增规则流'
		flowEditVisible.value = true
	}
	const handleDelete = (index: number, row: RuleFlow) => {
		const param = {
			"id": row.id
		}

		post("/api/delVeryRuleFlow", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
					message: '保存成功',
					type: 'success',
				})
				var isDel = false
				for (var index in ruleFlowTable.value.store.states.lazyTreeNodeMap.value) {
					const item = ruleFlowTable.value.store.states.lazyTreeNodeMap.value[index]
					for (var m in item) {
						console.log(item[m].id)
						if (item[m].id == row.id) {
							item.splice(m, 1)
							isDel = true
						}
					}
				}
				if(!isDel){
					getRuleFlowPage("root")
				}
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}
	/* ruleFlowPage */
	const pageCurrent = ref(1)
	const pageSize = ref(10)
	const pageTotal = ref(0)
	const small = ref(false)
	const background = ref(false)
	const handleSizeChange = (val: number) => {
		getRuleFlowPage("root")
	}
	const handleCurrentChange = (val: number) => {
		getRuleFlowPage("root")
	}
	/* ruleFlowEdit */
	const flowEditVisible = ref(false)
	const flowEditTitle = ref('')

	const cancelFlowEdit = () => {
		flowEditVisible.value = false
	}
	const successFlowUpdate = () => {
		flowEditVisible.value = false
		getRuleFlowPage("root")
	}
	const ruleFlowTable = ref()
	const ruleFlowTableRow = ref()
	const successFlowAdd = () => {
		flowEditVisible.value = false
		if (ruleFlowTableRow.value != undefined) {
			ruleFlowTableRow.value.hasChildren = true
			ruleFlowTable.value.store.loadOrToggle(ruleFlowTableRow.value)
		} else {
			getRuleFlowPage("root")
		}
		load(ruleFlowTableRow.value, null, null)
	}
	/* ruleFlowTempletEdit */
	const ruleFlowTemplet = ref()
	const handleTempletEdit = (index: number, row: RuleFlow) => {
		currentRuleFlowData.value = row
		getFlowTempletData(1)
	}
	const flowTempletEditVisible = ref(false)
	const cancelFlowTempletEdit = () => {
		flowTempletEditVisible.value = false
	}
	const updateVeryRuleFlow = (ruleFlowTempletCode) => {
		currentRuleFlowData.value.ruleFlowTempletCode = ruleFlowTempletCode
	}
	/* ruleFlowTempletEditDetail */
	const handleTempletDetail = (index: number, row: RuleFlow) => {
		currentRuleFlowData.value = row
		getFlowTempletData(2)
	}
	const flowTempletDetailVisible = ref(false)
	const cancelFlowTempletDetailEdit = () => {
		flowTempletDetailVisible.value = false
	}

	const handleTempletDel = (index: number, row: RuleFlow) => {
		currentRuleFlowData.value = row
		const param = {
			"ruleFlowTempletCode": currentRuleFlowData.value.ruleFlowTempletCode
		}
		post("/api/delVeryRuleFlowAndTemplet", param, (data) => {
			if (data.errorCode == 0) {
				currentRuleFlowData.value.ruleFlowTempletCode = ""
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}

	const getFlowTempletData = (templetType) => {
		ruleFlowTemplet.value = {
			"ruleFlowId": currentRuleFlowData.value.id,
			"ruleFlowTempletCode": "",
			"ruleFlowTemplet": [],
		}
		const param = {
			"ruleFlowTempletCode": currentRuleFlowData.value.ruleFlowTempletCode
		}
		post("/api/getVeryRuleFlowTempletList", param, (data) => {
			if (data.errorCode == 0) {
				if (templetType == 1) {
					flowTempletEditVisible.value = true
				} else {
					flowTempletDetailVisible.value = true
				}
				if (data.body.length > 0) {
					ruleFlowTemplet.value = {
						"ruleFlowId": currentRuleFlowData.value.id,
						"ruleFlowTempletCode": data.body[0].ruleFlowTempletCode,
						"ruleFlowTemplet": JSON.parse(data.body[0].ruleFlowTemplet),
					}
				}
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}
	/* veryFlowtempletCopy */
	const veryFlowtempletCopyVisible = ref(false)
	const veryFlowtempletCopyData = ref()
	const veryFlowAllData = ref()
	
	const handleCopy = (index: number, row: RuleFlow) => {
		const param = {}
		post("/api/getVeryRuleFlowList", param, (data) => {
			if (data.errorCode == 0) {
				veryFlowAllData.value = data.body
				veryFlowtempletCopyData.value = {
					"id": row.id,
					"parentRuleFlowCode": row.parentRuleFlowCode,
					"ruleFlowCode": row.ruleFlowCode + "Copy",
					"ruleFlowName": row.ruleFlowName + "副本",
				}
				veryFlowtempletCopyVisible.value = true
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
		
	}
	
	const successFlowCopy = ()=>{
		location.href='/'
	}

	const cancelFlowCopy = () => {
		veryFlowtempletCopyVisible.value = false
	}
	
	const templetTestVisible = ref(false)
	const templetTestResult = ref([])
	const showTest = (index: number, row: RuleFlow)=>{
		const param = {
			"ruleFlowTempletCode": row.ruleFlowTempletCode
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
				console.log(templetTestResult)
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
	
	const veryFlowtempletSceneVisible = ref(false)
	const veryFlowSceneData = ref([])
	const showScene=(index: number, row: RuleFlow)=>{
		veryFlowSceneData.value = []
		const param = {
			"ruleFlowTempletCode": row.ruleFlowTempletCode
		}
		post("/api/showSceneInfo", param, (data) => {
			if (data.errorCode == 0) {
				if (data.body.length > 0) {
					veryFlowSceneData.value = data.body
					veryFlowtempletSceneVisible.value = true
				}
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}
	const cancelFlowScene=()=>{
		veryFlowtempletSceneVisible.value = false
	}
</script>
