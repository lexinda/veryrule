<template>
	<el-form ref="veryFlowtempletCopyRef" :model="veryFlowtempletCopyData" label-width="140px"
		:rules="ruleFlowCopyrules">
		<el-form-item label="规则流父级" prop="parentRuleFlowCode">
			<el-select v-model="veryFlowtempletCopyData.parentRuleFlowCode" placeholder="请选择">
				<el-option v-for="(item,index) in veryFlowAllData" :label="item.ruleFlowCode"
					:value="item.ruleFlowName" />
			</el-select>
		</el-form-item>
		<el-form-item label="规则流标识" prop="ruleFlowCode">
			<el-input v-model="veryFlowtempletCopyData.ruleFlowCode"></el-input>
		</el-form-item>
		<el-form-item label="规则流名称" prop="ruleFlowName">
			<el-input v-model="veryFlowtempletCopyData.ruleFlowName"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="handleTempletCopyCommit">保存</el-button>
			<el-button @click="handleTempletCopyCancel">取消</el-button>
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

	const props = defineProps < {
		veryFlowAllData: Array,
		veryFlowtempletCopyData:Object
	} > ()

	const veryFlowtempletCopyRef = ref()
	const ruleFlowCopyrules = ref({
		parentRuleFlowCode: [{
			required: true,
			message: '父级规则流不能为空',
			trigger: 'blur',
		}],
		ruleFlowCode: [{
			required: true,
			message: '规则流标识不能为空',
			trigger: 'blur',
		}],
		ruleFlowName: [{
			required: true,
			message: '规则流名称不能为空',
			trigger: 'blur',
		}],
	})

	const emit = defineEmits(['cancelFlowCopy', 'successFlowCopy']);

	const handleTempletCopyCommit = (res: any) => {
		veryFlowtempletCopyRef.value.validate((valid: any, values) => {
			if (valid) {
				const param = {
					"id": props.veryFlowtempletCopyData.id,
					"parentRuleFlowCode": veryFlowtempletCopyRef.value["model"].parentRuleFlowCode,
					"ruleFlowCode": veryFlowtempletCopyRef.value["model"].ruleFlowCode,
					"ruleFlowName": veryFlowtempletCopyRef.value["model"].ruleFlowName,
				}
				post("/api/copyVeryRuleFlow", param, (data) => {
					if (data.errorCode == 0) {
						ElMessage({
							message: '保存成功',
							type: 'success',
						})
						emit('successFlowCopy')
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

	const handleTempletCopyCancel = () => {
		emit('cancelFlowCopy')
	}
</script>
