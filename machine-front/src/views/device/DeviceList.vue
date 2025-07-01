<!--
 * @文件名: DeviceList.vue
 * @描述: 设备管理页面组件。负责展示和管理指定机柜内的所有设备，并提供在机柜图片上进行设备位置的可视化标注（Marker）功能。
 * @API交互:
 *   - 机柜相关:
 *     - GET /api/cabinets/{id} - 获取当前机柜详情，用于加载机柜正反面图片
 *   - 设备相关:
 *     - GET /api/devices?cabinetId={id} - 获取当前机柜的设备列表
 *     - POST /api/devices - 新增设备
 *     - PUT /api/devices/{id} - 修改设备
 *     - DELETE /api/devices/{id} - 删除设备
 *   - 图片相关:
 *     - POST /api/images - 上传设备图片 (type: 'device_front'/'device_back')
 *     - GET /api/images/{id} - 获取图片URL
 *   - Marker标注相关:
 *     - GET /api/markers?parent_type=cabinet&parentId={id}&image_type={type} - 获取机柜图片上的标注列表
 *     - POST /api/markers - 新增标注
 *     - PUT /api/markers/{id} - 修改标注 (如拖拽后更新位置)
 *     - DELETE /api/markers/{id} - 删除标注
 * @数据结构:
 *   - 设备对象 (Device): { id, cabinetId, name, number, imageFront, imageBack, description }
 *   - 标注对象 (Marker): { id, parentType, parentId, imageType, x, y, name, icon, refType, refId, info }
 * @组件功能:
 *   1. 展示指定机柜的设备列表，支持增删改查。
 *   2. 展示机柜正/反面图，并通过Marker标注设备位置。
 *   3. 支持在图片上对Marker进行增删改、拖拽定位。
 *   4. 支持图片编辑功能，如缩放、平移。
 *   5. 支持跳转到具体设备的接口管理页面。
