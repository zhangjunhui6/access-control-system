<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>门禁设备管理</el-breadcrumb-item>
				<el-breadcrumb-item>设备列表</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<el-container>
				<el-header style="text-align: center">
					<el-input v-model="keyword" placeholder="请输入关键字" class="handle-input" @keyup.enter.native="handleSearch"></el-input>
					<el-button type="primary" size="medium" icon="el-icon-search" @click="handleSearch">
						搜索
					</el-button>
					<el-button type="success" size="medium"
					           icon="el-icon-plus" @click="handleAdd">
						添加设备
					</el-button>
				</el-header>
				<el-main>
					<el-table
						:data="deviceList.slice((page-1)*pageSize,page*pageSize)"
						border
						class="table"
						@sort-change="sortChange"
						:default-sort="{prop: 'id', order: 'ascending'}"
					>
						<el-table-column
							label="ID"
							header-align="center"
							align="center"
							width="100px"
							prop="id"
							sortable
						>
						</el-table-column>
						<el-table-column
							label="名称"
							header-align="center"
							align="center"
							prop="name"
							sortable
						>
						</el-table-column>
						<el-table-column
							label="所在位置"
							header-align="center"
							align="center"
							prop="location"
						>
						</el-table-column>
						<el-table-column
							label="状态"
							header-align="center"
							align="center"
							width="125px"
							prop="state"
							sortable
							:filters="[{text:'run',value:'run'},{text:'stop',value:'stop'}]"
							:filter-method="stateFilter"
						>
						</el-table-column>
						<el-table-column
							label="操作"
							header-align="center"
							align="center"
							width="250px"
						>
							<template slot-scope="scope">
								<el-button
									size="mini"
									type="info"
									@click="handleEdit(scope.$index, scope.row)">
									编辑
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
							:total="deviceList.length"
							@current-change="handlePageChange"
						></el-pagination>
					</div>
				</el-footer>
			</el-container>
		</div>
		<!-- 编辑弹出框 -->
		<el-dialog title="编辑" :visible.sync="editVisible" width="30%">
			<el-form ref="form" :model="form" label-width="70px">
				<el-form-item label="位置信息">
					<el-input v-model="form.location"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
							<span class="dialog-footer">
								<el-button @click="editVisible = false">取消</el-button>
								<el-button type="primary" @click="saveEdit">确定</el-button>
							</span>
			</template>
		</el-dialog>

		<!-- 添加设备弹出框 -->
		<el-dialog title="添加设备" :visible.sync="addVisible" width="30%">
			<el-form ref="form" :model="device" label-width="70px" style="width: 80%;margin-left: 10%">
				<el-form-item label="设备名称">
					<el-input v-model="device.name"></el-input>
				</el-form-item>
				<el-form-item label="所在位置">
					<el-input v-model="device.location"></el-input>
				</el-form-item>
				<el-form-item label="状态">
					<el-input v-model="device.state" disabled></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="addVisible = false">取消</el-button>
					<el-button type="primary" @click="saveAdd">添加</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script>

export default {
	name: "deviceManage",
	data() {
		return {
			deviceList: [],
			keyword: '',
			page: 1,
			pageSize: 8,
			index: 0,
			editVisible: false,
			form: {
				id: 0,
				name: '',
				location: '',
				state: ''
			},
			addVisible: false,
			device: {
				id: null,
				name: '',
				location: '',
				state: 'stop'
			}
		};
	},
	mounted() {
		this.getData()
	},
	methods: {
		/* 获取devices */
		getData() {
			this.$axios
				.get("/device/getAllDevices")
				.then(resp => {
					if (resp && resp.status === 200) {
						this.deviceList = resp.data;
					}
				})
		},
		// 排序操作
		sortChange(column) {
			let prop = column.prop;
			let order = column.order;
			this.deviceList.sort((a,b) => {
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
		stateFilter(value,row){
			return row.state === value;
		},
		// 搜索操作
		handleSearch() {
			var url = "/device/findByKeyword?keyword=" + this.keyword;
			this.$axios
			.get(url)
			.then(resp => {
				if (resp && resp.status === 200) {
					this.deviceList = resp.data;
				}
			})
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
						.post("device/deleteDevice", {
							"id": this.form.id,
							"name": this.form.name,
							"location": this.form.location,
							"state": this.form.state
						})
						.then(resp => {
							if (resp && resp.data.code === 200) {
								this.$message.success("删除成功");
								this.deviceList.splice(index, 1);
							} else {
								this.$message.error("删除失败!" + resp.data.message);
							}
						})
				})
				.catch(() => {
				});
		},
		// 编辑操作
		handleEdit(index, row) {
			this.index = index;
			this.form = row;
			this.editVisible = true;
		},
		// 保存编辑
		saveEdit() {
			this.$axios
				.post("/device/changeLocation", {
					id: this.form.id,
					location: this.form.location
				})
				.then(resp => {
					if (resp && resp.data.code === 200) {
						this.$message.success(`修改第 ${this.index + 1} 行的位置信息成功`);
						this.$set(this.deviceList, this.index, this.form);
					} else {
						this.$message.error('修改第 ${this.index+1}行的位置信息失败' + resp.data.message);
					}
				})
				.catch(error => {
					console.log(error);
				})
			this.editVisible = false;
		},
		// 添加设备操作
		handleAdd(){
			this.addVisible = true;
		},
		// 保存添加
		saveAdd(){
			this.$axios
			.post("/device/addDevice", this.device)
			.then(resp => {
				if (resp && resp.data.code === 200){
					this.getData();
					this.addVisible = false;
					this.$message.success("添加设备成功!");
				} else {
					this.$message.error("添加设备失败!" + resp.data.message);
				}
			})
		},
		// 分页导航
		handlePageChange(page) {
			this.page = page;
		},

		// 多选操作
		handleSelectionChange(val) {
			this.multipleSelection = val;
		},
		delAllSelection() {
			const length = this.multipleSelection.length;
			let str = "";
			this.delList = this.delList.concat(this.multipleSelection);
			for (let i = 0; i < length; i++) {
				str += this.multipleSelection[i].name + " ";
			}
			this.$message.error(`删除了${str}`);
			this.multipleSelection = [];
		}
	}
};
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
