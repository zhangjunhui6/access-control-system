<template>
	<div class="header">
		<!-- 折叠按钮 -->
		<div class="collapse-btn" @click="collapseChange">
			<i v-if="!collapse" class="el-icon-s-fold"></i>
			<i v-else class="el-icon-s-unfold"></i>
		</div>
		<!-- LOGO -->
		<div class="logo">门禁管理系统</div>
		<!-- 右上侧   -->
		<div class="header-right">
			<div class="header-user-con">
				<!-- 用户头像 -->
				<div class="user-avatar">
					<img src="../../assets/img.jpg" alt=""/>
				</div>
				<!-- 用户名下拉菜单 -->
				<el-dropdown class="user-name" trigger="click" @command="handleCommand">
					<span class="el-dropdown-link">
						{{ username }}
						<i class="el-icon-caret-bottom"></i>
					</span>
					<template #dropdown>
						<el-dropdown-menu>
							<el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
						</el-dropdown-menu>
					</template>
				</el-dropdown>
			</div>
		</div>
	</div>
</template>
<script>
export default {
	data() {
		return {
			name: "admin",
		};
	},
	computed: {
		username() {
			return this.$store.state.username;
		},
		collapse() {
			return this.$store.state.collapse;
		}
	},
	methods: {
		// 用户名下拉菜单选择事件
		handleCommand(command) {
			if (command === "logout") {
				this.$store.commit('logout');
				this.$router.push("/login");
			}
		},
		// 侧边栏折叠
		collapseChange() {
			this.$store.commit("handleCollapse", !this.collapse);
		}
	},
	mounted() {
		if (document.body.clientWidth < 1500) {
			this.collapseChange();
		}
	}
};
</script>
<style scoped>
.header {
	position: relative;
	box-sizing: border-box;
	width: 100%;
	height: 70px;
	font-size: 22px;
	color: #fff;
}

.collapse-btn {
	float: left;
	padding: 0 21px;
	cursor: pointer;
	line-height: 70px;
}

.header .logo {
	float: left;
	width: 250px;
	line-height: 70px;
}

.header-right {
	float: right;
	padding-right: 50px;
}

.header-user-con {
	display: flex;
	height: 70px;
	align-items: center;
}

.btn-bell .el-icon-bell {
	color: #fff;
}

.user-name {
	margin-left: 10px;
}

.user-avatar {
	margin-left: 20px;
}

.user-avatar img {
	display: block;
	width: 40px;
	height: 40px;
	border-radius: 50%;
}

.el-dropdown-link {
	color: #fff;
	cursor: pointer;
}
</style>
