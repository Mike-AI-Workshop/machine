<!--
 * @文件名: CabinetList.vue
 * @描述: 机柜管理页面组件。负责展示和管理指定机房下的所有机柜。
 * @API交互:
 *   - GET /api/cabinets?roomId={id} - 获取指定机房的机柜列表
 *   - POST /api/cabinets - 新增机柜
 *   - PUT /api/cabinets/{id} - 修改机柜信息
 *   - DELETE /api/cabinets/{id} - 删除机柜
 *   - POST /api/images - 上传机柜图片 (type: 'cabinet_front'/'cabinet_back')
 *   - GET /api/images/{id} - 获取图片URL
 * @数据结构:
 *   - 机柜对象 (Cabinet): {
 *     id: 数字,
 *     roomId: 数字,
 *     name: 字符串,
 *     number: 字符串,
 *     rowNumber: 字符串,
 *     columnNumber: 字符串,
 *     imageFront: 数字, // 正面图ID
 *     imageBack: 数字,  // 背面图ID
 *     description: 字符串
 *   }
 * @组件功能:
 *   1. 展示指定机房的机柜列表，支持增删改。
 *   2. 支持上传机柜的正面和背面图片。
 *   3. 支持跳转到具体机柜的设备管理页面。
-->
<template>
  <div class="cabinet-list-container">
    <!-- 1. 集成式操作栏 -->
    <div class="action-bar">
      <div class="action-bar-left">
        <el-button :icon="ArrowLeft" circle @click="goBack" />
        <h2 class="page-title">机柜管理</h2>
        <span v-if="roomName" class="page-subtitle"> / {{ roomName }}</span>
      </div>
    </div>

    <!-- 2. 主体内容区 (左右布局) -->
    <div class="main-content">
      <!-- 2.1 左侧机柜排导航栏 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h3 class="sidebar-title">机柜排</h3>
          <el-button v-if="authStore.isAdmin" type="primary" :icon="Plus" @click="openAddRow">新增排</el-button>
        </div>
        <div class="row-list">
          <div 
            v-for="row in rows" 
            :key="row.id" 
            class="row-item"
            :class="{ 'active': selectedRowId === row.id }"
            @click="handleSelectRow(row.id)"
          >
            <div class="row-info">
              <el-icon><Files /></el-icon>
              <span>{{ row.name }}</span>
            </div>
            <div v-if="authStore.isAdmin" class="row-actions">
              <el-button :icon="Edit" circle text @click.stop="openEditRow(row)"></el-button>
              <el-popconfirm
                title="确定删除此排及其下所有机柜吗？"
                @confirm="delRow(row)"
              >
                <template #reference>
                  <el-button :icon="Delete" circle text type="danger" @click.stop></el-button>
                </template>
              </el-popconfirm>
            </div>
          </div>
           <el-empty v-if="rows.length === 0" description="暂无机柜排" :image-size="80" />
        </div>
      </div>

      <!-- 2.2 右侧机柜卡片展示区 -->
      <div class="content-area">
        <div v-if="selectedRowId" class="cabinets-grid">
          <!-- 新增机柜卡片 -->
          <div v-if="authStore.isAdmin" class="add-cabinet-card" @click="openAddCabinet">
            <el-icon><Plus /></el-icon>
            <span>新增机柜</span>
          </div>
          <!-- 机柜列表卡片 -->
          <el-card v-for="cabinet in sortedCabinets" :key="cabinet.id" class="cabinet-card" shadow="hover">
            <template #header>
              <div class="card-header-image">
                <el-image 
                  :src="cabinet.imageFrontUrl || '/placeholder.png'" 
                  fit="cover" 
                  class="cabinet-image"
                />
                <span class="cabinet-number-tag">{{ cabinet.number }}</span>
              </div>
            </template>
            <div class="card-body">
              <h4 class="cabinet-name">{{ cabinet.name }}</h4>
              <p class="cabinet-description">{{ cabinet.description || '暂无描述' }}</p>
            </div>
            <div class="card-footer">
              <el-button type="primary" class="manage-btn" @click="goDeviceList(cabinet)">设备管理</el-button>
              <div v-if="authStore.isAdmin" class="action-icons">
                 <el-button :icon="Edit" circle text @click="openEditCabinet(cabinet)"></el-button>
                 <el-popconfirm
                    title="确定删除此机柜吗？"
                    @confirm="delCabinet(cabinet)"
                  >
                    <template #reference>
                      <el-button :icon="Delete" circle text type="danger"></el-button>
                    </template>
                  </el-popconfirm>
              </div>
            </div>
          </el-card>
        </div>
        <el-empty v-else description="请从左侧选择一个机柜排进行管理" />
      </div>
    </div>

    <!-- 弹窗部分保持不变，但样式会更协调 -->
    <el-dialog v-model="rowEditVisible" :title="rowEditMode === 'add' ? '新增机柜排' : '编辑机柜排'" width="400px">
      <el-form :model="rowEditForm" label-width="80px">
        <el-form-item label="排名称">
          <el-input v-model="rowEditForm.name" placeholder="例如：A排"/>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="rowEditForm.description" type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rowEditVisible = false">取消</el-button>
        <el-button v-if="authStore.isAdmin" type="primary" @click="submitRowEdit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="cabinetEditVisible" :title="cabinetEditMode === 'add' ? '新增机柜' : '编辑机柜'" width="500px">
      <el-form :model="cabinetEditForm" label-width="90px">
        <el-form-item label="机柜名称">
          <el-input v-model="cabinetEditForm.name" placeholder="例如：A01机柜"/>
        </el-form-item>
        <el-form-item label="机柜编号">
          <el-input v-model="cabinetEditForm.number" placeholder="例如：CAB-A01"/>
        </el-form-item>
        <el-form-item label="机柜列数">
          <el-input v-model="cabinetEditForm.columnNumber" placeholder="例如：1"/>
        </el-form-item>
        <el-form-item label="正面图片" v-if="authStore.isAdmin">
          <el-upload action="/api/images" :show-file-list="false" :on-success="handleUploadFrontSuccess" :data="{ type: 'cabinet_front' }" :before-upload="beforeUpload">
            <el-button>上传图片</el-button>
          </el-upload>
          <img v-if="cabinetEditForm.imageFrontUrl" :src="cabinetEditForm.imageFrontUrl" class="upload-preview" />
        </el-form-item>
        <el-form-item label="背面图片" v-if="authStore.isAdmin">
          <el-upload action="/api/images" :show-file-list="false" :on-success="handleUploadBackSuccess" :data="{ type: 'cabinet_back' }" :before-upload="beforeUpload">
            <el-button>上传图片</el-button>
          </el-upload>
          <img v-if="cabinetEditForm.imageBackUrl" :src="cabinetEditForm.imageBackUrl" class="upload-preview" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="cabinetEditForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cabinetEditVisible = false">取消</el-button>
        <el-button v-if="authStore.isAdmin" type="primary" @click="submitCabinetEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 导入依赖模块
 */
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElPopconfirm } from 'element-plus'
import { ArrowLeft, Plus, Edit, Delete, Files } from '@element-plus/icons-vue'
import { useAuthStore } from '../../store/auth'

