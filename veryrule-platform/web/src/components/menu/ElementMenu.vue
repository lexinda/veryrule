<template>
	<div class="NavMenu">
		<div class="SubMenu" :key="key">
			<ElementSubMenu :key="key" :subMenus="subMenusVal" :actives="activesVal"
				@menuselect="menuClick">
			</ElementSubMenu>
		</div>
	</div>
</template>
<script setup lang="ts">
	import {
		nextTick,
		ref,
		watch
	} from 'vue'
	import ElementSubMenu from './ElementSubMenu.vue'
	const props = defineProps < {
		menus: Array,
		actives: Array,
	} > ()

	const key = ref(0)
	const subMenusVal = ref([])
	watch(() => props.menus, (newVal, oldVal) => {
		key.value = key.value + 1
		nextTick(() => {
			subMenusVal.value = newVal
		})
	})

	const activesVal = ref([])
	watch(() => props.actives, (newVal, oldVal) => {
		key.value = key.value + 1
		activesVal.value = newVal
	})

	const emit = defineEmits(['menuselect']);
	const menuClick = (code,path) => {
		emit('menuselect', code,path);
	}
</script>
<style>
	@import url("../../assets/css/menu.css");
</style>
