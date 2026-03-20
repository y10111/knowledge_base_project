<template>
  <div class="activity-bar">
    <div 
      v-for="item in activityItems" 
      :key="item.id"
      class="activity-item"
      :class="{ active: activeActivity === item.id }"
      @click="setActiveActivity(item.id)"
      :title="item.title"
    >
      <el-badge :value="item.badge" v-if="item.badge">
        <i :class="item.icon"></i>
      </el-badge>
      <i :class="item.icon" v-else></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ActivityBar',
  data() {
    return {
      activeActivity: 'chat',
      activityItems: [
        { id: 'chat', title: '问答', icon: 'el-icon-chat-dot-round', badge: 3 },
        { id: 'search', title: '搜索', icon: 'el-icon-search' },
        { id: 'docs', title: '文档库', icon: 'el-icon-document' },
        { id: 'settings', title: '设置', icon: 'el-icon-setting' }
      ]
    }
  },
  methods: {
    setActiveActivity(id) {
      this.activeActivity = id;
      this.$emit('activity-change', id);
    }
  }
}
</script>

<style scoped>
.activity-bar {
  width: 50px;
  background-color: #333;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 0;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.activity-item {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 5px 0;
  border-radius: 4px;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s;
}

.activity-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.activity-item.active {
  background-color: #409EFF;
  color: #fff;
}

.activity-item i {
  font-size: 20px;
}
</style>