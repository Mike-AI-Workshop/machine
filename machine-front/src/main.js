import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

// 引入Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 引入Pinia
import { createPinia } from 'pinia'

// 引入Vue Router
import router from './router/index.js'
import { useAuthStore } from './store/auth';

const app = createApp(App)
app.use(ElementPlus)
const pinia = createPinia();
app.use(pinia);

const authStore = useAuthStore();
authStore.initialize();

app.use(router)

app.mount('#app')
