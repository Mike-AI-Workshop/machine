import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

// 引入Element Plus
import ElementPlus, { ElMessage } from 'element-plus'
import 'element-plus/dist/index.css'

// 引入Pinia
import { createPinia } from 'pinia'

// 引入Vue Router
import router from './router/index.js'
import { useAuthStore } from './store/auth';

import axios from 'axios'

const app = createApp(App)
app.use(ElementPlus)
const pinia = createPinia();
app.use(pinia);

const authStore = useAuthStore();
authStore.initialize();

// 设置Axios响应拦截器
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      // 检查存储中是否仍有token，避免在已经登出的情况下重复提示
      if (authStore.token) {
        ElMessage.error('会话已过期，请重新登录。');
        authStore.handleSessionExpired();
      }
    }
    return Promise.reject(error);
  }
);

app.use(router)

app.mount('#app')
