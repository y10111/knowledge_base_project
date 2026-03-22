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
