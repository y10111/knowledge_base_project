<template>
  <el-dialog
    title="欢迎使用"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="true"
    custom-class="welcome-dialog"
  >
    <div class="welcome-content">
      <markdown-viewer :src="'/docs/init.md'" />
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleClose">开始使用</el-button>
    </div>
  </el-dialog>
</template>

<script>
import MarkdownViewer from './MarkdownViewer.vue'

export default {
  name: 'WelcomeDialog',
  components: {
    MarkdownViewer
  },
  data() {
    return {
      dialogVisible: false
    }
  },
  mounted() {
    this.showWelcome()
  },
  methods: {
    showWelcome() {
      const hasSeenWelcome = localStorage.getItem('hasSeenWelcome')
      if (!hasSeenWelcome) {
        this.dialogVisible = true
      }
    },
    handleClose() {
      this.dialogVisible = false
      localStorage.setItem('hasSeenWelcome', 'true')
    }
  }
}
</script>

<style scoped>
.welcome-content {
  max-height: 60vh;
  overflow-y: auto;
  padding: 10px;
}

.dialog-footer {
  text-align: center;
}
</style>

<style>
.welcome-dialog .el-dialog__body {
  padding: 10px 20px;
}

.welcome-dialog .el-dialog__header {
  text-align: center;
  font-size: 18px;
  font-weight: 600;
}

.welcome-dialog .el-dialog__title {
  font-size: 18px;
  font-weight: 600;
}
</style>