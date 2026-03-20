<template>
  <div class="home">
    <el-container>
      <el-header>知识图谱平台</el-header>
      <el-main>
        <el-button type="primary">欢迎使用知识图谱平台</el-button>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'HomeView'
}
</script>

<style scoped>
.home {
  height: 100vh;
}

.el-header {
  background-color: #409EFF;
  color: white;
  text-align: center;
  line-height: 60px;
  font-size: 20px;
}

.el-main {
  padding: 20px;
}
</style>