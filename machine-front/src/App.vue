<template>
  <el-config-provider>
    <div class="common-layout">
      <NavBar />
      <main class="main-content">
        <router-view />
      </main>
      <LoginModal />
    </div>
  </el-config-provider>
</template>

<script setup>
import NavBar from './components/NavBar.vue';
import LoginModal from './components/LoginModal.vue';
import { useAuthStore } from './store/auth';
import { onMounted } from 'vue';

const authStore = useAuthStore();

// 在应用启动时，尝试从localStorage恢复登录状态
onMounted(() => {
  authStore.initialize();
});
</script>

<style>
/* 全局样式重置 */
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.common-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.main-content {
  flex-grow: 1; /* 关键：让主内容区自动填满剩余空间 */
  overflow-y: auto; /* 内容超长时，仅在垂直方向出现滚动条 */
  background-color: #f9fafb;
}

/* 覆盖一些element-plus的默认样式 */
.el-main {
  --el-main-padding: 0px;
  padding: 0; /* 再次确保没有内边距 */
}
</style>
