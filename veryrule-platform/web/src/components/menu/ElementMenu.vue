<template>
	<div class="NavMenu">
		<div class="SubMenu" :key="key">
			<ElementSubMenu :key="key" :subMenus="subMenusVal" :actives="activesVal" :menuPath="menuPathVal"
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

	const menuPathVal = ref([])
	const emit = defineEmits(['menuselect']);
	const menuClick = (path,parentPath) => {
		console.log(path)
		console.log(parentPath)
		emit('menuselect', path,parentPath);
	}
</script>
<style>
	* {
		box-sizing: border-box;
		margin: 0;
		padding: 0;
	}

	*::after {
		box-sizing: border-box;
	}

	*::before {
		box-sizing: border-box;
	}

	body {
		font: 14px/150% microsoft yahei, Tahoma, Arial;
		text-align: center;
		text-size-adjust: 100%;
		min-width: 320px;
		color: #232c45;
		margin: 0;
		padding: 0 0 20px 0;
	}

	a {
		outline: none;
		color: #00a4ff;
		text-decoration: none;
		transition: 0.2s;
	}

	a:hover {
		color: #ee4063;
	}

	::-webkit-scrollbar {
		/*Chrome适用*/
		width: 10px;
		height: 1px;
	}

	::-webkit-scrollbar-thumb {
		/*滚动条*/
		border-radius: 10px;
		background: #959da9;
	}

	::-webkit-scrollbar-track {
		/*滚动条里面轨道*/
		border-radius: 10px;
		background: #dce1e6;
		opacity: 0.7;
	}

	img {
		border: none
	}

	input,
	select,
	textarea {
		font-family: microsoft yahei, Helvetica, Tahoma, Arial, sans-serif;
		outline: none;
		border: none;
		background: #e9eef2;
	}

	.fleft {
		float: left
	}

	.fright {
		float: right
	}

	.clear {
		clear: both
	}

	.Col3 {
		width: 33%;
	}

	.Top {
		background: #2b303b;
		height: 48px;
	}

	.Line {
		background: rgba(120, 130, 150, .15);
		height: 1px;
		margin: 5px;
	}

	.Logo {
		background: url('../../assets/images/logo_ued.svg') no-repeat;
		width: 100px;
		height: 24px;
		margin: 12px;
		float: left
	}

	.NavMenu {
		/*所有产品*/
		height: 100vh;
		whidth: 200px;
	}

	.NavMenu .SubMenu {
		/*子菜单*/
		border-right: 1px solid rgba(120, 130, 150, .2);
		width: 200px;
		background: #ECEDEF;
		position: absolute;
		top: 55px;
		bottom: 0;
		left: 0px;
	}

	.NavMenu:hover .SubMenu {
		left: 0;
		top: 55px;
		transition: 0.2s;
		z-index: 10;
	}

	.NavMenu:hover .SubMenu:before,
	.NavMenu:hover .SubMenu ul:before {
		/*加大甜区*/
		content: "";
		position: absolute;
		width: 100%;
		left: 200px;
		bottom: 0;
		top: 55px;
	}

	.NavMenu .SubMenu ul {
		padding: 0 0 0 0;
	}

	.NavMenu .SubMenu li {
		border-bottom: 1px solid rgba(120, 130, 150, .15);
		line-height: 36px;
		text-align: left;
		padding: 0 32px 0 20px;
		list-style: none
	}

	.NavMenu .SubMenu li:first-child {
		border-top: 1px solid rgba(120, 130, 150, .15);
	}

	.NavMenu .SubMenu .arrow {
		background: url('../../assets/images/ico_arrow.svg') center right 10px no-repeat;
		background-size: 14px;
		transition: 0.2s
	}

	.NavMenu .arrow:hover {
		background-position: center right 5px !important;
	}

	.NavMenu .SubMenu span {
		color: #232c45;
		width: 100%;
		height: 100%;
		display: block;
	}

	.NavMenu .SubMenu li:hover {
		background-color: rgba(120, 130, 150, .2);
	}

	.NavMenu .SubMenu li span:hover {
		color: #D7063B;
	}

	.NavMenu .SubMenu li ul {
		/*二级菜单*/
		border-right: 1px solid rgba(120, 130, 200, .2);
		box-shadow: 0 0 10px rgba(120, 130, 200, .1);
		background: #F6F7F8;
		top: 0;
		bottom: 0;
		left: 200px;
		width: 200px;
		position: absolute;
		display: none;
		z-index: 99999;
		vh: 100;
	}

	.NavMenu .SubMenu li:hover ul {
		display: block
	}

	.NavMenu .SubMenu li:hover li ul {
		width: 320px;
		left: 200px;
		bottom: 0;
		top: 0;
		position: absolute;
		border-right: 1px solid rgba(120, 130, 150, .2);
		background: #FFF;
		box-shadow: 0 0 10px rgba(0, 0, 0, .1);
		display: none
	}

	.NavMenu .SubMenu li li:hover ul {
		display: block;
	}

	.icoRing {
		background-image: url('../../assets/images/ico_ring.svg');
	}

	.icoFullScreen {
		background-image: url('../../assets/images/ico_fullscreen.svg');
	}

	.icoSkin {
		background-image: url('../../assets/images/ico_magic.svg');
	}

	.FadeIn {
		/*出现*/
		animation: fadeIn 0.2s;
	}

	.ruleActive {
		border-left: 5px solid #D7063B;
		color: #D7063B !important;
		background-color: rgba(239, 224, 228, .2) !important;
	}

	.ruleActive>span {
		color: #D7063B !important;
	}

	@keyframes fadeIn {
		0% {
			transform: translate3d(-100%, 0, 0)
		}

		to {
			transform: none
		}
	}

	@keyframes fadeInX {
		0% {
			transform: translate3d(0, 10%, 0)
		}

		to {
			transform: none
		}
	}

	@keyframes fadeIn2 {
		50% {
			transform: translate3d(-10%, 0, 0);
		}

		to {
			transform: none;
		}
	}
</style>
