<!--
 * @文件名: InterfaceList.vue
 * @描述: 设备接口管理页面组件 (V2 - 重构版)。采用现代化二分栏布局，左侧为接口信息卡片，右侧为设备可视化预览，并集成高级图片编辑功能。
 * @API交互:
 *   - 设备相关:
 *     - GET /api/devices/{id} - 获取当前设备详情
 *     - GET /api/images/{id} - 获取设备图片URL
 *   - 接口相关:
 *     - GET /api/interfaces?deviceId={id} - 获取当前设备的接口列表
 *     - GET /api/interfaces/{id} - 获取单个接口详情（用于级联选择回显）
 *     - POST /api/interfaces - 新增接口
 *     - PUT /api/interfaces/{id} - 修改接口
 *     - DELETE /api/interfaces/{id} - 删除接口
 *   - Marker标注相关:
 *     - GET /api/markers?parentType=device&parentId={id}&imageType={type} - 获取设备图片上的标注列表
 *     - POST /api/markers - 新增标注
 *     - DELETE /api/markers/{id} - 删除标注
 *   - 级联选择相关:
 *     - GET /api/rooms - 获取机房列表
 *     - GET /api/cabinets?roomId={id} - 获取机柜列表
 *     - GET /api/devices?cabinetId={id} - 获取设备列表
 * @数据结构:
 *   - 接口对象 (Interface): { id, deviceId, name, number, description, targetId, targetDeviceId }
 *   - 标注对象 (Marker): { id, parentType, parentId, imageType, x, y, name, icon, refType, refId, info }
 * @组件功能:
 *   1. 展示指定设备的接口列表，支持增删改查。
 *   2. 展示设备正/反面图，并在图上通过Marker标注接口或设备位置。
 *   3. 支持Marker的增删改、拖拽定位。
 *   4. 新增/编辑接口时，通过 机房->机柜->设备->接口 的级联选择器设置目标接口。
 *   5. 支持从一个接口跳转到其目标接口所在的设备页面。
