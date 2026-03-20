<template>
  <div class="docs-content">
    <div v-if="documentsLoading" class="loading">
      <i class="el-icon-loading"></i> 加载中...
    </div>
    <div v-else-if="!currentDocument" class="empty-document">
      <el-empty description="选择一个文档查看详情" />
    </div>
    <div v-else class="document-detail">
      <div class="document-header">
        <h1 class="document-title">{{ currentDocument.title }}</h1>
        <div class="document-meta">
          <span class="meta-item">
            <i class="el-icon-view"></i> {{ currentDocument.viewCount }} 浏览
          </span>
          <span class="meta-item">
            <i class="el-icon-date"></i> {{ formatDate(currentDocument.createdAt) }}
          </span>
          <span v-if="currentDocument.categoryName" class="meta-item">
            <i class="el-icon-folder"></i> {{ currentDocument.categoryName }}
          </span>
        </div>
      </div>
      <div class="document-content">
        <div v-if="currentDocument.content" v-html="renderContent(currentDocument.content)"></div>
        <div v-else class="no-content">
          <el-empty description="文档内容为空" />
        </div>
      </div>
      <div class="document-actions">
        <el-button type="primary" size="small" @click="handleEditDocument">
          <i class="el-icon-edit"></i> 编辑
        </el-button>
        <el-button type="danger" size="small" @click="handleDeleteDocument">
          <i class="el-icon-delete"></i> 删除
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DocsContent',
  computed: {
    currentDocument() {
      return this.$store.getters.currentDocument
    },
    documentsLoading() {
      return this.$store.getters.documentsLoading
    }
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    },
    renderContent(content) {
      // 简单的Markdown渲染（实际项目中建议使用专门的Markdown渲染库）
      return content
        .replace(/\n/g, '<br>')
        .replace(/### (.*?)/g, '<h3>$1</h3>')
        .replace(/## (.*?)/g, '<h2>$1</h2>')
        .replace(/# (.*?)/g, '<h1>$1</h1>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
    },
    handleEditDocument() {
      // 编辑文档逻辑
      this.$message.info('编辑功能开发中')
    },
    handleDeleteDocument() {
      this.$confirm('确定要删除这个文档吗？', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const success = await this.$store.dispatch('deleteDocument', this.currentDocument.id)
          if (success) {
            this.$message.success('文档删除成功')
          } else {
            this.$message.error('文档删除失败')
          }
        } catch (error) {
          console.error('删除文档失败:', error)
          this.$message.error('删除文档失败，请重试')
        }
      }).catch(() => {
        // 取消删除
      })
    }
  }
}
</script>

<style scoped>
.docs-content {
  height: 100%;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #409EFF;
}

.empty-document {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.document-detail {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.document-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.document-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #303133;
}

.document-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #909399;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.document-content {
  flex: 1;
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
}

.document-content h1 {
  font-size: 20px;
  font-weight: 600;
  margin: 24px 0 16px 0;
}

.document-content h2 {
  font-size: 18px;
  font-weight: 600;
  margin: 20px 0 12px 0;
}

.document-content h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 16px 0 8px 0;
}

.document-content strong {
  font-weight: 600;
}

.document-content em {
  font-style: italic;
}

.no-content {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #909399;
}

.document-actions {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
  display: flex;
  gap: 10px;
}
</style>