-->
<template>
  <div class="device-list-container">
    <!-- 1. 顶部集成操作栏 -->
    <div class="action-bar">
      <div class="action-bar-left">
        <el-button :icon="ArrowLeft" circle @click="goBack" />
        <div class="page-title-with-context">
          <h2 class="page-title">设备管理</h2>
          <span v-if="cabinetName" class="context-name">/ {{ cabinetName }}</span>
    </div>
      </div>
    </div>

    <!-- 2. 主体内容区 (左右布局) -->
    <div class="main-content">
      <!-- 2.1 左侧设备列表面板 -->
      <el-card class="device-list-panel" shadow="never">
        <template #header>
          <div class="panel-header">
            <span>设备列表</span>
            <el-button v-if="authStore.isAdmin" type="primary" :icon="Plus" @click="openAdd">新增设备</el-button>
          </div>
        </template>
        <div v-if="devices.length > 0" class="device-list-scroll-container">
          <div v-for="device in devices" :key="device.id" class="device-card">
            <el-image :src="device.imageFrontUrl" fit="cover" class="device-card-image">
              <template #error>
                <div class="image-slot-error">
                  <span>暂无图片</span>
      </div>
              </template>
            </el-image>
            <div class="device-card-info">
              <div class="info-header">
                <h4 class="device-name">{{ device.name }}</h4>
                <span class="device-number">{{ device.number }}</span>
    </div>
              <p class="device-description">{{ device.description || '暂无描述' }}</p>
              <div class="device-card-actions">
                <el-button v-if="authStore.isAdmin" size="small" @click="openEdit(device)">编辑</el-button>
                <el-button v-if="authStore.isAdmin" size="small" type="danger" @click="delDevice(device)">删除</el-button>
                <el-button size="small" type="primary" @click="goInterfaceList(device)">接口</el-button>
      </div>
    </div>
      </div>
    </div>
        <el-empty v-else description="暂无设备" />
      </el-card>

      <!-- 2.2 右侧可视化预览面板 -->
      <el-card class="visual-preview-panel" shadow="never">
        <template #header>
          <div class="panel-header">
            <span>机柜可视化预览</span>
            <el-button v-if="authStore.isAdmin" type="primary" :icon="Edit" text @click="imageEditVisible = true">编辑布局</el-button>
      </div>
        </template>
        <div class="preview-controls">
          <el-radio-group v-model="showFront" size="small">
            <el-radio-button :value="true">正面</el-radio-button>
            <el-radio-button :value="false">背面</el-radio-button>
          </el-radio-group>
        </div>
        <div class="image-preview-container">
          <img :src="currentImageUrl" class="preview-image" v-if="currentImageUrl" />
          <el-empty description="暂无图片" v-else />
          <!-- 渲染Marker -->
          <div
            v-for="marker in markersToShow"
            :key="marker.id"
            class="preview-marker"
            :style="{ left: `${marker.x * 100}%`, top: `${marker.y * 100}%` }"
          >
            <el-popover
              placement="right"
              trigger="click"
              width="250"
            >
              <template #reference>
                <el-button size="small" :type="marker.refId ? 'primary' : 'warning'" plain>
              <span>{{ marker.icon || '🔘' }}</span>
                  <span>{{ getDeviceName(marker.refId) || '[未关联]' }}</span>
            </el-button>
              </template>
              <!-- Popover内容 -->
              <div class="marker-popover-content">
                <h4>{{ getDeviceName(marker.refId) || '[未关联]' }}</h4>
                <p><strong>类型:</strong> 设备</p>
                <p><strong>备注:</strong> {{ marker.info || '无' }}</p>
                <div class="popover-actions">
                  <el-button size="small" @click="goInterfaceList({ id: marker.refId })" v-if="marker.refId">管理接口</el-button>
                </div>
              </div>
            </el-popover>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 3. 弹窗 -->
    <!-- 新增/编辑设备弹窗 -->
    <el-dialog v-model="editVisible" :title="editMode === 'add' ? '新增设备' : '编辑设备'" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="editForm.name" /></el-form-item>
        <el-form-item label="编号"><el-input v-model="editForm.number" /></el-form-item>
        <el-form-item label="正面图片" v-if="authStore.isAdmin">
          <el-upload action="/api/images" :show-file-list="false" :on-success="handleDeviceFrontUploadSuccess" :data="{ type: 'device_front' }">
            <el-button>上传图片</el-button>
          </el-upload>
          <img v-if="editForm.imageFrontUrl" :src="editForm.imageFrontUrl" class="upload-preview" />
        </el-form-item>
        <el-form-item label="背面图片" v-if="authStore.isAdmin">
          <el-upload action="/api/images" :show-file-list="false" :on-success="handleDeviceBackUploadSuccess" :data="{ type: 'device_back' }">
            <el-button>上传图片</el-button>
          </el-upload>
          <img v-if="editForm.imageBackUrl" :src="editForm.imageBackUrl" class="upload-preview" />
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="editForm.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button v-if="authStore.isAdmin" type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>
    
    <!-- 全屏图片编辑器 -->
    <el-dialog v-model="imageEditVisible" fullscreen custom-class="fullscreen-editor-dialog">
       <template #header>
        <div class="editor-toolbar">
          <div class="toolbar-left">
            <el-button-group>
              <el-button :icon="ZoomIn" @click="handleZoomIn">放大</el-button>
              <el-button :icon="ZoomOut" @click="handleZoomOut">缩小</el-button>
            </el-button-group>
            <template v-if="authStore.isAdmin">
              <el-divider direction="vertical" />
              <el-button type="primary" :icon="Plus" @click="startAddMarker">添加标注</el-button>
              <el-divider direction="vertical" />
              <el-button-group>
                  <el-button :type="editorMode === 'move_image' ? 'primary' : 'default'" @click="toggleEditorMode('move_image')">移动图片</el-button>
                  <el-button :type="editorMode === 'move_marker' ? 'primary' : 'default'" @click="toggleEditorMode('move_marker')">移动标注</el-button>
              </el-button-group>
            </template>
          </div>
          <div class="toolbar-right">
            <el-button @click="imageEditVisible = false">取消</el-button>
            <el-button v-if="authStore.isAdmin" type="success" @click="saveImageEdit">保存并退出</el-button>
          </div>
            </div>
      </template>
      <!-- 编辑器主体 -->
      <div ref="imageAreaRef" class="editor-area">
        <div ref="transformWrapperRef" class="transform-wrapper" :style="transformWrapperStyle">
          <img :src="currentImageUrl" class="editable-image" @mousedown="onImageMouseDown" draggable="false" />
          <!-- Marker渲染 -->
          <div v-for="marker in markersToShow" :key="marker.id" class="editor-marker"
               :style="markerStyleInEditor(marker)"
               @mousedown.stop="handleMarkerMouseDown(marker, $event)">
              <el-popover placement="top" trigger="hover" :content="getDeviceName(marker.refId) || '[未关联]'" :hide-after="0">
                 <template #reference>
                    <el-button size="small" :type="marker.refId ? 'primary' : 'warning'" plain>
                      <span>{{ marker.icon || '🔘' }}</span>
                      <span>{{ getDeviceName(marker.refId) || '[新标注]' }}</span>
                    </el-button>
                 </template>
              </el-popover>
          </div>
          <!-- 临时Marker -->
          <div v-if="showTempMarker" class="editor-marker temp-marker" :style="tempMarkerStyleInEditor" @mousedown.stop="onTempMarkerMouseDown">
            <el-button size="small">
              <span>{{ tempMarker.icon || '🔘' }}</span>
              <span>{{ getDeviceName(tempMarker.refId) || '[新标注]' }}</span>
            </el-button>
            <div class="temp-marker-actions">
              <el-button size="small" type="danger" @click.stop="onTempMarkerDelete">删除</el-button>
              <el-button size="small" type="primary" @click.stop="onTempMarkerConfirm">确定位置</el-button>
            </div>
          </div>
        </div>
      </div>
       <!-- Marker信息填写弹窗 (嵌套) -->
      <el-dialog v-model="addMarkerDialogVisible" title="添加/编辑标注" width="400px" append-to-body>
        <el-form :model="markerForm" label-width="80px">
          <el-form-item label="图标">
            <el-select v-model="markerForm.icon" style="width:100%"><el-option v-for="icon in iconOptions" :key="icon" :label="icon" :value="icon" /></el-select>
          </el-form-item>
          <el-form-item label="关联对象">
            <el-select v-model="markerForm.refId" style="width:100%"><el-option v-for="dev in devices" :key="dev.id" :label="dev.name" :value="dev.id" /></el-select>
          </el-form-item>
          <el-form-item label="备注"><el-input v-model="markerForm.info" /></el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="cancelMarkerEdit">取消</el-button>
          <el-button type="danger" v-if="authStore.isAdmin && markerEditMode === 'edit'" @click="deleteMarker" style="margin-right: auto;">删除此标注</el-button>
          <el-button v-if="authStore.isAdmin" type="primary" @click="onMarkerFormConfirm">确定</el-button>
        </template>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Plus, Edit, ZoomIn, ZoomOut } from '@element-plus/icons-vue'
