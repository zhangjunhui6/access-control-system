<template>
	<div class="sidebar">
		<el-menu
			class="sidebar-el-menu"
			:default-active="onRoutes"
			:collapse="collapse"
			background-color="#324157"
			text-color="#bfcbd9"
			active-text-color="#20a0ff"
			unique-opened
			router
		>
			<template v-for="item in items">
				<template v-if="item.subs">
					<el-submenu :index="item.index" :key="item.index">
						<template #title>
							<i :class="item.icon"></i>
							<span>{{ item.title }}</span>
						</template>
						<template v-for="subItem in item.subs">
							<el-menu-item
								:index="subItem.index"
								:key="subItem.index"
							>{{ subItem.title }}
							</el-menu-item>
						</template>
					</el-submenu>
				</template>
				<template v-else>
					<el-menu-item :index="item.index" :key="item.index">
						<i :class="item.icon"></i>
						<template #title>{{ item.title }}</template>
					</el-menu-item>
				</template>
			</template>
		</el-menu>
	</div>
</template>

<script>

export default {
	data() {
		return {
			items: [
				{
					icon: "el-icon-house",
					index: "index",
					title: "主页"
				},
				{
					icon: "el-icon-house",
					index: "access",
					title: "门禁管理"
				},
				{
					icon: "el-icon-video-camera",
					index: "device",
					title: "设备管理"
				},
				{
					icon: "el-icon-user",
					index: "3",
					title: "人员管理",
					subs: [
						{
							index: "user",
							title: "用户管理"
						},
						{
							index: "admin",
							title: "管理员管理"
						}
					]
				}
			]
		};
	},
	computed: {
		onRoutes() {
			return this.$route.path.replace("/", "");
		},
		collapse() {
			return this.$store.state.collapse
		}
	}
};
</script>

<style scoped>
.sidebar {
	display: block;
	position: absolute;
	left: 0;
	top: 70px;
	bottom: 0;
	overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
	width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
	width: 200px;
}

.sidebar > ul {
	height: 100%;
}
</style>
