import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export const useContentStore = defineStore('content', () => {
  // State
  const homeContent = ref({})
  const loading = ref(false)

  // Actions
  /**
   * 获取首页内容
   */
  async function fetchHomeContent() {
    loading.value = true
    try {
      const res = await axios.get('/api/content/home')
      if (res.data.code === 0 && res.data.data) {
        homeContent.value = res.data.data
      } else {
        throw new Error(res.data.message || '获取内容失败')
      }
    } catch (error) {
      ElMessage.error(error.message || '请求内容时发生错误')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  /**
   * 更新首页内容
   * @param {Object} contentUpdates - 一个包含需要更新的键值对的对象
   */
  async function updateHomeContent(contentUpdates) {
    loading.value = true
    try {
      const res = await axios.put('/api/content/home', contentUpdates)
      if (res.data.code === 0) {
        ElMessage.success('内容更新成功！')
        // 更新成功后，重新获取最新内容以刷新页面
        await fetchHomeContent()
      } else {
        throw new Error(res.data.message || '更新失败')
      }
    } catch (error) {
      ElMessage.error(error.message || '更新内容时发生错误')
      console.error(error)
    } finally {
      loading.value = false
    }
  }

  return {
    homeContent,
    loading,
    fetchHomeContent,
    updateHomeContent
  }
}) 