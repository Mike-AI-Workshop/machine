<template>
  <div class="home-container" v-loading="contentStore.loading">
    <!-- Visual Banner Section -->
    <section class="visual-banner editable-wrapper">
      <el-carousel height="400px" arrow="always">
        <el-carousel-item v-for="(item, index) in homeContent.carousel?.items" :key="index">
          <img :src="item" class="carousel-img" alt="Banner Image" />
        </el-carousel-item>
      </el-carousel>
      <div class="banner-content">
        <h1 class="editable-text" @click="openTextEdit('home.banner.title', homeContent.banner?.title)">
          {{ homeContent.banner?.title }}
          <el-icon v-if="isEditMode" class="edit-icon"><Edit /></el-icon>
        </h1>
        <p class="editable-text" @click="openTextEdit('home.banner.subtitle', homeContent.banner?.subtitle)">
          {{ homeContent.banner?.subtitle }}
          <el-icon v-if="isEditMode" class="edit-icon"><Edit /></el-icon>
        </p>
        <div class="editable-text" @click="openTextEdit('home.banner.button.text', homeContent.banner?.button?.text)">
          <el-button type="primary" size="large" @click.stop="goRoomList">
            {{ homeContent.banner?.button?.text }}
            <el-icon v-if="isEditMode" class="edit-icon-inline"><Edit /></el-icon>
          </el-button>
        </div>
      </div>
       <el-button v-if="isEditMode" class="edit-fab" @click="openImageEdit" :icon="Camera" circle></el-button>
    </section>

    <!-- Feature Cards Section -->
    <section class="feature-cards">
      <transition name="fade-scale" mode="out-in">
        <!-- 原始卡片 -->
        <el-row v-if="!activeCard" :gutter="40" justify="center">
          <el-col :span="9">
            <el-card shadow="hover" class="feature-card" @click="activeCard = 'intro'">
              <template #header>
                <div class="card-header editable-text" @click.stop="openTextEdit('home.feature_cards.intro.title', homeContent.feature_cards?.intro?.title)">
                  <span>{{ homeContent.feature_cards?.intro?.title }}</span>
                  <el-icon v-if="isEditMode" class="edit-icon-inline"><Edit /></el-icon>
                </div>
              </template>
              <p class="editable-text" @click.stop="openTextEdit('home.feature_cards.intro.summary', homeContent.feature_cards?.intro?.summary)">
                {{ homeContent.feature_cards?.intro?.summary }}
                 <el-icon v-if="isEditMode" class="edit-icon-inline"><Edit /></el-icon>
              </p>
              <el-button text type="primary">{{ homeContent.feature_cards?.intro?.button_text }} &rarr;</el-button>
            </el-card>
          </el-col>
          <el-col :span="9">
             <el-card shadow="hover" class="feature-card" @click="activeCard = 'business'">
              <template #header>
                <div class="card-header editable-text" @click.stop="openTextEdit('home.feature_cards.business.title', homeContent.feature_cards?.business?.title)">
                  <span>{{ homeContent.feature_cards?.business?.title }}</span>
                   <el-icon v-if="isEditMode" class="edit-icon-inline"><Edit /></el-icon>
                </div>
              </template>
              <p class="editable-text" @click.stop="openTextEdit('home.feature_cards.business.summary', homeContent.feature_cards?.business?.summary)">
                {{ homeContent.feature_cards?.business?.summary }}
                 <el-icon v-if="isEditMode" class="edit-icon-inline"><Edit /></el-icon>
              </p>
              <el-button text type="primary">{{ homeContent.feature_cards?.business?.button_text }} &rarr;</el-button>
            </el-card>
          </el-col>
        </el-row>

        <!-- 详情视图 -->
        <div v-else class="detailed-view">
          <el-button class="back-button" @click="activeCard = null" circle> &larr; </el-button>
          <!-- 区段简介详情 -->
          <div v-if="activeCard === 'intro'" class="editable-text" @click="openTextEdit('home.feature_cards.intro.details', homeContent.feature_cards?.intro?.details, true)">
            <h2>{{ homeContent.feature_cards?.intro?.title }}</h2>
            <div v-html="homeContent.feature_cards?.intro?.details"></div>
            <el-icon v-if="isEditMode" class="edit-icon-fullscreen"><Edit /></el-icon>
          </div>
          <!-- 区段业务详情 -->
          <div v-if="activeCard === 'business'" class="editable-text" @click="openTextEdit('home.feature_cards.business.details', homeContent.feature_cards?.business?.details, true)">
            <h2>{{ homeContent.feature_cards?.business?.title }}</h2>
            <div v-html="homeContent.feature_cards?.business?.details"></div>
             <el-icon v-if="isEditMode" class="edit-icon-fullscreen"><Edit /></el-icon>
          </div>
        </div>
      </transition>
    </section>

    <!-- Footer -->
    <footer class="home-footer editable-text" @click="openTextEdit('home.footer.text', homeContent.footer?.text)">
      <p>{{ homeContent.footer?.text }}</p>
       <el-icon v-if="isEditMode" class="edit-icon-inline"><Edit /></el-icon>
    </footer>

    <!-- Admin: Edit Mode Toggle Button -->
    <el-button v-if="authStore.isAdmin" class="edit-mode-toggle" 
      :type="isEditMode ? 'success' : 'primary'"
      :icon="isEditMode ? Check : Edit"
      @click="isEditMode = !isEditMode"
      circle
    ></el-button>
    
    <!-- Dialog for Text Edit -->
    <el-dialog v-model="textEditDialogVisible" :title="'编辑 ' + editingKey" width="50%">
      <el-input v-if="!isRichText" v-model="editingValue" :autosize="{ minRows: 3, maxRows: 10 }" type="textarea" />
      <!-- A simple rich text editor could be added here if needed -->
      <div v-else v-html="editingValue" contenteditable="true" ref="richTextEditor" class="rich-text-editor"></div>
      <template #footer>
        <el-button @click="textEditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTextEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- Dialog for Image Edit -->
    <el-dialog v-model="imageEditDialogVisible" title="编辑轮播图" width="60%">
      <div class="carousel-editor">
        <div v-for="(item, index) in homeContent.carousel?.items" :key="index" class="carousel-item-editor">
          <el-image :src="item" fit="cover"></el-image>
          <el-upload
            class="upload-replacer"
            action="/api/images"
            :headers="{ Authorization: `Bearer ${authStore.token}` }"
            :show-file-list="false"
            :on-success="(res) => handleUploadSuccess(res, `home.carousel.items.${index}`)"
            :data="{ type: 'home_carousel' }"
          >
            <el-button type="primary">替换图片</el-button>
          </el-upload>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import { useContentStore } from '../store/content'
