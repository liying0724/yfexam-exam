<template>
  <div id = "data-analysis">

    <el-row :gutter="20" type="flex" style = "margin-top: 10px">
      <el-col :span="6" :offset="3">
        <el-card class="box-card" style="background: #0d90cb">
          <h3>本月考试人数</h3>
          <div>
            <i class="el-icon-s-custom" style="color: purple"></i>
            <span>{{examCount}}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="background: #b67b3d">
          <h3>本月新增考试课程数</h3>
          <div>
            <i class="el-icon-s-custom" style="color: purple"></i>
            <span>{{courseCount}}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card" style="background: #8cbda4">
          <h3>本月考试通过人数</h3>
          <div>
            <i class="el-icon-s-custom" style="color: purple"></i>
            <span>{{passExamCount}}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <!-- echarts图表 -->
      <div style="margin-left:2px">
        <!-- 折线图 -->
        <el-card style="height:260px">
          <!--<div slot="header"><span><i class="el-icon-pie-chart"></i> 每周考试通过人数</span></div>-->
          <div ref="lineChart" style="height:260px"></div>
        </el-card>
      </div>
    </el-row>
    <el-row>
      <div class="graph">
        <!-- 柱状图 -->
        <el-card style="height:220px">
          <!--<div slot="header"><span><i class="el-icon-pie-chart"></i> 每周考试最高分</span></div>-->
          <div ref="barChart" style="height:200px"></div>
        </el-card>
        <!-- 饼状图 -->
        <el-card style="height:220px">
          <!--<div slot="header"><span><i class="el-icon-odometer"></i> 每周考试人数统计</span></div>-->
          <div ref="pieChart" style="height:200px"></div>
        </el-card>
      </div>
    </el-row>

    <!--<el-row :gutter="20" type="flex">
      <el-col :span = "15">
        <el-card>
          <div slot="header"><span><i class="el-icon-pie-chart"></i> 每周考试人数统计</span></div>
          <div class="el-table el-table&#45;&#45;enable-row-hover el-table&#45;&#45;medium">
            <div ref="pieChart" style="height: 400px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span = "9">
        <el-card>
          <div slot="header"><span><i class="el-icon-stopwatch"></i> 每周考试通过人数</span></div>
          <div class="el-table el-table&#45;&#45;enable-row-hover el-table&#45;&#45;medium">
            <div ref="lineChart" style="height: 200px" />
          </div>
        </el-card>

        <el-card>
          <div slot="header"><span><i class="el-icon-odometer"></i> 每周考试最高分</span></div>
          <div class="el-table el-table&#45;&#45;enable-row-hover el-table&#45;&#45;medium">
            <div ref="raddarChart" style="height: 200px" />
          </div>
        </el-card>
      </el-col>

    </el-row>-->
  </div>

</template>
<script>
  import { getChartDate } from "@/api/chart/echart";
  import * as echarts from "echarts";
  export default {
    name: 'Dashboard',
    components: {

    },
    created() {
      this.getList();
    },
    data() {
      return {
        pieChart: null,
        lineChart: null,
        raddarChart: null,
        echartDate: [],
        examCount: 0,
        courseCount: 0,
        passExamCount: 0
      }
    },

    methods: {
      /** 查缓存询信息 */
      getList() {
        getChartDate().then((response) => {
          this.echartDate = response.data;
          let examCountList = this.echartDate.pieData;
          let passExamCountList = this.echartDate.lineData;
          for (let i = 0; i < examCountList.length; i++) {
            this.examCount += examCountList[i].value;
          }
          for (let i = 0; i < passExamCountList.length; i++) {
            this.passExamCount += passExamCountList[i];
          }
          this.courseCount = this.echartDate.paperCount;
          this.pieChart = echarts.init(this.$refs.pieChart, "macarons");
          this.pieChart.setOption({
            title: {
              text: '每周考试人数统计',
              left: 'right',
              textStyle: {
                fontSize: 14

              }
            },
            tooltip: {
              trigger: "item",
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [
              {
                name: "人数",
                type: "pie",
                roseType: "radius",
                radius: '50%',
                center: ["50%", "38%"],
                data: response.data.pieData,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                },
                animationEasing: "cubicInOut",
                animationDuration: 1000,
              },

            ]

          });
          this.lineChart = echarts.init(this.$refs.lineChart, "macarons");
          this.lineChart.setOption({
            title: {
              text: '每周考试通过人数',
              left: 'center'
            },
            xAxis: {
              type: 'category',
              data: ['第一周', '第二周', '第三周', '第四周', '第五周']
            },
            yAxis: {
              type: 'value',
            },
            grid: {
              left: 10,
              right: 10,
              bottom: 20,
              top: 30,
              containLabel: true
            },
            tooltip: {
              trigger: 'axis',

            },
            name: 'expected', itemStyle: {
              normal: {
                color: '#FF005A',
                lineStyle: {
                  color: '#FF005A',
                  width: 2
                }
              }
            },
            series: [
              {
                name: '人数',
                data: this.echartDate.lineData,
                type: 'line',
                animationEasing: 'cubicInOut'
              }
            ]

          });
          this.barChart = echarts.init(this.$refs.barChart, "macarons");
          this.barChart.setOption({
            title: {
              text: '每周考试最高分',
              x:'center',
              textAlign: null,

              zlevel: 0,
              z: 6,
              left: 'right',
              textStyle: {
                fontSize: 14
              }
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
              }
            },
            grid: {
              top: 10,
              left: '2%',
              right: '2%',
              bottom: '3%',
              containLabel: true
            },
            xAxis: [{
              type: 'category',
              data: ['第一周', '第二周', '第三周', '第四周', '第五周'],
              axisTick: {
                alignWithLabel: true
              }
            }],
            yAxis: [{
              type: 'value',
              axisTick: {
                show: false
              }
            }],
            series: [
              {
                name: '分数',
                type: 'bar',
                stack: 'vistors',
                barWidth: '60%',
                data: response.data.barData,
                animationDuration: 2800
              }
            ]
          });
        });
      },

    }
  }
</script>
<style lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .chart-wrapper {
      background: #4f6d74;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }

  .el-card{
    margin-bottom: 10px;
    text-align: center;
  }
  .graph {
    display: flex;
    // 两个靠边
    justify-content: space-between;
    margin-top: 20px;

    .el-card {
      width: 49%;
    }
  }

</style>
