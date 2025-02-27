<template>
  <n-config-provider>
    <n-dialog-provider>
      <n-message-provider>
        <router-view></router-view>
      </n-message-provider>
    </n-dialog-provider>
  </n-config-provider>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { 
  NConfigProvider, 
  NDialogProvider, 
  NMessageProvider 
} from 'naive-ui'

const router = useRouter()

onMounted(() => {
  // 添加全局错误处理
  window.addEventListener('unhandledrejection', event => {
    console.error('未处理的错误:', event.reason)
    // 可以在这里添加错误通知或其他错误处理逻辑
  })
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style> 