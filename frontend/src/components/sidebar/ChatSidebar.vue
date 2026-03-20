<template>
  <div class="chat-sidebar">
    <div class="sidebar-header">
      <h3>对话</h3>
      <el-button type="primary" size="small" @click="handleCreateConversation">
        <i class="el-icon-plus"></i> 新建
      </el-button>
    </div>
    <div class="conversation-list">
      <div v-if="isLoading" class="loading">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      <div v-else-if="conversationList.length === 0" class="empty-list">
        <el-empty description="暂无对话" />
        <div style="padding: 10px; color: red; font-size: 12px; text-align: center;">
          DEBUG: count={{ conversationList.length }}
        </div>
      </div>
      <div
        v-for="conversation in conversationList"
        :key="conversation.id"
        :class="['conversation-item', currentConversation?.id === conversation.id ? 'active' : '']"
        @click="handleSelectConversation(conversation)"
      >
        <div class="conversation-title">{{ conversation.title || '无标题' }}</div>
        <div class="conversation-actions">
          <el-button
            type="text"
            size="small"
            @click.stop="handleDeleteConversation(conversation.id)"
          >
            删除
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "ChatSidebar",
  data() {
    return {
      conversationList: [],
      isLoading: false
    }
  },
  computed: {
    currentConversation() {
      return this.$store.getters.currentConversation
    }
  },
  mounted() {
    console.log("=== ChatSidebar MOUNTED ===")
    this.loadConversations()
  },
  methods: {
    async loadConversations() {
      this.isLoading = true
      console.log("Starting to load conversations...")
      
      try {
        const response = await fetch("http://localhost:8080/api/conversations")
        const result = await response.json()
        console.log("API Result:", result)
        console.log("result.code:", result.code)
        console.log("result.data:", result.data)
        console.log("result.data.content:", result.data ? result.data.content : "NO DATA")
        
        if (result.code === 200 && result.data && result.data.content) {
          const conversations = result.data.content
          console.log("Going to set conversations:", conversations.length)
          
          // 使用 Vue.set 强制触发响应式
          this.$set(this, "conversationList", conversations)
          console.log("After $set - conversationList:", this.conversationList.length)
          
          // 同时更新 store
          this.$store.commit("SET_CONVERSATIONS", conversations)
        } else {
          console.error("Invalid response format:", result)
        }
      } catch (error) {
        console.error("FETCH ERROR:", error)
        alert("加载失败: " + error.message)
      } finally {
        this.isLoading = false
      }
    },
    async handleCreateConversation() {
      try {
        const response = await fetch("http://localhost:8080/api/conversations", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ title: "新对话" })
        })
        const result = await response.json()
        
        if (result.code === 200 && result.data) {
          this.$message.success("对话创建成功")
          await this.loadConversations()
          await this.$store.dispatch("fetchConversation", result.data.id)
          this.$emit("item-click", "chat", result.data)
        }
      } catch (error) {
        console.error("创建对话失败:", error)
        this.$message.error("创建对话失败，请重试")
      }
    },
    async handleSelectConversation(conversation) {
      try {
        await this.$store.dispatch("fetchConversation", conversation.id)
        this.$emit("item-click", "chat", conversation)
      } catch (error) {
        console.error("加载对话失败:", error)
        this.$message.error("加载对话失败，请重试")
      }
    },
    async handleDeleteConversation(conversationId) {
      try {
        await this.$confirm("确定要删除这个对话吗？", "删除确认", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        
        const response = await fetch(`http://localhost:8080/api/conversations/${conversationId}`, {
          method: "DELETE"
        })
        const result = await response.json()
        
        if (result.code === 200) {
          this.$message.success("对话删除成功")
          this.loadConversations()
        } else {
          this.$message.error("对话删除失败")
        }
      } catch (error) {
        if (error !== "cancel") {
          console.error("删除对话失败:", error)
          this.$message.error("删除对话失败，请重试")
        }
      }
    }
  }
}
</script>

<style scoped>
.chat-sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.sidebar-header {
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e4e7ed;
}

.sidebar-header h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #303133;
}

.conversation-list {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #409EFF;
}

.empty-list {
  padding: 40px 20px;
  text-align: center;
}

.conversation-item {
  padding: 15px;
  margin-bottom: 10px;
  background-color: #fff;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.conversation-item:hover {
  background-color: #ecf5ff;
  transform: translateY(-1px);
}

.conversation-item.active {
  background-color: #ecf5ff;
  border-left: 3px solid #409EFF;
}

.conversation-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
}

.conversation-actions {
  display: flex;
  gap: 5px;
}
</style>
