<template>
  <div>
    <n-card title="停车记录">
      <n-space vertical>
        <!-- 查询条件 -->
        <n-space>
          <n-input
            v-model:value="searchForm.vno"
            placeholder="请输入车牌号"
            style="width: 150px"
            clearable
          />

          <n-date-picker
            v-model:value="timeRange"
            type="daterange"
            clearable
            placeholder="选择日期范围"
          />

          <n-select
            v-model:value="searchForm.status"
            placeholder="停车状态"
            style="width: 150px"
            :options="statusOptions"
            clearable
          />

          <n-button 
            type="primary" 
            @click="handleSearch"
            :loading="loading"
          >
            搜索
          </n-button>
        </n-space>

        <!-- 数据表格 -->
        <n-data-table
          :columns="columns"
          :data="tableData"
          :loading="loading"
          @update:sorter="handleSorterChange"
        />
        
        <!-- 分页组件 -->
        <div style="display: flex; justify-content: flex-end">
          <n-pagination
            v-model:page="searchForm.pageNumber"
            v-model:page-size="searchForm.pageSize"
            :item-count="total"
            :page-sizes="[10, 20, 30, 40]"
            show-size-picker
            show-quick-jumper
            @update:page="onPageChange"
            @update:page-size="onPageSizeChange"
          />
        </div>
      </n-space>
    </n-card>
  </div>
</template>

<script setup>
import { ref, h } from 'vue'
import { 
  NCard,
  NSpace,
  NInput,
  NInputGroup,
  NDatePicker,
  NSelect,
  NButton,
  NDataTable,
  useMessage,
  NPagination
} from 'naive-ui'
import { getParkingRecord } from '@/api/vehicle'

const message = useMessage()
const loading = ref(false)
const timeRange = ref(null)

// 查询条件
const searchForm = ref({
  vno: '',
  status: null,
  pageNumber: 1,
  pageSize: 10,
  orders: []
})

// 状态选项
const statusOptions = [
  { label: '已进场', value: 0 },
  { label: '待支付', value: 1 },
  { label: '支付处理中', value: 2 },
  { label: '已支付', value: 3 },
  { label: '已离场', value: 4 }
]

// 表格列定义
const columns = [
  {
    title: 'ID',
    key: 'id',
    sorter: false
  },
  { 
    title: '车牌号', 
    key: 'vno',
    sorter: true
  },
  { 
    title: '入场时间', 
    key: 'enteredAt',
    sorter: true
  },
  { 
    title: '支付时间', 
    key: 'paidAt',
    sorter: true
  },
  { 
    title: '出场时间', 
    key: 'exitedAt',
    sorter: true
  },
  { 
    title: '状态', 
    key: 'status',
    render(row) {
      const statusMap = {
        0: '已进场',
        1: '支付中',
        2: '已支付',
        3: '已离场',
      }
      return statusMap[row.status] || '未知状态'
    }
  }
]

// 表格数据
const tableData = ref([])

// 添加总数记录
const total = ref(0)

// 修改日期格式化函数
const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`
}

// 查询数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      vno: searchForm.value.vno,
      status: searchForm.value.status,
      pageNumber: searchForm.value.pageNumber,
      pageSize: searchForm.value.pageSize,
      orders: searchForm.value.orders
    }
    
    // 处理时间范围
    if (timeRange.value) {
      params.startedAt = formatDate(timeRange.value[0])
      params.endedAt = formatDate(timeRange.value[1])
    }

    const response = await getParkingRecord(params)
    
    if (response.data.code === '0') {
      const { pageData, totalSize } = response.data.data
      tableData.value = pageData
      total.value = totalSize
    } else {
      message.error(response.data.message || '查询失败')
    }
  } catch (error) {
    message.error('查询失败')
  } finally {
    loading.value = false
  }
}

// 处理查询按钮点击
const handleSearch = () => {
  searchForm.value.pageNumber = 1
  fetchData()
}

// 页码变化处理
const onPageChange = (page) => {
  searchForm.value.pageNumber = page
  fetchData()
}

// 每页条数变化处理
const onPageSizeChange = (pageSize) => {
  searchForm.value.pageSize = pageSize
  searchForm.value.pageNumber = 1
  fetchData()
}

// 添加排序处理函数
const handleSorterChange = (sorter) => {
  if (!sorter) {
    searchForm.value.orders = []
  } else {
    searchForm.value.orders = [{
      fieldName: sorter.columnKey,
      direction: sorter.order === 'ascend' ? 'asc' : 'desc'
    }]
  }
  fetchData()
}

// 初始加载
fetchData()
</script> 