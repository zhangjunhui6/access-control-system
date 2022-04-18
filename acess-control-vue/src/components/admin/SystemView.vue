<template>
<!-- 各台设备的访问量(饼状图和折线图)；最近一次的访问时间、访问用户、通过的设备；用户体温异常情况-->
	<el-container>
		<el-header>
			<el-row style="height: 380px">
				<el-col :span="12">
					<!-- 最近一次访问情况 -->
					<el-card shadow="hover" style="width: 600px;margin-left: 10%">
						<div slot="header"
						     style="text-align: left; font-size: 22px;font-weight: 500"
						>
							<i class="el-icon-time"></i>
							<span>最近一次访问记录</span>
						</div>
						<el-form :model="lastRecord" label-position="left" label-width="150px" style="margin: auto 10%;">
							<el-form-item label="用户名:">
								<span style="font-size: 20px">{{lastRecord.userName}}</span>
							</el-form-item>
							<el-form-item label="访问时间:">
								<span style="font-size: 20px">{{lastRecord.time}}</span>
							</el-form-item>
							<el-form-item label="通过的设备:">
								<span style="font-size: 20px">{{lastRecord.deviceName}}</span>
							</el-form-item>
							<el-form-item label="体温值:">
								<span style="font-size: 20px">{{lastRecord.temp}}℃</span>
							</el-form-item>
						</el-form>
					</el-card>
				</el-col>
				<el-col :span="12">
					<!-- 体温检测情况 -->
					<el-card shadow="hover" style="width: 600px;float: right;margin-right: 10%" >
						<div slot="header"
						     style="text-align: left; font-size: 22px;font-weight: 500"
						>
							<span>体温检测情况</span>
						</div>
						<div id="tempView" style=" width: 600px;height: 255px">

						</div>
					</el-card>
				</el-col>
			</el-row>
		</el-header>
		<el-main>
			<!-- 设备门禁记录情况 -->
			<div id = "recordView" style="margin-top:310px;margin-left:500px;width: 600px;height: 400px;background-color: white ">
			</div>
		</el-main>
	</el-container>

</template>

<script>
export default {
	name: "Index",
	data(){
		return {
			recordList: [],
			lastRecord: {
				userName:'',
				time: '',
				deviceName: '',
				temp: ''
			},
			less30: 0,
			mid: 0,
			more37: 0,
			devices: this.$store.state.devices,
			records: []
		}
	},
	methods:{
		getData() {
			this.$axios
				.get("/record/getAllRecords")
				.then(resp => {
					if (resp && resp.status === 200) {
						this.recordList = resp.data;
						this.lastRecord = this.recordList[this.recordList.length-1];
						this.devices = this.$store.state.devices;
						console.log(this.devices);
						this.less30 = 0;
						this.mid = 0;
						this.more37 = 0;
						for (let i=0;i<this.devices.length;i++){
							this.records[i] = 0;
						}
						this.recordList.forEach(record => {
							if (record.temp < 30){
								this.less30 = this.less30 + 1;
							} else if (record.temp > 37.3){
								this.more37 = this.more37 + 1;
							} else {
								this.mid = this.mid + 1;
							}
							let index = (this.devices).findIndex((device) => device === record.did);
							this.records[index] = this.records[index] + 1;
						})
						this.drawTempChart();
						console.log(this.records);
						this.drawRecordChart();
					}
				})
		},
		drawTempChart(){
			let tempChart = this.$echarts.init(document.getElementById("tempView"));
			console.log(this.less30)
			tempChart.setOption({
				series: [
					{
						name: '体温异常情况',
						type: 'pie',
						radius: ['30%','65%'],
						center:['50%','50%'],
						data:[
							{value: parseInt(this.less30),name: '小于30℃'},
							{value: parseInt(this.mid),name: '30-37.3℃'},
							{value: parseInt(this.more37),name: '大于37.3℃'}
						],
						labelLine: {
							normal: {
								show: false
							}
						},
						label: {
							normal: {
								position: 'inner',
								formatter: '{d}%'
							}
						}

					}
				],
				legend: {
					orient: 'vertical',
					x: 'left',
					y: 'center',
					itemWidth: 24,
					itemHeight: 18,
					data:['小于30℃', '30-37.3℃', '大于37.3℃']
				}
			})
		},
		drawRecordChart() {
			// 基于准备好的dom，初始化echarts实例
			let myChart = this.$echarts.init(document.getElementById("recordView"));
			// 指定图表的配置项和数据
			let option = {
				title: {
					text: "各设备门禁记录情况"
				},
				tooltip: {},
				legend: {
					data: ["记录数量"]
				},
				xAxis: {
					data: this.devices
				},
				yAxis: {},
				series: [
					{
						name: "记录数量",
						type: "bar",
						data: this.records
					}
				]
			};
			// 使用刚指定的配置项和数据显示图表。
			myChart.setOption(option);
		}
	},
	mounted() {
		this.getData();

		/*this.drawChart();*/
	}
}
</script>

<style scoped>
	.el-form-item__label{
		font-size: 20px;
	}
</style>