-->
<template>
  <div class="interface-list-container">
    <!-- 1. 顶部集成操作栏 -->
    <div class="action-bar">
      <div class="action-bar-left">
        <el-button :icon="ArrowLeft" circle @click="goBack" />
        <div class="page-title-with-context">
          <h2 class="page-title">接口管理</h2>
          <span v-if="deviceName" class="context-name">/ {{ deviceName }}</span>
        </div>
      </div>
    </div>

    <!-- 2. 主体内容区 (左右布局) -->
    <div class="main-content">
      <!-- 2.1 左侧接口列表面板 -->
      <el-card class="interface-list-panel" shadow="never">
        <template #header>
          <div class="panel-header">
            <span>接口列表</span>
            <el-button v-if="authStore.isAdmin" type="primary" :icon="Plus" @click="openAddDialog">新增接口</el-button>
          </div>
        </template>
        <div v-if="formattedInterfaces.length > 0" class="interface-list-scroll-container">
          <div v-for="iface in formattedInterfaces" :key="iface.id" class="interface-card">
            <div class="card-main-info">
              <h4 class="interface-name">{{ iface.name }}</h4>
              <span class="interface-number">{{ iface.number }}</span>
              <p class="interface-description">{{ iface.description || '暂无描述' }}</p>
            </div>
            <div class="card-extra-info">
              <div class="target-info">
                <el-icon><Connection /></el-icon>
                <span>{{ iface.targetInfoText }}</span>
              </div>
              <div class="status-info">
                <el-switch :model-value="iface.status" @change="toggleStatus(iface)" />
                <span class="status-text">{{ iface.status ? '开启' : '关闭' }}</span>
            </div>
          </div>
            <div class="interface-card-actions">
              <el-button v-if="authStore.isAdmin" size="small" @click="openEditDialog(iface)">编辑</el-button>
              <el-button v-if="authStore.isAdmin" size="small" type="danger" @click="deleteInterface(iface.id)">删除</el-button>
              <el-button v-if="iface.targetInfo" class="jump-button" size="small" type="primary" plain @click="navigateToDevice(iface.targetInfo.deviceId)">
                跳转
              </el-button>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无接口" />
      </el-card>

      <!-- 2.2 右侧可视化预览面板 -->
      <el-card class="visual-preview-panel" shadow="never">
        <template #header>
          <div class="panel-header">
            <span>设备可视化预览</span>
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
          <div v-for="marker in markersToShow" :key="marker.id" class="preview-marker" :style="{ left: `${marker.x * 100}%`, top: `${marker.y * 100}%` }">
            <el-popover placement="right" trigger="click" width="250">
              <template #reference>
                <el-button size="small" :type="(marker.ref_id || marker.refId) ? 'primary' : 'warning'" plain>
                  <span>{{ marker.icon || '🔘' }}</span>
                  <span>{{ getMarkerName(marker) }}</span>
            </el-button>
              </template>
              <div class="marker-popover-content">
                <h4>{{ getMarkerName(marker) }}</h4>
                <p><strong>类型:</strong> {{ (marker.ref_type || marker.refType) === 'device' ? '设备' : '接口' }}</p>
                <p><strong>备注:</strong> {{ marker.info || '无' }}</p>
                <el-button
                  v-if="isMarkerLinkable(marker)"
                  type="primary"
                  plain
                  size="small"
                  @click="handleMarkerJump(marker)"
                  style="margin-top: 8px; width: 100%;"
                >
                  跳转
                </el-button>
            </div>
            </el-popover>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 3. 弹窗 -->
    <!-- 新增/编辑接口弹窗 -->
    <el-dialog v-model="editDialogVisible" :title="editMode === 'add' ? '新增接口' : '编辑接口'" width="500px" @close="resetForm">
      <el-form :model="editForm" label-width="80px" ref="editFormRef">
        <el-form-item label="名称" prop="name"><el-input v-model="editForm.name" /></el-form-item>
        <el-form-item label="编号" prop="number"><el-input v-model="editForm.number" /></el-form-item>
        <el-form-item label="目标接口" prop="targetFullPath">
           <el-cascader
            v-model="editForm.targetFullPath"
            :props="cascaderProps"
            clearable
            placeholder="可不选，选择目标接口进行连接"
            style="width: 100%"
           />
        </el-form-item>
        <el-form-item label="描述" prop="description"><el-input v-model="editForm.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button v-if="authStore.isAdmin" type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 全屏图片编辑器 (逻辑基本复用自DeviceList) -->
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
      <div ref="imageAreaRef" class="editor-area">
        <div ref="transformWrapperRef" class="transform-wrapper" :style="transformWrapperStyle">
          <img :src="currentImageUrl" class="editable-image" @mousedown="onImageMouseDown" draggable="false" />
          <!-- Marker渲染 -->
          <div v-for="marker in markersToShow" :key="marker.id" class="editor-marker" :style="markerStyleInEditor(marker)" @mousedown.stop="handleMarkerMouseDown(marker, $event)">
            <el-popover placement="top" trigger="hover" :content="getMarkerName(marker)" :hide-after="0">
              <template #reference>
                <el-button size="small" :type="marker.refId ? 'primary' : 'warning'" plain>
                  <span>{{ marker.icon || '🔘' }}</span>
                  <span>{{ getMarkerName(marker) }}</span>
                </el-button>
              </template>
            </el-popover>
          </div>
          <!-- 临时Marker -->
          <div v-if="showTempMarker" class="editor-marker temp-marker" :style="tempMarkerStyleInEditor" @mousedown.stop="onTempMarkerMouseDown">
            <el-button size="small"><span>{{ tempMarker.icon || '🔘' }}</span><span>[新标注]</span></el-button>
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
          <el-form-item label="图标"><el-select v-model="markerForm.icon" style="width:100%"><el-option v-for="icon in iconOptions" :key="icon" :label="icon" :value="icon" /></el-select></el-form-item>
        <el-form-item label="类型">
             <el-select v-model="markerForm.refType" style="width:100%" @change="markerForm.refId = null">
               <el-option label="接口" value="interface" />
            <el-option label="设备" value="device" />
          </el-select>
        </el-form-item>
          <el-form-item label="关联对象">
             <el-select v-if="markerForm.refType === 'interface'" v-model="markerForm.refId" style="width:100%"><el-option v-for="iface in interfaces" :key="iface.id" :label="iface.name" :value="iface.id" /></el-select>
             <el-select v-else v-model="markerForm.refId" style="width:100%"><el-option :key="device.id" :label="device.name" :value="device.id" /></el-select>
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

    <!-- 加载遮罩层 -->
    <div v-if="isLoading" class="loading-overlay">
      <el-icon class="is-loading" size="26"><Loading /></el-icon>
      <span style="margin-left: 10px;">加载中...</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter, onBeforeRouteUpdate } from 'vue-router';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import { ArrowLeft, Plus, Edit, ZoomIn, ZoomOut, Connection, Loading } from '@element-plus/icons-vue';
