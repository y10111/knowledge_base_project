<template>
  <div class="chat-content">
    <div class="chat-messages">
      <div class="message user-message">
        <div class="message-avatar">用户</div>
        <div class="message-content">你好，如何使用知识图谱平台？</div>
      </div>
      <div class="message bot-message">
        <div class="message-avatar">AI</div>
        <div class="message-content">
          知识图谱平台是一个用于管理和查询知识的系统，您可以通过问答功能获取信息，通过搜索功能查找文档，通过文档库管理上传的文档。
        </div>
      </div>
    </div>
    <div class="chat-input">
      <el-input
        type="textarea"
        placeholder="输入你的问题..."
        :rows="3"
      >
      </el-input>
      <el-button type="primary" class="send-button">发送</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatContent'
}
</script>

<style scoped>
.chat-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.message {
  display: flex;
  margin-bottom: 20px;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
  margin-left: auto;
}

.bot-message {
  align-self: flex-start;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #409EFF;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 10px;
  font-size: 14px;
  font-weight: 600;
}

.user-message .message-avatar {
  background-color: #67C23A;
}

.message-content {
  padding: 12px 16px;
  border-radius: 18px;
  background-color: #f0f0f0;
  line-height: 1.5;
}

.user-message .message-content {
  background-color: #409EFF;
  color: #fff;
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
  background-color: #fafafa;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.send-button {
  margin-top: 10px;
  align-self: flex-end;
}
</style>