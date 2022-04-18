<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>人员管理</el-breadcrumb-item>
				<el-breadcrumb-item>管理员管理</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<el-container>
				<el-header style="text-align: center">
					<el-input v-model="keyword" placeholder="请输入关键字" class="handle-input" @keyup.enter.native="handleSearch"></el-input>
					<el-button type="primary" size="medium" icon="el-icon-search" @click="handleSearch">
						搜索
					</el-button>
					<el-button type="success" size="medium" @click="handleAddAdmin">
						添加管理员
					</el-button>
					<el-button type="danger" size="medium" @click="handleDeleteAdmin">
						批量删除
					</el-button>
				</el-header>
				<el-main>
					<el-table
						:data="adminList.slice((page-1)*pageSize,page*pageSize)"
						border
						class="table"
						:default-sort="{prop: 'username', order: 'ascending'}"
						@sort-change="sortChange"
						@selection-change="handleSelectionChange"
					>
						<el-table-column
							type="selection"
						>
						</el-table-column>
						<el-table-column
							label="名称"
							header-align="center"
							align="center"
							prop="username"
							sortable
						>
						</el-table-column>
						<el-table-column
							label="权限列表"
							header-align="center"
							align="center"
						>
							<template slot-scope="scope">
								<el-tag
									:key="tag"
									v-for="tag in scope.row.devices"
									type="success"
									effect="dark"
									style="margin: auto 5px"
								>
									{{ tag }}
								</el-tag>
							</template>
						</el-table-column>
						<el-table-column
							label="操作"
							header-align="center"
							align="center"
						>
							<template slot-scope="scope">
								<el-button
									size="mini"
									type="primary"
									@click="handleAuthority(scope.$index, scope.row)">
									权限设置
								</el-button>
								<el-button
									size="mini"
									type="danger"
									@click="handleDelete(scope.$index, scope.row)">
									删除
								</el-button>
							</template>
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
							:total="adminList.length"
							@current-change="handlePageChange"
						></el-pagination>
					</div>
				</el-footer>
			</el-container>
		</div>

		<!-- 弹出框 -->
		<el-dialog title="权限设置" :visible.sync="editVisible" width="30%">
			<el-form ref="form" :model="form" label-width="70px">
				<el-form-item label="改变设备权限">
					<el-checkbox :indeterminate="isIndeterminate1" v-model="checkAll1" @change="handleCheckAllChange1">
						全选
					</el-checkbox>
					<div style="margin: 15px 0;"></div>
					<el-checkbox-group v-model="form.devices" @change="handleCheckedDevicesChange1">
						<el-checkbox v-for="device in allDevices" :label="device" :key="device">{{ device }}
						</el-checkbox>
					</el-checkbox-group>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="noChangeAuthority">取消</el-button>
					<el-button type="primary" @click="changeAuthority">确定</el-button>
				</span>
			</template>
		</el-dialog>

		<!-- 添加管理员弹出框 -->
		<el-dialog title="添加管理员" :visible.sync="addVisible" width="30%">
			<el-form ref="form" :model="admin" label-width="70px" style="width: 80%;margin-left: 10%">
				<el-form-item label="用户名">
					<el-input v-model="admin.username"></el-input>
				</el-form-item>
				<el-form-item label="设备权限">
					<el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选
					</el-checkbox>
					<div style="margin: 15px 0;"></div>
					<el-checkbox-group v-model="admin.devices" @change="handleCheckedDevicesChange">
						<el-checkbox v-for="device in allDevices" :label="device" :key="device">{{ device }}
						</el-checkbox>
					</el-checkbox-group>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addVisible = false">取消</el-button>
					<el-button type="primary" @click="addAdmin">添加</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script>
