<template>
  <div class="search-content">
    <div class="search-header">
      <el-input
        v-model="searchKeyword"
        placeholder="输入关键词搜索..."
        clearable
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i> 搜索
          </el-button>
        </template>
      </el-input>
      <div class="search-options">
        <el-radio-group v-model="searchType">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="doc">文档</el-radio-button>
          <el-radio-button label="conversation">对话</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    
    <div class="search-results">
      <div v-if="searchLoading" class="loading">
        <i class="el-icon-loading"></i> 搜索中...
      </div>
      <div v-else-if="!searchResults" class="empty-search">
        <el-empty description="输入关键词开始搜索" />
      </div>
      <div v-else class="results-container">
        <!-- 文档结果 -->
        <div v-if="searchResults.docs && searchResults.docs.length > 0" class="result-section">
          <h3 class="section-title">
            <i class="el-icon-document"></i> 文档结果
          </h3>
          <div class="result-list">
            <div
              v-for="doc in searchResults.docs"
              :key="doc.id"
              class="result-item"
              @click="handleSelectDocument(doc)"
            >
              <div class="result-title">{{ doc.title }}</div>
              <div class="result-meta">
                <span class="meta-item">{{ doc.viewCount }} 浏览</span>
                <span v-if="doc.categoryName" class="meta-item">{{ doc.categoryName }}</span>
              </div>
              <div class="result-preview">{{ getPreview(doc.content) }}</div>
            </div>
          </div>
        </div>
        
        <!-- 对话结果 -->
        <div v-if="searchResults.conversations && searchResults.conversations.length > 0" class="result-section">
          <h3 class="section-title">
            <i class="el-icon-chat-dot-round"></i> 对话结果
          </h3>
          <div class="result-list">
            <div
              v-for="conversation in searchResults.conversations"
              :key="conversation.id"
              class="result-item"
              @click="handleSelectConversation(conversation)"
            >
              <div class="result-title">{{ conversation.title }}</div>
            </div>
          </div>
        </div>
        
        <!-- 无结果 -->
        <div v-if="(!searchResults.docs || searchResults.docs.length === 0) && (!searchResults.conversations || searchResults.conversations.length === 0)" class="no-results">
          <el-empty description="未找到相关结果" />
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
      searchKeyword: '',
      searchType: 'all'
    }
  },
  computed: {
    searchResults() {
      return this.$store.getters.searchResults
    },
    searchLoading() {
      return this.$store.getters.searchLoading
    }
  },
  methods: {
    async handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.$message.warning('请输入搜索关键词')
        return
      }
      
      try {
        await this.$store.dispatch('search', {
          keyword: this.searchKeyword.trim(),
          type: this.searchType
        })
      } catch (error) {
        console.error('搜索失败:', error)
        this.$message.error('搜索失败，请重试')
      }
    },
    getPreview(content) {
      if (!content) return ''
      const preview = content.replace(/\n/g, ' ').substring(0, 100)
      return preview + (content.length > 100 ? '...' : '')
    },
    async handleSelectDocument(document) {
      try {
        await this.$store.dispatch('fetchDocument', document.id)
        // 切换到文档标签
        this.$emit('switchTab', 'docs')
      } catch (error) {
        console.error('加载文档失败:', error)
        this.$message.error('加载文档失败，请重试')
      }
    },
    async handleSelectConversation(conversation) {
      try {
        await this.$store.dispatch('fetchConversation', conversation.id)
        // 切换到对话标签
        this.$emit('switchTab', 'chat')
      } catch (error) {
        console.error('加载对话失败:', error)
        this.$message.error('加载对话失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
.search-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.search-header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #f5f7fa;
}

.search-options {
  margin-top: 15px;
}

.search-results {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #409EFF;
}

.empty-search {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.results-container {
  flex: 1;
}

.result-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 15px 0;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.result-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.result-item {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  border-left: 3px solid #409EFF;
}

.result-item:hover {
  background-color: #ecf5ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.result-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
}

.result-meta {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.meta-item {
  margin-right: 15px;
}

.result-preview {
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
}

.no-results {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}
</style>