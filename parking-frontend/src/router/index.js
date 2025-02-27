import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import Dashboard from '../views/Dashboard.vue'

// 定义路由和权限
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: MainLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: Dashboard,
        meta: { roles: [0, 1] }  // 所有用户可访问
      },
      {
        path: 'parking-lot',
        name: 'ParkingLot',
        component: () => import('../views/ParkingLot.vue'),
        meta: { roles: [0] }  // 仅管理员可访问
      },
      {
        path: 'plate-recognition',
        name: 'PlateRecognition',
        component: () => import('../views/PlateRecognition.vue'),
        meta: { roles: [0, 1] }  // 所有用户可访问
      },
      {
        path: 'parking-records',
        name: 'ParkingRecords',
        component: () => import('../views/ParkingRecords.vue'),
        meta: { roles: [0, 1] }  // 所有用户可访问
      },
      {
        path: 'payment-records',
        name: 'PaymentRecords',
        component: () => import('../views/PaymentRecords.vue'),
        meta: { roles: [0, 1] }  // 所有用户可访问
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue'),
        meta: { roles: [0] }  // 仅管理员可访问
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('../views/Logs.vue'),
        meta: { roles: [0] }  // 仅管理员可访问
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 修改全局前置守卫
router.beforeEach((to, from, next) => {
  // 如果是登录页或注册页，直接放行
  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }

  // 检查是否有token
  const token = localStorage.getItem('token')
  if (!token) {
    next('/login')
    return
  }

  // 检查用户类型和路由权限
  const userType = parseInt(localStorage.getItem('userType') || '1')
  if (to.meta.roles && !to.meta.roles.includes(userType)) {
    next('/')  // 无权限时跳转到首页
    return
  }

  next()
})

export default router 