export default {
	name: "AdminManager",
	data() {
		return {
			page: 1,
			pageSize: 8,
			adminList: [],
			allDevices: this.$store.state.devices,
			keyword: '',

			editVisible: false,
			index: null,
			form: {
				username: '',
				devices: []
			},
			checkAll1: false,
			isIndeterminate1: true,

			addVisible: false,
			admin: {
				username: '',
				devices: []
			},
			checkAll: false,
			isIndeterminate: true,
			multipleSelection: []
		};
	},
	mounted() {
		this.getData();
	},
	methods: {
		/* 获取users */
		getData() {
			this.$axios
				.get("/admin/getAllAdmin")
				.then(resp => {
					if (resp && resp.status === 200) {
						this.adminList = resp.data;
					}
				})
		},
		// 排序操作
		sortChange(column) {
			let prop = column.prop;
			let order = column.order;
			this.adminList.sort((a,b) => {
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
		// 模糊搜索操作
		handleSearch() {
			var url = "/admin/findByKeyword?keyword=" + this.keyword;
			this.$axios
				.get(url)
				.then(resp => {
					if (resp && resp.status === 200) {
						this.adminList = resp.data;
					}
				})
		},
		// 添加管理员操作
		handleAddAdmin() {
			this.addVisible = true;
		},
		// 全选
		handleCheckAllChange(val) {
			this.admin.devices = val ? this.allDevices : [];
			this.isIndeterminate = false;
		},
		// 多选框改变
		handleCheckedDevicesChange(value) {
			let checkedCount = value.length;
			this.checkAll = checkedCount === this.allDevices.length;
			this.isIndeterminate = checkedCount > 0 && checkedCount < this.allDevices.length;
		},
		// 添加管理员
		addAdmin() {
			this.$axios
				.post("/admin/addAdmin", this.admin)
				.then(resp => {
					if (resp && resp.data.code === 200) {
						this.getData();
						this.addVisible = false;
						this.$message.success("添加管理员以及其权限成功!");
					} else {
						this.$message.error(resp.data.message);
					}
				})
		},
		// 批量删除管理员
		handleDeleteAdmin() {
			var rows = this.multipleSelection;
			console.log(rows);
			if (rows.length !== 0) {
				this.$confirm("确定要批量删除吗？", "警告", {
					type: "error"
				}).then(() => {
					rows.forEach(row => {
						this.$axios
							.post("admin/deleteAdmin", {
								"username": row.username
							})
							.then(resp => {
								if (resp && resp.data.code === 200) {
									this.adminList.splice(index, 1);
								} else {
									this.$message.error("删除失败!" + resp.data.message);
								}
							})
							.catch(error => {
								console.log(error);
							})
					})
					this.$message.success("批量删除成功!");
				}).catch(() => {
				})
			} else {
				this.$message.info("没有选中任何行!")
			}
		},
		// 删除操作
		handleDelete(index, row) {
			this.form = row
			// 二次确认删除
			this.$confirm("确定要删除吗？", "提示", {
				type: "warning"
			})
				.then(() => {
					this.$axios
						.post("admin/deleteAdmin", {
							"username": this.form.username
						})
						.then(resp => {
							if (resp && resp.data.code === 200) {
								this.$message.success("删除成功");
								this.adminList.splice(index, 1);
							} else {
								this.$message.error("删除失败!" + resp.data.message);
							}
						})
				})
				.catch(() => {
				});
		},
		// 权限设置
		handleAuthority(index, row) {
			this.index = index;
			this.form = row;
			this.editVisible = true;
		},
		// 全选
		handleCheckAllChange1(val) {
			this.form.devices = val ? this.allDevices : [];
			this.isIndeterminate1 = false;
		},
		// 多选框改变
		handleCheckedDevicesChange1(value) {
			let checkedCount = value.length;
			this.checkAll1 = checkedCount === this.allDevices.length;
			this.isIndeterminate1 = checkedCount > 0 && checkedCount < this.allDevices.length;
		},
		// 权限改变
		changeAuthority() {
			this.$axios
				.post("/admin/changeAuthority", this.form)
				.then(resp => {
					if (resp && resp.data.code === 200) {
						this.$message.success(`修改第 ${this.index + 1} 行的权限信息成功`);
						this.$set(this.adminList, this.index, this.form);
					} else {
						this.$message.error('修改第 ${this.index+1}行的权限信息失败' + resp.data.message);
						this.getData();
					}
				})
				.catch(error => {
					console.log(error);
				})
			this.editVisible = false;
		},
		noChangeAuthority() {
			this.editVisible = false;
			this.getData();
		},
		// 多选改变
		handleSelectionChange(val) {
			this.multipleSelection = val;
		},
		// 分页导航
		handlePageChange(page) {
			this.page = page;
		}
	}
}
</script>

<style scoped>
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
