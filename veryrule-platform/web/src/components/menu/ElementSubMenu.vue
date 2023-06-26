<template>
	<ul>
		<li v-for="(menu,index) in subMenus" :path="menu.ruleCode" :parentPath = 'menu.ruleCode+"-"+menuPath'
			:class="{'arrow':menu.arrow,'ruleActive':actives.filter(item => item == menu.ruleCode).length>0}"
			:ref="menu.id" @click="menuClick">
			<span :path="menu.ruleCode" :parentPath = 'menu.ruleCode+"-"+menuPath'>{{menu.ruleName}}</span>
			<ElementSubMenu :key="menu.id" :subMenus="menu.children" :actives="actives" :menuPath='menu.ruleCode+"-"+menuPath'
				v-if="menu.children !=null && menu.children.length>0"></ElementSubMenu>
		</li>
	</ul>
</template>
<script setup lang="ts">
	const props = defineProps < {
		subMenus: Array,
		actives: Array,
		menuPath: String
	} > ()

	const emit = defineEmits(['menuselect']);
	const menuClick = (event) => {
		let parentPath = event.srcElement.attributes.parentpath.value.split('-').filter(path=>path!='').reverse()
		let path = event.srcElement.attributes.path.value
		emit('menuselect', path,parentPath);
	}
</script>
