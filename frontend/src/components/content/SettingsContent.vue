<template>
  <div class="settings-content">
    <CategoryManagement v-if="currentMenu === 'data-dictionary'" />
    <DataManagement v-else-if="currentMenu === 'data-management'" />
    <AboutContent v-else-if="currentMenu === 'about'" />
    <HealthCheck v-else-if="currentMenu === 'health'" />
  </div>
</template>

<script>
import { CategoryManagement, DataManagement, AboutContent, HealthCheck } from './Settings'

export default {
  name: 'SettingsContent',
  components: {
    CategoryManagement,
    DataManagement,
    AboutContent,
    HealthCheck
  },
  data() {
    return {
      currentMenu: 'data-dictionary'
    }
  },
  watch: {
    '$store.getters.settingsMenu': {
      handler(newVal) {
        this.currentMenu = newVal
      },
      immediate: true
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

/* 统一的 header 样式 */
:deep(.content-header) {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
  box-sizing: border-box;
}

:deep(.content-header h3) {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #303133;
}

:deep(.content-header .el-button) {
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 4px;
}

/* 统一的内容区域样式 */
:deep(.content-body) {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
