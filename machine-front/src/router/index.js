import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '首页', isPublic: true }
  },
  // 机房管理
  {
    path: '/rooms',
    name: 'RoomList',
    component: () => import('../views/room/RoomList.vue'),
    meta: { title: '机房管理' }
  },
  // 机柜管理
  {
    path: '/cabinets',
    name: 'CabinetList',
    component: () => import('../views/cabinet/CabinetList.vue'),
    meta: { title: '机柜管理' }
  },
  // 设备管理
  {
    path: '/devices',
    name: 'DeviceList',
    component: () => import('../views/device/DeviceList.vue'),
    meta: { title: '设备管理' }
  },
  // 接口管理
  {
    path: '/interfaces',
    name: 'InterfaceList',
    component: () => import('../views/interface/InterfaceList.vue'),
    meta: { title: '接口管理' }
  },
  // 新增：兼容 /interface 路由
  {
    path: '/interface',
    name: 'InterfaceListSingle',
    component: () => import('../views/interface/InterfaceList.vue'),
    meta: { title: '接口管理' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.isAuthenticated;

  if (!to.meta.isPublic && !isAuthenticated) {
    authStore.openLoginModal();
    next(false);
  } else {
    next();
  }
});

export default router; 