const authStore = useAuthStore();

/**
 * 响应式状态定义
 */
const route = useRoute()
const router = useRouter()
const roomId = ref(route.query.roomId) // 从路由参数获取当前机房ID
const roomName = ref('') // 新增：用于存储机房名称

// --- 机柜排相关状态 ---
const rows = ref([])
const selectedRowId = ref(null)
const rowEditVisible = ref(false)
const rowEditMode = ref('add')
const rowEditForm = ref({ id: null, roomId: roomId.value, name: '', description: '' })

// --- 机柜相关状态 ---
const cabinets = ref([])
const cabinetEditVisible = ref(false)
const cabinetEditMode = ref('add')
const cabinetEditForm = ref({
  id: null,
  rowId: null,
  name: '',
  number: '',
  columnNumber: '',
  imageFront: '',
  imageFrontUrl: '',
  imageBack: '',
  imageBackUrl: '',
  description: ''
})

/**
 * 计算属性：对机柜按列数排序
 */
const sortedCabinets = computed(() => {
  if (!cabinets.value) return []
  // 使用slice()创建副本再排序，避免直接修改原始数组
  return cabinets.value.slice().sort((a, b) => {
    const colA = Number(a.columnNumber) || 0;
    const colB = Number(b.columnNumber) || 0;
    return colA - colB;
  });
});

/**
 * 获取当前机房信息（为了显示名称）
 */
async function fetchRoomInfo() {
  if (!roomId.value) return;
  try {
    const res = await axios.get(`/api/rooms/${roomId.value}`);
    if (res.data.code === 0) {
      roomName.value = res.data.data.name;
    }
  } catch (error) {
    console.error("获取机房名称失败", error);
  }
}

/**
 * 获取机柜排列表
 * API: GET /api/rows?roomId={id}
 */