import { useAuthStore } from '../../store/auth';

const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();
const deviceId = ref(Number(route.query.deviceId));

// --- 核心数据 ---
const device = ref({});
const deviceName = ref('');
const interfaces = ref([]); // 原始接口列表
const frontImageUrl = ref('');
const backImageUrl = ref('');
const frontMarkers = ref([]);
const backMarkers = ref([]);

// --- 新增：加载状态 ---
const isLoading = ref(false);

// --- UI状态 ---
const showFront = ref(true);
const editDialogVisible = ref(false);
const imageEditVisible = ref(false);
const addMarkerDialogVisible = ref(false);

// --- 表单与临时状态 ---
const editMode = ref('add');
const editFormRef = ref(null);
const editForm = ref({
  id: null,
  name: '',
  number: '',
  description: '',
  targetFullPath: [],
});

const markerEditMode = ref('add');
const markerForm = ref({});
const tempMarker = ref({});
const showTempMarker = ref(false);
const tempMarkerPos = ref({ x: 0.5, y: 0.5 });
const iconOptions = ['🔘','⭐','⚡','🔒','🔑','📌','📎','🖲️','🖱️','🖥️','💡','🔌','🔋'];

// --- 图片编辑器状态 (同DeviceList) ---
const imageScale = ref(1);
const imageOffset = ref({ x: 0, y: 0 });
const editorMode = ref('');
const dragOffset = ref({ x: 0, y: 0 });
const imageAreaRef = ref(null);
const transformWrapperRef = ref(null);
const draggingMarker = ref(null);

// --- 计算属性 ---
const currentImageUrl = computed(() => (showFront.value ? frontImageUrl.value : backImageUrl.value));
const markersToShow = computed(() => (showFront.value ? frontMarkers.value : backMarkers.value));
const getDeviceName = (id) => (id === device.value.id ? device.value.name : '其他设备');
const getInterfaceName = (id) => interfaces.value.find(i => i.id === id)?.name;

const getMarkerName = (marker) => {
  if (!marker) return '';
  // 之前的代码在这里做了兼容，保持不变
  const refId = marker.refId || marker.ref_id;
  const refType = marker.refType || marker.ref_type;
  if (refType === 'device') return getDeviceName(refId) || '[未关联设备]';
  if (refType === 'interface') return getInterfaceName(refId) || '[未关联接口]';
  return '[新标注]';
};

const formatFullPath = (fullPathInfo) => {
  if (!fullPathInfo) return '';
  const parts = [
    fullPathInfo.roomName,
    fullPathInfo.rowName,
    fullPathInfo.cabinetName,
    fullPathInfo.deviceName,
  ];
  return parts.filter(p => p).join(' / ');
};

