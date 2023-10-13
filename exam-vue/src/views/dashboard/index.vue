<template>
  <div id = "data-analysis">

    <el-row :gutter="20" type="flex" style = "margin-top: 10px">
      <el-col :span="6" :offset="3">
        <el-card class="box-card">
          <h3>本月考试人数</h3>
          <div>
            <i class="el-icon-s-shop" style="color: purple"></i>
            <span>2424132</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <h3>本月新增考试课程数</h3>
          <div>
            <i class="el-icon-s-shop" style="color: purple"></i>
            <span>2424132</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <h3>本月考试通过人数</h3>
          <div>
            <i class="el-icon-s-shop" style="color: purple"></i>
            <span>2424132</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" type="flex">
      <el-col :span = "15">
        <el-card>
          <div slot="header"><span><i class="el-icon-pie-chart"></i> 每日考试人数统计</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="pieChart" style="height: 400px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span = "9">
        <el-card>
          <div slot="header"><span><i class="el-icon-stopwatch"></i> 每日新增考试课程数</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="lineChart" style="height: 200px" />
          </div>
        </el-card>

        <el-card>
          <div slot="header"><span><i class="el-icon-odometer"></i> 每日考试通过人数</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="raddarChart" style="height: 200px" />
          </div>
        </el-card>
      </el-col>

    </el-row>
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
        echartDate: []
      }
    },

    methods: {
      /** 查缓存询信息 */
      getList() {
        getChartDate().then((response) => {
          this.echartDate = response.data;

          this.pieChart = echarts.init(this.$refs.pieChart, "macarons");
          this.pieChart.setOption({
            tooltip: {
              trigger: "item",
              formatter: "{a} <br/>{b} : {c} ({d}%)",
            },
            series: [
              {
                name: "人数",
                type: "pie",
                roseType: "radius",
                radius: [15, 95],
                center: ["50%", "38%"],
                data: response.data.pieDate,
                animationEasing: "cubicInOut",
                animationDuration: 1000,
              }
            ]
          });

          this.lineChart = echarts.init(this.$refs.lineChart, "macarons");
          this.lineChart.setOption({
            xAxis: {
              type: 'category',
              data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
              type: 'value'
            },

            series: [
              {
                data: [
                  {
                    data: this.echartDate.lineDate,
                    type: 'line',
                    smooth: true,
                    name: "人数"
                  }
                ]
              }
            ]
          });
          this.raddarChart = echarts.init(this.$refs.raddarChart, "macarons");
          this.raddarChart.setOption({
            tooltip: {
              formatter: "{b} <br/>{a} : " + this.cache.info.used_memory_human,
            },
            series: [
              {
                name: "峰值",
                type: "gauge",
                min: 0,
                max: 1000,
                detail: {
                  formatter: this.cache.info.used_memory_human,
                },
                data: [
                  {
                    value: parseFloat(this.cache.info.used_memory_human),
                    name: "内存消耗",
                  }
                ]
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
    margin-bottom: 20px;
    text-align: center;
  }

</style>
