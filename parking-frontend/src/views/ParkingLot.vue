<template>
  <div>
    <n-card title="停车场设置">
      <n-space vertical>
        <n-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-placement="left"
          label-width="120"
          require-mark-placement="right-hanging"
        >
          <n-form-item label="车位总数" path="totalSpaces">
            <n-input-number
              v-model:value="formData.totalSpaces"
              :min="1"
              placeholder="请输入车位总数"
              style="width: 200px"
            />
          </n-form-item>

          <n-form-item label="免费时长(分钟)" path="freeMinutes">
            <n-input-number
              v-model:value="formData.freeMinutes"
              :min="0"
              placeholder="请输入免费时长"
              style="width: 200px"
            />
          </n-form-item>

          <n-form-item label="计费单位(分钟)" path="billingUnit">
            <n-input-number
              v-model:value="formData.billingUnit"
              :min="1"
              placeholder="请输入计费单位"
              style="width: 200px"
            />
          </n-form-item>

          <n-form-item label="计费单价(元)" path="pricePerUnit">
            <n-input-number
              v-model:value="formData.pricePerUnit"
              :min="0"
              :precision="2"
              placeholder="请输入计费单价"
              style="width: 200px"
            />
          </n-form-item>

          <n-form-item label="最大收费(元)" path="maxDailyFee">
            <n-input-number
              v-model:value="formData.maxDailyFee"
              :min="0"
              :precision="2"
              placeholder="请输入最大收费"
              style="width: 200px"
            />
          </n-form-item>

          <n-form-item>
            <n-space>
              <n-button type="primary" @click="handleSubmit">
                保存设置
              </n-button>
              <n-button @click="resetForm">
                重置
              </n-button>
            </n-space>
          </n-form-item>
        </n-form>
      </n-space>
    </n-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { 
  NCard, 
  NSpace, 
  NButton, 
  NForm,
  NFormItem,
  NInputNumber,
  useMessage
} from 'naive-ui'
import request from '@/utils/request'

const message = useMessage()
const formRef = ref(null)
const formData = ref({})
const currentSettings = ref({})

const rules = {
  totalSpaces: {
    required: true,
    message: '请输入车位总数',
    trigger: 'blur',
    type: 'number'
  },
  freeMinutes: {
    required: true,
    message: '请输入免费时长',
    trigger: 'blur',
    type: 'number'
  },
  billingUnit: {
    required: true,
    message: '请输入计费单位',
    trigger: 'blur',
    type: 'number'
  },
  pricePerUnit: {
    required: true,
    message: '请输入计费单价',
    trigger: 'blur',
    type: 'number'
  },
  maxDailyFee: {
    required: true,
    message: '请输入最大收费',
    trigger: 'blur',
    type: 'number'
  }
}

const fetchParkingInfo = async () => {
  try {
    const response = await request.get('/config/get')
    if (response.data.code === '0') {
      const data = {
        totalSpaces: response.data.data.totalSpaces,
        freeMinutes: response.data.data.freeTime,
        billingUnit: response.data.data.billingUnit,
        pricePerUnit: response.data.data.unitPrice,
        maxDailyFee: response.data.data.maxPrice
      }
      currentSettings.value = { ...data }
      formData.value = { ...data }
    }
  } catch (error) {
    message.error('获取停车场信息失败')
  }
}

const handleSubmit = (e) => {
  formRef.value?.validate(async (errors) => {
    if (!errors) {
      try {
        const response = await request.post('/admin/config/update', {
          totalSpaces: formData.value.totalSpaces,
          freeTime: formData.value.freeMinutes,
          billingUnit: formData.value.billingUnit,
          unitPrice: formData.value.pricePerUnit,
          maxPrice: formData.value.maxDailyFee
        })
        
        if (response.data.code === '0') {
          currentSettings.value = { ...formData.value }
          message.success('保存成功')
        } else {
          message.error(response.data.message || '保存失败')
        }
      } catch (error) {
        message.error('保存失败')
      }
    }
  })
}

const resetForm = () => {
  formRef.value?.restoreValidation()
  formData.value = { ...currentSettings.value }
}

onMounted(() => {
  fetchParkingInfo()
})
</script> 