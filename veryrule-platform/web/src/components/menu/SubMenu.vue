<template>
  <el-sub-menu :index="menuInfo.ruleCode">
    <template #title>
      <span>{{ menuInfo.ruleName }}</span>
    </template>
    <template v-for="item in menuInfo.children">
      <el-menu-item v-if="!item.children"  :index="item.ruleCode">
		<span>{{ item.ruleName }}</span>
      </el-menu-item>
      <SubMenu v-else :menuInfo="item" />
    </template>
  </el-sub-menu>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const props = defineProps<{
  menuInfo: Object
}>()

</script>