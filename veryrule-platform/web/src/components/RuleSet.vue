<template>
	<el-row>
		<el-col :span="3">
			<el-tabs :tab-position="tabPosition" style="height: 90vh;" type="border-card" @tab-click="tabClick">
				<el-tab-pane label="执行条件(Condation)">
				</el-tab-pane>
				<el-tab-pane label="执行动作(Action)">
				</el-tab-pane>
			</el-tabs>
		</el-col>
		<el-col :span="21">
			<el-row>
				<el-form ref="ruleActionFormRef" :inline="true" :model="rule" class="demo-form-inline">
					<el-form-item label="规则" prop="ruleCode">
						<el-input v-model="rule.ruleCode" placeholder="请输入规则标识"></el-input>
					</el-form-item>
					<el-form-item label="规则名" prop="ruleName">
						<el-input v-model="rule.ruleName" placeholder="请输入规则名"></el-input>
					</el-form-item>
					<el-form-item label="组名" prop="groupName">
						<el-input v-model="rule.groupName" placeholder="请输入组名"></el-input>
					</el-form-item>
					<el-form-item label="规则类型" prop="ruleType" v-if="ruleType == 12 || ruleType == 1 || ruleType == 2">
						<el-select v-model="rule.ruleType" clearable placeholder="请选择类型" @change="handleConditionChange" style="width: 240px">
							<el-option  key="1" label="无返回值" value="1" />
							<el-option  key="2" label="有返回值" value="2" />
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="searchRule">查询</el-button>
						<el-button type="primary" @click="addRule">新增</el-button>
					</el-form-item>
				</el-form>
			</el-row>
			<i style="font-size:10px;color:#aaaaaa;">
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
			<el-table :data="ruleTableData" border style="width: 100%">
				<el-table-column label="规则" align="center">
					<template #default="scope">
						<div style="text-align: center">
							{{ scope.row.ruleName }}
							<el-icon v-if="scope.row.ruleType == 1">
							    <Bell />
							  </el-icon>
							  <el-icon v-if="scope.row.ruleType == 2">
							      <BellFilled />
							    </el-icon>
								<el-icon v-if="scope.row.ruleType == 3">
								    <Promotion />
								  </el-icon>
						</div>
						<div style="text-align: center" v-if="scope.row.groupName != ''">
							({{ scope.row.ruleCode }}∈{{ scope.row.groupName }})
						</div>
						<div style="text-align: center" v-else>
							({{ scope.row.ruleCode }})
						</div>
					</template>
				</el-table-column>
				<el-table-column label="指定对象" prop="ruleKey"></el-table-column>
				<el-table-column label="默认值" prop="ruleValue"></el-table-column>
				<el-table-column label="表达式" prop="ruleExpr"></el-table-column>
				<el-table-column label="描述">
					<template #default="scope">
						<el-popover effect="light" trigger="hover" placement="top" width="auto">
							<template #default>
								<div>{{ scope.row.ruleDesc }}</div>
							</template>
							<template #reference>
								<el-tag>详</el-tag>
							</template>
						</el-popover>
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template #default="scope">
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
						<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row)">
							<template #reference>
								<el-button size="small" type="danger">删除</el-button>
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
		</el-col>
	</el-row>

	<el-dialog v-model="ruleEditVisible" :title="ruleEditTitle" destroy-on-close width="30%">
		<RuleEdit :ruleGroupData="ruleGroup" :ruleData="currentRuleData" @cancelRuleEdit="cancelRuleEdit" @successRuleUpdate="successRuleUpdate"
			@successRuleAdd="successRuleAdd"></RuleEdit>
	</el-dialog>
</template>

