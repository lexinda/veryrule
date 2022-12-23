<template>
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
	<el-row class="tac">
		<el-col :span="3">
			<el-menu :collapse="true" active-text-color="#ffd04b" background-color="#545c64" class="el-menu-vertical-demo"
				:default-active="defaultActive" text-color="#fff" style="height:85vh;" @select="menuSelect">
				<template v-for="item in menuData">
					<el-menu-item v-if="!item.children" :index="item.ruleCode">
						<i>{{item.ruleName}}</i>
						<span>{{item.ruleName}}</span>
						<el-icon v-if="item.ruleType == 1">
						    <Bell />
						  </el-icon>
						  <el-icon v-if="item.ruleType == 2">
						      <BellFilled />
						    </el-icon>
							<el-icon v-if="item.ruleType == 3">
							    <Promotion />
							  </el-icon>
					</el-menu-item>
					<SubMenu v-else :menuInfo="item" />
				</template>
			</el-menu>
		</el-col>
		<el-col :span="16">
			<div style="height:85vh;overflow-y:scroll;">
				<div v-for="(scene,index) in ruleScene">
					<fieldset style="margin-top: 10px;min-width: 90%;border: 1px solid #dcdfe6;" v-if="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].length>0">
						<legend style="font-size: 16px;">{{scene.ruleSceneName}}({{scene.ruleSceneCode}})</legend>
						<el-table :data="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 1)" v-if="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 1).length>0" style="width: 100%;" border @row-click="tableRowClick">
							<el-table-column label="(无返回值)条件规则名称" align="center">
								<template #default="scope">
									<div>{{ scope.row.ruleCode }}
									<el-icon>
									    <Bell />
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
									<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row,scene.ruleSceneCode)">
										<template #reference>
											<el-button size="small" type="danger">删除</el-button>
										</template>
									</el-popconfirm>
									<el-button size="small" @click="handleEdit(scope.$index, scope.row,scene.ruleSceneCode)">编辑</el-button>
									<el-button-group style="margin-left:10px;">
										<el-button type="primary" :icon="SortUp" size="small" title="向上" v-if="scope.$index == 0"
											disabled>
										</el-button>
										<el-button type="primary" :icon="SortUp" size="small" title="向上" v-else
											@click="handleUp(scope.$index, scope.row,scene.ruleSceneCode)">
										</el-button>
										<el-button type="primary" :icon="SortDown" size="small" title="向下"
											v-if="scope.$index == ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 1).length-1" disabled>
										</el-button>
										<el-button type="primary" :icon="SortDown" size="small" title="向下" v-else
											@click="handleDown(scope.$index, scope.row,scene.ruleSceneCode)">
										</el-button>
									</el-button-group>
								</template>
							</el-table-column>
						</el-table>
						<el-table :data="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 2)" v-if="ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 2).length>0" style="width: 100%;" border @row-click="tableRowClick">
							<el-table-column label="(有返回值)条件规则名称" align="center">
								<template #default="scope">
									<div>{{ scope.row.ruleCode }}
									  <el-icon>
									      <BellFilled />
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
									<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row,scene.ruleSceneCode)">
										<template #reference>
											<el-button size="small" type="danger">删除</el-button>
										</template>
									</el-popconfirm>
									<el-button size="small" @click="handleEdit(scope.$index, scope.row,scene.ruleSceneCode)">编辑</el-button>
									<el-button-group style="margin-left:10px;">
										<el-button type="primary" :icon="SortUp" size="small" title="向上" v-if="scope.$index == 0"
											disabled>
										</el-button>
										<el-button type="primary" :icon="SortUp" size="small" title="向上" v-else
											@click="handleUp(scope.$index, scope.row,scene.ruleSceneCode)">
										</el-button>
										<el-button type="primary" :icon="SortDown" size="small" title="向下"
											v-if="scope.$index == ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 2).length-1" disabled>
										</el-button>
										<el-button type="primary" :icon="SortDown" size="small" title="向下" v-else
											@click="handleDown(scope.$index, scope.row,scene.ruleSceneCode)">
										</el-button>
									</el-button-group>
								</template>
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
							<el-table-column label="异常提示" prop="ruleErrMsg">
							</el-table-column>
							<el-table-column label="操作" width="220">
								<template #default="scope">
									<el-popconfirm title="是否删除?" @confirm="handleDelete(scope.$index, scope.row,scene.ruleSceneCode)">
										<template #reference>
											<el-button size="small" type="danger">删除</el-button>
										</template>
									</el-popconfirm>
									<el-button size="small" @click="handleEdit(scope.$index, scope.row,scene.ruleSceneCode)">编辑</el-button>
									<el-button-group style="margin-left:10px;">
										<el-button type="primary" :icon="SortUp" size="small" title="向上" v-if="scope.$index == 0"
											disabled>
										</el-button>
										<el-button type="primary" :icon="SortUp" size="small" title="向上" v-else
											@click="handleUp(scope.$index, scope.row,scene.ruleSceneCode)">
										</el-button>
										<el-button type="primary" :icon="SortDown" size="small" title="向下"
											v-if="scope.$index == ruleFlowTempletData.ruleFlowTemplet[scene.ruleSceneCode].filter(item => item.ruleType == 3).length-1" disabled>
										</el-button>
										<el-button type="primary" :icon="SortDown" size="small" title="向下" v-else
											@click="handleDown(scope.$index, scope.row,scene.ruleSceneCode)">
										</el-button>
									</el-button-group>
								</template>
							</el-table-column>
						</el-table>
					</fieldset>
				</div>
			</div>
		</el-col>
		<el-col :span="5">
			<div style="height:85vh;">
				<el-row class="mb-4">
					<el-button style="margin-left:5px;" type="primary" @click="handleTempletTest()">测试</el-button>
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
	import SubMenu from "../menu/SubMenu.vue";
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
		ruleFlowTempletData: Object
	} > ()
	
	onMounted(() => {
		getVeryRuleElementMenu(1)
	})

	const emit = defineEmits(['updateVeryRuleFlow']);

	/* menu */
	
	const menuData = ref([])
	const haveScene = ref(0)
	const ruleScene = ref([])
	const getVeryRuleElementMenu = (ruleType) => {
		const param = {
			"ruleMenu": ruleType,
			"ruleSceneId": props.ruleFlowTempletData.ruleSceneId,
		}
		post("/api/getVeryRuleElementMenu", param, (data) => {
			if (data.errorCode == 0) {
				menuData.value = data.body.ruleMenu
				haveScene.value = data.body.haveScene
				if(haveScene.value == 1){
					ruleScene.value = data.body.ruleScene
				}
			}
		});
	}
	const currentSceneCode = ref("")
	const menuSelect = (index, path) => {
		var ruleItem = {}
		var isHave = false
		for (var menu in menuData.value) {
			if (path[0] == menuData.value[menu].ruleCode) {
				if(path.length==1){//规则
					ruleItem = menuData.value[menu]
				}if(path.length==2){//分组-》规则
					for (var menuChild in menuData.value[menu].children) {
						if (index == menuData.value[menu].children[menuChild].ruleCode) {
							ruleItem = menuData.value[menu].children[menuChild]
							break
						}
					}
				}if(path.length==3){//场景-》分组-》规则
					for (var menuChilds in menuData.value[menu].children) {
						for (var menuChild in menuData.value[menu].children[menuChilds].children) {
							if (index == menuData.value[menu].children[menuChilds].children[menuChild].ruleCode) {
								ruleItem = menuData.value[menu].children[menuChilds].children[menuChild]
								break
							}
						}
					}
				}
				
			}
		}
		if(props.ruleFlowTempletData.ruleFlowTemplet[path[0]].filter(item => item.ruleCode == ruleItem.ruleCode).length>0){
			isHave = true
		}
		if (isHave) {
			ElMessage.error('规则' + ruleItem.ruleName + '已存在')
		} else {
			templetEditType.value = 1
			templetEditVisible.value = true
			ruleData.value = ruleItem
			currentSceneCode.value = path[0]
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
		ruleType: number
		ruleErrMsg: string
		ruleDesc: string
		children: RuleData[]
	}
	
	const currentTableDataIndex = ref(0)
	
	const templetEditVisible = ref(false)
	
	const tableCondationData: RuleData[] = ref([])
	
	const tableResultCondationData: RuleData[] = ref([])
	
	const tableActionData: RuleData[] = ref([])
	
	const templetEditType = ref(1)
	
	const ruleData = ref()
	
	const handleEdit = (index: number, row: RuleData,sceneCode:String) => {
		templetEditType.value = 2
		ruleData.value = row
		currentTableDataIndex.value = index
		templetEditVisible.value = true
		currentSceneCode.value = sceneCode
		refreshJson()
	}
	const handleDelete = (index: number, row: RuleData,sceneCode:String) => {
		let allTableData = props.ruleFlowTempletData.ruleFlowTemplet[sceneCode]
		props.ruleFlowTempletData.ruleFlowTemplet[sceneCode] = allTableData.filter(item => item.ruleCode != row.ruleCode)
		refreshJson()
	}
	
	const handleUp = (index: number, row: RuleData,sceneCode: String) => {
		let itemIndex = index - 1
		let allTableData = props.ruleFlowTempletData.ruleFlowTemplet[sceneCode]
		let tableData = allTableData.filter(item => item.ruleType == row.ruleType )
		let itemData = tableData[itemIndex]
		var oldIndex = 0
		var newIndex = 0
		allTableData.forEach((item,index)=>{
			if(item.ruleCode == itemData.ruleCode){
				newIndex = index
			}
			if(item.ruleCode == row.ruleCode){
				oldIndex = index
			}
		})
		allTableData[oldIndex] = itemData
		allTableData[newIndex] = row
		refreshJson()
	}
	
	const handleDown = (index: number, row: RuleData,sceneCode: String) => {
		let itemIndex = index + 1
		let allTableData = props.ruleFlowTempletData.ruleFlowTemplet[sceneCode]
		let tableData = allTableData.filter(item => item.ruleType == row.ruleType )
		let itemData = tableData[itemIndex]
		var oldIndex = 0
		var newIndex = 0
		allTableData.forEach((item,index)=>{
			if(item.ruleCode == itemData.ruleCode){
				newIndex = index
			}
			if(item.ruleCode == row.ruleCode){
				oldIndex = index
			}
		})
		allTableData[oldIndex] = itemData
		allTableData[newIndex] = row
		refreshJson()
	}
	
	/* json */
	
	const handleTempletAdd = () => {
		templetCommitVisible.value = true
		templetCommitData.value = {
			"ruleFlowTempletCode": props.ruleFlowTempletData.ruleFlowTempletCode,
			"ruleFlowTemplet": jsonData.value
		}
	}
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
	
	const templetTestClose = ()=>{
		templetTestVisible.value = false
	}
	
	import "vue3-json-viewer/dist/index.css";
	
	const jsonData = ref(props.ruleFlowTempletData.ruleFlowTemplet)
	
	const refreshJson = () => {
		jsonData.value = {}
		nextTick(() => {
			jsonData.value = props.ruleFlowTempletData.ruleFlowTemplet
		})
	}
	/* TempletEdit */
	
	const successTempletAdd = (templet) => {
		templetEditVisible.value = false
		var pushIndex = 0
		props.ruleFlowTempletData.ruleFlowTemplet[currentSceneCode.value].forEach((item,index)=>{
			if(item.ruleType == templet.ruleType){
				pushIndex = index
			}
		})
		props.ruleFlowTempletData.ruleFlowTemplet[currentSceneCode.value].splice(pushIndex,0,templet)
		refreshJson()
	}
	
	const successTempletUpdate = (templet) => {
		templetEditVisible.value = false
		var tableData = props.ruleFlowTempletData.ruleFlowTemplet[currentSceneCode.value]
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
					"id": props.ruleFlowTempletData.ruleFlowId,
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
<style>
	.el-menu--collapse>.el-menu-item>span, .el-menu--collapse>.el-sub-menu>.el-sub-menu__title>span{
		visibility:visible;
		height:100%;
		width:100%;
	}
	.el-menu--collapse>.el-menu-item .el-sub-menu__icon-arrow, .el-menu--collapse>.el-sub-menu>.el-sub-menu__title .el-sub-menu__icon-arrow{
		display:block;
	}
	.el-menu--collapse{
		width:100%;
	}
</style>
