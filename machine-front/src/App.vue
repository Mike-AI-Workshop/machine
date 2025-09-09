<template>
  <el-config-provider>
    <div class="common-layout">
      <NavBar />
      <main class="main-content">
        <!-- 
          新增一个包裹层，并应用flex布局。
          这将为所有通过 <router-view> 渲染的页面组件提供一个统一的、可伸缩的布局上下文。
          这是实现“一劳永逸”布局问题的关键。
        -->
        <div class="router-view-wrapper">
          <router-view />
        </div>
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
  // authStore.initialize(); // 此处调用已在 main.js 中执行，此处移除以避免重复
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
  flex-grow: 1;
  overflow-y: auto;
  background-color: #f9fafb;
  /* 移除 display: flex，将布局控制权下移到wrapper */
}

.router-view-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100%; /* 使用min-height确保在内容不足时也能撑满父容器 */
  box-sizing: border-box;
}

/* 覆盖一些element-plus的默认样式 */
.el-main {
  --el-main-padding: 0px;
  padding: 0; /* 再次确保没有内边距 */
}
</style>