async function fetchRows() {
  if (!roomId.value) return
  try {
    const res = await axios.get('/api/rows', { params: { roomId: roomId.value } })
    if (res.data.code === 0) {
      rows.value = res.data.data || []
      // 如果之前没有选中，或者选中的ID已不存在，则默认选中第一个
      if (rows.value.length > 0 && (!selectedRowId.value || !rows.value.some(r => r.id === selectedRowId.value))) {
        handleSelectRow(rows.value[0].id)
      } else if (rows.value.length === 0) {
        selectedRowId.value = null
      }
    } else {
      ElMessage.error(res.data.message || '获取机柜排列表失败')
    }
  } catch (error) {
    ElMessage.error('请求机柜排列表失败')
  }
}

/**
 * 获取指定排下的机柜列表
 * API: GET /api/cabinets?rowId={id}
 */
async function fetchCabinets(rowId) {
  if (!rowId) {
    cabinets.value = []
    return
  }
  try {
    const res = await axios.get('/api/cabinets', { params: { rowId } })
    if (res.data.code === 0) {
      const list = res.data.data || []
      // 并行获取图片URL
      await Promise.all(list.map(async (cab) => {
        if (cab.imageFront) {
          try {
            const imgRes = await axios.get(`/api/images/${cab.imageFront}`)
            cab.imageFrontUrl = imgRes.data.data.url
          } catch { cab.imageFrontUrl = '' }
        }
        if (cab.imageBack) {
          try {
            const imgRes = await axios.get(`/api/images/${cab.imageBack}`)
            cab.imageBackUrl = imgRes.data.data.url
          } catch { cab.imageBackUrl = '' }
        }
      }))
      cabinets.value = list
    } else {
      ElMessage.error(res.data.message || '获取机柜列表失败')
      cabinets.value = []
    }
  } catch (error) {
    ElMessage.error('请求机柜列表失败')
    cabinets.value = []
  }
}

/**
 * 处理选择机柜排的事件
 */
function handleSelectRow(rowId) {
  selectedRowId.value = rowId
}

/**
 * 监听selectedRowId的变化，自动加载机柜
 */
watch(selectedRowId, (newRowId) => {
  fetchCabinets(newRowId)
})

/**
 * 打开新增机柜排弹窗
 */
function openAddRow() {
  rowEditMode.value = 'add'
  rowEditForm.value = { id: null, roomId: roomId.value, name: '', description: '' }
  rowEditVisible.value = true
}

/**
 * 打开编辑机柜排弹窗
 * @param {Object} row - 表格中当前行的数据
 */
function openEditRow(row) {
  rowEditMode.value = 'edit'
  rowEditForm.value = { ...row }
  rowEditVisible.value = true
}

/**
 * 提交新增/编辑机柜排表单
 */
