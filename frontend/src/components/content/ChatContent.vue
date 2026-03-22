<template>
  <div class="chat-content">
    <div class="chat-messages" ref="messagesContainer">
      <div v-if="conversationsLoading" class="loading">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      <div v-else-if="conversationMessages.length === 0" class="empty-messages">
        <el-empty description="开始新的对话吧" />
      </div>
      <div
        v-for="message in conversationMessages"
        :key="message.id"
        :class="['message', message.role === 'user' ? 'user-message' : 'bot-message']"
      >
        <div class="message-avatar">{{ message.role === 'user' ? '用户' : 'AI' }}</div>
        <div class="message-content">{{ message.content }}</div>
      </div>
      <div v-if="isSending" class="message bot-message">
        <div class="message-avatar">AI</div>
        <div class="message-content sending">
          <i class="el-icon-loading"></i>
          正在回复...
        </div>
      </div>
    </div>
    <div class="chat-input">
      <el-input
        v-model="inputMessage"
        type="textarea"
        placeholder="输入你的问题..."
        :rows="3"
        @keyup.enter.exact="handleSendMessage"
      >
      </el-input>
      <el-button
        type="primary"
        class="send-button"
        @click="handleSendMessage"
        :loading="isSending"
        :disabled="!inputMessage.trim()"
      >
        发送
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatContent',
  data() {
    return {
      inputMessage: '',
      isSending: false
    }
  },
  computed: {
    conversationMessages() {
      return this.$store.getters.conversationMessages
    },
    currentConversation() {
      return this.$store.getters.currentConversation
    },
    conversationsLoading() {
      return this.$store.getters.conversationsLoading
    }
  },
  watch: {
    conversationMessages() {
      this.scrollToBottom()
    }
  },
  mounted() {
    // 不再自动创建或加载对话，等待用户操作
  },
  methods: {
    async handleSendMessage() {
      if (!this.inputMessage.trim() || this.isSending) return

      const content = this.inputMessage.trim()
      this.inputMessage = ''
      this.isSending = true

      try {
        let conversationId = this.currentConversation?.id

        // 如果没有当前对话，先创建一个新对话
        if (!conversationId) {
          const newConversation = await this.$store.dispatch('createConversation', '新对话')
          conversationId = newConversation.id
        }

        await this.$store.dispatch('sendMessage', {
          conversationId: conversationId,
          content
        })
      } catch (error) {
        console.error('发送消息失败:', error)
        this.$message.error('发送消息失败，请重试')
      } finally {
        this.isSending = false
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      })
    }
  }
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

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #409EFF;
}

.empty-messages {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
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
  flex-shrink: 0;
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

.message-content.sending {
  display: flex;
  align-items: center;
  gap: 8px;
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