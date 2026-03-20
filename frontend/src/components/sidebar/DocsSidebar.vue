<template>
  <div class="docs-sidebar">
    <div class="sidebar-header">
      <h3>文档</h3>
      <el-button type="primary" size="small" @click="handleCreateDocument">
        <i class="el-icon-plus"></i> 新建
      </el-button>
    </div>
    <div class="tree-section">
      <div v-if="localLoading" class="loading">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      <div v-else-if="localTreeData.length === 0" class="empty-list">
        <el-empty description="暂无文档" />
      </div>
      <el-tree
        v-else
        ref="tree"
        :data="localTreeData"
        :props="treeProps"
        node-key="id"
        :expand-on-click-node="false"
        :default-expanded-keys="expandedKeys"
        :highlight-current="false"
        @node-expand="handleNodeExpand"
        @node-collapse="handleNodeCollapse"
        @node-click="handleNodeClick"
        class="document-tree"
      >
        <template #default="{ node, data }">
          <span class="tree-node" :class="{ 'is-document': data.type === 'document', 'is-category': data.type === 'category', 'active': data.type === 'document' && selectedDocId === data.id }">
            <i v-if="data.type === 'category'" class="el-icon-folder"></i>
            <i v-else class="el-icon-document"></i>
            <span class="node-label">{{ data.name }}</span>
          </span>
        </template>
      </el-tree>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DocsSidebar',
  data() {
    return {
      treeProps: {
        children: 'children',
        label: 'name'
      },
      localTreeData: [],
      expandedKeys: [],
      selectedDocId: null,
      localLoading: false,
      initialized: false
    }
  },
  computed: {
    treeData() {
      return this.$store.getters.documentTree
    }
  },
  watch: {
    treeData: {
      handler(newVal) {
        if (JSON.stringify(newVal) !== JSON.stringify(this.localTreeData)) {
          this.localTreeData = JSON.parse(JSON.stringify(newVal))
        }
      },
      immediate: true
    }
  },
  async mounted() {
    this.localLoading = true
    await Promise.all([
      this.$store.dispatch('fetchCategories'),
      this.$store.dispatch('fetchDocuments', { page: 1, size: 100 })
    ])
    this.localLoading = false
    this.initialized = true
  },
  methods: {
    handleCreateDocument() {
      this.$message.info('创建文档功能开发中')
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
    async handleNodeClick(data) {
      if (data.type === 'document') {
        this.selectedDocId = data.id
        try {
          await this.$store.dispatch('fetchDocument', data.id)
          this.$emit('item-click', 'docs', data)
        } catch (error) {
          console.error('加载文档失败:', error)
          this.$message.error('加载文档失败，请重试')
        }
      }
    }
  }
}
</script>

<style scoped>
.docs-sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.sidebar-header {
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e4e7ed;
}

.sidebar-header h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #303133;
}

.tree-section {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #409EFF;
}

.empty-list {
  padding: 40px 20px;
  text-align: center;
}

.document-tree {
  background: transparent;
}

.document-tree >>> .el-tree-node__content {
  padding: 4px 0;
  height: 32px;
  transition: all 0.3s;
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  width: 100%;
}

.node-label {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tree-node i.el-icon-folder {
  color: #E6A23C;
  font-size: 16px;
}

.tree-node i.el-icon-document {
  color: #409EFF;
  font-size: 16px;
}

.is-document {
  cursor: pointer;
  padding: 8px;
  margin-bottom: 6px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.is-document:hover {
    background-color: #ecf5ff;
}

.is-document.active {
  border-left: 3px solid #409EFF;
    background-color: #ecf5ff;
}
</style>
