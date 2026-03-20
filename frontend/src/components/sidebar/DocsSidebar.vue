<template>
  <div class="sidebar-content">
    <div class="sidebar-header">
      <h3>文档库</h3>
      <el-button size="small" type="primary" plain>
        <i class="el-icon-upload"></i> 上传文档
      </el-button>
    </div>
    <div class="sidebar-body">
      <el-tree
        :data="docTree"
        node-key="id"
        default-expand-all
        class="doc-tree"
        @node-click="(data) => $emit('item-click', 'docs', data)"
      >
      </el-tree>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DocsSidebar',
  data() {
    return {
      docTree: [
        {
          id: 1,
          label: '技术文档',
          children: [
            { id: 2, label: '前端开发' },
            { id: 3, label: '后端开发' },
            { id: 4, label: '数据库' }
          ]
        },
        {
          id: 5,
          label: '业务文档',
          children: [
            { id: 6, label: '产品需求' },
            { id: 7, label: '用户手册' }
          ]
        }
      ]
    }
  }
}
</script>

<style scoped>
.sidebar-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
}

.sidebar-header {
  padding: 0 15px;
  height: 60px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fafafa;
  flex-shrink: 0;
  box-sizing: border-box;
}

.sidebar-body {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
}

.doc-tree {
  height: 100%;
}
</style>