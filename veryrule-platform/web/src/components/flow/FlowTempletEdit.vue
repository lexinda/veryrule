<template>
	<el-row class="tac">
		<el-col :span="3">
			<el-menu active-text-color="#ffd04b" background-color="#545c64" class="el-menu-vertical-demo"
				:default-active="defaultActive" text-color="#fff" style="height:85vh;" @select="menuSelect">
				<template v-for="item in menuData">
					<el-menu-item v-if="!item.children" :index="item.ruleCode">
						<span>{{item.ruleName}}</span>
					</el-menu-item>
					<SubMenu v-else :menuInfo="item" />
				</template>
			</el-menu>
		</el-col>
		<el-col :span="17">
			<el-table :data="tableData" style="width: 100%;height:85vh" border @row-click="tableRowClick">
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
				<el-table-column label="操作" width="220">
					<template #default="scope">
						<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row)">
							<template #reference>
								<el-button size="small" type="danger">删除</el-button>
							</template>
						</el-popconfirm>
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
						<el-button-group style="margin-left:10px;">
							<el-button type="primary" :icon="SortUp" size="small" title="向上" v-if="scope.$index == 0"
								disabled>
							</el-button>
							<el-button type="primary" :icon="SortUp" size="small" title="向上" v-else
								@click="handleUp(scope.$index, scope.row)">
							</el-button>
							<el-button type="primary" :icon="SortDown" size="small" title="向下"
								v-if="scope.$index == tableData.length-1" disabled>
							</el-button>
							<el-button type="primary" :icon="SortDown" size="small" title="向下" v-else
								@click="handleDown(scope.$index, scope.row)">
							</el-button>
						</el-button-group>
					</template>
				</el-table-column>
			</el-table>
		</el-col>
		<el-col :span="4">
			<div style="height:85vh;">
				<el-row class="mb-4">
					<el-button type="primary" @click="handleTempletTest()">测试</el-button>
					<el-popconfirm title="是否保存?" @confirm="handleTempletAdd()">
						<template #reference>
							<el-button type="danger">保存</el-button>
						</template>
					</el-popconfirm>
				</el-row>
				<div style="height:80vh;overflow-y:scroll;margin-top: 10px;">
					<json-viewer :value="jsonData" box sort :expandDepth="9" />
				</div>
			</div>
		</el-col>
	</el-row>
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
			<el-form-item label="规则流模板" prop="ruleFlowTemplet" style="max-height: 500px;overflow-y: scroll;">
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
	import SubMenu from "../menu/SubMenu.vue";
	import TempletEdit from "../templet/TempletEdit.vue";

	import {
		SortUp,
		SortDown,
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
	
	onMounted(() => {
		getVeryRuleElementMenu(1)
	})

	const emit = defineEmits(['updateVeryRuleFlow']);

	/* menu */
	
	const menuData = ref([])
	
	const getVeryRuleElementMenu = (ruleType) => {
		const param = {
			"ruleType": ruleType,
		}
		post("/api/getVeryRuleElementMenu", param, (data) => {
			if (data.errorCode == 0) {
				menuData.value = data.body
			}
		});
	}
	
	const menuSelect = (index, path) => {
		var ruleItem = {}
		var isHave = false
		for (var menu in menuData.value) {
			if (path[0] == menuData.value[menu].ruleCode) {
				if(path.length==1){
					ruleItem = menuData.value[menu]
				}else{
					for (var menuChild in menuData.value[menu].children) {
						if (index == menuData.value[menu].children[menuChild].ruleCode) {
							ruleItem = menuData.value[menu].children[menuChild]
						}
					}
				}
				
			}
		}
		for (var rule in tableData.value) {
			if (tableData.value[rule].ruleCode == ruleItem.ruleCode) {
				isHave = true
			}
		}
		if (isHave) {
			ElMessage.error('规则' + ruleItem.ruleName + '已存在')
		} else {
			templetEditType.value = 1
			templetEditVisible.value = true
			ruleData.value = ruleItem
		}
	}
	
	/* table */
	const defaultActive = ref()
	const tableRowClick = (row, column, event)=>{
		defaultActive.value = row.ruleCode
	}
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
		children: RuleData[]
	}
	
	const currentTableDataIndex = ref(0)
	
	const templetEditVisible = ref(false)
	
	const tableData: RuleData[] = ref(props.ruleFlowTemplet.ruleFlowTemplet)
	
	const templetEditType = ref(1)
	
	const ruleData = ref()
	
	const handleEdit = (index: number, row: RuleData) => {
		templetEditType.value = 2
		ruleData.value = row
		currentTableDataIndex.value = index
		templetEditVisible.value = true
		refreshJson()
	}
	const handleDelete = (index: number, row: RuleData) => {
		tableData.value = tableData.value.filter(item => item.ruleCode != row.ruleCode)
		refreshJson()
	}
	
	const handleUp = (index: number, row: RuleData) => {
		let itemIndex = index - 1
		let itemData = tableData.value[itemIndex]
		tableData.value[itemIndex] = row
		tableData.value[index] = itemData
		refreshJson()
	}
	
	const handleDown = (index: number, row: RuleData) => {
		let itemIndex = index + 1
		let itemData = tableData.value[itemIndex]
		tableData.value[itemIndex] = row
		tableData.value[index] = itemData
		refreshJson()
	}
	
	/* json */
	
	const handleTempletAdd = () => {
		templetCommitVisible.value = true
		templetCommitData.value = {
			"ruleFlowTempletCode": props.ruleFlowTemplet.ruleFlowTempletCode,
			"ruleFlowTemplet": tableData.value
		}
	}
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
	
	const templetTestClose = ()=>{
		templetTestVisible.value = false
	}
	
	import "vue3-json-viewer/dist/index.css";
	
	const jsonData = ref(tableData.value)
	
	const refreshJson = () => {
		jsonData.value = {}
		nextTick(() => {
			jsonData.value = tableData.value
		})
	}
	/* TempletEdit */
	
	const successTempletAdd = (templet) => {
		templetEditVisible.value = false
		tableData.value.push(templet)
		refreshJson()
	}
	
	const successTempletUpdate = (templet) => {
		templetEditVisible.value = false
		for (var rule in tableData.value) {
			if (templet.ruleCode == tableData.value[rule].ruleCode) {
				tableData.value[rule].ruleName = templet.ruleName
				tableData.value[rule].ruleValue = templet.ruleValue
				tableData.value[rule].ruleKey = templet.ruleKey
				tableData.value[rule].ruleCondation = templet.ruleCondation
				tableData.value[rule].ruleCondations = templet.ruleCondations
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
					"ruleFlowTemplet": tableData.value
				}
				post("/api/updateVeryRuleFlowAndTemplet", param, (data) => {
					if (data.errorCode == 0) {
						ElMessage({
							message: '保存成功',
							type: 'success',
						})
						templetCommitVisible.value = false
						emit('updateVeryRuleFlow', ruleFlowTempletCode)
					}else{
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
