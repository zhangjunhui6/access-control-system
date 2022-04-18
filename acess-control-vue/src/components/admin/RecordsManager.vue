<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>门禁管理</el-breadcrumb-item>
				<el-breadcrumb-item>人员出入记录表</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<el-container>
				<el-header style="text-align: center">
					<el-input v-model="keyword" placeholder="请输入关键字" class="handle-input" @keyup.enter.native="handleSearch"></el-input>
					<el-button type="primary" size="medium" icon="el-icon-search" @click="handleSearch">
						搜索
					</el-button>
					<el-button type="danger" size="medium" @click="handleDeleteRecord">
						批量删除
					</el-button>
				</el-header>
				<el-main>
					<el-table
						:data="recordList.slice((page-1)*pageSize,page*pageSize)"
						border
						class="table"
						@sort-change="sortChange"
						:default-sort="{prop: 'id', order: 'ascending'}"
						@selection-change="handleSelectionChange"
					>
						<el-table-column type="selection"></el-table-column>
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
								<el-tag v-if="scope.row.temp >= 37.3" type="danger">
									{{scope.row.temp}}
								</el-tag>
								<el-tag v-else type="success">
									{{scope.row.temp}}
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
						<el-table-column
							label="操作"
							header-align="center"
							align="center"
						>
							<template slot-scope="scope">
								<el-button
									size="mini"
									type="danger"
									@click="handleDelete(scope.$index, scope.row)"
								>
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
							:total="recordList.length"
							@current-change="handlePageChange"
						>
						</el-pagination>
					</div>
				</el-footer>
			</el-container>
		</div>
	</div>
</template>

<script>
export default {
	name: "RecordsManager",
	data() {
		return {
			page: 1,
			pageSize: 8,
			recordList: [],
			keyword: '',
			multipleSelection: [],
			deviceFilters: []
		};
	},
	mounted() {
		this.getData();
		let deviceIds = this.$store.state.devices;
		deviceIds.forEach(device => {
			this.deviceFilters.push({text: device,value: device});
		})
	},
	methods: {
		/* 获取records */
		getData() {
			this.$axios
				.get("/record/getAllRecords")
				.then(resp => {
					if (resp && resp.status === 200) {
						this.recordList = resp.data;
					}
				})
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
		// 模糊搜索操作
		handleSearch() {
			let url = "/record/findByKeyword?keyword=" + this.keyword;
			this.$axios
				.get(url)
				.then(resp => {
					if (resp && resp.status === 200) {
						this.recordList = resp.data;
					}
				})
		},
		// 批量删除记录
		handleDeleteRecord() {
			let rows = this.multipleSelection;
			if (rows.length !== 0) {
				this.$confirm("确定要批量删除吗？", "警告", {
					type: "error"
				}).then(() => {
					rows.forEach(row => {
						this.$axios
							.post("/record/deleteRecord", {
								"id": row.id
							})
							.then(resp => {
								if (resp && resp.data.code !== 200) {
									this.$message.error("删除失败!" + resp.data.message);
								}
							})
							.catch(error => {
								console.log(error);
							})
					})
					this.$message.success("批量删除成功!");
					this.recordList = this.getData();
				}).catch(() => {
				})
			} else {
				this.$message.info("没有选中任何行!")
			}

		},
		// 删除操作
		handleDelete(index, row) {
			// 二次确认删除
			this.$confirm("确定要删除吗？", "提示", {
				type: "warning"
			})
				.then(() => {
					this.$axios
						.post("/record/deleteRecord", {
							"id": row.id,
						})
						.then(resp => {
							if (resp && resp.data.code === 200) {
								this.$message.success("删除成功");
								this.recordList.splice(index, 1);
							} else {
								this.$message.error("删除失败!" + resp.data.message);
							}
						})
				})
				.catch(() => {
				});
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
