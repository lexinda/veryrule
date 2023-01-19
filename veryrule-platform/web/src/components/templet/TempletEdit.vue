<template>
	<el-form ref="TempletEditFormRef" :model="ruleData" label-width="120px">
		<el-form-item label="指定关键字:" prop="ruleKey">
			<el-input v-model="ruleData.ruleKey"></el-input>
		</el-form-item>
		<el-form-item label="默认值:" prop="ruleValue">
			<el-input v-model="ruleData.ruleValue"></el-input>
		</el-form-item>
		<el-form-item label="类型:" prop="ruleType">
			<span v-if="ruleData.ruleType == 1">条件(无返回值)</span>
			<span v-if="ruleData.ruleType == 2">条件(有返回值)</span>
			<span v-if="ruleData.ruleType == 3">动作</span>
		</el-form-item>
		<el-form-item label="是否异步:" prop="ruleAsyn"  v-if="ruleData.ruleType == 2">
			<el-select v-model="ruleData.ruleAsyn">
				<el-option  label="异步" value="1" />
				<el-option  label="同步" value="2" />
			</el-select>
		</el-form-item>
		<el-form-item label="表达式:" prop="ruleExpr">
			<el-input v-model="ruleData.ruleExpr"></el-input>
		</el-form-item>
		<el-form-item label="异常提示:" prop="ruleErrMsg">
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
	import {
		ZoomIn,
	} from '@element-plus/icons-vue'
	const TempletEditFormRef = ref()

	const props = defineProps < {
		ruleData: Object,
		templetEditType: number,
	} > ()

	const emit = defineEmits(['cancelTempletEdit', 'successTempletUpdate', 'successTempletAdd']);

	const onSubmit = (res: any) => {
		TempletEditFormRef.value.validate((valid: any, values) => {
			if (valid) {
				const templet = {
					"ruleCode": TempletEditFormRef.value["model"].ruleCode,
					"ruleName": props.ruleData.ruleName,
					"ruleValue": TempletEditFormRef.value["model"].ruleValue,
					"ruleKey": TempletEditFormRef.value["model"].ruleKey,
					"ruleExpr": TempletEditFormRef.value["model"].ruleExpr,
					"ruleType": TempletEditFormRef.value["model"].ruleType,
					"ruleAsyn": TempletEditFormRef.value["model"].ruleAsyn,
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
