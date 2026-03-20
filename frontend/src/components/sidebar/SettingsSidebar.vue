<template>
  <div class="sidebar-content">
    <div class="sidebar-header">
      <h3>设置</h3>
    </div>
    <div class="sidebar-body">
      <div class="settings-menu">
        <div 
          class="menu-item" 
          :class="{ 'active': activeMenu === 'data-dictionary' }"
          @click="handleMenuClick('data-dictionary')"
        >
          <i class="el-icon-s-grid"></i>
          <span>数据字典</span>
        </div>
        <div 
          class="menu-item" 
          :class="{ 'active': activeMenu === 'data-management' }"
          @click="handleMenuClick('data-management')"
        >
          <i class="el-icon-setting"></i>
          <span>数据管理</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SettingsSidebar',
  data() {
    return {
      activeMenu: 'data-dictionary'
    }
  },
  methods: {
    handleMenuClick(menu) {
      this.activeMenu = menu
      this.$emit('menu-change', menu)
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

.sidebar-header h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #303133;
}

.sidebar-body {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
}

.settings-menu {
  display: flex;
  flex-direction: column;
  gap: 8px;
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
}

.menu-item:hover {
  background-color: #f5f7fa;
  color: #409EFF;
}

.menu-item.active {
  background-color: #ecf5ff;
  color: #409EFF;
  border-left: 3px solid #409EFF;
}

.menu-item i {
  font-size: 18px;
}
</style>
