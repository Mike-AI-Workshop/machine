<template>
  <div>
    <el-header class="main-header">
      <div class="header-content">
        <div class="logo-container">
          <img src="/logo.png" alt="Logo" class="logo-img" />
        </div>

        <el-menu mode="horizontal" :ellipsis="false" router class="main-menu">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/rooms">机房管理</el-menu-item>
        </el-menu>

        <div class="user-actions">
          <div v-if="authStore.isAuthenticated">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <el-avatar :icon="UserFilled" size="small" />
                <span class="username">{{ authStore.user.username }}</span>
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div v-else>
            <el-button text @click="authStore.openLoginModal">登录</el-button>
          </div>
        </div>
      </div>
    </el-header>
    <ChangePasswordModal 
      :visible="changePasswordModalVisible"
      @update:visible="changePasswordModalVisible = $event" 
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '../store/auth';
import { useRouter } from 'vue-router';
import { UserFilled, ArrowDown } from '@element-plus/icons-vue';
import ChangePasswordModal from './ChangePasswordModal.vue';

const authStore = useAuthStore();
const router = useRouter();
const changePasswordModalVisible = ref(false);

const handleCommand = (command) => {
  if (command === 'logout') {
    authStore.logout();
  } else if (command === 'changePassword') {
    changePasswordModalVisible.value = true;
  }
};
</script>

<style scoped>
.main-header {
  /* border-bottom: 1px solid #e4e7ed; */ /* 移除此行以解决1px布局冲突 */
  background-color: #ffffff;
  position: sticky;
  top: 0;
  z-index: 1000;
  height: 50px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo-container {
  display: flex;
  align-items: center;
  /* 为右侧用户区域留出对称空间 */
  min-width: 150px; 
}

.logo-img {
  height: 40px;
}

.main-menu {
  flex-grow: 1;
  border-bottom: none;
  justify-content: center;
  background-color: transparent;
  height: 100%; /* 关键：强制菜单高度与父容器一致，覆盖默认的60px */
}

.el-menu--horizontal > .el-menu-item {
  font-size: 16px;
}

.user-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 150px;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  outline: none;
}

.username {
  font-size: 16px;
}
</style> 