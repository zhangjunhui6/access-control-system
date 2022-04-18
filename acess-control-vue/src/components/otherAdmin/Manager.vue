<template>
	<el-container>
		<el-header class="header">
			<div class="logo">门禁管理系统</div>
			<div class="header-right">
				<div class="header-user-con">
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
								<el-dropdown-item divided command="changePassword">修改密码</el-dropdown-item>
								<el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</div>
				<!-- 编辑弹出框 -->
				<el-dialog title="修改密码" :visible.sync="editVisible" width="30%">
					<el-form :model="form" label-width="70px" style="width: 70%;margin-left: 15%">
						<el-form-item label="旧密码">
							<el-input type="password" v-model="form.oldPassword"></el-input>
						</el-form-item>
						<el-form-item label="新密码">
							<el-input type="password" v-model="form.newPassword"></el-input>
						</el-form-item>
						<el-form-item label="确认密码">
							<el-input type="password" v-model="form.confirmPassword"></el-input>
						</el-form-item>
					</el-form>
					<template #footer>
							<span class="dialog-footer">
								<el-button @click="editVisible = false;">取消</el-button>
								<el-button type="primary" @click="changePassword">确定</el-button>
							</span>
					</template>
				</el-dialog>
			</div>
		</el-header>
		<el-main>
			<div class="container">
				<el-container>
					<el-header style="text-align: center">
						<el-input v-model="keyword" placeholder="请输入关键字" class="handle-input" @keyup.enter.native="handleSearch"></el-input>
						<el-button type="primary" size="medium" icon="el-icon-search" @click="handleSearch">
							搜索
						</el-button>
					</el-header>
					<el-main>
						<el-table
							:data="recordList.slice((page-1)*pageSize,page*pageSize)"
							border
							class="table"
							:default-sort="{prop: 'id', order: 'ascending'}"
							@sort-change="sortChange"
						>
							<el-table-column
								label="ID"
								header-align="center"
								align="center"
								prop="id"
								sortable
							>
							</el-table-column>
							<el-table-column
								label="用户ID"
								header-align="center"
								align="center"
								prop="uid"
								sortable
							>
							</el-table-column>
							<el-table-column
								label="用户名"
								header-align="center"
								align="center"
								prop="userName"
								sortable
							>
							</el-table-column>
							<el-table-column
								label="设备ID"
								header-align="center"
								align="center"
								prop="did"
								sortable
								:filters="deviceFilters"
								:filter-method="deviceFilter"
							>
							</el-table-column>
							<el-table-column
								label="设备名称"
								header-align="center"
								align="center"
								prop="deviceName"
								sortable
							>
							</el-table-column>
							<el-table-column
								label="体温信息"
								header-align="center"
								align="center"
								prop="temp"
								sortable
								:filters="[{text:'异常',value:'异常'},{text:'正常',value:'正常'}]"
								:filter-method="tempFilter"
							>
								<template slot-scope="scope">
									<el-tag v-if="scope.row.temp > 30.3" type="danger">
										{{ scope.row.temp }}
									</el-tag>
									<el-tag v-else type="success">
										{{ scope.row.temp }}
									</el-tag>
								</template>
							</el-table-column>
							<el-table-column
								label="时间"
								header-align="center"
								align="center"
								prop="time"
							>
							</el-table-column>
						</el-table>
					</el-main>
					<el-footer>
						<div class="pagination">
							<el-pagination
								background
								layout="total, prev, pager, next"
								:current-page="page"
								:page-size="pageSize"
								:total="recordList.length"
								@current-change="handlePageChange"
							>
							</el-pagination>
						</div>
					</el-footer>
				</el-container>
			</div>
		</el-main>
	</el-container>
</template>

<script>
export default {
	name: "Manager",
	data() {
		return {
			name: "",
			page: 1,
			pageSize: 10,
			recordList: [],
			keyword: '',
			editVisible: false,
			form: {
				oldPassword: '',
				newPassword: '',
				confirmPassword: ''
			},
			deviceFilters: []
		}
	},
	computed: {
		username() {
			return this.$store.state.username;
		}
	},
	mounted() {
		this.getData();
		let deviceIds = this.$store.state.devices;
		deviceIds.forEach(device => {
			this.deviceFilters.push({text: device,value: device});
		})
	},
	methods: {
		// 用户名下拉菜单选择事件
		handleCommand(command) {
			if (command === "logout") {
				this.$store.commit('logout');
				this.$router.push("/login");
			} else if (command === "changePassword") {
				this.editVisible = true
				this.form = {}
			}
		},
		// 排序操作
		sortChange(column) {
			let prop = column.prop;
			let order = column.order;
			this.recordList.sort((a,b) => {
				let x = a[prop];
				let y = b[prop];
				if (order === "ascending"){
					return ((x<y)?-1:(x>y)?1:0);
				} else if (order === "descending") {
					return ((x<y)?1:(x>y)?-1:0);
				} else {
					return 0;
				}
			})
		},
		// 状态筛选
		tempFilter(value,row){
			if (value === "异常")
				return row.temp > 37.3;
			if (value === '正常')
				return row.temp <= 37.3;
		},
		// 设备ID号筛选
		deviceFilter(value,row){
			return row.did === value;
		},
		/* 获取records */
		getData() {
			this.$axios
				.post("/record/getRecords", {
					"devices": this.$store.state.devices
				})
				.then(resp => {
					if (resp && resp.status === 200) {
						this.recordList = resp.data;
					}
				})
				.catch(error => {
					console.log(error);
				})

			this.timeOut = setTimeout(() => {
				this.getData();
			}, 3000);

		},
		/* 模糊搜索 */
		handleSearch() {
			let url = "/record/findByKeyWordAndDevices?keyword=" + this.keyword;
			this.$axios
				.post(url, {
					"devices": this.$store.state.devices
				})
				.then(resp => {
					if (resp && resp.status === 200) {
						this.recordList = resp.data;
					}
				})
		},
		/* 分页导航 */
		handlePageChange(page) {
			this.page = page;
		},
		/* 修改密码 */
		changePassword() {
			let oldPassword = this.form.oldPassword;
			let newPassword = this.form.newPassword;
			let confirmPassword = this.form.confirmPassword;
			if (newPassword !== confirmPassword){
				this.$message.error("两次输入密码不一致!");
			} else {
				let url = "/admin/changePassword?username="+this.$store.state.username+
					"&oldPassword="+oldPassword+"&newPassword="+newPassword
				this.$axios
				.post(url)
				.then(resp => {
					if (resp && resp.data.code === 200) {
						this.$message.success("修改密码成功，需要重新登录!")
						this.$store.commit('logout');
						this.$router.push("/login");
					} else {
						this.$message.error(resp.data.message)
					}
				})
				.catch(error => {
					this.$message.error(error)
				})
			}
		}

	}
}
</script>

<style scoped>
.header {
	position: relative;
	box-sizing: border-box;
	width: 100%;
	height: 70px;
	font-size: 22px;
	color: #ffffff;
}

.header .logo {
	float: left;
	width: 250px;
	margin-left: 30px;
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
	color: #ffffff;
	cursor: pointer;
}

.handle-input {
	width: 250px;
	margin-right: 15px;
	display: inline-block;
}

.table {
	width: 90%;
	margin: auto 5%;
	font-size: 15px;
}
</style>
