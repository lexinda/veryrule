<template>
	<el-form ref="flowEditFormRef" :model="ruleFlowDataItem" label-width="120px" :rules = "flowEditrules">
		<el-form-item label="上级流标识" prop="parentRuleFlowCode">
			<el-input v-model="ruleFlowDataItem.parentRuleFlowCode" disabled></el-input>
		</el-form-item>
		<el-form-item label="流标识" prop="ruleFlowCode"  v-if="ruleFlowDataItem.id>0" >
			<el-input v-model="ruleFlowDataItem.ruleFlowCode" disabled></el-input>
		</el-form-item>
		<el-form-item label="流标识" prop="ruleFlowCode"  v-else >
			<el-input v-model="ruleFlowDataItem.ruleFlowCode"></el-input>
		</el-form-item>
		<el-form-item label="流名称" prop="ruleFlowName">
			<el-input v-model="ruleFlowDataItem.ruleFlowName"></el-input>
		</el-form-item>
		<el-form-item label="组织" prop="groupName">
			<el-input v-model="ruleFlowDataItem.groupName"></el-input>
		</el-form-item>
		<el-form-item label="规则场景" prop="ruleSceneId">
			<el-select v-model="ruleFlowDataItem.ruleSceneId" placeholder="请选择">
				<el-option label="无" value="0" />
				<el-option v-for="(item,index) in ruleSceneData" :label="item.ruleSceneName"
					:value="item.id" />
			</el-select>
		</el-form-item>
		<el-form-item label="描述" prop="ruleFlowDesc">
			<el-input v-model="ruleFlowDataItem.ruleFlowDesc" type="textarea"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="onSubmit">保存</el-button>
			<el-button @click="onCancel">取消</el-button>
		</el-form-item>
	</el-form>
</template>

<script lang="ts" setup>
	import {
		reactive,
		ref,
		unref
	} from 'vue'
	
	import post from "../../axios/post.js";
	
	import { ElMessage } from 'element-plus'

	const flowEditFormRef = ref()
	
	const flowEditrules = reactive({
		ruleFlowCode: [{
				required: true,
				message: '规则流标识不能为空',
				trigger: 'blur',
			}
		],
		ruleFlowName: [{
				required: true,
				message: '规则流名称不能为空',
				trigger: 'blur',
			}
		],
		ruleFlowDesc: [{
				required: true,
				message: '规则流描述不能为空',
				trigger: 'blur',
			}
		],
	})

	const props = defineProps < {
		ruleFlowData: Object,
		ruleSceneData:Array,
	} > ()
	
	const ruleFlowDataItem = ref({
		"id": props.ruleFlowData.id,
		"pid": props.ruleFlowData.pid,
		"ruleFlowCode": props.ruleFlowData.ruleFlowCode,
		"ruleFlowName": props.ruleFlowData.ruleFlowName,
		"parentRuleFlowCode": props.ruleFlowData.parentRuleFlowCode,
		"groupName": props.ruleFlowData.groupName,
		"ruleFlowDesc": props.ruleFlowData.ruleFlowDesc,
		"ruleSceneId": props.ruleFlowData.ruleSceneId,
	})
	
	const emit = defineEmits(['cancelFlowEdit','successFlowAdd','successFlowUpdate']);

	const onSubmit = (res: any) => {
		flowEditFormRef.value.validate((valid: any, values) => {
			if (valid) {
				const id = flowEditFormRef.value["model"].id
				const param = {
					"id": id,
					"ruleFlowCode": flowEditFormRef.value["model"].ruleFlowCode,
					"ruleFlowName": flowEditFormRef.value["model"].ruleFlowName,
					"parentRuleFlowCode": flowEditFormRef.value["model"].parentRuleFlowCode,
					"groupName": flowEditFormRef.value["model"].groupName,
					"ruleFlowDesc": flowEditFormRef.value["model"].ruleFlowDesc,
					"ruleSceneId": flowEditFormRef.value["model"].ruleSceneId,
				}
				if(id>0){
					updateVeryRuleFlow(param)
				}else{
					addVeryRuleFlow(param)
				}
			} else {
				console.log('error submit!!')
				return false
			}
		})
	}

	const onCancel = () => {
		emit('cancelFlowEdit')
	}

	const addVeryRuleFlow = (param) => {
		const body = new URLSearchParams()
		post("/api/addVeryRuleFlow", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
				    message: '保存成功',
				    type: 'success',
				  })
				emit('successFlowAdd')
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
	
	const updateVeryRuleFlow = (param) => {
		const body = new URLSearchParams()
		post("/api/updateVeryRuleFlow", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
				    message: '保存成功',
				    type: 'success',
				  })
				emit('successFlowUpdate',ruleFlowDataItem.value)
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
	
</script>
