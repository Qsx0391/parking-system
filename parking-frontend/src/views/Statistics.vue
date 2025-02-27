<template>
  <div>
    <n-space vertical>
      <n-grid :cols="4" :x-gap="12">
        <n-grid-item>
          <n-card>
            <n-statistic label="今日收入" :value="todayIncome">
              <template #prefix>
                ¥
              </template>
            </n-statistic>
          </n-card>
        </n-grid-item>
        <n-grid-item>
          <n-card>
            <n-statistic label="今日车流量" :value="todayTraffic" />
          </n-card>
        </n-grid-item>
        <n-grid-item>
          <n-card>
            <n-statistic label="车位使用率" :value="parkingUsage">
              <template #suffix>
                %
              </template>
            </n-statistic>
          </n-card>
        </n-grid-item>
        <n-grid-item>
          <n-card>
            <n-statistic label="会员总数" :value="memberCount" />
          </n-card>
        </n-grid-item>
      </n-grid>

      <n-card title="收入统计">
        <div ref="incomeChartRef" style="width: 100%; height: 300px"></div>
      </n-card>

      <n-card title="车流量分析">
        <div ref="trafficChartRef" style="width: 100%; height: 300px"></div>
      </n-card>
    </n-space>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { 
  NSpace, 
  NCard, 
  NGrid,
  NGridItem,
  NStatistic
} from 'naive-ui'
import * as echarts from 'echarts'

const incomeChartRef = ref(null)
const trafficChartRef = ref(null)

const todayIncome = ref(2358.00)
const todayTraffic = ref(156)
const parkingUsage = ref(75.5)
const memberCount = ref(328)

onMounted(() => {
  // 收入统计图表
  const incomeChart = echarts.init(incomeChartRef.value)
  incomeChart.setOption({
    title: {
      text: '近7天收入统计'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['3-14', '3-15', '3-16', '3-17', '3-18', '3-19', '3-20']
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: [1820, 1932, 2101, 1834, 1890, 2300, 2358],
      type: 'line',
      smooth: true
    }]
  })

  // 车流量分析图表
  const trafficChart = echarts.init(trafficChartRef.value)
  trafficChart.setOption({
    title: {
      text: '今日车流量分布'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['00:00', '03:00', '06:00', '09:00', '12:00', '15:00', '18:00', '21:00']
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: [5, 3, 12, 35, 28, 30, 42, 15],
      type: 'bar'
    }]
  })
})
</script> 