import { useAuthStore } from '../../store/auth'

const authStore = useAuthStore();
const route = useRoute()
const router = useRouter()
const cabinetId = ref(route.query.cabinetId)
const cabinetName = ref('')

// --- 核心数据 ---
const devices = ref([])
const cabinetFrontUrl = ref('')
const cabinetBackUrl = ref('')
const frontMarkers = ref([])
const backMarkers = ref([])

// --- UI状态 ---
const showFront = ref(true)
const editVisible = ref(false)
const imageEditVisible = ref(false)
const addMarkerDialogVisible = ref(false)

// --- 表单与临时状态 ---
const editMode = ref('add')
const editForm = ref({})
const markerEditMode = ref('add')
const markerForm = ref({})
const tempMarker = ref({})
const showTempMarker = ref(false)
const tempMarkerPos = ref({ x: 0.5, y: 0.5 })

// --- 图片编辑器状态 ---
const imageScale = ref(1)
const imageOffset = ref({ x: 0, y: 0 })
const editorMode = ref('') // 'move_image', 'move_marker' or ''
const draggingImage = ref(false)
const dragStart = ref({ x: 0, y: 0 })
const dragOffset = ref({ x: 0, y: 0 }) // For smooth dragging
const imageAreaRef = ref(null)
const transformWrapperRef = ref(null)
const draggingMarker = ref(null)
const iconOptions = ['🔘','⭐','⚡','🔒','🔑','📌','📎','🖲️','🖱️','🖥️','💡','🔌','🔋']

