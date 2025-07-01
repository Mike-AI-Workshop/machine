<template>
  <div class="home-container">
    <!-- Visual Banner Section -->
    <section class="visual-banner">
      <el-carousel height="400px" arrow="always">
        <el-carousel-item v-for="item in carouselItems" :key="item.id">
          <img :src="item.img" class="carousel-img" alt="Banner Image" />
        </el-carousel-item>
      </el-carousel>
      <div class="banner-content">
        <h1>智能机房管理平台</h1>
        <p>高效、可靠、可视化的机房资产与运维解决方案</p>
        <el-button type="primary" size="large" @click="goRoomList">开始探索</el-button>
      </div>
    </section>

    <!-- Feature Cards Section -->
    <section class="feature-cards">
      <transition name="fade-scale" mode="out-in">
        <!-- 原始卡片 -->
        <el-row v-if="!activeCard" :gutter="40" justify="center">
          <el-col :span="9">
            <el-card shadow="hover" class="feature-card" @click="activeCard = 'intro'">
              <template #header>
                <div class="card-header">
                  <span>区段简介</span>
                </div>
              </template>
              <p>这里是关于本区段的详细介绍，包括其历史、规划和定位。</p>
              <el-button text type="primary">了解更多 &rarr;</el-button>
            </el-card>
          </el-col>
          <el-col :span="9">
            <el-card shadow="hover" class="feature-card" @click="activeCard = 'business'">
              <template #header>
                <div class="card-header">
                  <span>区段业务</span>
                </div>
              </template>
              <p>本区段专注于提供领先的智能化解决方案，服务覆盖多个行业领域。</p>
              <el-button text type="primary">了解更多 &rarr;</el-button>
            </el-card>
          </el-col>
        </el-row>

        <!-- 详情视图 -->
        <div v-else class="detailed-view">
          <el-button class="back-button" @click="activeCard = null" circle> &larr; </el-button>
          <!-- 区段简介详情 -->
          <div v-if="activeCard === 'intro'">
            <h2>区段简介详情</h2>
            <p>这里是关于区段简介的更详尽的描述。我们可以添加更多的段落、图片或者图表来丰富内容。</p>
            <p>例如，介绍项目的背景、发展历程和未来的愿景等等。</p>
          </div>
          <!-- 区段业务详情 -->
          <div v-if="activeCard === 'business'">
            <h2>区段业务详情</h2>
            <p>这里是关于区段业务的更详尽的描述。可以详细介绍我们提供的服务、技术优势和成功案例。</p>
            <ul>
              <li>智能化解决方案 A</li>
              <li>云服务集成 B</li>
              <li>数据分析平台 C</li>
            </ul>
          </div>
        </div>
      </transition>
    </section>

    <!-- Footer -->
    <footer class="home-footer">
      <p>@2025智能机房管理系统</p>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const authStore = useAuthStore()

const activeCard = ref(null)

const carouselItems = [
  { id: 1, img: '/home-backend1.jpg' },
  { id: 2, img: '/home-backend2.jpg' },
  { id: 3, img: '/home-backend3.jpg' }
]

function goRoomList() {
  if (!authStore.isAuthenticated) {
    authStore.openLoginModal()
  } else {
    router.push('/rooms')
  }
}
</script>

<style scoped>
.home-container {
  width: 100%;
  padding-bottom: 80px; /* 为固定页脚留出空间 */
}

/* Visual Banner */
.visual-banner {
  position: relative;
  text-align: center;
  color: white;
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(0.6);
}

.banner-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 10;
  width: 80%;
}

.banner-content h1 {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.8);
}

.banner-content p {
  font-size: 1.5rem;
  max-width: 800px;
  margin: 0 auto 30px;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.8);
}

/* Feature Cards */
.feature-cards {
  padding: 60px 50px;
  background-color: #f9fafb;
  min-height: 400px; /* 增加最小高度以容纳详情视图 */
}

.feature-card {
  text-align: center;
  transition: all 0.3s ease;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  cursor: pointer; /* 增加手型光标 */
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 1.5rem;
  font-weight: 600;
  padding: 10px 0;
}

.feature-card p {
  color: #606266;
  padding: 0 15px;
  margin-bottom: 25px;
  min-height: 45px;
  line-height: 1.6;
}

/* 详情视图样式 */
.detailed-view {
  position: relative;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.back-button {
  position: absolute;
  top: 20px;
  left: 20px;
  font-size: 1.2rem;
  width: 40px;
  height: 40px;
}

.detailed-view h2 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 2rem;
}

.detailed-view p,
.detailed-view ul {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #606266;
  max-width: 800px;
  margin: 0 auto 20px;
}

.detailed-view ul {
  padding-left: 40px;
}

/* 过渡动画 */
.fade-scale-enter-active,
.fade-scale-leave-active {
  transition: all 0.4s ease;
}

.fade-scale-enter-from,
.fade-scale-leave-to {
  opacity: 0;
  transform: scale(0.95);
}

.el-button--text {
    font-size: 1rem;
}

/* Footer */
.home-footer {
  text-align: center;
  padding: 25px;
  color: #909399;
  font-size: 14px;
  background-color: #ffffff;
  border-top: 1px solid #e4e7ed;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 100;
}
</style> 