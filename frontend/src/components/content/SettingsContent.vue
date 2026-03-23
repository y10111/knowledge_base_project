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
</style>