const formattedInterfaces = computed(() => {
  return interfaces.value.map(iface => {
    let text = '未连接';
    if (iface.targetInfo && iface.targetInfo.fullPath) {
      const path = formatFullPath(iface.targetInfo.fullPath);
      const name = iface.targetInfo.name || '[未知接口]';
      const number = iface.targetInfo.number || '无编号';
      text = `-> ${path} / ${name} (${number})`;
    } else if (iface.targetInfo) {
      const name = iface.targetInfo.name || '[未知接口]';
      const number = iface.targetInfo.number || '无编号';
      text = `-> ${name} (${number})`;
    }
    return { ...iface, targetInfoText: text };
  });
});

const transformWrapperStyle = computed(() => ({
  transform: `translate(${imageOffset.value.x}px, ${imageOffset.value.y}px) scale(${imageScale.value})`,
}));
const markerStyleInEditor = (marker) => ({
  position: 'absolute', left: `${marker.x * 100}%`, top: `${marker.y * 100}%`,
  transform: 'translate(-50%, -50%)', cursor: authStore.isAdmin && editorMode.value === 'move_marker' ? 'grab' : 'pointer'
});
const tempMarkerStyleInEditor = computed(() => ({
  position: 'absolute', left: `${tempMarkerPos.value.x * 100}%`, top: `${tempMarkerPos.value.y * 100}%`,
  transform: 'translate(-50%, -50%)', cursor: 'grab'
}));


// --- 数据加载 ---
async function fetchAllData() {
  if (!deviceId.value) return;
  isLoading.value = true;
  // 重置/清空旧数据，防止闪烁
  interfaces.value = [];
  frontMarkers.value = [];
  backMarkers.value = [];
  frontImageUrl.value = '';
  backImageUrl.value = '';
  deviceName.value = '';

  try {
    // 使用 Promise.all 并行加载，提高速度
    await Promise.all([
      fetchDeviceDetails(),
      fetchInterfaces(),
      fetchMarkers()
    ]);
  } catch (error) {
    console.error("数据加载时发生错误:", error);
    ElMessage.error('加载页面数据失败，请刷新重试');
  } finally {
    isLoading.value = false;
  }
}

async function fetchDeviceDetails() {
  try {
    const res = await axios.get(`/api/devices/${deviceId.value}`);
    if (res.data.code === 0) {
      device.value = res.data.data;
      deviceName.value = device.value.name;
      if (device.value.imageFront) frontImageUrl.value = (await axios.get(`/api/images/${device.value.imageFront}`)).data.data.url;
      if (device.value.imageBack) backImageUrl.value = (await axios.get(`/api/images/${device.value.imageBack}`)).data.data.url;
    }
  } catch { console.error("Failed to fetch device details."); }
}

async function fetchInterfaces() {
  try {
    // 还原: 使用扁平化路由
    const res = await axios.get('/api/interfaces', { params: { deviceId: deviceId.value } });
    const rawInterfaces = res.data.code === 0 ? res.data.data : [];

    // 保留: 异步获取目标接口详细信息的逻辑仍然有效和重要
    const enrichedInterfaces = await Promise.all(
      rawInterfaces.map(async (iface) => {
        let targetInfo = null;
        if (iface.targetId) {
          try {
            // 唯一的一次API调用，获取所有信息
            const targetRes = await axios.get(`/api/interfaces/${iface.targetId}/with-full-path`);
            if (targetRes.data.code === 0) {
              targetInfo = targetRes.data.data;
            }
          } catch (e) {
            console.error(`获取目标接口 ${iface.targetId} 的完整信息失败`, e);
          }
        }
        return { ...iface, targetInfo };
      })
    );
    interfaces.value = enrichedInterfaces;
  } catch (e) {
    console.error("获取接口列表失败。", e);
    interfaces.value = []; // 出错时清空，避免显示旧数据
  }
}

