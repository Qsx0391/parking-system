<template>
  <div>
    <n-card title="欢迎使用停车场管理系统">
      <n-space vertical>
        <n-grid :cols="4" :x-gap="12">
          <n-grid-item>
            <n-card>
              <n-statistic label="总车位" :value="parkingInfo.totalSpaces" />
            </n-card>
          </n-grid-item>
          <n-grid-item>
            <n-card>
              <n-statistic label="已停车辆" :value="parkingInfo.totalSpaces - parkingInfo.currentSpaces" />
            </n-card>
          </n-grid-item>
          <n-grid-item>
            <n-card>
              <n-statistic label="空余车位" :value="parkingInfo.currentSpaces" />
            </n-card>
          </n-grid-item>
          <n-grid-item>
            <n-card>
              <n-statistic label="使用率" :value="`${(((parkingInfo.totalSpaces - parkingInfo.currentSpaces) / parkingInfo.totalSpaces) * 100).toFixed(1)}%`" />
            </n-card>
          </n-grid-item>
        </n-grid>
        
        <n-card title="收费标准">
          <n-descriptions 
            bordered 
            :column="4"
            :label-style="{ width: '100px', 'text-align': 'center' }"
            :content-style="{ 'text-align': 'center' }"
          >
            <n-descriptions-item label="免费停车时长">
              {{ parkingInfo.freeTime }}分钟
            </n-descriptions-item>
            <n-descriptions-item label="计费单位">
              {{ parkingInfo.billingUnit }}分钟
            </n-descriptions-item>
            <n-descriptions-item label="单价">
              {{ parkingInfo.unitPrice }}元
            </n-descriptions-item>
            <n-descriptions-item label="封顶价格">
              {{ parkingInfo.maxPrice }}元
            </n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-space>
    </n-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { 
  NCard, 
  NSpace, 
  NStatistic, 
  NGrid,
  NGridItem,
  NDescriptions,
  NDescriptionsItem
} from 'naive-ui'
import { getParkingConfig } from '@/api/config'

const parkingInfo = ref({
  totalSpaces: 0,
  currentSpaces: 0,
  freeTime: 0,
  billingUnit: 0,
  unitPrice: 0,
  maxPrice: 0
})

// 获取停车场信息
const getParkingInfo = async () => {
  try {
    const response = await getParkingConfig()
    if (response.data.code === '0') {
      parkingInfo.value = response.data.data
    }
  } catch (error) {
    console.error('获取停车场信息失败', error)
  }
}

onMounted(() => {
  getParkingInfo()
})
</script> 