import { Edit, Check, Camera } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()
const contentStore = useContentStore()

const activeCard = ref(null)
const isEditMode = ref(false)

// Data binding for dialogs
const textEditDialogVisible = ref(false)
const imageEditDialogVisible = ref(false)
const editingKey = ref('')
const editingValue = ref('')
const isRichText = ref(false)
const richTextEditor = ref(null)

// Computed property for easier access in template
const homeContent = computed(() => contentStore.homeContent)

onMounted(() => {
  contentStore.fetchHomeContent()
})

function goRoomList() {
  if (!authStore.isAuthenticated) {
    authStore.openLoginModal()
  } else {
    router.push('/rooms')
  }
}

// --- Editing Logic ---

function openTextEdit(key, value, rich = false) {
  if (!isEditMode.value) return
  editingKey.value = key
  editingValue.value = value
  isRichText.value = rich
  textEditDialogVisible.value = true
}

function openImageEdit() {
  if (!isEditMode.value) return
  imageEditDialogVisible.value = true
}

async function saveTextEdit() {
  const finalValue = isRichText.value ? richTextEditor.value.innerHTML : editingValue.value;
  await contentStore.updateHomeContent({ [editingKey.value]: finalValue })
  textEditDialogVisible.value = false
}

function handleUploadSuccess(res, key) {
  if (res.code === 0 && res.data) {
    contentStore.updateHomeContent({ [key]: res.data.url })
    ElMessage.success('图片替换成功！')
  } else {
    ElMessage.error('图片上传失败')
  }
}

</script>

<style scoped>
/* Core Typography - Following Modern Sans-Serif approach */
.home-container {
  font-family: 'Inter', 'Manrope', 'Helvetica Neue', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  width: 100%;
  color: #000000;
  background-color: #ffffff;
  padding-bottom: 80px;
}

/* Edit Mode Styles - Maintaining existing functionality */
.edit-mode-toggle {
  position: fixed;
  bottom: 100px;
  right: 30px;
  z-index: 1001;
  width: 50px;
  height: 50px;
  font-size: 24px;
}

.editable-wrapper {
  position: relative;
}

.editable-text {
  position: relative;
  cursor: pointer;
  transition: background-color 0.3s;
}

.editable-text:hover .edit-icon,
.editable-text:hover .edit-icon-inline,
.editable-text:hover .edit-icon-fullscreen {
  opacity: 1;
}

.edit-icon {
  position: absolute;
  top: 50%;
  right: -30px;
  transform: translateY(-50%);
  font-size: 1.5rem;
  color: #2563EB;
  opacity: 0;
  transition: opacity 0.3s;
}

.edit-icon-inline {
  margin-left: 8px;
  color: #2563EB;
  opacity: 0;
  transition: opacity 0.3s;
}

