<template>
  <div class="category-management">
    <div class="content-header">
      <h3>系统数据管理</h3>
      <el-button type="primary" size="small" @click="handleAddRootCategory">
        <i class="el-icon-plus"></i> 新建一级分类
      </el-button>
    </div>
    <div class="category-tree">
      <el-tree
        ref="categoryTree"
        :data="categoryTreeData"
        :props="treeProps"
        node-key="id"
        :expand-on-click-node="false"
        :default-expanded-keys="expandedKeys"
        @node-expand="handleNodeExpand"
        @node-collapse="handleNodeCollapse"
        @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <span class="tree-node">
            <i class="el-icon-folder"></i>
            <span class="node-label">{{ data.name }}</span>
            <span class="node-actions">
              <el-button
                type="text"
                size="mini"
                @click.stop="handleMoveUp(data)"
                :disabled="!canMoveUp(data)"
              >
                <i class="el-icon-arrow-up"></i>
              </el-button>
              <el-button
                type="text"
                size="mini"
                @click.stop="handleMoveDown(data)"
                :disabled="!canMoveDown(data)"
              >
                <i class="el-icon-arrow-down"></i>
              </el-button>
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
    <CategoryDialog
      :visible="dialogVisible"
      :title="dialogTitle"
      :form="categoryForm"
      :rules="categoryRules"
      @close="handleDialogClose"
      @save="handleSaveCategory"
    />
  </div>
</template>

<script>
import CategoryDialog from './CategoryDialog.vue'

