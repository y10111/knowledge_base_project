<template>
  <div class="sidebar-content">
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
      this.$emit('create-document')
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