async function fetchMarkers() {
  try {
    // 还原: 使用扁平化路由和 parentType/parentId 参数
    const params = { parentType: 'device', parentId: deviceId.value };
    const resFront = await axios.get('/api/markers', { params: { ...params, imageType: 'front' } });
    frontMarkers.value = resFront.data.code === 0 ? resFront.data.data : [];
    const resBack = await axios.get('/api/markers', { params: { ...params, imageType: 'back' } });
    backMarkers.value = resBack.data.code === 0 ? resBack.data.data : [];
  } catch(e) {
    console.error("获取标注失败", e);
    frontMarkers.value = [];
    backMarkers.value = [];
  }
}

// --- 接口CRUD ---
function resetForm() {
  editForm.value = { id: null, name: '', number: '', description: '', targetFullPath: [] };
}

function openAddDialog() {
  editMode.value = 'add';
  resetForm();
  editDialogVisible.value = true;
}

async function openEditDialog(item) {
  editMode.value = 'edit';
  resetForm();
  // 注意：item是来自计算属性的只读代理，直接修改会警告。应从原始数据源查找。
  const originalItem = interfaces.value.find(i => i.id === item.id) || item;

  // 修复回显逻辑
  let fullPath = [];
  if (originalItem.targetInfo && originalItem.targetInfo.fullPath) {
    const pathInfo = originalItem.targetInfo.fullPath;
    fullPath = [
      pathInfo.roomId,
      pathInfo.rowId,
      pathInfo.cabinetId,
      pathInfo.deviceId,
      originalItem.targetId,
    ].filter(id => id != null);
  }

  editForm.value = {
    id: originalItem.id,
    name: originalItem.name,
    number: originalItem.number,
    description: originalItem.description,
    targetFullPath: fullPath,
  };
  editDialogVisible.value = true;
}

async function deleteInterface(id) {
  await ElMessageBox.confirm('确定删除此接口吗?', '警告', { type: 'warning' });
  await axios.delete(`/api/interfaces/${id}`);
  ElMessage.success('删除成功');
  fetchInterfaces();
}

async function submitEdit() {
  const targetId = editForm.value.targetFullPath?.slice(-1)[0] || null;

  // 最终修复：统一Payload结构，确保所有模式下都包含所需字段
  const payload = {
    deviceId: deviceId.value,
    name: editForm.value.name,
    number: editForm.value.number,
    description: editForm.value.description,
    targetId: targetId,
    status: true, // 默认为true，适用于新增
  };

  if (editMode.value === 'add') {
    // 还原: 新增操作使用扁平化路由
    await axios.post('/api/interfaces', payload);
    ElMessage.success('新增成功');
  } else {
    // 对于编辑模式，获取原始status值并覆盖
    const originalItem = interfaces.value.find(i => i.id === editForm.value.id);
    if (originalItem) {
      payload.status = originalItem.status;
    }
    await axios.put(`/api/interfaces/${editForm.value.id}`, payload);
    ElMessage.success('修改成功');
  }
  editDialogVisible.value = false;
  fetchInterfaces(); // 重新获取数据以刷新UI
}

// --- 级联选择器 ---
const cascaderProps = {
  lazy: true,
  async lazyLoad(node, resolve) {
    const { level, data } = node;
    try {
      if (level === 0) { // Level 0: 加载机房 (无变化)
        const res = await axios.get('/api/rooms');
        const nodes = res.data.data.map(item => ({ value: item.id, label: item.name, leaf: false }));
        resolve(nodes);
      } else if (level === 1) { // Level 1: 还原 - 根据机房ID加载机柜排
        const res = await axios.get('/api/rows', { params: { roomId: data.value } });
        const nodes = res.data.data.map(item => ({ value: item.id, label: item.name, leaf: false }));
        resolve(nodes);
      } else if (level === 2) { // Level 2: 还原 - 根据机柜排ID加载机柜
        const res = await axios.get('/api/cabinets', { params: { rowId: data.value } });
        const nodes = res.data.data.map(item => ({ value: item.id, label: item.name, leaf: false }));
        resolve(nodes);
      } else if (level === 3) { // Level 3: 还原 - 根据机柜ID加载设备
        const res = await axios.get('/api/devices', { params: { cabinetId: data.value } });
        const nodes = res.data.data.map(item => ({ value: item.id, label: item.name, leaf: false }));
        resolve(nodes);
      } else if (level === 4) { // Level 4: 还原 - 根据设备ID加载接口
        const res = await axios.get('/api/interfaces', { params: { deviceId: data.value } });
        const nodes = res.data.data.map(item => ({ value: item.id, label: item.name, leaf: true }));
        resolve(nodes);
      }
    } catch (e) {
      console.error("Cascader lazy load failed:", e);
      resolve([]);
    }
  },
};


