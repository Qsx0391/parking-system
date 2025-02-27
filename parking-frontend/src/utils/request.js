import axios from 'axios'
import router from '@/router'

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.token = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 用户不存在或token失效时跳转到登录页
    if (response.data.code === 'A000201') {
      localStorage.removeItem('token')
      router.push('/login')
      window.$message?.error(response.data.message || 'token已过期，请重新登录')
    } else if (response.data.code === 'A000200') {
      localStorage.removeItem('token')
      router.push('/login')
      window.$message?.error(response.data.message || '请先登录')
    }
    return response
  },
  error => {
    window.$message?.error('请求失败')
    return Promise.reject(error)
  }
)

export default request 