// --- 计算属性 ---
const currentImageUrl = computed(() => showFront.value ? cabinetFrontUrl.value : cabinetBackUrl.value)
const markersToShow = computed(() => showFront.value ? frontMarkers.value : backMarkers.value)
const getDeviceName = (id) => devices.value.find(d => d.id === id)?.name

const transformWrapperStyle = computed(() => ({
  transform: `translate(${imageOffset.value.x}px, ${imageOffset.value.y}px) scale(${imageScale.value})`,
}))
const markerStyleInEditor = (marker) => ({
  position: 'absolute', left: `${marker.x * 100}%`, top: `${marker.y * 100}%`,
  transform: 'translate(-50%, -50%)', cursor: authStore.isAdmin && editorMode.value === 'move_marker' ? 'grab' : 'pointer'
})
const tempMarkerStyleInEditor = computed(() => ({
  position: 'absolute', left: `${tempMarkerPos.value.x * 100}%`, top: `${tempMarkerPos.value.y * 100}%`,
  transform: 'translate(-50%, -50%)', cursor: 'grab'
}))

// --- 数据加载 ---
async function fetchAllData() {
  await fetchCabinetDetails()
  await fetchDevices()
  await fetchMarkers()
}
async function fetchCabinetDetails() {
  try {
    const res = await axios.get(`/api/cabinets/${cabinetId.value}`)
    if (res.data.code === 0) {
      const cab = res.data.data
      cabinetName.value = cab.name
      if (cab.imageFront) cabinetFrontUrl.value = (await axios.get(`/api/images/${cab.imageFront}`)).data.data.url
      if (cab.imageBack) cabinetBackUrl.value = (await axios.get(`/api/images/${cab.imageBack}`)).data.data.url
    }
  } catch { console.error("Failed to fetch cabinet details.") }
}
async function fetchDevices() {
  try {
    const res = await axios.get('/api/devices', { params: { cabinetId: cabinetId.value } });
    if (res.data.code !== 0) {
      devices.value = [];
      return;
    }

    const fetchedDevices = res.data.data || [];

    // 并行获取所有设备的图片URL
    const devicesWithUrls = await Promise.all(
        fetchedDevices.map(async (device) => {
          let imageFrontUrl = '';
          let imageBackUrl = '';

          if (device.imageFront) {
            try {
              const imgRes = await axios.get(`/api/images/${device.imageFront}`);
              if (imgRes.data.code === 0) {
                imageFrontUrl = imgRes.data.data.url;
              }
            } catch (e) {
              console.error(`Failed to fetch front image for device ${device.id}`, e);
            }
          }

          if (device.imageBack) {
            try {
              const imgRes = await axios.get(`/api/images/${device.imageBack}`);
              if (imgRes.data.code === 0) {
                imageBackUrl = imgRes.data.data.url;
              }
            } catch (e) {
              console.error(`Failed to fetch back image for device ${device.id}`, e);
            }
          }

          return { ...device, imageFrontUrl, imageBackUrl };
        })
    );

    devices.value = devicesWithUrls;
  } catch (error) {
    console.error("Failed to fetch devices:", error);
    devices.value = [];
  }
}
async function fetchMarkers() {
  const params = { parentType: 'cabinet', parentId: cabinetId.value }
  const resFront = await axios.get('/api/markers', { params: { ...params, imageType: 'front' } })
  frontMarkers.value = resFront.data.code === 0 ? resFront.data.data : []
  const resBack = await axios.get('/api/markers', { params: { ...params, imageType: 'back' } })
  backMarkers.value = resBack.data.code === 0 ? resBack.data.data : []
}

