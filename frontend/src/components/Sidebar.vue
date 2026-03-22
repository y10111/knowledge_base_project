<template>
  <div class="sidebar">
    <ChatSidebar v-if="activeActivity === 'chat'" @item-click="$emit('item-click', ...arguments)" />
    <SearchSidebar v-if="activeActivity === 'search'" @item-click="$emit('item-click', ...arguments)" />
    <DocsSidebar v-if="activeActivity === 'docs'" @item-click="$emit('item-click', ...arguments)" />
    <SettingsSidebar v-if="activeActivity === 'settings'" @menu-change="$emit('menu-change', ...arguments)" @item-click="$emit('item-click', ...arguments)" />
  </div>
</template>

<script>
import ChatSidebar from './sidebar/ChatSidebar.vue'
import SearchSidebar from './sidebar/SearchSidebar.vue'
import DocsSidebar from './sidebar/DocsSidebar.vue'
import SettingsSidebar from './sidebar/SettingsSidebar.vue'

export default {
  name: 'Sidebar',
  components: {
    ChatSidebar,
    SearchSidebar,
    DocsSidebar,
    SettingsSidebar
  },
  props: {
    activeActivity: {
      type: String,
      default: 'chat'
    }
  }
}
</script>

<style>
/* ==================== Sidebar 容器样式 ==================== */
.sidebar {
  width: 300px;
  background-color: #fff;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
}

/* ==================== 统一 Sidebar 内容容器 ==================== */
.sidebar-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

/* ==================== 统一 Sidebar Header ==================== */
.sidebar-header {
  padding: 0 20px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
  box-sizing: border-box;
}

.sidebar-header h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #303133;
  line-height: 60px;
}

/* ==================== 统一 Sidebar Body ==================== */
.sidebar-body {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

/* ==================== 通用列表样式 ==================== */
.sidebar-list {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

/* ==================== 通用列表项样式 ==================== */
.sidebar-item {
  padding: 15px;
  margin-bottom: 10px;
  background-color: #fff;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.sidebar-item:hover {
  background-color: #ecf5ff;
  transform: translateY(-1px);
}

.sidebar-item.active {
  background-color: #ecf5ff;
  border-left: 3px solid #409EFF;
}

.sidebar-item-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
}

.sidebar-item-actions {
  display: flex;
  gap: 5px;
}

/* ==================== 通用菜单样式 ==================== */
.sidebar-menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 15px;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
  color: #606266;
  font-size: 14px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.menu-item:hover {
  background-color: #ecf5ff;
  color: #409EFF;
  transform: translateY(-1px);
}

.menu-item.active {
  background-color: #ecf5ff;
  color: #409EFF;
  border-left: 3px solid #409EFF;
}

.menu-item i {
  font-size: 18px;
}

/* ==================== 通用搜索样式 ==================== */
.search-input {
  margin-bottom: 15px;
}

.search-results {
  display: flex;
  flex-direction: column;
}

.search-item {
  padding: 10px;
  margin-bottom: 5px;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-item:hover {
  background-color: #ecf5ff;
}

.search-score {
  font-size: 12px;
  color: #909399;
}

/* ==================== 通用树形结构样式 ==================== */
.tree-section {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.document-tree {
  background: transparent !important;
}

.document-tree .el-tree-node__content {
  padding: 4px 0;
  height: 32px;
  transition: all 0.3s;
  background-color: transparent !important;
}

.document-tree .el-tree-node:focus > .el-tree-node__content {
  background-color: transparent !important;
}

.document-tree .el-tree-node__content:hover {
  background-color: #ecf5ff !important;
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  width: 100%;
}

.tree-node .node-label {
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

.tree-node.is-document {
  cursor: pointer;
  padding: 8px;
  margin-bottom: 6px;
  border-radius: 4px;
  transition: all 0.3s;
}

.tree-node.is-document:hover {
  background-color: #ecf5ff;
}

.tree-node.is-document.active {
  background-color: #ecf5ff;
  border-left: 3px solid #409EFF;
}

/* ==================== 通用状态样式 ==================== */
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
</style>
