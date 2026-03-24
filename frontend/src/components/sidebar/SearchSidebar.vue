<template>
  <div class="sidebar-content">
    <div class="sidebar-header">
      <h3>搜索</h3>
    </div>
    <div class="sidebar-body">
      <el-input
        v-model="searchKeyword"
        placeholder="输入关键词搜索..."
        clearable
        @keyup.enter="handleSearch"
        class="search-input"
      >
        <template #append>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i> 搜索
          </el-button>
        </template>
      </el-input>

      <div v-if="searchLoading" class="loading">
        <i class="el-icon-loading"></i> 搜索中...
      </div>
      <div v-else-if="!searchResults" class="empty-list">
        <el-empty description="请输入关键词开始搜索" />
      </div>
      <div v-else class="search-results">
        <!-- 文档结果 -->
        <div v-if="searchResults.docs && searchResults.docs.length > 0" class="result-section">
          <h4 class="section-title">
            <i class="el-icon-document"></i> 文档结果
          </h4>
          <div class="search-item" v-for="doc in searchResults.docs" :key="doc.id" @click="handleSelectDocument(doc)">
            <span class="search-item-title">{{ doc.title }}</span>
            <span class="search-score">{{ Math.round(doc.score * 100) }}%</span>
          </div>
        </div>
        
        <!-- 对话结果 -->
        <div v-if="searchResults.conversations && searchResults.conversations.length > 0" class="result-section">
          <h4 class="section-title">
            <i class="el-icon-chat-dot-round"></i> 对话结果
          </h4>
          <div class="search-item" v-for="conversation in searchResults.conversations" :key="conversation.id" @click="handleSelectConversation(conversation)">
            <span class="search-item-title">{{ conversation.title }}</span>
            <span class="search-score">{{ Math.round(conversation.score * 100) }}%</span>
          </div>
        </div>
        
        <!-- 无结果 -->
        <div v-if="(!searchResults.docs || searchResults.docs.length === 0) && (!searchResults.conversations || searchResults.conversations.length === 0)" class="empty-list">
          <el-empty description="未找到相关结果" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SearchSidebar',
  data() {
    return {
      searchKeyword: ''
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
          type: 'all'
        })
      } catch (error) {
        console.error('搜索失败:', error)
        this.$message.error('搜索失败，请重试')
      }
    },
    handleSelectDocument(document) {
      // 为文档对象添加 type 属性
      const docWithType = {
        ...document,
        type: 'doc'
      };
      this.$emit('item-click', 'doc', docWithType)
    },
    handleSelectConversation(conversation) {
      // 为对话对象添加 type 属性
      const conversationWithType = {
        ...conversation,
        type: 'conversation'
      };
      this.$emit('item-click', 'conversation', conversationWithType)
    }
  }
}
</script>

<style scoped>
.result-section {
  margin-bottom: 20px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 10px 0;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 5px;
}

.search-item-title {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
}
</style>