// --- 设备CRUD ---
function openAdd() {
  editMode.value = 'add'
  editForm.value = {
    cabinetId: cabinetId.value, name: '', number: '', description: '',
    imageFront: null, imageBack: null, imageFrontUrl: '', imageBackUrl: ''
  }
  editVisible.value = true
}
function openEdit(row) {
  editMode.value = 'edit'
  editForm.value = { ...row }
  editVisible.value = true
}
async function delDevice(row) {
  await ElMessageBox.confirm(`确定删除设备 "${row.name}" 吗?`, '警告', { type: 'warning' })
  await axios.delete(`/api/devices/${row.id}`)
  ElMessage.success('删除成功')
  fetchDevices()
}
async function submitEdit() {
  const payload = { ...editForm.value };
  delete payload.imageFrontUrl;
  delete payload.imageBackUrl;

  if (editMode.value === 'add') {
    await axios.post('/api/devices', payload)
    ElMessage.success('新增成功')
  } else {
    await axios.put(`/api/devices/${editForm.value.id}`, payload)
    ElMessage.success('修改成功')
  }
  editVisible.value = false
  fetchDevices()
}

/**
 * 处理设备图片上传成功回调
 * @param {object} res - 后端响应
 * @param {string} type - 'front' or 'back'
 */
function handleDeviceImageUploadSuccess(res, type) {
  if (res.code === 0 && res.data) {
    if (type === 'front') {
      editForm.value.imageFront = res.data.id;
      editForm.value.imageFrontUrl = res.data.url;
    } else {
      editForm.value.imageBack = res.data.id;
      editForm.value.imageBackUrl = res.data.url;
    }
    ElMessage.success('图片上传成功');
  } else {
    ElMessage.error('图片上传失败');
  }
}

const handleDeviceFrontUploadSuccess = (res) => handleDeviceImageUploadSuccess(res, 'front');
const handleDeviceBackUploadSuccess = (res) => handleDeviceImageUploadSuccess(res, 'back');

// --- Marker编辑器核心逻辑 ---
function startAddMarker() {
  markerEditMode.value = 'add'
  markerForm.value = { icon: '🔘', refId: null, info: '' }
  addMarkerDialogVisible.value = true
}

function onMarkerFormConfirm() {
  if (markerEditMode.value === 'add') {
    tempMarker.value = { ...markerForm.value, name: getDeviceName(markerForm.value.refId) }
    showTempMarker.value = true
    tempMarkerPos.value = { x: 0.5, y: 0.5 }
    ElMessage.info('请在图上点选位置，然后点击"确定位置"')
  } else { // edit mode
    const markerToUpdate = { ...markerForm.value }
    axios.put(`/api/markers/${markerToUpdate.id}`, markerToUpdate).then(() => {
      ElMessage.success("标注更新成功")
      fetchMarkers()
    })
  }
  addMarkerDialogVisible.value = false
}

function cancelMarkerEdit() {
    addMarkerDialogVisible.value = false
}

async function deleteMarker() {
  const deviceName = getDeviceName(markerForm.value.refId) || '该'
  await ElMessageBox.confirm(`确定删除关联到 "${deviceName}" 的标注吗?`, '警告', { type: 'warning' })
  axios.delete(`/api/markers/${markerForm.value.id}`).then(() => {
    fetchMarkers()
    addMarkerDialogVisible.value = false
    ElMessage.success("删除成功")
  })
}

