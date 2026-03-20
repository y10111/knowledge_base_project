<template>
  <div class="main-content">
    <div class="content-body">
      <ChatContent v-if="activeActivity === 'chat'" />
      <SearchContent v-if="activeActivity === 'search'" @switchTab="handleSwitchTab" />
      <DocsContent v-if="activeActivity === 'docs'" />
      <SettingsContent v-if="activeActivity === 'settings'" />
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
    }
  },
  methods: {
    handleSwitchTab(tab) {
      // 发出事件通知父组件切换标签
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
</style>