// --- 图片编辑器 (逻辑与DeviceList一致) ---
function handleZoomIn() { imageScale.value = Math.min(5, imageScale.value + 0.2); }
function handleZoomOut() { imageScale.value = Math.max(0.2, imageScale.value - 0.2); }
function toggleEditorMode(mode) { editorMode.value = editorMode.value === mode ? '' : mode; }
function onImageMouseDown(e) {
  if (authStore.isAdmin && editorMode.value !== 'move_image') return;
  const dragStartPos = { x: e.clientX - imageOffset.value.x, y: e.clientY - imageOffset.value.y };
  const onMove = (ev) => { imageOffset.value = { x: ev.clientX - dragStartPos.x, y: ev.clientY - dragStartPos.y }; };
  const onUp = () => { window.removeEventListener('mousemove', onMove); window.removeEventListener('mouseup', onUp); };
  window.addEventListener('mousemove', onMove);
  window.addEventListener('mouseup', onUp);
}
function saveImageEdit() {
  ElMessage.success('布局已保存');
  imageEditVisible.value = false;
}

// --- Marker 编辑器 (与DeviceList一致，但表单不同) ---
function startAddMarker() {
  markerEditMode.value = 'add';
  markerForm.value = { icon: '🔘', refType: 'interface', refId: null, info: '' };
  addMarkerDialogVisible.value = true;
}
function onMarkerFormConfirm() {
  if (markerEditMode.value === 'add') {
    tempMarker.value = { ...markerForm.value };
    showTempMarker.value = true;
    tempMarkerPos.value = { x: 0.5, y: 0.5 };
    ElMessage.info('请在图上点选位置，然后点击"确定位置"');
  } else {
    axios.put(`/api/markers/${markerForm.value.id}`, markerForm.value).then(() => {
      ElMessage.success("标注更新成功");
      fetchMarkers();
    });
  }
  addMarkerDialogVisible.value = false;
}
function cancelMarkerEdit() { addMarkerDialogVisible.value = false; }
async function deleteMarker() {
  await ElMessageBox.confirm(`确定删除此标注吗?`, '警告', { type: 'warning' });
  axios.delete(`/api/markers/${markerForm.value.id}`).then(() => {
    fetchMarkers();
    addMarkerDialogVisible.value = false;
    ElMessage.success("删除成功");
  });
}
function onTempMarkerMouseDown(e) {
  const rect = e.currentTarget.getBoundingClientRect();
  const imageRect = transformWrapperRef.value.getBoundingClientRect();
  dragOffset.value = { x: (e.clientX - rect.left) / imageRect.width, y: (e.clientY - rect.top) / imageRect.height };
  const onMove = (ev) => {
    tempMarkerPos.value.x = Math.max(0, Math.min(1, (ev.clientX - imageRect.left) / imageRect.width - dragOffset.value.x));
    tempMarkerPos.value.y = Math.max(0, Math.min(1, (ev.clientY - imageRect.top) / imageRect.height - dragOffset.value.y));
  };
  const onUp = () => { window.removeEventListener('mousemove', onMove); window.removeEventListener('mouseup', onUp); };
  window.addEventListener('mousemove', onMove);
  window.addEventListener('mouseup', onUp);
}
function onTempMarkerDelete() { showTempMarker.value = false; }
async function onTempMarkerConfirm() {
  const markerData = {
    // 还原: 使用 parentType, parentId
    parentType: 'device',
    parentId: deviceId.value,
    imageType: showFront.value ? 'front' : 'back',
    x: tempMarkerPos.value.x, y: tempMarkerPos.value.y,
    // 还原: ...tempMarker.value 包含了 refType, refId 等
    ...tempMarker.value
  };
  // 还原: 使用 /api/markers
  await axios.post('/api/markers', markerData);
  ElMessage.success('添加成功');
  fetchMarkers();
  showTempMarker.value = false;
}
function handleMarkerMouseDown(marker, e) {
  if (authStore.isAdmin && editorMode.value === 'move_marker') {
    draggingMarker.value = marker;
    const rect = e.currentTarget.getBoundingClientRect();
    const imageRect = transformWrapperRef.value.getBoundingClientRect();
    dragOffset.value = { x: (e.clientX - rect.left) / imageRect.width, y: (e.clientY - rect.top) / imageRect.height };
    const onMove = (ev) => {
      if (!draggingMarker.value) return;
      draggingMarker.value.x = Math.max(0, Math.min(1, (ev.clientX - imageRect.left) / imageRect.width - dragOffset.value.x));
      draggingMarker.value.y = Math.max(0, Math.min(1, (ev.clientY - imageRect.top) / imageRect.height - dragOffset.value.y));
    };
    const onUp = () => {
      if (draggingMarker.value) {
        axios.put(`/api/markers/${draggingMarker.value.id}`, draggingMarker.value);
        draggingMarker.value = null;
      }
      window.removeEventListener('mousemove', onMove);
      window.removeEventListener('mouseup', onUp);
    };
    window.addEventListener('mousemove', onMove);
    window.addEventListener('mouseup', onUp);
  } else {
    markerEditMode.value = 'edit';
    markerForm.value = { ...marker };
    addMarkerDialogVisible.value = true;
  }
}

