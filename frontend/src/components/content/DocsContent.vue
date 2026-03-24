<template>
  <div class="docs-content">
    <div v-if="loading" class="loading">
      <i class="el-icon-loading"></i> 加载中...
    </div>
    <div v-else-if="!currentDocument" class="empty-document">
      <el-empty description="请选择一个文档或创建新文档" />
    </div>
    <div v-else class="document-detail">
      <div class="document-header">
        <h2>{{ currentDocument.title }}</h2>
        <div class="document-actions">
          <el-button type="primary" @click="handleEditDocument">编辑</el-button>
          <el-button type="danger" @click="handleDeleteDocument">删除</el-button>
        </div>
      </div>
      <div class="document-meta">
        <span class="meta-item">
          <i class="el-icon-time"></i> {{ formatDate(currentDocument.createdAt) }}
        </span>
        <span class="meta-item">
          <i class="el-icon-view"></i> {{ currentDocument.views }} 浏览
        </span>
        <span class="meta-item">
          <i class="el-icon-thumb"></i> {{ currentDocument.likes }} 点赞
        </span>
      </div>
      <div class="document-content" v-html="renderContent(currentDocument.content)"></div>
    </div>

    <!-- 新建/编辑文档对话框 -->
    <el-dialog
      :title="isEdit ? '编辑文档' : '新建文档'"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="documentForm" :rules="rules" ref="documentForm">
        <el-form-item label="标题" prop="title">
          <el-input v-model="documentForm.title" placeholder="请输入文档标题" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="documentForm.categoryId" placeholder="请选择分类">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="documentForm.content"
            type="textarea"
            :rows="10"
            placeholder="请输入文档内容"
          />
        </el-form-item>
        <el-form-item label="上传人">
          <el-input v-model="documentForm.uploader" placeholder="请输入上传人" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitDocument" :loading="submitLoading">
          {{ isEdit ? '保存' : '创建' }}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DocsContent',
  data() {
    return {
      dialogVisible: false,
      isEdit: false,
      submitLoading: false,
      documentForm: {
        title: '',
        content: '',
        categoryId: null,
        uploader: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入文档标题', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入文档内容', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    currentDocument() {
      return this.$store.getters.currentDocument
    },
    loading() {
      return this.$store.getters.documentsLoading
    },
    categories() {
      return this.$store.getters.allCategories
    }
  },
  methods: {
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString()
    },
    renderContent(content) {
      if (!content) return ''
      return content.replace(/\n/g, '<br>')
    },
    handleCreateDocument() {
      this.isEdit = false
      this.documentForm = {
        title: '',
        content: '',
        categoryId: null,
        uploader: ''
      }
      this.dialogVisible = true
    },
    handleEditDocument() {
      if (!this.currentDocument) return
      this.isEdit = true
      this.documentForm = {
        title: this.currentDocument.title,
        content: this.currentDocument.content,
        categoryId: this.currentDocument.categoryId,
        uploader: this.currentDocument.uploader || ''
      }
      this.dialogVisible = true
    },
    async handleSubmitDocument() {
      this.$refs.documentForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.isEdit) {
              await this.$store.dispatch('updateDocument', {
                id: this.currentDocument.id,
                document: this.documentForm
              })
              this.$message.success('文档更新成功')
            } else {
              await this.$store.dispatch('createDocument', this.documentForm)
              this.$message.success('文档创建成功')
            }
            this.dialogVisible = false
          } catch (error) {
            console.error('操作失败:', error)
            this.$message.error('操作失败，请重试')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async handleDeleteDocument() {
      this.$confirm('确定要删除这个文档吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const success = await this.$store.dispatch('deleteDocument', this.currentDocument.id)
          if (success) {
            this.$message.success('文档删除成功')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请重试')
        }
      }).catch(() => {})
    },
    async loadDocument(docId) {
      try {
        await this.$store.dispatch('fetchDocument', docId)
      } catch (error) {
        console.error('加载文档失败:', error)
        this.$message.error('加载文档失败，请重试')
      }
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
  /* padding: 20px; */
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
  padding: 30px;
  overflow-y: auto;
  flex: 1;
}

.document-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.document-header h2 {
  margin: 0;
  color: #303133;
}

.document-actions {
  display: flex;
  gap: 10px;
}

.document-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  color: #909399;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.document-content {
  line-height: 1.6;
  color: #303133;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
