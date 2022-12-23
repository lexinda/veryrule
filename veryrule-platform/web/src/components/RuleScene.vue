<template>
	<el-row>
		<el-form ref="ruleSceneForm" :inline="true" :model="ruleScene" class="demo-form-inline">
			<el-form-item label="场景编码" prop="ruleSceneCode">
				<el-input v-model="ruleScene.ruleSceneCode" placeholder="请输入场景编码"></el-input>
			</el-form-item>
			<el-form-item label="场景名称" prop="ruleSceneName">
				<el-input v-model="ruleScene.ruleSceneName" placeholder="请输入场景名称"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="queryScene">查询</el-button>
				<el-button type="primary" @click="addScene">新增</el-button>
			</el-form-item>
		</el-form>
	</el-row>
	<el-table :data="tableData" ref="ruleSceneTable" style="width: 100%;" row-key="id" border lazy :load="load"
		:tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
		<el-table-column prop="ruleSceneCode" label="场景编码">
		</el-table-column>
		<el-table-column prop="ruleSceneName" label="场景名称">
		</el-table-column>
		<el-table-column label="场景描述">
			<template #default="scope">
				<div>{{ scope.row.ruleSceneDesc }}</div>
			</template>
		</el-table-column>
		<el-table-column label="操作" width="400px">
			<template #default="scope">
				<el-button size="small" type="primary" v-if="scope.row.ruleSceneType == '1'" @click="handleAdd(scope.$index, scope.row)">新增</el-button>
				<el-button size="small" type="warning" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
				<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row)">
					<template #reference>
						<el-button v-if="!scope.row.hasChildren" size="small" type="danger">删除</el-button>
					</template>
				</el-popconfirm>
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
	<el-dialog v-model="sceneEditVisible" :title="sceneEditTitle" destroy-on-close width="30%">
		<SceneEdit :ruleSceneData="currentRuleSceneData" @cancelSceneEdit="cancelSceneEdit"
			@successSceneUpdate="successSceneUpdate" @successSceneAdd="successSceneAdd"></SceneEdit>
	</el-dialog>
</template>

<script lang="ts" setup>
	import SceneEdit from "./scene/SceneEdit.vue";
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
		getRuleScenePage("0")
	})

	/* ruleSceneForm  */

	const ruleScene = ref({
		id: 0,
		ruleSceneCode: '',
		ruleSceneName: '',
		ruleSceneType: '1',
		ruleSceneDesc: '',
	})

	const addScene = () => {
		currentRuleSceneData.value = {
			"id": 0,
			"pid":0,
			"ruleSceneCode": "",
			"ruleSceneName": "",
			"ruleSceneType": "1",
			"ruleSceneDesc": ""
		}
		sceneEditTitle.value = '新增规则场景'
		sceneEditVisible.value = true
	}

	const queryScene = () => {
		pageCurrent.value = 1
		getRuleScenePage("0")
	}

	/* ruleSceneTable */

	const tableData = ref(null)

	const currentRuleSceneData = ref({})

	interface ruleScene {
		id: number
		ruleSceneName: string
		ruleSceneType: number
		ruleSceneDesc: string
		hasChildren ? : boolean
		children ? : ruleScene[]
	}

	const ruleSceneForm = ref()

	const resolveFunc = ref()
	const load = (
		row: ruleScene,
		treeNode: unknown,
		resolve: (ruleSceneCode: ruleScene[]) => void
	) => {
		const param = {
			"pid": row.id,
			"currentPage": 1,
			"pageSize": pageSize.value,
		}
		post("/api/getVeryRuleScenePage", param, (data) => {
			if (data.errorCode == 0) {
				if (data.body.records.length > 0) {
					if (typeof resolve === "function") {
						resolve(data.body.records)
					} else {
						var ruleSceneData = ruleSceneTable.value.store.states.lazyTreeNodeMap.value[row.id]
						if (ruleSceneData != undefined) {
							ruleSceneData = []
							for (var index in data.body.records) {
								ruleSceneData.push(data.body.records[index])
							}
							ruleSceneTable.value.store.states.lazyTreeNodeMap.value[row.id] = ruleSceneData
						}
					}
				}
			}
		});
	}


	const getRuleScenePage = (pid) => {
		const param = {
			"pid": pid,
			"currentPage": pageCurrent.value,
			"pageSize": pageSize.value,
			"ruleSceneNameL": ruleSceneForm.value["model"].ruleSceneName,
		}
		post("/api/getVeryRuleScenePage", param, (data) => {
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

	const handleEdit = (index: number, row: ruleScene) => {
		sceneEditTitle.value = row.ruleSceneName
		sceneEditVisible.value = true
		currentRuleSceneData.value = row
	}

	const handleAdd = (index: number, row: ruleScene) => {
		ruleSceneTableRow.value = row
		currentRuleSceneData.value = {
			"id": 0,
			"ruleSceneType": "2",
			"ruleSceneName": "",
			"pid": row.id,
			"ruleSceneDesc": "",
		}
		sceneEditTitle.value = row.ruleSceneName + '-新增规则操作'
		sceneEditVisible.value = true
	}
	const handleDelete = (index: number, row: ruleScene) => {
		const param = {
			"id": row.id
		}

		post("/api/delVeryRuleScene", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
					message: '保存成功',
					type: 'success',
				})
				var isDel = false
				for (var index in ruleSceneTable.value.store.states.lazyTreeNodeMap.value) {
					const item = ruleSceneTable.value.store.states.lazyTreeNodeMap.value[index]
					for (var m in item) {
						if (item[m].id == row.id) {
							item.splice(m, 1)
							isDel = true
						}
					}
				}
				getRuleScenePage("0")
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}
	/* ruleScenePage */
	const pageCurrent = ref(1)
	const pageSize = ref(10)
	const pageTotal = ref(0)
	const small = ref(false)
	const background = ref(false)
	const handleSizeChange = (val: number) => {
		getRuleScenePage("0")
	}
	const handleCurrentChange = (val: number) => {
		getRuleScenePage("0")
	}
	/* ruleSceneEdit */
	const sceneEditVisible = ref(false)
	const sceneEditTitle = ref('')

	const cancelSceneEdit = () => {
		sceneEditVisible.value = false
	}
	const successSceneUpdate = () => {
		sceneEditVisible.value = false
		getRuleScenePage("0")
	}
	const ruleSceneTable = ref()
	const ruleSceneTableRow = ref()
	const successSceneAdd = () => {
		sceneEditVisible.value = false
		if (ruleSceneTableRow.value != undefined) {
			ruleSceneTableRow.value.hasChildren = true
			ruleSceneTable.value.store.loadOrToggle(ruleSceneTableRow.value)
		} else {
			getRuleScenePage("0")
		}
		load(ruleSceneTableRow.value, null, null)
	}
</script>