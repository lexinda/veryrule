<template>
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
	<div class="common-layout">
		<el-container>
			<el-aside width="200px">
				<ElementMenu :menus="menuData" :actives="activesNodes" @menuselect="menuClick"></ElementMenu>
			</el-aside>
			<el-main>
				<el-row class="tac">
					<el-col :span="19">
						<div style="height:85vh;overflow-y:scroll;">
							<el-table :data="tableCondationData" style="width: 100%;" border @row-click="tableRowClick">
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
										<div v-if="scope.row.ruleAsyn == 1">{{ scope.row.ruleName }}(<font color="red">
												异步</font>)</div>
										<div v-else>{{ scope.row.ruleName }}</div>
									</template>
								</el-table-column>
								<el-table-column label="指定入参key" prop="ruleKey">
								</el-table-column>
								<el-table-column label="默认值" prop="ruleValue">
								</el-table-column>
								<el-table-column label="异常提示" prop="ruleErrMsg">
								</el-table-column>
								<el-table-column label="操作" width="220">
									<template #default="scope">
										<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row)">
											<template #reference>
												<el-button size="small" type="danger">删除</el-button>
											</template>
										</el-popconfirm>
										<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑
										</el-button>
										<el-button-group style="margin-left:10px;">
											<el-button type="primary" :icon="SortUp" size="small" title="向上"
												v-if="scope.$index == 0" disabled>
											</el-button>
											<el-button type="primary" :icon="SortUp" size="small" title="向上" v-else
												@click="handleUp(scope.$index, scope.row)">
											</el-button>
											<el-button type="primary" :icon="SortDown" size="small" title="向下"
												v-if="scope.$index == tableCondationData.length-1" disabled>
											</el-button>
											<el-button type="primary" :icon="SortDown" size="small" title="向下" v-else
												@click="handleDown(scope.$index, scope.row)">
											</el-button>
										</el-button-group>
									</template>
								</el-table-column>
							</el-table>
							<el-table :data="tableActionData" style="width: 100%;" border @row-click="tableRowClick"
								v-if="tableActionData.length>0">
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
								<el-table-column label="异常提示" prop="ruleErrMsg">
								</el-table-column>
								<el-table-column label="操作" width="220">
									<template #default="scope">
										<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row)">
											<template #reference>
												<el-button size="small" type="danger">删除</el-button>
											</template>
										</el-popconfirm>
										<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑
										</el-button>
										<el-button-group style="margin-left:10px;">
											<el-button type="primary" :icon="SortUp" size="small" title="向上"
												v-if="scope.$index == 0" disabled>
											</el-button>
											<el-button type="primary" :icon="SortUp" size="small" title="向上" v-else
												@click="handleUp(scope.$index, scope.row)">
											</el-button>
											<el-button type="primary" :icon="SortDown" size="small" title="向下"
												v-if="scope.$index == tableActionData.length-1" disabled>
											</el-button>
											<el-button type="primary" :icon="SortDown" size="small" title="向下" v-else
												@click="handleDown(scope.$index, scope.row)">
											</el-button>
										</el-button-group>
									</template>
								</el-table-column>
							</el-table>
						</div>
					</el-col>
					<el-col :span="5">
						<div style="height:85vh;">
							<el-row class="mb-4">
								<el-button style="margin-left:5px;" type="primary" @click="handleTempletTest()">测试
								</el-button>
								<el-popconfirm title="是否保存?" @confirm="handleTempletAdd()">
									<template #reference>
										<el-button type="danger">保存</el-button>
									</template>
								</el-popconfirm>
							</el-row>
							<div style="height:80vh;overflow-y:scroll;margin-top: 10px;text-align: left;">
								<json-viewer :value="jsonData" box sort :expandDepth="9" />
							</div>
						</div>
					</el-col>
				</el-row>
			</el-main>
		</el-container>
	</div>
	<el-dialog v-model="templetEditVisible" :title="templetEditTitle" destroy-on-close width="30%">
		<TempletEdit :templetEditType="templetEditType" @cancelTempletEdit="cancelTempletEdit"
			@successTempletUpdate="successTempletUpdate" @successTempletAdd="successTempletAdd" :ruleData="ruleData">
		</TempletEdit>
	</el-dialog>
	<el-dialog v-model="templetCommitVisible" destroy-on-close width="500px">
		<el-form ref="templetCommitRef" :model="templetCommitData" label-width="140px" :rules="flowTempletEditRules">
			<el-form-item label="规则流模板标识" prop="ruleFlowTempletCode" required>
				<el-input v-model="templetCommitData.ruleFlowTempletCode"></el-input>
			</el-form-item>
			<el-form-item label="规则流模板" prop="ruleFlowTemplet" style="max-height: 500px;overflow-y: scroll;text-align: left;">
				<json-viewer :value="templetCommitData.ruleFlowTemplet" box sort :expandDepth="9" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="handleTempletCommit">保存</el-button>
				<el-button @click="handleTempletCancel">取消</el-button>
			</el-form-item>
		</el-form>
	</el-dialog>
	<el-dialog v-model="templetTestVisible" destroy-on-close width="500px" @close="templetTestClose">
		<div style="max-height: 500px;overflow-y: scroll;">
			<div v-for="(item,index) in templetTestResult"
				style="width: 460px;border-bottom: 1px dotted #aaaaaa;height: 40px;padding:5px 0px;">
				<div style=" text-align: center;width: 200px;float: left;">
					<span>{{item.ruleCode}}</span><br>
					<span>{{item.ruleName}}</span>
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
	import ElementMenu from "../menu/ElementMenu.vue";
	import TempletEdit from "../templet/TempletEdit.vue";

	import {
		SortUp,
		SortDown,
		Bell,
		BellFilled,
		Promotion,
	} from '@element-plus/icons-vue'

	import {
		ElMessage
	} from 'element-plus'

	import {
		ref,
		onMounted,
		nextTick
	} from 'vue'

	import post from "../../axios/post.js";

	const props = defineProps < {
		ruleFlowTemplet: Object
	} > ()

	const tableCondationData: RuleData[] = ref([])

	const tableActionData: RuleData[] = ref([])

	onMounted(() => {
		getVeryRuleElementMenu(1)
		if (props.ruleFlowTemplet.ruleFlowTemplet.length > 0) {
			tableCondationData.value = props.ruleFlowTemplet.ruleFlowTemplet.filter(item => item.ruleType == 1 ||
				item.ruleType == 2)

			tableActionData.value = props.ruleFlowTemplet.ruleFlowTemplet.filter(item => item.ruleType == 3)
		}

	})

	const emit = defineEmits(['updateVeryRuleFlow']);

	/* menu */

	const menuData = ref([])
	const activesNodes = ref([])
	const getVeryRuleElementMenu = (ruleType) => {
		const param = {
			"ruleMenu": ruleType,
		}
		post("/api/getVeryRuleElementMenu", param, (data) => {
			if (data.errorCode == 0) {
				menuData.value = data.body.ruleMenu
				activesNodes.value = []
				let ruleFlowTemplet = props.ruleFlowTemplet.ruleFlowTemplet
				console.log(ruleFlowTemplet)
				var ruleCodeArr = []
				for (var index in ruleFlowTemplet) {
					let templetData = ruleFlowTemplet[index]
					ruleCodeArr.push(templetData.ruleCode)
				}
				addGroupActivesNodes(ruleCodeArr)
			}
		});
	}
	const addGroupActivesNodes = (nodes: Array) => {
		for (var g in menuData.value) {
			let ruleGroup = menuData.value[g]
			var have = 0
			if (nodes.filter(n => n == ruleGroup.ruleCode).length > 0) {
				activesNodes.value.push(ruleGroup.path)
				continue
			} else {
				if (ruleGroup.children != null && ruleGroup.children.length > 0) {
					for (var r in ruleGroup.children) {
						let ruleItem = ruleGroup.children[r]
						if (nodes.filter(n => n == ruleItem.ruleCode).length > 0) {
							activesNodes.value.push(ruleItem.path)
							have = 1
						}
					}
				}
			}
			if (have == 1) {
				activesNodes.value.push(ruleGroup.path)
			}
		}
	}
	const menuClick = (index, path) => {
		if (!index.startsWith('g-')) {
			var ruleItem = {}
			var isHave = false
			for (var menu in menuData.value) {
				if (path[0] == menuData.value[menu].ruleCode) {
					if (path.length == 1) {
						ruleItem = menuData.value[menu]
					} else {
						for (var menuChild in menuData.value[menu].children) {
							if (index == menuData.value[menu].children[menuChild].ruleCode) {
								ruleItem = menuData.value[menu].children[menuChild]
							}
						}
					}

				}
			}
			if (ruleItem.ruleType == 3) {
				for (var rule in tableActionData.value) {
					if (tableActionData.value[rule].ruleCode == ruleItem.ruleCode) {
						isHave = true
					}
				}
			} else if (ruleItem.ruleType == 1 || ruleItem.ruleType == 2) {
				for (var rule in tableCondationData.value) {
					if (tableCondationData.value[rule].ruleCode == ruleItem.ruleCode) {
						isHave = true
					}
				}
			}
			if (isHave) {
				ElMessage.error('规则' + ruleItem.ruleName + '已存在')
			} else {
				templetEditType.value = 1
				templetEditVisible.value = true
				if (ruleItem.ruleType == 2 && ruleItem.ruleAsyn == undefined) {
					ruleItem.ruleAsyn = String("1");
				} else {
					ruleItem.ruleAsyn = String("2");
				}
				ruleData.value = ruleItem
			}
		}
	}

	/* table */
	const defaultActive = ref()
	const tableRowClick = (row, column, event) => {
		defaultActive.value = row.ruleCode
	}
	interface RuleData {
		id: number
		ruleCode: string
		ruleName: string
		ruleValue: string
		ruleKey: string
		ruleType: number,
			ruleAsyn: number,
			ruleErrMsg: string
		ruleDesc: string
		children: RuleData[]
	}

	const currentTableDataIndex = ref(0)

	const templetEditVisible = ref(false)



	const templetEditType = ref(1)

	const ruleData = ref()

	const handleEdit = (index: number, row: RuleData) => {
		templetEditType.value = 2
		if (row.ruleAsyn == undefined) {
			row.ruleAsyn = String("1");
		}
		ruleData.value = row
		currentTableDataIndex.value = index
		templetEditVisible.value = true
		refreshJson()
	}
	const handleDelete = (index: number, row: RuleData) => {
		if (row.ruleType == 3) {
			tableActionData.value = tableActionData.value.filter(item => item.ruleCode != row.ruleCode)
		} else if (row.ruleType == 1 || row.ruleType == 2) {
			tableCondationData.value = tableCondationData.value.filter(item => item.ruleCode != row.ruleCode)
		}
		refreshJson()
	}

	const handleUp = (index: number, row: RuleData) => {
		let itemIndex = index - 1
		if (row.ruleType == 3) {
			let itemData = tableActionData.value[itemIndex]
			tableActionData.value[itemIndex] = row
			tableActionData.value[index] = itemData
		} else if (row.ruleType == 2 || row.ruleType == 1) {
			let itemData = tableCondationData.value[itemIndex]
			tableCondationData.value[itemIndex] = row
			tableCondationData.value[index] = itemData
		}
		refreshJson()
	}

	const handleDown = (index: number, row: RuleData) => {
		let itemIndex = index + 1
		if (row.ruleType == 3) {
			let itemData = tableActionData.value[itemIndex]
			tableActionData.value[itemIndex] = row
			tableActionData.value[index] = itemData
		} else if (row.ruleType == 2 || row.ruleType == 1) {
			let itemData = tableCondationData.value[itemIndex]
			tableCondationData.value[itemIndex] = row
			tableCondationData.value[index] = itemData
		}
		refreshJson()
	}

	/* json */

	const handleTempletAdd = () => {
		templetCommitVisible.value = true
		templetCommitData.value = {
			"ruleFlowTempletCode": props.ruleFlowTemplet.ruleFlowTempletCode,
			"ruleFlowTemplet": jsonData.value
		}
	}
	const templetTestVisible = ref(false)
	const templetTestResult = ref([])
	const handleTempletTest = () => {
		const param = {
			"ruleFlowTemplet": jsonData.value
		}
		post("/api/testVeryRuleFlow", param, (data) => {
			templetTestResult.value = []
			if (data.errorCode == 0) {
				templetTestVisible.value = true
				const result = data.body;
				for (var ruleCode in result) {
					const ruleName = result[ruleCode]
					var resultData = {
						"ruleCode": ruleCode,
						"ruleName": ruleName,
						"success": true
					}
					if (ruleName == '') {
						resultData.success = false
					}

					templetTestResult.value.push(resultData)
				}
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}

	const templetTestClose = () => {
		templetTestVisible.value = false
	}

	import "vue3-json-viewer/dist/index.css";

	const jsonData = ref(props.ruleFlowTemplet.ruleFlowTemplet)

	const refreshJson = () => {
		jsonData.value = {}
		nextTick(() => {
			var tableData = [];
			for (var rc = 0; rc < tableCondationData.value.length; rc++) {
				tableData.push(tableCondationData.value[rc])
			}

			for (var ra = 0; ra < tableActionData.value.length; ra++) {
				tableData.push(tableActionData.value[ra])
			}
			jsonData.value = tableData
			activesNodes.value = []
			let ruleFlowTemplet = tableData
			var ruleCodeArr = []
			for(var key in ruleFlowTemplet){
				let templetData = ruleFlowTemplet[key]
				ruleCodeArr.push(templetData.ruleCode)
			}
			addGroupActivesNodes(ruleCodeArr)
		})
	}
	/* TempletEdit */

	const successTempletAdd = (templet) => {
		templetEditVisible.value = false
		if (templet.ruleType == 3) {
			tableActionData.value.push(templet)
		} else if (templet.ruleType == 1) {
			tableCondationData.value.splice(0,0,templet)
		}else if (templet.ruleType == 2) {
			tableCondationData.value.push(templet)
		}
		refreshJson()
	}

	const successTempletUpdate = (templet) => {
		templetEditVisible.value = false
		var tableData = []
		if (templet.ruleType == 3) {
			tableData = tableActionData.value
		} else if (templet.ruleType == 1 || templet.ruleType == 2) {
			tableData = tableCondationData.value
		}
		for (var rule in tableData.value) {
			if (templet.ruleCode == tableData.value[rule].ruleCode) {
				tableData.value[rule].ruleName = templet.ruleName
				tableData.value[rule].ruleValue = templet.ruleValue
				tableData.value[rule].ruleKey = templet.ruleKey
				tableData.value[rule].ruleType = templet.ruleType
				tableData.value[rule].ruleErrMsg = templet.ruleErrMsg
			}
		}
		refreshJson()
	}

	const cancelTempletEdit = () => {
		templetEditVisible.value = false
		refreshJson()
	}

	/* FlowTempletCommit */
	const templetCommitVisible = ref(false)
	const templetCommitData = ref()
	const templetCommitRef = ref()
	const handleTempletCommit = (res: any) => {
		templetCommitRef.value.validate((valid: any, values) => {
			if (valid) {
				const ruleFlowTempletCode = templetCommitRef.value["model"].ruleFlowTempletCode
				const param = {
					"id": props.ruleFlowTemplet.ruleFlowId,
					"ruleFlowTempletCode": ruleFlowTempletCode,
					"ruleFlowTemplet": jsonData.value
				}
				post("/api/updateVeryRuleFlowAndTemplet", param, (data) => {
					if (data.errorCode == 0) {
						ElMessage({
							message: '保存成功',
							type: 'success',
						})
						templetCommitVisible.value = false
						emit('updateVeryRuleFlow', ruleFlowTempletCode)
					} else {
						ElMessage.error(data.errorDesc)
					}
				});
			} else {
				console.log('error submit!!')
				return false
			}
		})
	}

	const handleTempletCancel = () => {
		templetCommitVisible.value = false
	}

	const flowTempletEditRules = ref({
		ruleFlowTempletCode: [{
			required: true,
			message: '规则流模板标识不能为空',
			trigger: 'blur',
		}],
	})
</script>
