<template>
	<el-form ref="veryFlowtempletSceneRef" :model="veryFlowtempletSceneData" label-width="140px"
		:rules="ruleFlowCopyrules">
		<div class="demo-image__placeholder">
		    <div class="block" v-for="(item,index) in veryFlowSceneData">
		      <el-image :src="item" 
			  :preview-src-list="veryFlowSceneData"
			  :initial-index="index"
				fit="cover"/>
		    </div>
		  </div>
	</el-form>
</template>

<script lang="ts" setup>
	import {
		reactive,
		ref,
		unref
	} from 'vue'

	import {
		ElMessage
	} from 'element-plus'

	const props = defineProps < {
		veryFlowSceneData: Array,
	} > ()

	const emit = defineEmits(['cancelFlowScene']);

	const handleTempletSceneCancel = () => {
		emit('cancelFlowScene')
	}
</script>

<style scoped>
.demo-image__placeholder .block {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  display: inline-block;
  width: 49%;
  box-sizing: border-box;
  vertical-align: top;
}
.demo-image__placeholder .demonstration {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}
.demo-image__placeholder .el-image {
  padding: 0 5px;
  max-width: 400px;
  max-height: 400px;
}

.demo-image__placeholder.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 14px;
}
.demo-image__placeholder .dot {
  animation: dot 2s infinite steps(3, start);
  overflow: hidden;
}
</style>
