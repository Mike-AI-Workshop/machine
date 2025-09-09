<!--
 * @文件名: RoomList.vue
 * @描述: 机房管理页面组件
 * @API交互:
 *   - GET /api/rooms - 获取所有机房列表
 *   - GET /api/rooms/{id} - 获取单个机房详情
 *   - POST /api/rooms - 新增机房
 *   - PUT /api/rooms/{id} - 修改机房信息
 *   - DELETE /api/rooms/{id} - 删除机房
 *   - POST /api/images - 上传机房图片
 *   - GET /api/images/{id} - 获取图片详情
 * @数据结构:
 *   机房对象: {
 *     id: 数字, // 机房ID
 *     name: 字符串, // 机房名称
 *     location: 字符串, // 机房位置
 *     image_id: 数字, // 图片ID
 *     description: 字符串 // 机房描述
 *   }
 * @组件功能:
 *   1. 机房数据展示与搜索
 *   2. 机房详情查看
 *   3. 机房新增、编辑和删除
 *   4. 机房图片上传
 *   5. 跳转至机柜管理
 -->
<template>
  <div class="room-list-container">
    <!-- 1. 集成式操作栏 -->
    <div class="action-bar">
      <div class="action-bar-left">
        <el-button :icon="ArrowLeft" circle @click="goBack" />
        <h2 class="page-title">机房管理</h2>
      </div>
      <div class="action-bar-right">
        <el-input 
          v-model="searchName" 
          placeholder="按名称搜索机房" 
          :prefix-icon="Search"
          style="width: 240px;" 
          clearable 
        />
        <el-button v-if="authStore.isAdmin" type="primary" :icon="Plus" @click="openAdd">新增机房</el-button>
      </div>
    </div>

    <!-- 2. 机房信息卡片 -->
    <div class="room-cards-container">
      <el-card v-for="room in filteredRooms" :key="room.id" class="room-card" shadow="hover">
        <template #header>
          <el-image 
            :src="room.imageUrl || '/placeholder.png'" 
            fit="cover" 
            class="room-image"
            :preview-src-list="room.imageUrl ? [room.imageUrl] : []"
            preview-teleported
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
        <div class="card-body">
          <h3 class="room-name">{{ room.name }}</h3>
          <p class="room-info">
            <el-icon><Location /></el-icon>
            <span>{{ room.location || '暂无位置信息' }}</span>
          </p>
          <p class="room-info">
            <el-icon><Document /></el-icon>
            <span>{{ room.description || '暂无描述' }}</span>
          </p>
        </div>
        <div class="card-footer">
          <el-button type="primary" class="manage-btn" @click="goCabinetList(room.id)">进入管理</el-button>
          <div v-if="authStore.isAdmin" class="action-icons">
            <el-button :icon="Edit" circle text @click="openEdit(room)"></el-button>
            <el-popconfirm
              title="确定要删除这个机房吗?"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="delRoom(room)"
            >
              <template #reference>
                <el-button :icon="Delete" circle text type="danger"></el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      v-model="editVisible" 
      :title="editMode === 'add' ? '新增机房' : '编辑机房'" 
      width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="editForm.location" />
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
            v-if="authStore.isAdmin"
            action="/api/images"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :data="{ type: 'room' }"
          >
            <el-button>上传图片</el-button>
          </el-upload>
          <img v-if="editForm.imageUrl" :src="editForm.imageUrl" style="width: 150px; margin-top: 10px; border-radius: 4px;" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button v-if="authStore.isAdmin" type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 导入依赖模块
 * - Vue 3 Composition API：提供响应式状态管理和生命周期钩子
 * - axios：处理HTTP请求
 * - vue-router：处理页面导航
 */
import { ref, computed, onMounted } from 'vue' // Vue 3 Composition API
import axios from 'axios' // HTTP请求库
import { useRouter } from 'vue-router' // Vue Router路由
import { ElMessage } from 'element-plus'
import { ArrowLeft, Plus, Search, Edit, Delete, Location, Document, Picture } from '@element-plus/icons-vue'
import { useAuthStore } from '../../store/auth'

const authStore = useAuthStore();

/**
 * 响应式状态定义
 * Vue 3中使用ref()创建响应式变量，当这些变量变化时，相关UI会自动更新
 */
const rooms = ref([])         // 存储从API获取的机房列表数据
const searchName = ref('')    // 搜索框输入值，用于过滤机房列表
const editVisible = ref(false)    // 控制编辑/新增弹窗显示状态
const editMode = ref('add')       // 编辑模式：'add'=新增，'edit'=编辑
// 编辑表单数据结构，与API文档中的机房对象结构对应
const editForm = ref({ id: null, name: '', location: '', imageId: '', imageUrl: '', description: '' })
const router = useRouter()    // 路由实例，用于页面跳转

// 新增：创建一个计算属性，它会响应式地持有当前的认证头
const uploadHeaders = computed(() => {
  return {
    Authorization: `Bearer ${authStore.token}`
  }
});

