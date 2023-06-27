<template>
	<ul>
		<li v-for="(menu,index) in subMenus" :code="menu.ruleCode" :path="menu.path"
			:class="{'arrow':menu.arrow,'ruleActive':actives.filter(item => item == menu.path).length>0}"
			:ref="menu.id" @click="menuClick">
			<span :code="menu.ruleCode" :path="menu.path">{{menu.ruleName}}</span>
			<ElementSubMenu :key="menu.id" :subMenus="menu.children" :actives="actives"
				v-if="menu.children !=null && menu.children.length>0"></ElementSubMenu>
		</li>
	</ul>
</template>
<script setup lang="ts">
	const props = defineProps < {
		subMenus: Array,
		actives: Array,
	} > ()

	const emit = defineEmits(['menuselect']);
	const menuClick = (event) => {
		let code = event.srcElement.attributes.code.value
		let path = event.srcElement.attributes.path.value.split('_')
		emit('menuselect', code,path);
	}
</script>
