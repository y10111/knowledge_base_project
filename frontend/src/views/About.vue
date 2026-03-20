<template>
  <div class="about">
    <el-container>
      <el-header>关于</el-header>
      <el-main>
        <el-card>
          <h2>知识图谱平台</h2>
          <p>基于Vue2 + Element UI + ECharts + Axios + Vue Router + Vuex</p>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'AboutView'
}
</script>

<style scoped>
.about {
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