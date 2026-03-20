<template>
  <div class="settings-content">
    <div v-if="currentMenu === 'data-dictionary'" class="data-dictionary">
      <div class="content-header">
        <h3>数据字典</h3>
        <el-button type="primary" size="small" @click="handleAddRootCategory">
          <i class="el-icon-plus"></i> 新建一级分类
        </el-button>
      </div>
      <div class="dictionary-tree">
        <el-tree
          ref="categoryTree"
          :data="categoryTreeData"
          :props="treeProps"
          node-key="id"
          :expand-on-click-node="false"
          :default-expanded-keys="expandedKeys"
          @node-click="handleNodeClick"
        >
          <template #default="{ node, data }">
            <span class="tree-node">
              <i class="el-icon-folder"></i>
              <span class="node-label">{{ data.name }}</span>
              <span class="node-actions">
                <el-button
                  v-if="data.level === 1"
                  type="text"
                  size="mini"
                  @click.stop="handleAddSubCategory(data)"
                >
                  <i class="el-icon-plus"></i>
                </el-button>
                <el-button
                  type="text"
                  size="mini"
                  @click.stop="handleEditCategory(data)"
                >
                  <i class="el-icon-edit"></i>
                </el-button>
                <el-button
                  type="text"
                  size="mini"
                  @click.stop="handleDeleteCategory(data)"
                >
                  <i class="el-icon-delete"></i>
                </el-button>
              </span>
            </span>
          </template>
        </el-tree>
      </div>
    </div>

    <div v-else-if="currentMenu === 'data-management'" class="data-management">
      <div class="content-header">
        <h3>数据管理</h3>
      </div>
      <div class="management-actions">
        <el-card class="action-card">
          <div class="action-item">
            <div class="action-icon">
              <i class="el-icon-folder-delete"></i>
            </div>
            <div class="action-content">
              <h4>清空数据字典</h4>
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

    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form :model="categoryForm" :rules="categoryRules" ref="categoryForm" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="categoryForm.sortOrder" :min="0" :max="999"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveCategory">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'SettingsContent',
  data() {
    return {
      currentMenu: 'data-dictionary',
      categoryTreeData: [],
      expandedKeys: [],
      dialogVisible: false,
      dialogTitle: '新建分类',
      categoryForm: {
        id: null,
        name: '',
        parentId: null,
        sortOrder: 0
      },
      categoryRules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        sortOrder: [
          { required: true, message: '请输入排序', trigger: 'blur' }
        ]
      },
      treeProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  computed: {
    categories() {
      return this.$store.getters.allCategories
    }
  },
  watch: {
    '$store.getters.settingsMenu': {
      handler(newVal) {
        this.currentMenu = newVal
        if (newVal === 'data-dictionary') {
          this.loadCategories()
        }
      },
      immediate: true
    }
  },
  async mounted() {
    await this.loadCategories()
  },
  methods: {
    async loadCategories() {
      await this.$store.dispatch('fetchCategories')
      this.buildCategoryTree()
    },
    buildCategoryTree() {
      const categories = this.categories || []
      const buildTree = (parentId = 0) => {
        return categories
          .filter(cat => cat.parentId === parentId)
          .map(cat => ({
            ...cat,
            level: parentId === 0 ? 1 : 2,
            children: buildTree(cat.id)
          }))
      }
      this.categoryTreeData = buildTree()
    },
    handleNodeClick(data) {
      console.log('Node clicked:', data)
    },
    handleAddRootCategory() {
      this.dialogTitle = '新建一级分类'
      this.categoryForm = {
        id: null,
        name: '',
        parentId: 0,
        sortOrder: 0
      }
      this.dialogVisible = true
    },
    handleAddSubCategory(data) {
      this.dialogTitle = '新建二级分类'
      this.categoryForm = {
        id: null,
        name: '',
        parentId: data.id,
        sortOrder: 0
      }
      this.dialogVisible = true
    },
    handleEditCategory(data) {
      this.dialogTitle = '编辑分类'
      this.categoryForm = {
        id: data.id,
        name: data.name,
        parentId: data.parentId,
        sortOrder: data.sortOrder || 0
      }
      this.dialogVisible = true
    },
    async handleDeleteCategory(data) {
      const hasChildren = data.children && data.children.length > 0
      if (hasChildren) {
        this.$message.warning('该分类下有子分类，请先删除子分类')
        return
      }
      
      try {
        await this.$confirm('确定要删除该分类吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await this.$api.categoryApi.deleteCategory(data.id)
        if (response.code === 200) {
          this.$message.success('删除成功')
          await this.loadCategories()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除分类失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    async handleSaveCategory() {
      this.$refs.categoryForm.validate(async (valid) => {
        if (!valid) return
        
        try {
          let response
          if (this.categoryForm.id) {
            response = await this.$api.categoryApi.updateCategory(this.categoryForm.id, this.categoryForm)
          } else {
            response = await this.$api.categoryApi.createCategory(this.categoryForm)
          }
          
          if (response.code === 200) {
            this.$message.success(this.categoryForm.id ? '更新成功' : '创建成功')
            this.dialogVisible = false
            await this.loadCategories()
          } else {
            this.$message.error(response.message || '操作失败')
          }
        } catch (error) {
          console.error('保存分类失败:', error)
          this.$message.error('操作失败')
        }
      })
    },
    handleDialogClose() {
      this.$refs.categoryForm.resetFields()
    },
    async handleClearCategories() {
      try {
        await this.$confirm('确定要清空所有数据字典吗？此操作不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })
        
        const categories = this.categories
        for (const category of categories) {
          await this.$api.categoryApi.deleteCategory(category.id)
        }
        
        this.$message.success('清空成功')
        await this.loadCategories()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清空数据字典失败:', error)
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
.settings-content {
  height: 100%;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.content-header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.content-header h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #303133;
}

.data-dictionary {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.dictionary-tree {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding-right: 20px;
}

.node-label {
  flex: 1;
  font-size: 14px;
}

.node-actions {
  display: flex;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tree-node:hover .node-actions {
  opacity: 1;
}

.data-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.management-actions {
  flex: 1;
  padding: 20px;
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

.dialog-footer {
  text-align: right;
}
</style>
