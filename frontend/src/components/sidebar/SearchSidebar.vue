<template>
  <div class="sidebar-content">
    <div class="sidebar-header">
      <h3>搜索</h3>
    </div>
    <div class="sidebar-body">
      <el-input
        placeholder="搜索内容"
        prefix-icon="el-icon-search"
        class="search-input"
      >
      </el-input>
      <div class="search-results">
        <div class="search-item" v-for="i in 3" :key="i" @click="handleSearchClick(i)">
          <span>搜索结果 {{ i }}</span>
          <span class="search-score">95%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SearchSidebar',
  methods: {
    handleSearchClick(item) {
      this.$emit('item-click', 'search', { id: item, name: `搜索结果 ${item}` })
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

.sidebar-body {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
}

.search-input {
  margin-bottom: 15px;
}

.search-results {
  display: flex;
  flex-direction: column;
}

.search-item {
  padding: 10px;
  margin-bottom: 5px;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.search-item:hover {
  background-color: #ecf5ff;
}

.search-score {
  font-size: 12px;
  color: #909399;
}
</style>