<template>
  <div class="search-content">
    <el-empty description="搜索结果将显示在这里" />
  </div>
</template>

<script>
export default {
  name: 'SearchContent'
}
</script>

<style scoped>
.search-content {
  height: 100%;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>