<template>
  <div class="health-check">
    <div class="content-header">
      <h3>系统健康检查</h3>
      <el-button type="primary" @click="checkHealth" :loading="loading">
        检查健康状态
      </el-button>
    </div>
    <div class="content-body">
      <el-card v-if="healthStatus" class="health-card">
        <template slot="header">
          <div class="health-card-header">
            <span>RAG 服务状态</span>
            <el-tag :type="healthStatus.status === 'ok' ? 'success' : 'danger'">
              {{ healthStatus.status === 'ok' ? '正常' : '异常' }}
            </el-tag>
          </div>
        </template>
        <el-descriptions :column="2">
          <el-descriptions-item label="状态">
            {{ healthStatus.status === 'ok' ? '正常运行' : '服务异常' }}
          </el-descriptions-item>
          <el-descriptions-item label="索引片段数">
            {{ healthStatus.index_size || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="检查时间">
            {{ checkTime }}
          </el-descriptions-item>
          <el-descriptions-item label="服务地址">
            http://localhost:5001
          </el-descriptions-item>
        </el-descriptions>
        <div v-if="healthStatus && healthStatus.status !== 'ok'" class="health-error">
          <el-alert
            type="error"
            title="RAG 服务异常"
            description="请检查 Python 服务是否启动"
            show-icon
          />
        </div>
      </el-card>
      <div v-else-if="loading" class="health-loading">
        <el-skeleton :rows="6" animated />
      </div>
      <div v-else class="health-empty">
        <el-empty description="点击检查按钮查看系统状态" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HealthCheck',
  data() {
    return {
      loading: false,
      healthStatus: null,
      checkTime: ''
    }
  },
  methods: {
    async checkHealth() {
      this.loading = true
      try {
        const response = await fetch('http://localhost:5001/health')
        if (response.ok) {
          this.healthStatus = await response.json()
        } else {
          this.healthStatus = { status: 'error' }
        }
        this.checkTime = new Date().toLocaleString()
      } catch (error) {
        console.error('健康检查失败:', error)
        this.healthStatus = { status: 'error' }
        this.checkTime = new Date().toLocaleString()
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.health-check {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content-body {
  flex: 1;
  overflow-y: auto;
}

.health-card {
  margin-bottom: 20px;
}

.health-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.health-error {
  margin-top: 15px;
}

.health-loading {
  margin-top: 20px;
}

.health-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  min-height: 300px;
}
</style>
