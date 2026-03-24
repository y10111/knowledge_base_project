<template>
  <div class="data-management">
    <div class="content-header">
      <h3>系统数据管理</h3>
    </div>
    <div class="content-body">
      <el-card class="action-card">
        <div class="action-item">
          <div class="action-icon">
            <i class="el-icon-folder-delete"></i>
          </div>
          <div class="action-content">
            <h4>清空文档分类</h4>
            <p>清空所有文档分类，此操作不可恢复</p>
          </div>
          <el-button type="danger" size="small" @click="handleClearCategories">
            清空
          </el-button>
        </div>
      </el-card>

      <el-card class="action-card">
        <div class="action-item">
          <div class="action-icon">
            <i class="el-icon-chat-line-square"></i>
          </div>
          <div class="action-content">
            <h4>清空对话</h4>
            <p>清空所有对话记录，此操作不可恢复</p>
          </div>
          <el-button type="danger" size="small" @click="handleClearConversations">
            清空
          </el-button>
        </div>
      </el-card>

      <el-card class="action-card">
        <div class="action-item">
          <div class="action-icon">
            <i class="el-icon-document-delete"></i>
          </div>
          <div class="action-content">
            <h4>清空文档</h4>
            <p>清空所有文档，此操作不可恢复</p>
          </div>
          <el-button type="danger" size="small" @click="handleClearDocuments">
            清空
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DataManagement',
  methods: {
    async handleClearCategories() {
      try {
        await this.$confirm('确定要清空所有文档分类吗？此操作不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })
        
        const categories = this.$store.getters.allCategories
        for (const category of categories) {
          await this.$api.categoryApi.deleteCategory(category.id)
        }
        
        this.$message.success('清空成功')
        await this.$store.dispatch('fetchCategories')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清空文档分类失败:', error)
          this.$message.error('清空失败')
        }
      }
    },
    async handleClearConversations() {
      try {
        await this.$confirm('确定要清空所有对话吗？此操作不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })
        
        const conversations = this.$store.getters.allConversations
        for (const conversation of conversations) {
          await this.$api.conversationApi.deleteConversation(conversation.id)
        }
        
        this.$store.commit('SET_CONVERSATIONS', [])
        this.$store.commit('SET_CURRENT_CONVERSATION', null)
        this.$store.commit('SET_CONVERSATION_MESSAGES', [])
        this.$message.success('清空成功')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清空对话失败:', error)
          this.$message.error('清空失败')
        }
      }
    },
    async handleClearDocuments() {
      try {
        await this.$confirm('确定要清空所有文档吗？此操作不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })
        
        const documents = this.$store.getters.allDocuments
        for (const document of documents) {
          await this.$api.documentApi.deleteDocument(document.id)
        }
        
        this.$store.commit('SET_DOCUMENTS', [])
        this.$store.commit('SET_CURRENT_DOCUMENT', null)
        this.$message.success('清空成功')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清空文档失败:', error)
          this.$message.error('清空失败')
        }
      }
    }
  }
}
</script>

<style scoped>
.data-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}



.content-body {
  flex: 1;
  overflow-y: auto;
}

.action-card {
  margin-bottom: 20px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 20px;
}

.action-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  background-color: #ecf5ff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #409EFF;
}

.action-content {
  flex: 1;
}

.action-content h4 {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #303133;
}

.action-content p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}
</style>