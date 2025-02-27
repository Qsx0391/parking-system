<template>
  <div class="login-container">
    <n-card class="login-card" title="停车场管理系统">
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
        <n-space vertical :size="24">
          <n-button
            type="primary"
            size="large"
            block
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </n-button>
          <n-space justify="space-between">
            <n-button text @click="handleRegister">
              注册账号
            </n-button>
            <n-button text>
              忘记密码？
            </n-button>
          </n-space>
        </n-space>
      </n-form>
    </n-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { 
  NCard,
  NForm,
  NFormItem,
  NInput,
  NButton,
  NSpace,
  NIcon,
  useMessage
} from 'naive-ui'
import { UserOutlined, LockOutlined } from '@vicons/antd'
import { login } from '@/api/user'

const router = useRouter()
const message = useMessage()
const formRef = ref(null)
const loading = ref(false)

const formData = ref({
  username: '',
  password: ''
})

const rules = {
  username: {
    required: true,
    message: '请输入用户名',
    trigger: 'blur'
  },
  password: {
    required: true,
    message: '请输入密码',
    trigger: 'blur'
  }
}

const handleRegister = () => {
  router.push('/register')
}

const handleLogin = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true
    
    const response = await login({
      username: formData.value.username,
      password: formData.value.password
    })

    if (response.data.code === '0') {
      const { type, token } = response.data.data
      // 保存 token
      localStorage.setItem('token', token)
      // 保存用户类型
      localStorage.setItem('userType', type.toString())
      
      message.success('登录成功')
      router.push('/')
    } else if (response.data.code === 'A00301') {
      message.error('用户名或密码错误')
    } else {
      message.error(response.data.message || '登录失败')
    }
  } catch (error) {
    message.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.login-card {
  width: 360px;
}

.login-card :deep(.n-card-header) {
  text-align: center;
  font-size: 24px;
  padding: 24px 0;
}
</style> 