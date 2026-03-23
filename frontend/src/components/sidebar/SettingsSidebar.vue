<template>
  <div class="sidebar-content">
    <div class="sidebar-header">
      <h3>设置</h3>
    </div>
    <div class="sidebar-body">
      <div class="sidebar-menu">
        <div 
          class="menu-item" 
          :class="{ 'active': activeMenu === 'data-dictionary' }"
          @click="handleMenuClick('data-dictionary')"
        >
          <i class="el-icon-s-grid"></i>
          <span>文档分类管理</span>
        </div>
        <div 
          class="menu-item" 
          :class="{ 'active': activeMenu === 'data-management' }"
          @click="handleMenuClick('data-management')"
        >
          <i class="el-icon-s-data"></i>
          <span>系统数据管理</span>
        </div>
        <div 
          class="menu-item" 
          :class="{ 'active': activeMenu === 'about' }"
          @click="handleMenuClick('about')"
        >
          <i class="el-icon-info"></i>
          <span>关于与帮助</span>
        </div>
        <div 
          class="menu-item" 
          :class="{ 'active': activeMenu === 'health' }"
          @click="handleMenuClick('health')"
        >
          <i class="el-icon-connection"></i>
          <span>健康检查</span>
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
      this.$emit('item-click', 'settings', { id: menu, name: this.getMenuName(menu) })
    },
    getMenuName(menu) {
      const menuNames = {
        'data-dictionary': '文档分类管理',
        'data-management': '系统数据管理',
        'about': '关于与帮助',
        'health': '健康检查'
      }
      return menuNames[menu] || menu
    }
  }
}
</script>