// --- 其他 ---
async function toggleStatus(iface) {
  // 关键修复：从原始数据源 `interfaces.value` 中查找并修改对象
  const originalIface = interfaces.value.find(i => i.id === iface.id);
  if (!originalIface) return;

  const originalStatus = originalIface.status;
  const newStatus = !originalStatus;

  // 1. 乐观更新UI（修改原始对象以触发响应式更新）
  originalIface.status = newStatus;

  try {
    // 2. 调用API端点
    await axios.put(`/api/interfaces/${iface.id}/status`, { status: newStatus });
    ElMessage.success(`接口已${newStatus ? '开启' : '关闭'}`);
  } catch (error) {
    // 3. 如果失败，回滚UI并提示错误
    originalIface.status = originalStatus;
    console.error("更新接口状态失败:", error);
    ElMessage.error('状态更新失败，请重试');
  }
}
function goBack() { router.back(); }

function navigateToDevice(targetDeviceId) {
  if (!targetDeviceId) return;
  router.push({ query: { deviceId: targetDeviceId } });
}

// --- 标注跳转相关 ---
function isMarkerLinkable(marker) {
  const refType = marker.ref_type || marker.refType;
  const refId = marker.ref_id || marker.refId;

  if (refType === 'interface' && refId) {
    const iface = interfaces.value.find(i => i.id === refId);
    // 只要接口存在，并且其 targetId 有效，就认为可以跳转
    return iface && iface.targetId;
  }

  // 对 'device' 类型的判断保持不变（虽然在此页面基本用不到）
  if (refType === 'device' && refId) {
    return refId !== deviceId.value;
  }
  
  return false;
}

