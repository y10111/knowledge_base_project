<template>
  <div class="sidebar-content">
    <div class="sidebar-header">
      <h3>设置</h3>
    </div>
    <div class="sidebar-body">
      <div class="settings-content">
        <p>预留设置功能</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SettingsSidebar'
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
</style>