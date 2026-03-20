<template>
  <div class="docs-content">
    <el-empty description="文档详情将显示在这里" />
  </div>
</template>

<script>
export default {
  name: 'DocsContent'
}
</script>

<style scoped>
.docs-content {
  height: 100%;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>