function onTempMarkerMouseDown(e) {
  const rect = e.currentTarget.getBoundingClientRect();
  const imageRect = transformWrapperRef.value.getBoundingClientRect();
  dragOffset.value = {
    x: (e.clientX - rect.left) / imageRect.width,
    y: (e.clientY - rect.top) / imageRect.height
  };
  
  window.addEventListener('mousemove', onTempMarkerMouseMove)
  window.addEventListener('mouseup', onTempMarkerMouseUp)
}
function onTempMarkerMouseMove(e) {
  const rect = transformWrapperRef.value.getBoundingClientRect()
  tempMarkerPos.value.x = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width - dragOffset.value.x))
  tempMarkerPos.value.y = Math.max(0, Math.min(1, (e.clientY - rect.top) / rect.height - dragOffset.value.y))
}
function onTempMarkerMouseUp() {
  window.removeEventListener('mousemove', onTempMarkerMouseMove)
  window.removeEventListener('mouseup', onTempMarkerMouseUp)
}
function onTempMarkerDelete() { showTempMarker.value = false }
async function onTempMarkerConfirm() {
  const markerData = {
    parentType: 'cabinet', parentId: cabinetId.value,
    imageType: showFront.value ? 'front' : 'back',
    x: tempMarkerPos.value.x, y: tempMarkerPos.value.y,
    refType: 'device', // 强制类型
    name: getDeviceName(tempMarker.value.refId) || '新标注', // 自动生成name
    ...tempMarker.value
  }
  delete markerData.name // name不存入数据库，仅用于显示
  await axios.post('/api/markers', markerData)
  ElMessage.success('添加成功')
  fetchMarkers()
  showTempMarker.value = false
}

// --- 图片/Marker拖拽 ---
function handleMarkerMouseDown(marker, e) {
  if (authStore.isAdmin && editorMode.value === 'move_marker') {
    draggingMarker.value = marker;
    const rect = e.currentTarget.getBoundingClientRect();
    const imageRect = transformWrapperRef.value.getBoundingClientRect();
    dragOffset.value = {
        x: (e.clientX - rect.left) / imageRect.width,
        y: (e.clientY - rect.top) / imageRect.height,
    };
    window.addEventListener('mousemove', handleMarkerMouseMove)
    window.addEventListener('mouseup', handleMarkerMouseUp)
  } else {
      markerEditMode.value = 'edit'
      markerForm.value = { ...marker }
      addMarkerDialogVisible.value = true
  }
}
function handleMarkerMouseMove(e) {
  if (!draggingMarker.value) return
  const rect = transformWrapperRef.value.getBoundingClientRect()
  draggingMarker.value.x = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width - dragOffset.value.x))
  draggingMarker.value.y = Math.max(0, Math.min(1, (e.clientY - rect.top) / rect.height - dragOffset.value.y))
}
async function handleMarkerMouseUp() {
  if (draggingMarker.value) {
    await axios.put(`/api/markers/${draggingMarker.value.id}`, draggingMarker.value)
    draggingMarker.value = null
  }
  window.removeEventListener('mousemove', handleMarkerMouseMove)
  window.removeEventListener('mouseup', handleMarkerMouseUp)
}

// --- 缩放/平移 ---
function handleZoomIn() { imageScale.value = Math.min(5, imageScale.value + 0.2) }
function handleZoomOut() { imageScale.value = Math.max(0.2, imageScale.value - 0.2) }

function toggleEditorMode(mode) {
  editorMode.value = editorMode.value === mode ? '' : mode;
}

function onImageMouseDown(e) {
  if (authStore.isAdmin && editorMode.value !== 'move_image') return
  dragStart.value = { x: e.clientX - imageOffset.value.x, y: e.clientY - imageOffset.value.y }
  window.addEventListener('mousemove', onImageMouseMove)
  window.addEventListener('mouseup', onImageMouseUp)
}
function onImageMouseMove(e) {
  imageOffset.value = { x: e.clientX - dragStart.value.x, y: e.clientY - dragStart.value.y }
}
function onImageMouseUp() {
  window.removeEventListener('mousemove', onImageMouseMove)
  window.removeEventListener('mouseup', onImageMouseUp)
}

