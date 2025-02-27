<template>
  <div class="register-container">
    <n-card class="register-card" title="注册账号">
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        size="large"
        label-placement="left"
        label-width="0"
        require-mark-placement="right-hanging"
      >
        <n-form-item path="username">
          <n-input
            v-model:value="formData.username"
            placeholder="用户名"
          >
            <template #prefix>
              <n-icon><user-outlined /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-form-item path="password">
          <n-input
            v-model:value="formData.password"
            type="password"
            show-password-on="click"
            placeholder="密码"
          >
            <template #prefix>
              <n-icon><lock-outlined /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-form-item path="confirmPassword">
          <n-input
            v-model:value="formData.confirmPassword"
            type="password"
            show-password-on="click"
            placeholder="确认密码"
          >
            <template #prefix>
              <n-icon><lock-outlined /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-space vertical :size="16">
          <n-button
            type="primary"
            size="large"
            block
            :loading="loading"
            @click="handleSubmit"
          >
            注册
          </n-button>
          <n-button block @click="router.push('/login')">
            返回登录
          </n-button>
        </n-space>
      </n-form>
    </n-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { 
  NCard,
  NInput,
  NButton,
  NSpace,
  NIcon,
  NForm,
  NFormItem,
  useMessage
} from 'naive-ui'
import { UserOutlined, LockOutlined } from '@vicons/antd'
import { register } from '@/api/user'

const router = useRouter()
const message = useMessage()
const loading = ref(false)
const formRef = ref(null)

const formData = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名' },
    {
      validator: async (rule, value) => {
        if (!value) return
        try {
          const response = await checkUsernameApi(value)
          if (response.data.code === 'A000111') {
            throw new Error('用户名已被占用')
          }
        } catch (error) {
          if (error.message === '用户名已被占用') {
            throw error
          }
          throw new Error('检查用户名失败')
        }
      },
      trigger: 'blur'
    }
  ],
  password: [
    { required: true, message: '请输入密码' },
    { min: 6, message: '密码长度不能小于6位' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码' },
    {
      validator: (rule, value) => {
        if (value !== formData.password) {
          return new Error('两次输入的密码不一致')
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ]
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true
    
    const response = await register({
      username: formData.username,
      password: formData.password
    })

    if (response.data.code === '0') {
      message.success('注册成功')
      router.push('/login')
    } else {
      message.error(response.data.message || '注册失败')
    }
  } catch (error) {
    if (error?.errorMessages?.[0]) {
      message.error(error.errorMessages[0])
    } else {
      message.error('注册失败')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.register-card {
  width: 360px;
}

.register-card :deep(.n-card-header) {
  text-align: center;
  font-size: 24px;
  padding: 24px 0;
}

.error-tip {
  color: #d03050;
  font-size: 12px;
  margin-top: -8px;
  margin-bottom: 8px;
}
</style> 