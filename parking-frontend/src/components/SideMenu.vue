<template>
  <n-menu
    :collapsed="collapsed"
    :collapsed-width="64"
    :collapsed-icon-size="22"
    :options="menuOptions"
  />
</template>

<script setup>
import { h, ref } from 'vue'
import { NMenu } from 'naive-ui'
import { 
  DashboardOutlined,
  CarOutlined,
  ScanOutlined,
  HistoryOutlined,
  PayCircleOutlined,
  BarChartOutlined,
  FileTextOutlined
} from '@vicons/antd'
import { RouterLink } from 'vue-router'

const collapsed = ref(false)

function renderIcon(icon) {
  return () => h(icon)
}

const menuOptions = [
  {
    label: () => h(RouterLink, { to: { name: 'Dashboard' } }, { default: () => '仪表盘' }),
    key: 'dashboard',
    icon: renderIcon(DashboardOutlined)
  },
  {
    label: () => h(RouterLink, { to: { name: 'ParkingLot' } }, { default: () => '停车场管理' }),
    key: 'parking-lot',
    icon: renderIcon(CarOutlined)
  },
  {
    label: () => h(RouterLink, { to: { name: 'PlateRecognition' } }, { default: () => '车牌识别' }),
    key: 'plate-recognition',
    icon: renderIcon(ScanOutlined)
  },
  {
    label: () => h(RouterLink, { to: { name: 'ParkingRecords' } }, { default: () => '停车记录' }),
    key: 'parking-records',
    icon: renderIcon(HistoryOutlined)
  },
  {
    label: () => h(RouterLink, { to: { name: 'PaymentRecords' } }, { default: () => '缴费记录' }),
    key: 'payment-records',
    icon: renderIcon(PayCircleOutlined)
  },
  {
    label: () => h(RouterLink, { to: { name: 'Statistics' } }, { default: () => '统计分析' }),
    key: 'statistics',
    icon: renderIcon(BarChartOutlined)
  },
  {
    label: () => h(RouterLink, { to: { name: 'Logs' } }, { default: () => '日志管理' }),
    key: 'logs',
    icon: renderIcon(FileTextOutlined)
  }
]
</script> 