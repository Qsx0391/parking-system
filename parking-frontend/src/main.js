import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { create, NMessageProvider } from 'naive-ui'

const naive = create({
  components: [NMessageProvider]
})

const app = createApp(App)

app.use(router)
app.use(naive)
app.mount('#app') 