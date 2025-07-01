<template>
  <el-dialog
    v-model="authStore.isLoginModalVisible"
    :show-close="true"
    width="480px"
    class="login-dialog"
    @close="authStore.closeLoginModal"
    align-center
    title="欢迎回来"
  >
    <div class="login-modal-container">
      <el-tabs v-model="activeTab" class="login-tabs" stretch>
        <el-tab-pane label="登 录" name="login">
          <el-form :model="loginForm" @submit.prevent="handleLogin" class="form-content">
            <el-form-item>
              <el-input v-model="loginForm.username" placeholder="请输入用户名" size="large" :prefix-icon="User" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password size="large" :prefix-icon="Lock" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleLogin" :loading="loading" native-type="submit" style="width: 100%;" size="large">
                立即登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="注 册" name="register">
          <el-form :model="registerForm" @submit.prevent="handleRegister" class="form-content">
            <el-form-item>
              <el-input v-model="registerForm.username" placeholder="请输入用户名" size="large" :prefix-icon="User" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password size="large" :prefix-icon="Lock" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码" show-password size="large" :prefix-icon="Lock" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleRegister" :loading="loading" native-type="submit" style="width: 100%;" size="large">
                立即注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '../store/auth';
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';

const authStore = useAuthStore();
const activeTab = ref('login');

const loginForm = ref({
  username: '',
  password: '',
});

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
});

const loading = ref(false);

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入用户名和密码');
    return;
  }
  loading.value = true;
  try {
    await authStore.login(loginForm.value);
    ElMessage.success('登录成功');
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名或密码');
  } finally {
    loading.value = false;
  }
};

const handleRegister = async () => {
  if (!registerForm.value.username || !registerForm.value.password) {
    ElMessage.warning('用户名和密码不能为空');
    return;
  }
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致');
    return;
  }
  loading.value = true;
  try {
    await authStore.register({
      username: registerForm.value.username,
      password: registerForm.value.password
    });
    ElMessage.success('注册成功，请登录');
    activeTab.value = 'login';
  } catch (error) {
    ElMessage.error('注册失败，用户名可能已被占用');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-dialog .el-dialog__body {
  padding: 20px 40px 40px 40px;
}

.login-tabs {
    width: 100%;
}

.form-content {
    margin-top: 20px;
}
</style> 