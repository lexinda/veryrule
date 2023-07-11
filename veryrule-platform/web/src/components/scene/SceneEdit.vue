<template>
	<el-form ref="sceneEditFormRef" :model="ruleSceneItem" label-width="120px" :rules = "sceneEditrules">
		<el-input type="hidden" v-model="ruleSceneItem.id"></el-input>
		<el-input type="hidden" v-model="ruleSceneItem.pid"></el-input>
		<el-form-item label="场景编码" prop="ruleSceneCode" >
			<el-input v-if="ruleSceneItem.id>0" readonly v-model="ruleSceneItem.ruleSceneCode"></el-input>
			<el-input v-else v-model="ruleSceneItem.ruleSceneCode"></el-input>
		</el-form-item>
		<el-form-item label="场景名称" prop="ruleSceneName">
			<el-input v-model="ruleSceneItem.ruleSceneName"></el-input>
		</el-form-item>
		<el-form-item label="场景类型:" prop="ruleSceneType">
			<span v-if="ruleSceneItem.ruleSceneType == '1'">
				场景
			</span>
			<span v-else>
				操作
			</span>
		</el-form-item>
		<el-form-item label="描述" prop="ruleSceneDesc">
			<el-input v-model="ruleSceneItem.ruleSceneDesc" type="textarea"></el-input>
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

	const sceneEditFormRef = ref()
	
	const sceneEditrules = reactive({
		ruleSceneCode: [{
				required: true,
				message: '规则场景编码不能为空',
				trigger: 'blur',
			}
		],
		ruleSceneName: [{
				required: true,
				message: '规则场景名称不能为空',
				trigger: 'blur',
			}
		],
	})

	const props = defineProps < {
		ruleSceneData: Object
	} > ()
	
	const ruleSceneItem = ref({
		'id':props.ruleSceneData.id,
		'pid':props.ruleSceneData.pid,
		'ruleSceneCode':props.ruleSceneData.ruleSceneCode,
		'ruleSceneName':props.ruleSceneData.ruleSceneName,
		'ruleSceneType':props.ruleSceneData.ruleSceneType,
		'ruleSceneDesc':props.ruleSceneData.ruleSceneDesc,
	})
	
	const emit = defineEmits(['cancelSceneEdit','successSceneAdd','successSceneUpdate']);

	const onSubmit = (res: any) => {
		sceneEditFormRef.value.validate((valid: any, values) => {
			if (valid) {
				const id = sceneEditFormRef.value["model"].id
				const param = {
					"id": id,
					"pid": props.ruleSceneData.pid,
					"ruleSceneCode": sceneEditFormRef.value["model"].ruleSceneCode,
					"ruleSceneName": sceneEditFormRef.value["model"].ruleSceneName,
					"ruleSceneType": props.ruleSceneData.ruleSceneType,
					"ruleSceneDesc": sceneEditFormRef.value["model"].ruleSceneDesc,
				}
				if(id>0){
					updateVeryRuleScene(param)
				}else{
					addVeryRuleScene(param)
				}
			} else {
				return false
			}
		})
	}

	const onCancel = () => {
		emit('cancelSceneEdit')
	}

	const addVeryRuleScene = (param) => {
		const body = new URLSearchParams()
		post("/api/addVeryRuleScene", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
				    message: '保存成功',
				    type: 'success',
				  })
				emit('successSceneAdd')
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
	
	const updateVeryRuleScene = (param) => {
		const body = new URLSearchParams()
		post("/api/updateVeryRuleScene", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
				    message: '保存成功',
				    type: 'success',
				  })
				emit('successSceneUpdate',ruleSceneItem.value)
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
</script>