.edit-icon-fullscreen {
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 2rem;
  color: #2563EB;
  opacity: 0;
  transition: opacity 0.3s;
  background: rgba(255,255,255,0.9);
  border-radius: 50%;
  padding: 5px;
}

.edit-fab {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 20;
}

.rich-text-editor {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  min-height: 200px;
  line-height: 1.6;
}

.carousel-editor {
  display: flex;
  justify-content: space-around;
  gap: 32px;
}

.carousel-item-editor {
  text-align: center;
}

.carousel-item-editor .el-image {
  width: 200px;
  height: 120px;
  border: 1px solid #e5e7eb;
  margin-bottom: 16px;
  border-radius: 8px;
}

/* Visual Banner - Dramatic Scaling & Focus */
.visual-banner {
  position: relative;
  text-align: center;
  color: white;
  margin-bottom: 120px;
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(0.4);
}

.banner-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 10;
  width: 90%;
  max-width: 1200px;
}

.banner-content h1 {
  font-size: clamp(3rem, 8vw, 5rem);
  font-weight: 900;
  margin-bottom: 32px;
  text-shadow: 2px 2px 12px rgba(0, 0, 0, 0.9);
  letter-spacing: -0.02em;
  line-height: 1.1;
}

.banner-content p {
  font-size: clamp(1.25rem, 2.5vw, 1.75rem);
  max-width: 800px;
  margin: 0 auto 40px;
  text-shadow: 1px 1px 6px rgba(0, 0, 0, 0.8);
  line-height: 1.5;
  font-weight: 400;
}

/* Feature Cards - Clarity & Space */
.feature-cards {
  padding: 80px 120px;
  background-color: #f9fafb;
  min-height: 500px;
  margin-bottom: 80px;
}

.feature-card {
  text-align: center;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  cursor: pointer;
  background-color: #ffffff;
  padding: 40px 32px;
  height: 100%;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
  border-color: #2563EB;
}

.card-header {
  font-size: 1.75rem;
  font-weight: 800;
  padding: 16px 0;
  color: #000000;
  line-height: 1.2;
}

.feature-card p {
  color: #6b7280;
  padding: 0 8px;
  margin-bottom: 32px;
  min-height: 60px;
  line-height: 1.6;
  font-size: 1.125rem;
  font-weight: 400;
}

/* Detailed View - Professional & Clean */
.detailed-view {
  position: relative;
  padding: 60px 80px;
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.05);
  border: 1px solid #e5e7eb;
  max-width: 1000px;
  margin: 0 auto;
}

.back-button {
  position: absolute;
  top: 24px;
  left: 24px;
  font-size: 1.2rem;
  width: 48px;
  height: 48px;
  z-index: 10;
  border: 1px solid #e5e7eb;
  background-color: #ffffff;
  color: #374151;
  transition: all 0.2s ease;
}

.back-button:hover {
  background-color: #f9fafb;
  border-color: #2563EB;
  color: #2563EB;
}

.detailed-view h2 {
  text-align: center;
  margin-bottom: 40px;
  font-size: 2.5rem;
  font-weight: 900;
  color: #000000;
  line-height: 1.2;
}

.detailed-view p,
.detailed-view ul {
  font-size: 1.125rem;
  line-height: 1.8;
  color: #6b7280;
  max-width: 800px;
  margin: 0 auto 24px;
}

.detailed-view ul {
  padding-left: 40px;
}

.detailed-view li {
  margin-bottom: 12px;
}

/* Transitions - Smooth & Professional */
.fade-scale-enter-active,
.fade-scale-leave-active {
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-scale-enter-from,
.fade-scale-leave-to {
  opacity: 0;
  transform: scale(0.95);
}

.el-button--text {
  font-size: 1rem;
  font-weight: 600;
}

/* Primary Action Color - Single Action Color Strategy */
.el-button--primary {
  background-color: #2563EB;
  border-color: #2563EB;
  font-weight: 600;
  transition: all 0.3s ease;
}

.el-button--primary:hover {
  background-color: #1d4ed8;
  border-color: #1d4ed8;
  transform: translateY(-1px);
}

.el-button--text {
  color: #2563EB;
}

/* Footer - Minimal & Professional */
.home-footer {
  text-align: center;
  padding: 32px;
  color: #9ca3af;
  font-size: 14px;
  background-color: #ffffff;
  border-top: 1px solid #f3f4f6;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 100;
  font-weight: 400;
}

/* Responsive Design */
@media (max-width: 768px) {
  .feature-cards {
    padding: 40px 24px;
  }

  .detailed-view {
    padding: 40px 24px;
    margin: 0 16px;
  }

  .banner-content h1 {
    font-size: 2.5rem;
  }

  .banner-content p {
    font-size: 1.25rem;
  }
}
</style> 