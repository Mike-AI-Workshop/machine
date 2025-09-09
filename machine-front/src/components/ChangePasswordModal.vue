<template>
  <el-dialog
    :model-value="visible"
    title="修改密码"
    width="400px"
    @close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="form.oldPassword" type="password" show-password />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" type="password" show-password />
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" show-password />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="submitForm">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import { useAuthStore } from '../store/auth';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:visible']);

const authStore = useAuthStore();
const formRef = ref(null);

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
});

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'));
  } else if (value !== form.newPassword) {
    callback(new Error("两次输入的新密码不一致!"));
  } else {
    callback();
  }
};

const rules = reactive({
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' },
  ],
});

const handleClose = () => {
  formRef.value.resetFields();
  emit('update:visible', false);
};

const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const payload = {
          oldPassword: form.oldPassword,
          newPassword: form.newPassword,
        };
        console.log('即将发送的认证Token:', authStore.token);
        await axios.post('/api/auth/change-password', payload, {
          headers: {
            'Authorization': `Bearer ${authStore.token}`
          }
        });
        ElMessage.success('密码修改成功，请重新登录');
        authStore.logout(); // 退出登录
        handleClose(); // 关闭并重置表单
      } catch (error) {
        if (error.response && error.response.data) {
           ElMessage.error(error.response.data || '操作失败');
        } else {
           ElMessage.error('操作失败，请稍后重试');
        }
      }
    }
  });
};
</script> 