async function submitRowEdit() {
  const payload = { ...rowEditForm.value }
  if (!payload.name) {
    ElMessage.warning('请输入机柜排名称')
    return;
  }
  try {
    if (rowEditMode.value === 'add') {
      await axios.post('/api/rows', payload)
      ElMessage.success('新增成功')
    } else {
      await axios.put(`/api/rows/${payload.id}`, payload)
      ElMessage.success('修改成功')
    }
    rowEditVisible.value = false
    fetchRows()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

/**
 * 删除机柜排
 * @param {Object} row - 表格中当前行的数据
 */
async function delRow(row) {
  try {
    await axios.delete(`/api/rows/${row.id}`)
    ElMessage.success('删除成功')
    if (selectedRowId.value === row.id) {
      selectedRowId.value = null
    }
    fetchRows()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

/**
 * 打开新增机柜弹窗
 */
function openAddCabinet() {
  cabinetEditMode.value = 'add'
  cabinetEditForm.value = {
    id: null, rowId: selectedRowId.value, name: '', number: '', columnNumber: '',
    imageFront: '', imageFrontUrl: '', imageBack: '', imageBackUrl: '', description: ''
  }
  cabinetEditVisible.value = true
}

/**
 * 打开编辑机柜弹窗
 * @param {Object} row - 表格中当前行的数据
 */
function openEditCabinet(cabinet) {
  cabinetEditMode.value = 'edit'
  cabinetEditForm.value = { ...cabinet }
  cabinetEditVisible.value = true
}

/**
 * 正面图片上传成功回调
 * @param {Object} res - 后端返回的响应数据
 */
function handleUploadFrontSuccess(res) {
  if (res.code === 0) {
    cabinetEditForm.value.imageFront = res.data.id
    cabinetEditForm.value.imageFrontUrl = res.data.url
  }
}

/**
 * 背面图片上传成功回调
 * @param {Object} res - 后端返回的响应数据
 */
function handleUploadBackSuccess(res) {
  if (res.code === 0) {
    cabinetEditForm.value.imageBack = res.data.id
    cabinetEditForm.value.imageBackUrl = res.data.url
  }
}

/**
 * 删除机柜
 * API: DELETE /api/cabinets/{id}
 * @param {Object} row - 表格中当前行的数据
 */
async function delCabinet(row) {
  try {
    await axios.delete(`/api/cabinets/${row.id}`)
    ElMessage.success('删除成功')
    fetchCabinets(selectedRowId.value)
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

/**
 * 提交新增/编辑机柜表单
 * 新增 API: POST /api/cabinets
 * 编辑 API: PUT /api/cabinets/{id}
 */
async function submitCabinetEdit() {
  const payload = { ...cabinetEditForm.value }
   if (!payload.name || !payload.number) {
    ElMessage.warning('请填写机柜名称和编号')
    return;
  }
  delete payload.imageFrontUrl
  delete payload.imageBackUrl
  
  try {
    if (cabinetEditMode.value === 'add') {
      await axios.post('/api/cabinets', payload)
      ElMessage.success('新增成功')
    } else {
      await axios.put(`/api/cabinets/${payload.id}`, payload)
      ElMessage.success('修改成功')
    }
    cabinetEditVisible.value = false
    fetchCabinets(selectedRowId.value)
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

/**
 * 上传前的校验
 * @param {File} file - 用户选择的文件
 */
function beforeUpload(file) {
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过10MB！')
  }
  return isLt10M
}

/**
 * 跳转到设备列表页面
 * @param {Object} row - 表格中当前行的数据
 */
function goDeviceList(row) {
  router.push({ path: '/devices', query: { cabinetId: row.id, roomId: roomId.value } })
}

/**
 * 返回上一页
 */
function goBack() {
  router.back()
}

/**
 * Vue生命周期钩子：组件挂载到DOM后执行
 * 用于初始化页面数据
 */
onMounted(() => {
  fetchRoomInfo();
  fetchRows();
});
</script>

<style scoped>
.cabinet-list-container {
  padding: 24px;
  display: flex;
  flex-direction: column;
  height: 100%;
  box-sizing: border-box;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-shrink: 0;
}

.action-bar-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}
.page-subtitle {
  font-size: 18px;
  color: #909399;
}

.main-content {
  display: flex;
  gap: 24px;
  flex-grow: 1;
  overflow: hidden;
}

.sidebar {
  width: 280px;
  flex-shrink: 0;
  background-color: #f9fafb;
  border-radius: 8px;
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 12px;
}

.sidebar-title {
  font-size: 18px;
  margin: 0;
}

.row-list {
  overflow-y: auto;
  flex-grow: 1;
}

.row-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.row-item .row-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.row-item:hover {
  background-color: #ecf5ff;
}

.row-item.active {
  background-color: #409eff;
  color: #fff;
}

.row-actions {
  display: none;
}

.row-item:hover .row-actions {
  display: block;
}
.row-item.active .row-actions {
  display: block;
}
/* Active state icon color fix */
.row-item.active .row-actions .el-button {
  color: #fff;
}


.content-area {
  flex-grow: 1;
  overflow-y: auto;
  padding: 8px;
  background: #fff;
  border-radius: 8px;
}

.cabinets-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.cabinet-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
}

.card-header-image {
  position: relative;
}

.cabinet-image {
  width: 100%;
  height: 180px;
  display: block;
}

.cabinet-number-tag {
  position: absolute;
  top: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.card-body {
  padding: 16px;
}

.cabinet-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px;
}

.cabinet-description {
  font-size: 14px;
  color: #606266;
  min-height: 40px;
}

.card-footer {
  padding: 0 16px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-preview {
  width: 150px;
  margin-top: 10px;
  border-radius: 4px;
}

.add-cabinet-in-empty {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 200px;
  width: 100%;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  color: #909399;
}
.add-cabinet-in-empty .el-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.add-cabinet-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  color: var(--el-text-color-secondary);
  background-color: #f9fafb;
  min-height: 200px; /* 保证和其他卡片差不多高 */
}

.add-cabinet-card:hover {
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
  background-color: #ecf5ff;
}

.add-cabinet-card .el-icon {
  font-size: 40px;
  margin-bottom: 12px;
}
</style> 