export default {
  name: 'CategoryManagement',
  components: {
    CategoryDialog
  },
  data() {
    return {
      categoryTreeData: [],
      expandedKeys: [],
      dialogVisible: false,
      dialogTitle: '新建分类',
      categoryForm: {
        id: null,
        name: '',
        parentId: null
      },
      categoryRules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
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
          .sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
          .map(cat => ({
            ...cat,
            level: parentId === 0 ? 1 : 2,
            children: buildTree(cat.id)
          }))
      }
      this.categoryTreeData = buildTree()
    },
    handleNodeExpand(data) {
      if (!this.expandedKeys.includes(data.id)) {
        this.expandedKeys.push(data.id)
      }
    },
    handleNodeCollapse(data) {
      const index = this.expandedKeys.indexOf(data.id)
      if (index > -1) {
        this.expandedKeys.splice(index, 1)
      }
    },
    handleNodeClick(data) {
      console.log('Node clicked:', data)
    },
    canMoveUp(data) {
      const siblings = this.getSiblings(data)
      const index = siblings.findIndex(cat => cat.id === data.id)
      return index > 0
    },
    canMoveDown(data) {
      const siblings = this.getSiblings(data)
      const index = siblings.findIndex(cat => cat.id === data.id)
      return index < siblings.length - 1
    },
    getSiblings(data) {
      const parentId = data.parentId || 0
      return this.categories
        .filter(cat => cat.parentId === parentId)
        .sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    },
    async handleMoveUp(data) {
      console.log('handleMoveUp called with data:', data)
      const siblings = this.getSiblings(data)
      console.log('siblings:', siblings)
      const index = siblings.findIndex(cat => cat.id === data.id)
      console.log('index:', index)
      if (index <= 0) {
        console.log('Cannot move up, index is:', index)
        return
      }
      
      const prevCategory = siblings[index - 1]
      const currentSortOrder = data.sortOrder || 0
      const prevSortOrder = prevCategory.sortOrder || 0
      
      console.log('Moving up:', data.id, 'with sortOrder:', currentSortOrder, 'to:', prevSortOrder)
      console.log('Moving down:', prevCategory.id, 'with sortOrder:', prevSortOrder, 'to:', currentSortOrder)
      
      try {
        await this.$api.categoryApi.updateCategory(data.id, {
          name: data.name,
          parentId: data.parentId,
          sortOrder: prevSortOrder
        })
        await this.$api.categoryApi.updateCategory(prevCategory.id, {
          name: prevCategory.name,
          parentId: prevCategory.parentId,
          sortOrder: currentSortOrder
        })
        await this.loadCategories()
      } catch (error) {
        console.error('移动失败:', error)
        this.$message.error('移动失败')
      }
    },
    async handleMoveDown(data) {
      console.log('handleMoveDown called with data:', data)
      const siblings = this.getSiblings(data)
      console.log('siblings:', siblings)
      const index = siblings.findIndex(cat => cat.id === data.id)
      console.log('index:', index)
      if (index >= siblings.length - 1) {
        console.log('Cannot move down, index is:', index, 'and siblings length is:', siblings.length)
        return
      }
      
      const nextCategory = siblings[index + 1]
      const currentSortOrder = data.sortOrder || 0
      const nextSortOrder = nextCategory.sortOrder || 0
      
      console.log('Moving down:', data.id, 'with sortOrder:', currentSortOrder, 'to:', nextSortOrder)
      console.log('Moving up:', nextCategory.id, 'with sortOrder:', nextSortOrder, 'to:', currentSortOrder)
      
      try {
        await this.$api.categoryApi.updateCategory(data.id, {
          name: data.name,
          parentId: data.parentId,
          sortOrder: nextSortOrder
        })
        await this.$api.categoryApi.updateCategory(nextCategory.id, {
          name: nextCategory.name,
          parentId: nextCategory.parentId,
          sortOrder: currentSortOrder
        })
        await this.loadCategories()
      } catch (error) {
        console.error('移动失败:', error)
        this.$message.error('移动失败')
      }
    },
    handleAddRootCategory() {
      this.dialogTitle = '新建一级分类'
      this.categoryForm = {
        id: null,
        name: '',
        parentId: 0
      }
      this.dialogVisible = true
    },
    handleAddSubCategory(data) {
      this.dialogTitle = '新建二级分类'
      this.categoryForm = {
        id: null,
        name: '',
        parentId: data.id
      }
      this.dialogVisible = true
    },
    handleEditCategory(data) {
      this.dialogTitle = '编辑分类'
      this.categoryForm = {
        id: data.id,
        name: data.name,
        parentId: data.parentId
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
        
        const savedExpandedKeys = [...this.expandedKeys]
        
        console.log('删除分类:', data.id)
        const response = await this.$api.categoryApi.deleteCategory(data.id)
        console.log('删除分类响应:', response)
        
        if (response.code === 200) {
          this.$message.success('删除成功')
          const index = savedExpandedKeys.indexOf(data.id)
          if (index > -1) {
            savedExpandedKeys.splice(index, 1)
          }
          await this.loadCategories()
          this.expandedKeys = savedExpandedKeys
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除分类失败:', error)
          this.$message.error('删除失败: ' + (error.message || '未知错误'))
        }
      }
    },
    async handleSaveCategory() {
      const savedExpandedKeys = [...this.expandedKeys]
      
      try {
        let response
        if (this.categoryForm.id) {
          console.log('更新分类:', this.categoryForm)
          response = await this.$api.categoryApi.updateCategory(this.categoryForm.id, this.categoryForm)
          console.log('更新分类响应:', response)
        } else {
          const siblings = this.categories.filter(cat => cat.parentId === this.categoryForm.parentId)
          const maxSortOrder = siblings.length > 0 
            ? Math.max(...siblings.map(cat => cat.sortOrder || 0))
            : -1
          const newSortOrder = maxSortOrder + 1
          
          const categoryData = {
            ...this.categoryForm,
            sortOrder: newSortOrder
          }
          
          console.log('创建分类:', categoryData)
          response = await this.$api.categoryApi.createCategory(categoryData)
          console.log('创建分类响应:', response)
        }
        
        if (response.code === 200) {
          this.$message.success(this.categoryForm.id ? '更新成功' : '创建成功')
          this.dialogVisible = false
          await this.loadCategories()
          this.expandedKeys = savedExpandedKeys
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('保存分类失败:', error)
        this.$message.error('操作失败: ' + (error.message || '未知错误'))
      }
    },
    handleDialogClose() {
      this.dialogVisible = false
      this.categoryForm = {
        id: null,
        name: '',
        parentId: null
      }
    }
  }
}
</script>

<style scoped>
.category-management {
  height: 100%;
  display: flex;
  flex-direction: column;
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

.category-tree {
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
  opacity: 1;
  transition: opacity 0.3s;
}

.tree-node:hover .node-actions {
  opacity: 1;
}
</style>