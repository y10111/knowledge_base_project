<template>
  <div class="main-content">
    <div v-if="selectedSidebarItem || activeActivity === 'chat'" class="content-body">
      <ChatContent v-if="activeActivity === 'chat'" />
      <SearchContent v-if="activeActivity === 'search'" @switchTab="handleSwitchTab" />
      <DocsContent v-if="activeActivity === 'docs'" />
      <SettingsContent v-if="activeActivity === 'settings'" />
    </div>
    <div v-else class="empty-state">
      <div class="empty-content">
        <i class="el-icon-folder-opened empty-icon"></i>
        <p class="empty-text">请在左侧侧边栏选择一个项目</p>
      </div>
    </div>
  </div>
</template>

<script>
import ChatContent from './content/ChatContent.vue'
import SearchContent from './content/SearchContent.vue'
import DocsContent from './content/DocsContent.vue'
import SettingsContent from './content/SettingsContent.vue'

export default {
  name: 'MainContent',
  components: {
    ChatContent,
    SearchContent,
    DocsContent,
    SettingsContent
  },
  props: {
    activeActivity: {
      type: String,
      default: 'chat'
    },
    selectedSidebarItem: {
      type: Object,
      default: null
    }
  },
  methods: {
    handleSwitchTab(tab) {
      this.$emit('switchTab', tab)
    }
  }
}
</script>

<style scoped>
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.content-body {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.empty-content {
  text-align: center;
  color: #909399;
}

.empty-icon {
  font-size: 80px;
  color: #dcdfe6;
  margin-bottom: 20px;
}

.empty-text {
  font-size: 16px;
  margin: 0;
}
</style>