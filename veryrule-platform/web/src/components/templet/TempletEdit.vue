<template>
	<el-form ref="TempletEditFormRef" :model="ruleData" label-width="120px">
		<el-form-item label="指定关键字" prop="ruleKey">
			<el-input v-model="ruleData.ruleKey"></el-input>
		</el-form-item>
		<el-form-item label="默认值" prop="ruleValue">
			<el-input v-model="ruleData.ruleValue"></el-input>
		</el-form-item>
		<el-form-item label="执行条件" prop="ruleCondation">
			<el-input v-model="ruleData.ruleCondation" type="textarea"></el-input>
			<font color="red">多个,隔开</font>
		</el-form-item>
		<el-form-item label="异常提示" prop="ruleErrMsg">
			<el-input v-model="ruleData.ruleErrMsg" type="textarea"></el-input>
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

	const TempletEditFormRef = ref()

	const props = defineProps < {
		ruleData: Object,
		templetEditType: number,
	} > ()

	const emit = defineEmits(['cancelTempletEdit', 'successTempletUpdate', 'successTempletAdd']);

	const onSubmit = (res: any) => {
		TempletEditFormRef.value.validate((valid: any, values) => {
			if (valid) {
				var ruleCondations = []
				const ruleCondation = TempletEditFormRef.value["model"].ruleCondation
				if (ruleCondation != null && ruleCondation != "") {
					console.log(ruleCondation.indexOf(','))
					if (ruleCondation.indexOf(',') > 0) {
						ruleCondations = ruleCondation.split(',')
					} else {
						ruleCondations.push(ruleCondation)
					}
				}
				const templet = {
					"ruleCode": TempletEditFormRef.value["model"].ruleCode,
					"ruleName": props.ruleData.ruleName,
					"ruleValue": TempletEditFormRef.value["model"].ruleValue,
					"ruleKey": TempletEditFormRef.value["model"].ruleKey,
					"ruleCondation": ruleCondation,
					"ruleCondations": ruleCondations,
					"ruleErrMsg": TempletEditFormRef.value["model"].ruleErrMsg,
				}
				if (props.templetEditType == 1) {
					emit('successTempletAdd', templet)
				} else {
					emit('successTempletUpdate', templet)
				}

			} else {
				console.log('error submit!!')
				return false
			}
		})
	}

	const onCancel = () => {
		emit('cancelTempletEdit')
	}
</script>
