<template>
  <div class="search-content">
    <div class="search-results">
      <div v-if="searchLoading" class="loading">
        <i class="el-icon-loading"></i> 搜索中...
      </div>
      <div v-else-if="!selectedItem" class="empty-search">
        <el-empty :image-size="100" description="请搜索后点击具体项目显示详情" />
      </div>
      <div v-else>
        <!-- 文档详情 -->
        <div v-if="selectedItem.type === 'doc'" class="doc-detail">
          <h2 class="detail-title">{{ currentDocument?.title }}</h2>
          <div class="detail-meta">
            <span class="meta-item">{{ currentDocument?.viewCount }} 浏览</span>
            <span class="meta-item">{{ currentDocument?.createdAt }}</span>
          </div>
          <div class="detail-content" v-html="currentDocument?.content"></div>
        </div>
        
        <!-- 对话详情 -->
        <div v-else-if="selectedItem.type === 'conversation'" class="conversation-detail">
          <div class="message-list">
            <div
              v-for="message in conversationMessages"
              :key="message.id"
              class="message-item"
              :class="message.role === 'user' ? 'user-message' : 'assistant-message'"
            >
              <div class="message-header">
                <span class="message-role">{{ message.role === 'user' ? '我' : 'AI' }}</span>
                <span class="message-time">{{ message.createdAt }}</span>
              </div>
              <div class="message-content">{{ message.content }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SearchContent',
  data() {
    return {
      selectedItem: null
    }
  },
  computed: {
    searchLoading() {
      return this.$store.getters.searchLoading
    },
    currentDocument() {
      return this.$store.getters.currentDocument
    },
    currentConversation() {
      return this.$store.getters.currentConversation
    },
    conversationMessages() {
      return this.$store.getters.conversationMessages
    }
  },
  methods: {
    async loadDocument(docId) {
      try {
        await this.$store.dispatch('fetchDocument', docId)
        this.selectedItem = { type: 'doc', id: docId }
      } catch (error) {
        console.error('加载文档失败:', error)
        this.$message.error('加载文档失败，请重试')
      }
    },
    async loadConversation(conversationId) {
      try {
        await this.$store.dispatch('fetchConversation', conversationId)
        this.selectedItem = { type: 'conversation', id: conversationId }
      } catch (error) {
        console.error('加载对话失败:', error)
        this.$message.error('加载对话失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
.search-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 搜索结果区域 */
.search-results {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

/* 加载状态 */
.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #409EFF;
  font-size: 14px;
  gap: 10px;
}

.loading i {
  font-size: 16px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 空状态 */
.empty-search {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.empty-search .el-empty__description {
  font-size: 14px;
  color: #909399;
}

/* 文档详情 */
.doc-detail {
  padding: 24px;
  background-color: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 24px;
}

/* 对话详情 */
.conversation-detail {
  padding: 24px;
  background-color: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 24px;
  max-height: 600px;
  overflow-y: auto;
}

/* 标题样式 */
.detail-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

/* 元数据样式 */
.detail-meta {
  font-size: 12px;
  color: #909399;
  margin-bottom: 16px;
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 文档内容 */
.detail-content {
  font-size: 14px;
  line-height: 1.6;
  color: #303133;
  white-space: pre-wrap;
}

/* 消息列表 */
.message-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 消息项 */
.message-item {
  padding: 16px;
  border-radius: 8px;
  max-width: 80%;
  border: 1px solid #e4e7ed;
}

/* 用户消息 */
.user-message {
  align-self: flex-end;
  background-color: #ecf5ff;
  border-color: #d9ecff;
}

/* AI 消息 */
.assistant-message {
  align-self: flex-start;
  background-color: #f9f9f9;
  border-color: #e4e7ed;
}

/* 消息头部 */
.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 12px;
  color: #909399;
}

/* 消息角色 */
.message-role {
  font-weight: 500;
}

/* 消息内容 */
.message-content {
  font-size: 14px;
  line-height: 1.6;
  color: #303133;
  word-wrap: break-word;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-results {
    padding: 16px;
  }
  
  .doc-detail,
  .conversation-detail {
    padding: 16px;
  }
  
  .detail-title {
    font-size: 18px;
  }
  
  .message-item {
    max-width: 90%;
    padding: 12px;
  }
}
</style>