<script lang="ts" setup>
	import RuleEdit from "./rule/RuleEdit.vue"
	import {
		Bell,
		BellFilled,
		Promotion,
		Key
	} from '@element-plus/icons-vue'
	import {
		ElMessage
	} from 'element-plus'
	import post from "../axios/post.js";
	import {
		ref,
		onMounted
	} from 'vue'

	onMounted(() => {
		getVeryRuleElementList()
	})

	/* tab */
	const tabPosition = ref('left')
	const ruleType = ref(12)
	const tabClick = (target: string) => {
		if (target.index == 0) {
			ruleType.value = 12
		} else {
			ruleType.value = 3
		}
		ruleTableData.value = []
		pageCurrent.value = 1
		getVeryRuleElementList()
	}
	/* from */
	const rule = ref({
		ruleCode: "",
		ruleName: "",
		groupName: "",
	})

	const ruleActionFormRef = ref()

	const searchRule = () => {
		pageCurrent.value = 1
		getVeryRuleElementList()
	}

	const addRule = () => {
		if(ruleType==3){
			ruleEditTitle.value = "新增执行动作"
		}else{
			ruleEditTitle.value = "新增执行条件"
		}
		ruleEditVisible.value = true
		currentRuleData.value = {
			id: 0,
			"ruleCode": "",
			"ruleName": "",
			"ruleValue": "",
			"ruleDesc": "",
			"ruleType": ruleType.value
		}
		if(ruleType.value==12){
			currentRuleData.value.ruleType="1"
		}
		getVeryRuleElementGroup();
	}
	/* table */
	interface Rule {
		ruleCode: string
		ruleName: string
		ruleValue: string
		ruleDesc: string,
		ruleType:number
	}
	const ruleTableData: Rule[] = ref([])
	const currentRuleData = ref({})
	const handleConditionChange = (value)=>{
		if(value == ''){
			ruleType.value = 12
		}else{
			ruleType.value = value
		}
	}
	const getVeryRuleElementList = () => {
		const param = {
			"ruleCode": ruleActionFormRef.value["model"].ruleCode,
			"ruleName": ruleActionFormRef.value["model"].ruleName,
			"groupName": ruleActionFormRef.value["model"].groupName,
			"ruleType": ruleType.value,
			"currentPage": pageCurrent.value,
			"pageSize": pageSize.value,
			"ruleType": ruleType.value
		}
		post("/api/getVeryRuleElementList", param, (data) => {
			if (data.errorCode == 0) {
				pageCurrent.value = data.body.current
				pageSize.value = data.body.size
				pageTotal.value = data.body.total
				ruleTableData.value = data.body.records
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
	const handleEdit = (index: number, row: User) => {
		row.ruleType = String(row.ruleType)
		currentRuleData.value = row
		ruleEditTitle.value = "编辑规则"
		ruleEditVisible.value = true
		getVeryRuleElementGroup()
	}
	const handleDelete = (index: number, row: User) => {
		const param = {
			"id": row.id,
		}
		post("/api/delVeryRuleElement", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
					message: '保存成功',
					type: 'success',
				})
				getVeryRuleElementList()
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
	/* page */
	const pageCurrent = ref(1)
	const pageSize = ref(10)
	const pageTotal = ref(0)
	const small = ref(false)
	const background = ref(false)
	const handleSizeChange = (val: number) => {
		getVeryRuleElementList()
	}
	const handleCurrentChange = (val: number) => {
		getVeryRuleElementList()
	}
	/* RuleEdit */
	const ruleEditVisible = ref(false)
	const ruleEditTitle = ref("")
	const cancelRuleEdit = () => {
		ruleEditVisible.value = false
	}
	const successRuleUpdate = () => {
		ruleEditVisible.value = false
		getVeryRuleElementList()
	}
	const successRuleAdd = () => {
		ruleEditVisible.value = false
		pageCurrent.value = 1
		getVeryRuleElementList()
	}
	const ruleGroup = ref([])
	const getVeryRuleElementGroup = () => {
		const param = {
		}
		post("/api/getVeryRuleElementGroup", param, (data) => {
			if (data.errorCode == 0) {
				ruleGroup.value = data.body
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
</script>

