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
<style>
	.el-menu {
		--el-menu-item-height:40px;
	}
	.el-menu--collapse>.el-menu-item>span, .el-menu--collapse>.el-sub-menu>.el-sub-menu__title>span {
	    height: 40px;
	    width: 100%;
	    overflow: auto;
	    visibility: visible;
	    display: inline-block;
		font-size: 13px;
	}
	.el-sub-menu .el-menu-item {
	    height: 40px;
	    line-height: 40px;
	    padding: 0;
	    min-width: 200px;
	}
</style>