async function handleMarkerJump(marker) {
  const refType = marker.ref_type || marker.refType;
  const refId = marker.ref_id || marker.refId;

  if (refType === 'interface' && refId) {
    const iface = interfaces.value.find(i => i.id === refId);
    // 直接从预加载的 targetInfo 中获取 deviceId 进行跳转
    if (iface && iface.targetInfo && iface.targetInfo.deviceId) {
      navigateToDevice(iface.targetInfo.deviceId);
    } else {
      ElMessage.warning('无法找到该接口的跳转信息');
    }
  } else if (refType === 'device' && refId) {
      // 保留设备跳转逻辑
      navigateToDevice(refId);
  }
}

onMounted(fetchAllData);

// 关键修复：使用 onBeforeRouteUpdate 导航守卫处理组件内跳转
onBeforeRouteUpdate((to, from) => {
  // 确保查询参数真的改变了，并且不是无关的查询参数变化
  const newDeviceId = Number(to.query.deviceId);
  if (newDeviceId && newDeviceId !== deviceId.value) {
    deviceId.value = newDeviceId;
    fetchAllData(); // 使用新的ID重新加载所有数据
  }
});
</script> 

<style scoped>
/* Reset & Base */
.interface-list-container {
  padding: 24px;
  display: flex;
  flex-direction: column;
  height: 100%;
  box-sizing: border-box;
  position: relative; /* 为加载遮罩层提供定位上下文 */
}

/* 加载遮罩层样式 */
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  font-size: 1.2rem;
  color: #333;
}

/* Action Bar */
.action-bar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.action-bar-left { display: flex; align-items: center; gap: 16px; }
.page-title-with-context { display: flex; align-items: baseline; gap: 8px; }
.page-title { font-size: 24px; font-weight: 600; margin: 0; }
.context-name { font-size: 20px; font-weight: 400; color: #909399; }

/* Main Content */
.main-content { display: flex; gap: 24px; flex-grow: 1; overflow: hidden; }
.interface-list-panel, .visual-preview-panel { display: flex; flex-direction: column; }
.interface-list-panel { width: 40%; }
.visual-preview-panel { width: 60%; }
.panel-header { display: flex; justify-content: space-between; align-items: center; }

/* Interface List Panel */
.interface-list-panel .el-card__body { padding: 0; flex-grow: 1; display: flex; flex-direction: column; }
.interface-list-scroll-container { flex-grow: 1; overflow-y: auto; padding: 20px; }
.interface-card {
  position: relative;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 16px;
  transition: box-shadow 0.3s;
}
.interface-card:hover { box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.12); }
.interface-card:hover .interface-card-actions { opacity: 1; }
.card-main-info { margin-bottom: 12px; }
.interface-name { font-size: 16px; font-weight: 600; margin: 0 0 4px 0; }
.interface-number { font-size: 12px; color: #909399; margin-bottom: 8px; display: block; }
.interface-description { font-size: 14px; color: #606266; margin: 0; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden; text-overflow: ellipsis; min-height: 40px; }
.card-extra-info { display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #f2f2f2; padding-top: 12px; }
.target-info, .status-info { display: flex; align-items: center; gap: 8px; font-size: 14px; color: #606266; }
.status-info .el-switch { height: auto; }
.interface-card-actions { position: absolute; top: 16px; right: 16px; display: flex; gap: 8px; opacity: 0; transition: opacity 0.3s; background: #fff; padding: 4px; border-radius: 6px; }
.interface-card-actions .el-button + .el-button { margin-left: 0; }
.jump-button { margin-left: 8px; }

/* Visual Preview Panel */
.preview-controls { margin-bottom: 16px; }
.image-preview-container { position: relative; width: 100%; height: calc(100% - 48px); background: #f5f7fa; border-radius: 4px; display: flex; align-items: center; justify-content: center; overflow: hidden; }
.preview-image { max-width: 100%; max-height: 100%; object-fit: contain; }
.preview-marker { position: absolute; transform: translate(-50%, -50%); }
.marker-popover-content h4 { margin: 0 0 10px; }
.marker-popover-content p { margin: 4px 0; font-size: 14px; }

/* Fullscreen Editor (Copied from DeviceList) */
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
</style> 