function saveImageEdit() {
  ElMessage.success('布局已保存')
  imageEditVisible.value = false
}

// --- 导航 ---
function goInterfaceList(row) {
  router.push({ path: '/interfaces', query: { deviceId: row.id } })
}
function goBack() { router.back() }

onMounted(fetchAllData)
</script>

<style scoped>
.device-list-container { padding: 24px; display: flex; flex-direction: column; height: 100%; box-sizing: border-box; }
.action-bar { display: flex; align-items: center; gap: 16px; margin-bottom: 24px; }
.action-bar-left { display: flex; align-items: center; gap: 16px; }
.page-title-with-context {
  display: flex;
  align-items: baseline;
  gap: 8px;
}
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.context-name {
  font-size: 20px;
  font-weight: 400;
  color: #909399;
}
.main-content { display: flex; gap: 24px; flex-grow: 1; overflow: hidden; }
.device-list-panel { width: 40%; display: flex; flex-direction: column; }
.visual-preview-panel { width: 60%; }
.panel-header { display: flex; justify-content: space-between; align-items: center; }
.device-list-panel .el-card__body {
  height: 100%;
  padding: 0;
  display: flex; /* For el-empty centering */
  flex-direction: column;
}
.device-list-scroll-container {
  flex-grow: 1;
  overflow-y: auto;
  padding: 20px;
}
.device-card {
  display: flex;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 16px;
  transition: box-shadow 0.3s;
  position: relative;
}
.device-card:hover {
  box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.12);
}
.device-card:hover .device-card-actions {
  opacity: 1;
}
.device-card-image {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  border-radius: 6px;
  margin-right: 16px;
  background-color: #f5f7fa;
}
.image-slot-error {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #a8abb2;
  font-size: 12px;
}
.device-card-info {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}
.info-header {
  margin-bottom: 8px;
}
.device-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}
.device-number {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
.device-description {
  font-size: 14px;
  color: #606266;
  margin: 0 0 12px 0;
  flex-grow: 1;
  /* Line clamp */
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  min-height: 40px; /* approx 2 lines */
}
.device-card-actions {
  position: absolute;
  bottom: 16px;
  right: 16px;
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}
.preview-controls { margin-bottom: 16px; }
.image-preview-container { position: relative; width: 100%; height: calc(100% - 48px); background: #f5f7fa; border-radius: 4px; display: flex; align-items: center; justify-content: center; }
.preview-image { max-width: 100%; max-height: 100%; object-fit: contain; }
.preview-marker { position: absolute; transform: translate(-50%, -50%); }
.marker-popover-content h4 { margin: 0 0 10px; }
.marker-popover-content p { margin: 4px 0; font-size: 14px; }
.popover-actions { margin-top: 10px; text-align: right; }

/* Fullscreen Editor */
.fullscreen-editor-dialog .el-dialog__header { padding: 0; }
.fullscreen-editor-dialog .el-dialog__body { padding: 0; height: 100%; }
.editor-toolbar { display: flex; justify-content: space-between; align-items: center; background: #fff; padding: 8px 16px; border-bottom: 1px solid #dcdfe6; }
.toolbar-left, .toolbar-right { display: flex; align-items: center; gap: 16px; }
.editor-area { height: calc(100vh - 55px); background-color: #f0f2f5; overflow: hidden; position: relative; }
.transform-wrapper { position: absolute; transform-origin: top left; }
.editable-image { cursor: grab; }
.editor-marker { position: absolute; }
.editor-marker.temp-marker { z-index: 10; border: 2px dashed #f56c6c; padding: 5px; border-radius: 4px; background: rgba(255,255,255,0.8); }
.temp-marker-actions { position: absolute; bottom: -40px; left: 50%; transform: translateX(-50%); display: flex; gap: 8px; }

.upload-preview {
  width: 100px;
  height: 100px;
  margin-top: 10px;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
  object-fit: cover;
}
</style> 