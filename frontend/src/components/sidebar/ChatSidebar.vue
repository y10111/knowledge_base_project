<template>
  <div class="sidebar-content">
    <div class="sidebar-header">
      <h3>问答</h3>
      <el-button size="small" type="primary" plain>
        <i class="el-icon-plus"></i> 新建问答
      </el-button>
    </div>
    <div class="sidebar-body">
      <div class="chat-history">
        <div class="chat-item" v-for="i in 5" :key="i" @click="$emit('item-click', 'chat', i)">
          <span>历史对话 {{ i }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatSidebar'
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

.chat-history {
  display: flex;
  flex-direction: column;
}

.chat-item {
  padding: 10px;
  margin-bottom: 5px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.chat-item:hover {
  background-color: #f5f7fa;
}
</style>