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

<style scoped>
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
</style>