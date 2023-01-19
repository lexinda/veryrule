<template>
	<el-form ref="ruleEditFormRef" :model="ruleData" label-width="120px" :rules="ruleEditRules">
		<el-form-item label="规则标识" prop="ruleCode" v-if="ruleData.id>0">
			<el-input v-model="ruleData.ruleCode" disabled></el-input>
		</el-form-item>
		<el-form-item label="规则标识" prop="ruleCode" v-else required>
			<el-input v-model="ruleData.ruleCode"></el-input>
		</el-form-item>
		<el-form-item label="规则名" prop="ruleName" required>
			<el-input v-model="ruleData.ruleName"></el-input>
		</el-form-item>
		<el-form-item label="规则类型" prop="ruleName" required v-if="ruleData.ruleType != 3">
			<el-select v-model="ruleData.ruleType" @change="handleConditionChange" style="width: 240px">
				<el-option  key="1" label="无返回值" value="1" />
				<el-option  key="2" label="有返回值" value="2" />
			</el-select>
		</el-form-item>
		<el-form-item label="组名" prop="groupName">
			<el-select v-model="ruleData.groupName" placeholder="请选择">
				<el-option v-for="(item,index) in ruleGroupData" :label="item.name"
					:value="item.name" />
			</el-select>
		</el-form-item>
		<el-form-item label="指定对象" prop="ruleKey">
			<el-input v-model="ruleData.ruleKey"></el-input>
		</el-form-item>
		<el-form-item label="默认值" prop="ruleValue">
			<el-input v-model="ruleData.ruleValue"></el-input>
		</el-form-item>
		<el-form-item label="表达式" prop="ruleExpr">
			<el-input v-model="ruleData.ruleExpr"></el-input>
		</el-form-item>
		<el-form-item label="描述" prop="ruleDesc" required>
			<el-input v-model="ruleData.ruleDesc" type="textarea"></el-input>
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

	import {
		ElMessage
	} from 'element-plus'

	const ruleEditFormRef = ref()

	const ruleEditRules = reactive({
		ruleCode: [{
			required: true,
			message: '规则标识不能为空',
			trigger: 'blur',
		}],
		ruleName: [{
			required: true,
			message: '规则名不能为空',
			trigger: 'blur',
		}],
		ruleDesc: [{
			required: true,
			message: '规则描述不能为空',
			trigger: 'blur',
		}],
	})

	const props = defineProps < {
		ruleData: Object,
		ruleGroupData:Array,
	} > ()

	const emit = defineEmits(['cancelRuleEdit', 'successRuleAdd', 'successRuleUpdate']);

	const onSubmit = (res: any) => {
		ruleEditFormRef.value.validate((valid: any, values) => {
			if (valid) {
				const id = ruleEditFormRef.value["model"].id
				var ruleType = props.ruleData.ruleType
				if(ruleType !=3){
					ruleType = ruleEditFormRef.value["model"].ruleType
				}
				const param = {
					"id": id,
					"ruleCode": ruleEditFormRef.value["model"].ruleCode,
					"ruleName": ruleEditFormRef.value["model"].ruleName,
					"ruleValue": ruleEditFormRef.value["model"].ruleValue,
					"groupName": ruleEditFormRef.value["model"].groupName,
					"ruleDesc": ruleEditFormRef.value["model"].ruleDesc,
					"ruleKey": ruleEditFormRef.value["model"].ruleKey,
					"ruleExpr": ruleEditFormRef.value["model"].ruleExpr,
					"ruleType": ruleType,
				}
				if (id > 0) {
					updateVeryrule(param)
				} else {
					addVeryrule(param)
				}
			} else {
				console.log('error submit!!')
				return false
			}
		})
	}

	const onCancel = () => {
		emit('cancelRuleEdit')
	}

	const addVeryrule = (param) => {
		post("/api/addVeryRuleElement", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
					message: '保存成功',
					type: 'success',
				})
				emit('successRuleAdd')
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}

	const updateVeryrule = (param) => {
		const body = new URLSearchParams()
		post("/api/updateVeryRuleElement", param, (data) => {
			if (data.errorCode == 0) {
				ElMessage({
					message: '保存成功',
					type: 'success',
				})
				emit('successRuleUpdate')
			}else{
				ElMessage.error(data.errorDesc)
			}
		});
	}
</script>
