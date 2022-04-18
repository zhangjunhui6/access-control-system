<template>
	<div class="login-wrap">
		<div class="ms-login">
			<div class="ms-title">门禁管理系统</div>
			<el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
				<el-form-item prop="username">
					<el-input type="text" v-model="param.username" placeholder="用户名">
						<template #prepend>
							<el-button icon="el-icon-user"></el-button>
						</template>
					</el-input>
				</el-form-item>
				<el-form-item prop="password">
					<el-input type="password" v-model="param.password" placeholder="密码"
					          @keyup.enter.native="submitForm()">
						<template #prepend>
							<el-button icon="el-icon-lock"></el-button>
						</template>
					</el-input>
				</el-form-item>
				<div class="login-btn">
					<el-button type="primary" @click="submitForm()">登录</el-button>
				</div>
			</el-form>
		</div>
	</div>
</template>

<script>
export default {
	name: "Login",
	data() {
		return {
			param: {
				username: "",
				password: ""
			},
			rules: {
				username: [
					{required: true, message: "请输入用户名", trigger: "blur"}
				],
				password: [
					{required: true, message: "请输入密码", trigger: "blur"}
				]
			}
		};
	},
	/*created() {
	  this.$store.commit("clearTags");
	},*/
	methods: {
		submitForm() {
			let username = this.param.username;
			let password = this.param.password;
			this.$axios
				.post('/admin/login', {
					username: username,
					password: password
				})
				.then(response => {
					if (response.data.code === 200) {
						/* 将用户名存入Vuex的store中*/
						let devices = response.data.data;
						this.$store.commit('login', username);
						this.$store.commit('setDevices', devices);
						/* 判断身份，进入对应的页面中 */
						if (username === "admin"){
							this.$router.replace("/home");
						} else {
							this.$router.replace("/manager")
						}
					} else {
						this.$message.error(response.data.message);
					}
				})
				.catch(error => {
				})
		}
	}
}
</script>

<style scoped>
.login-wrap {
	position: relative;
	width: 100%;
	height: 100%;
	background-image: url(../assets/login-bg.jpg);
	background-size: 100%;
}

.ms-title {
	width: 100%;
	line-height: 50px;
	text-align: center;
	font-size: 20px;
	color: #fff;
	border-bottom: 1px solid #ddd;
}

.ms-login {
	position: absolute;
	left: 50%;
	top: 50%;
	width: 350px;
	margin: -190px 0 0 -175px;
	border-radius: 5px;
	background: rgba(255, 255, 255, 0.3);
	overflow: hidden;
}

.ms-content {
	padding: 30px 30px;
}

.login-btn {
	text-align: center;
}

.login-btn button {
	width: 100%;
	height: 36px;
	margin-bottom: 10px;
}

.login-tips {
	font-size: 12px;
	line-height: 30px;
	color: #fff;
}
</style>