/**
 * 获取机房列表数据
 * 调用API: GET /api/rooms
 * 成功响应结构: { code: 0, data: [...机房对象数组] }
 */
async function fetchRooms() {
  try {
    const res = await axios.get('/api/rooms')
    if (res.data.code === 0 && res.data.data) {
      const list = res.data.data
      for (const room of list) {
        if (room.imageId) {
          try {
            const imgRes = await axios.get(`/api/images/${room.imageId}`)
            if (imgRes.data.code === 0 && imgRes.data.data) {
              room.imageUrl = imgRes.data.data.url
            }
          } catch {
            room.imageUrl = null // Set to null if image fetch fails
          }
        } else {
            room.imageUrl = null
        }
      }
      rooms.value = list
    } else {
      rooms.value = []
    }
  } catch (error) {
    ElMessage.error('获取机房列表失败')
    console.error(error);
  }
}

/**
 * 组件挂载后自动执行
 * Vue 3生命周期钩子：组件挂载到DOM后执行
 */
onMounted(fetchRooms)

/**
 * 计算属性：过滤后的机房列表
 * 根据searchName过滤rooms数组
 * 计算属性会在依赖变化时自动重新计算
 */
const filteredRooms = computed(() => {
  if (!searchName.value) return rooms.value
  return rooms.value.filter(r => r.name.toLowerCase().includes(searchName.value.toLowerCase()))
})

/**
 * 打开新增机房弹窗
 * 重置表单数据并设置为新增模式
 */
function openAdd() {
  editMode.value = 'add'
  editForm.value = { id: null, name: '', location: '', imageId: '', imageUrl: '', description: '' }
  editVisible.value = true
}

/**
 * 打开编辑机房弹窗
 * @param {Object} row - 表格行数据，包含机房基本信息
 */
function openEdit(row) {
  editMode.value = 'edit'
  editForm.value = { ...row }
  editVisible.value = true
}

/**
 * 处理图片上传成功事件
 * 上传图片调用API: POST /api/images
 * @param {Object} res - 上传成功后的响应数据，格式：{ code: 0, data: { id, url } }
 */
function handleUploadSuccess(res) {
  if (res.code === 0 && res.data) {
    editForm.value.imageId = res.data.id
    editForm.value.imageUrl = res.data.url
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败')
  }
}

/**
 * 删除机房
 * 调用API: DELETE /api/rooms/{id}
 * @param {Object} row - 表格行数据，包含机房基本信息
 */
async function delRoom(row) {
  try {
    await axios.delete(`/api/rooms/${row.id}`)
    ElMessage.success('删除成功')
    fetchRooms()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

/**
 * 提交新增/编辑表单
 * 新增调用API: POST /api/rooms
 * 编辑调用API: PUT /api/rooms/{id}
 */
async function submitEdit() {
  const payload = { ...editForm.value }
  delete payload.imageUrl
  
  try {
    if (editMode.value === 'add') {
      await axios.post('/api/rooms', payload)
      ElMessage.success('新增成功')
    } else {
      await axios.put(`/api/rooms/${editForm.value.id}`, payload)
      ElMessage.success('更新成功')
    }
    editVisible.value = false
    fetchRooms()
  } catch (error) {
     ElMessage.error('操作失败')
  }
}

/**
 * 跳转到机柜管理页面
 * 使用vue-router导航到机柜页面，并传递roomId参数
 * @param {number|string} roomId - 机房ID
 */
function goCabinetList(roomId) {
  router.push({ path: '/cabinets', query: { roomId } })
}

/**
 * 返回上一页
 * 使用vue-router的back方法返回上一级页面
 */
function goBack() {
  router.back()
}
</script>

<style scoped>
.room-list-container {
  padding: 24px;
  height: 100%; /* 恢复height: 100% */
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  /* flex-grow: 1; */ /* 移除 flex-grow */
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-shrink: 0; /* 防止操作栏被压缩 */
}

.action-bar-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.action-bar-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.room-cards-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 默认三列布局 */
  gap: 24px;
  overflow-y: auto; /* 内容超长时，卡片区域滚动 */
  flex-grow: 1; /* 占据剩余空间 */
}

.room-card {
  transition: all 0.3s ease;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
}

.room-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.room-image {
  width: 100%;
  height: 200px;
  background-color: #f5f7fa;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  color: #c0c4cc;
  font-size: 30px;
}

.card-body {
  padding: 16px;
  /* flex-grow: 1; */ /* 移除此行，避免在空间不足时挤占footer的空间 */
}

.room-name {
  font-size: 22px; /* 增大字体 */
  font-weight: 600;
  margin: 0 0 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.room-info {
  display: flex;
  align-items: center;
  color: #606266;
  font-size: 14px;
  margin: 8px 0;
  gap: 8px;
}

.card-footer {
  padding: 16px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.manage-btn {
  width: 60%;
}

.action-icons {
  display: flex;
  gap: 8px;
}

/* 响应式布局调整 */
@media (max-width: 1200px) {
  .room-cards-container {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .room-cards-container {
    grid-template-columns: 1fr;
  }

  .action-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }
}
</style> 