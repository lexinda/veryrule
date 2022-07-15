<template>
  <el-sub-menu :index="menuInfo.ruleCode">
    <template #title>
      <span>{{ menuInfo.ruleName }}</span>
    </template>
    <template v-for="item in menuInfo.children">
      <el-menu-item v-if="!item.children"  :index="item.ruleCode">
		<span>{{ item.ruleName }}</span>
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
  </el-sub-menu>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
		Bell,
		BellFilled,
		Promotion
	} from '@element-plus/icons-vue'
const props = defineProps<{
  menuInfo: Object
}>()

</script>