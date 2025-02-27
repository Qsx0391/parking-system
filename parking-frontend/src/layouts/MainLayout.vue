<template>
  <n-layout has-sider>
    <n-layout-sider
      bordered
      collapse-mode="width"
      :collapsed-width="64"
      :width="240"
      :collapsed="collapsed"
      show-trigger
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <n-menu
        :collapsed="collapsed"
        :collapsed-width="64"
        :collapsed-icon-size="22"
        :options="menuOptions"
        :value="activeKey"
        @update:value="handleMenuClick"
      />
    </n-layout-sider>
    <n-layout>
      <n-layout-header bordered style="height: 64px; padding: 16px">
        <n-space justify="end">
          <n-dropdown :options="userOptions" @select="handleSelect">
            <n-button text>
              <n-space align="center">
                <n-icon size="18">
                  <user-outlined />
                </n-icon>
                <span>管理员</span>
              </n-space>
            </n-button>
          </n-dropdown>
        </n-space>
      </n-layout-header>
      <n-layout-content content-style="padding: 24px;">
        <router-view></router-view>
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup>
import { ref, h, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  NLayout, 
  NLayoutSider, 
  NLayoutHeader, 
  NLayoutContent, 
  NSpace, 
  NButton,
  NDropdown,
  NIcon,
  NMenu,
  useDialog,
  useMessage
} from 'naive-ui'
import { UserOutlined, LogoutOutlined, DashboardOutlined, CameraOutlined, CarOutlined, PayCircleOutlined, EnvironmentOutlined, BarChartOutlined, FileTextOutlined } from '@vicons/antd'
import SideMenu from '../components/SideMenu.vue'

const router = useRouter()
const route = useRoute()
const dialog = useDialog()
const message = useMessage()
const collapsed = ref(false)

// 根据当前路由路径设置激活的菜单项
const activeKey = computed(() => route.path)

// 获取用户类型
const userType = parseInt(localStorage.getItem('userType') || '1')

// 使用计算属性动态生成菜单项
const menuOptions = computed(() => {
  const baseMenus = [
    {
      label: '仪表盘',
      key: '/',
      icon: renderIcon(DashboardOutlined)
    },
    {
      label: '车牌识别',
      key: '/plate-recognition',
      icon: renderIcon(CameraOutlined)
    },
    {
      label: '停车记录',
      key: '/parking-records',
      icon: renderIcon(CarOutlined)
    },
    {
      label: '支付记录',
      key: '/payment-records',
      icon: renderIcon(PayCircleOutlined)
    }
  ]

  // 仅管理员可见的菜单
  const adminMenus = [
    {
      label: '停车场管理',
      key: '/parking-lot',
      icon: renderIcon(EnvironmentOutlined)
    },
    {
      label: '统计分析',
      key: '/statistics',
      icon: renderIcon(BarChartOutlined)
    },
    {
      label: '操作日志',
      key: '/logs',
      icon: renderIcon(FileTextOutlined)
    }
  ]

  return userType === 0 ? [...baseMenus, ...adminMenus] : baseMenus
})

const userOptions = [
  {
    label: '退出登录',
    key: 'logout',
    icon: renderIcon(LogoutOutlined)
  }
]

function renderIcon(icon) {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const handleSelect = (key) => {
  if (key === 'logout') {
    dialog.warning({
      title: '提示',
      content: '确定要退出登录吗？',
      positiveText: '确定',
      negativeText: '取消',
      onPositiveClick: () => {
        // 清除登录信息
        localStorage.removeItem('token')
        message.success('已退出登录')
        // 跳转到登录页
        router.push('/login')
      }
    })
  }
}

// 处理菜单点击
const handleMenuClick = (key) => {
  router.push(key)
}
</script> 