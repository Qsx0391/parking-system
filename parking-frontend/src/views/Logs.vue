<template>
  <div>
    <n-card title="操作日志">
      <n-space vertical>
        <!-- 查询条件 -->
        <n-space>
          <n-input
            v-model:value="searchForm.username"
            placeholder="请输入用户名"
            style="width: 150px"
            clearable
          />

          <n-input
            v-model:value="searchForm.operation"
            placeholder="请输入操作类型"
            style="width: 150px"
            clearable
          />

          <n-date-picker
            v-model:value="timeRange"
            type="daterange"
            clearable
            placeholder="选择日期范围"
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
  NButton,
  NDataTable,
  useMessage,
  NPagination
} from 'naive-ui'
import { getOperationLogRecord } from '@/api/log'

const message = useMessage()
const loading = ref(false)
const timeRange = ref(null)

// 查询条件
const searchForm = ref({
  username: '',
  operation: null,
  pageNumber: 1,
  pageSize: 10,
  orders: []  // 修改为数组格式
})

// 表格列定义
const columns = [
  {
    title: 'ID',
    key: 'id',
    sorter: false
  },
  { 
    title: '用户名', 
    key: 'username',
    sorter: true
  },
  { 
    title: '操作类型', 
    key: 'operation',
    sorter: true
  },
  { 
    title: '执行时长', 
    key: 'duration',
    sorter: true,
    render(row) {
      return `${row.duration}ms`
    }
  },
  { 
    title: '请求参数', 
    key: 'params',
    ellipsis: {
      tooltip: true
    },
    render(row) {
      return row.params ? row.params : '-'
    }
  },
  { 
    title: 'IP地址', 
    key: 'ip'
  },
  { 
    title: '创建时间', 
    key: 'createdAt',
    sorter: true,
    render(row) {
      return h('span', {}, new Date(row.createdAt).toLocaleString())
    }
  }
]

// 表格数据
const tableData = ref([])

// 总数记录
const total = ref(0)

// 日期格式化函数
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
      username: searchForm.value.username,
      operation: searchForm.value.operation,
      pageNumber: searchForm.value.pageNumber,
      pageSize: searchForm.value.pageSize,
      orders: searchForm.value.orders
    }
    
    // 处理时间范围
    if (timeRange.value) {
      params.startedAt = formatDate(timeRange.value[0])
      params.endedAt = formatDate(timeRange.value[1])
    }

    const response = await getOperationLogRecord(params)
    
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