<template>
	<el-button size="small" type="primary" :icon="Plus" plain @click="addDocument()" style="margin-left:20px;">添加
	</el-button>
	<el-button size="small" v-if="tableData.length>0" type="primary" :icon="Document" plain @click="downloadDocument()" style="margin-left:20px;">导出
	</el-button>
	<el-table :data="tableData" :border="parentBorder" style="width: 100%">
		<el-table-column type="expand">
			<template #default="props">
				<div m="4" style="padding-left:10px;background-color: #F9FAFC;">
					<p m="t-0 b-2"><span style="font-weight: bold;">描述:</span> <br>
						<span style="margin-left: 60px;">{{ props.row.desc }}</span>
					</p>
					<p m="t-0 b-2"><span style="font-weight: bold;">请求URL:</span><br> <div style="margin-left: 60px;color:red;"
							v-for="(domain,index) in props.row.domains">{{ domain.key }}:{{ domain.value }}</div>
					</p>
					<p m="t-0 b-2"><span style="font-weight: bold;">请求方式:</span><br> <span
							style="margin-left: 60px;">{{ props.row.request.type }}</span><br><span
							style="margin-left: 60px;">{{ props.row.request.contentType }}</span></p>
					<p m="t-0 b-2"><span style="font-weight: bold;">入参:</span>
						<el-table border :data="props.row.paramIn" :border="childBorder">
							<el-table-column label="参数" prop="field" />
							<el-table-column label="参数名" prop="name" />
							<el-table-column label="必填" prop="require" />
							<el-table-column label="类型" prop="dtype" />
							<el-table-column label="说明" prop="desc" />
						</el-table>
					</p>
					<p m="t-0 b-2"><span style="font-weight: bold;">入参示例:</span> <br>
					<p>{{ props.row.paramInExample}}</p>
					</p>
					<p m="t-0 b-2"><span style="font-weight: bold;">出参:</span>
						<el-table border :data="props.row.paramOut" :border="childBorder">
							<el-table-column label="参数" prop="field" />
							<el-table-column label="参数名" prop="name" />
							<el-table-column label="类型" prop="dtype" />
							<el-table-column label="说明" prop="desc" />
						</el-table>
					</p>
					<p m="t-0 b-2"><span style="font-weight: bold;">出参示例:</span> <br>
					<p>{{ props.row.paramOutExample}}</p>
					</p>
				</div>
			</template>
		</el-table-column>
		<el-table-column label="接口地址" width="350">
			<template #default="scope">
				<div style="align-items: center" v-for="(domain,index) in scope.row.domains">
					<span style="margin-left: 10px">{{ domain.key }}:{{ domain.value }}</span>
				</div>
			</template>
		</el-table-column>
		<el-table-column label="接口名称" prop="name" width="150" />
		<el-table-column label="接口人" prop="author" width="100" />
		<el-table-column align="center" width="140" label="操作">
			<template #default="scope">
				<el-button size="small" @click="editDocument(scope.$index, scope.row)">编辑</el-button>
				<el-popconfirm title="是否删除?" @confirm="deleteDocument(scope.$index, scope.row)">
					<template #reference>
						<el-button size="small" type="danger">删除</el-button>
					</template>
				</el-popconfirm>
			</template>
		</el-table-column>
	</el-table>

	<el-dialog v-model="documentEditVisible" destroy-on-close width="700px">
		<el-form ref="documentFormRef" :model="documentForm" style="max-width: 650px;mars-top:20px;">
			<el-form-item v-for="(domain, index) in documentForm.domains" :key="domain.key" :label="'URL' + index"
				:prop="'domains.' + index + '.value'" :rules="{
			         required: true,
			         message: 'URL不能为空',
			         trigger: 'blur',
			       }">
				<el-col :span="6">
					<el-form-item>
						<el-select v-model="domain.key">
							<el-option label="beta" value="beta" />
							<el-option label="online" value="online" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="18">
					<el-form-item style="margin-bottom:5px;">
						<el-input v-model="domain.value" />
					</el-form-item>
				</el-col>
				<el-button class="mt-2" @click.prevent="removeDomain(domain)">删除</el-button>
				<el-button @click="addDomain" v-if="index == (documentForm.domains.length-1)">新增</el-button>
			</el-form-item>
			<el-form-item label="请求方式" :label-width="formLabelWidth">
				<el-col :span="6">
					<el-form-item>
						<el-select v-model="documentForm.request.type">
							<el-option label="post" value="post" />
							<el-option label="get" value="get" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="18">
					<el-form-item>
						<el-select placeholder="application/json" v-model="documentForm.request.contentType">
							<el-option label="application/json" value="application/json" />
							<el-option label="application/x-www-form-urlencoded"
								value="application/x-www-form-urlencoded" />
							<el-option label="multipart/form-data" value="multipart/form-data" />
							<el-option label="text/xml" value="text/xml" />
						</el-select>
					</el-form-item>
				</el-col>
			</el-form-item>
			<el-form-item label="名称" :label-width="formLabelWidth" prop="name" :rules="[
				     {
				 	  						  type: 'string',
				       required: true,
				       message: '请输入字段名称',
				       trigger: 'blur',
				     },
				   ]">
				<el-input v-model="documentForm.name" />
			</el-form-item>
			<el-form-item label="关键字" :label-width="formLabelWidth" prop="key">
				<el-input v-model="documentForm.key" />
			</el-form-item>
			<el-form-item label="接口人" :label-width="formLabelWidth" prop="author" :rules="[
	  	              {
	  						  type: 'string',
	  	                required: true,
	  	                message: '请输入接口人',
	  	                trigger: 'blur',
	  	              },
	  	            ]">
				<el-input v-model="documentForm.author" />
			</el-form-item>
			<el-form-item label="入参" :label-width="formLabelWidth">
				<el-button type="warning" plain size="small" :icon="Plus" circle @click="addParam(1)"></el-button>
				<el-table :data="documentForm.paramIn" style="width: 100%;font-size:8px;">
					<el-table-column label="参数" width="100" prop="field">
					</el-table-column>
					<el-table-column label="参数名" width="120" prop="name">
					</el-table-column>
					<el-table-column label="必填" width="60" prop="require">
					</el-table-column>
					<el-table-column label="类型" width="60" prop="dtype">
					</el-table-column>
					<el-table-column label="说明" width="120" prop="desc">
					</el-table-column>
					<el-table-column label="操作">
						<template #default="scope">
							<el-button type="primary" size="small" :icon="Edit" circle
								@click="editParam(scope.$index, scope.row,1)"></el-button>
							<el-button type="danger" size="small" :icon="Delete" circle
								@click="deleteParam(scope.$index, scope.row,1)">
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-form-item>
			<el-form-item label="入参示例" :label-width="formLabelWidth" prop="key">
				<el-input v-model="documentForm.paramInExample" autocomplete="off" type="textarea" maxlength="350"
					show-word-limit />
			</el-form-item>
			<el-form-item label="出参" :label-width="formLabelWidth">
				<el-button type="warning" plain size="small" :icon="Plus" circle @click="addParam(2)"></el-button>
				<el-table :data="documentForm.paramOut" style="width: 100%;font-size:8px;">
					<el-table-column label="参数" width="100" prop="field">
					</el-table-column>
					<el-table-column label="参数名" width="120" prop="name">
					</el-table-column>
					<el-table-column label="类型" width="60" prop="dtype">
					</el-table-column>
					<el-table-column label="说明" width="120" prop="desc">
					</el-table-column>
					<el-table-column label="操作">
						<template #default="scope">
							<el-button type="primary" size="small" :icon="Edit" circle
								@click="editParam(scope.$index, scope.row,2)"></el-button>
							<el-button type="danger" size="small" :icon="Delete" circle
								@click="deleteParam(scope.$index, scope.row,2)">
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-form-item>
			<el-form-item label="出参示例" :label-width="formLabelWidth" prop="key">
				<el-input v-model="documentForm.paramOutExample" autocomplete="off" type="textarea" maxlength="350"
					show-word-limit />
			</el-form-item>
			<el-form-item label="描述" :label-width="formLabelWidth">
				<el-input v-model="documentForm.desc" autocomplete="off" type="textarea" maxlength="150"
					show-word-limit />
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="documentEditVisible = false">取消</el-button>
				<el-button type="primary" @click="saveDocumentInfo(documentFormRef)">
					提交
				</el-button>
			</span>
		</template>
	</el-dialog>


	<el-dialog v-model="documenParamtEditVisible" destroy-on-close width="500px">
		<el-form ref="documentParamFormRef" :model="documentParamForm" style="max-width: 450px;mars-top:20px;">
			<el-form-item label="参数" :label-width="formLabelWidth" :rules="[
				     {
				 	  						  type: 'string',
				       required: true,
				       message: '请输入字段',
				       trigger: 'blur',
				     },
				   ]">
				<el-input v-model="documentParamForm.field" />
			</el-form-item>
			<el-form-item label="参数名" :label-width="formLabelWidth" :rules="[
				     {
				 	  						  type: 'string',
				       required: true,
				       message: '请输入字段名称',
				       trigger: 'blur',
				     },
				   ]">
				<el-input v-model="documentParamForm.name" />
			</el-form-item>
			<el-form-item label="必填" :label-width="formLabelWidth">
				<el-select v-model="documentParamForm.require">
					<el-option label="是" value="是" />
					<el-option label="否" value="否" />
				</el-select>
			</el-form-item>
			<el-form-item label="类型" :label-width="formLabelWidth">
				<el-select v-model="documentParamForm.dtype">
					<el-option label="string" value="string" />
					<el-option label="int" value="int" />
					<el-option label="long" value="long" />
					<el-option label="decima" value="decima" />
					<el-option label="array" value="array" />
					<el-option label="map" value="map" />
				</el-select>
			</el-form-item>
			<el-form-item label="说明" :label-width="formLabelWidth">
				<el-input v-model="documentParamForm.desc" autocomplete="off" type="textarea" maxlength="150"
					show-word-limit />
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="documenParamtEditVisible = false">取消</el-button>
				<el-button type="primary" @click="saveDocumentParam(documentParamFormRef)">
					提交
				</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script lang="ts" setup>
	import {
		Delete,
		Edit,
		Plus,
		Document,
	} from '@element-plus/icons-vue'
	import {
		reactive,
		ref,
		nextTick
	} from 'vue'
	import {
		ElMessage
	} from 'element-plus'
	import type {
		FormInstance,
		FormRules,
	} from 'element-plus'

	import post from "../../axios/post.js";

	const removeDomain = (item: DomainItem) => {
		const index = documentForm.domains.indexOf(item)
		if (index !== -1) {
			documentForm.domains.splice(index, 1)
		}
	}

	const addDomain = () => {
		documentForm.domains.push({
			key: 'beta',
			value: '',
		})
	}

	const parentBorder = ref(false)
	const childBorder = ref(false)

	const props = defineProps < {
		veryFlowDocumentData: Array,
		documentRuleFlowCode: String
	} > ()

	const formLabelWidth = '80px'
	const documentFormRef = ref < FormInstance > ()
	const documentForm = reactive({
		id: 0,
		domains: [{
			key: 'beta',
			value: '',
		}, ],
		name: '',
		author: '',
		key: '',
		request: {
			type: 'post',
			contentType: 'application/json',
		},
		paramIn: [{
			id: 0,
			field: '',
			name:'',
			require: '是',
			dtype: 'String',
			desc: '',
		}],
		paramInExample: "",
		paramOut: [{
			id: 0,
			field: '',
			name:'',
			require: '是',
			dtype: 'String',
			desc: '',
		}],
		paramOutExample: "",
		desc: '',
	})

	const editDocument = (index: number, row: Object) => {
		documentEditVisible.value = true
		nextTick(() => {
			documentForm.id = row.id
			documentForm.domains = row.domains
			documentForm.name = row.name
			documentForm.author = row.author
			documentForm.key = row.key
			documentForm.request = row.request
			documentForm.paramIn = row.paramIn
			documentForm.paramInExample = row.paramInExample
			documentForm.paramOut = row.paramOut
			documentForm.paramOutExample = row.paramOutExample
			documentForm.desc = row.desc
		});
	}
	const deleteDocument = (index: number, row: Object) => {
		tableData.value.splice(index, 1)
		let data = {
			"ruleFlowCode": props.documentRuleFlowCode,
			"ruleFlowDocument": tableData.value
		}
		updateVeryRuleDocument(data);
	}

	const documentEditVisible = ref(false);

	const addDocument = () => {
		documentEditVisible.value = true
		documentForm.id = 0
		documentForm.domains = [{
			key: 'beta',
			value: '',
		}, ]
		documentForm.name = ''
		documentForm.author = ''
		documentForm.key = ''
		documentForm.request = {
			type: 'post',
			contentType: 'application/json',
		}
		documentForm.paramIn = [{
			id: 0,
			field: '',
			name:'',
			require: '是',
			dtype: 'String',
			desc: '',
		}]
		documentForm.paramInExample = ""
		documentForm.paramOut = [{
			id: 0,
			field: '',
			name:'',
			require: '是',
			dtype: 'String',
			desc: '',
		}]
		documentForm.paramOutExample = ""
		documentForm.desc = ''
	}

	const saveDocumentInfo = async (formEl: FormInstance | undefined) => {
		if (!formEl) return
		await formEl.validate((valid, fields) => {
			if (valid) {
				if (documentForm.id > 0) {
					tableData.value.forEach(function(data) {
						if (documentForm.id == data.id) {
							data.domains = documentForm.domains
							data.name = documentForm.name
							data.author = documentForm.author
							data.key = documentForm.key
							data.request = documentForm.request
							data.paramIn = documentForm.paramIn
							data.paramInExample = documentForm.paramInExample
							data.paramOut = documentForm.paramOut
							data.paramOutExample = documentForm.paramOutExample
							data.desc = documentForm.desc
						}
					})
				} else {
					var dataId = 1
					tableData.value.forEach(function(data) {
						if(data.id>dataId){
							dataId = data.id+1;
						}
					})
					documentForm.id = dataId
					tableData.value.push(documentForm)
				}
				console.log(documentForm.id)
				let data = {
					"ruleFlowCode": props.documentRuleFlowCode,
					"ruleFlowDocument": tableData.value
				}
				updateVeryRuleDocument(data);
			} else {
				console.log('error submit!', fields)
			}
		})
	}

	const documenParamtEditVisible = ref(false)
	const documentParamFormRef = ref < FormInstance > ()
	const documentParamForm = ref({
		id: 0,
		type: 1,
		field: '',
		name:'',
		require: '是',
		dtype: 'String',
		desc: '',
	})

	const addParam = (type: number) => {
		let row = {
			id: 0,
			field: '',
			name:'',
			require: '是',
			dtype: 'String',
			desc: '',
		}
		if (type == 1) {
			row.type = 1
		} else {
			row.type = 2
		}
		documentParamForm.value = row
		documenParamtEditVisible.value = true
	}

	const editParam = (index: number, row: Object, type: number) => {
		documenParamtEditVisible.value = true
		if (type == 1) {
			row.type = 1
		} else {
			row.type = 2
		}
		documentParamForm.value = row
	}

	const saveDocumentParam = () => {
		var haveField = 0
		var dataIndex = 0;
		if (documentParamForm.value.type = 1) {
			documentForm.paramIn.forEach(function(data,index) {
				if (documentParamForm.value.field == data.field) {
					dataIndex = index
					haveField = 1
				}
			})
			if(haveField == 0){
				documentForm.paramIn.push(documentParamForm.value)
			}
			if(dataIndex>0){
				documentForm.paramIn.splice(dataIndex, 1,documentParamForm.value)
			}
		} else {
			documentForm.paramOut.forEach(function(data,index) {
				if (documentParamForm.value.field == data.field) {
					dataIndex = index
					haveField = 1
				}
			})
			if(haveField == 0){
				documentForm.paramOut.push(documentParamForm.value)
			}
			if(dataIndex>0){
				documentForm.paramOut.splice(dataIndex, 1,documentParamForm.value)
			}
		}
		documenParamtEditVisible.value = false
	}

	const deleteParam = (index: number, row: Object, type: number) => {
		if (type == 1) {
			documentForm.paramIn.splice(index, 1)
		} else {
			documentForm.paramOut.splice(index, 1)
		}
	}
	const emit = defineEmits(['successVeryRuleFlowDocumentUpdate']);
	const updateVeryRuleDocument = (param) => {
		const body = new URLSearchParams()
		post("/api/updateVeryRuleFlowDocument", param, (data) => {
			if (data.errorCode == 0) {
				documentEditVisible.value = false
				ElMessage({
					message: '保存成功',
					type: 'success',
				})
				emit('successVeryRuleFlowDocumentUpdate')
			} else {
				ElMessage.error(data.errorDesc)
			}
		});
	}

	const tableData = ref(props.veryFlowDocumentData)
	
	const downloadDocument = ()=>{
		location.href = "http://localhost:8090/veryrule/downVeryRuleFlowDocument?ruleFlowCode="+props.documentRuleFlowCode
	}
	
	
</script>