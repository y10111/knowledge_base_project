<template>
  <div id="app">
    <div class="vscode-layout">
      <!-- 活动栏（固定不动） -->
      <ActivityBar @activity-change="handleActivityChange" />

      
      <!-- 侧边栏（内部可滚动） -->
      <Sidebar :active-activity="activeActivity" @item-click="selectSidebarItem" />
      
      <!-- 核心区域（内部可滚动） -->
      <div class="main-content-container">
        <div v-if="!selectedSidebarItem" class="markdown-content">
          <markdown-viewer :src="'/docs/init.md'" />
        </div>
        <MainContent v-else :active-activity="activeActivity" />
      </div>
    </div>
  </div>
</template>

<script>
import MarkdownViewer from './components/MarkdownViewer.vue'
import ActivityBar from './components/ActivityBar.vue'
import Sidebar from './components/Sidebar.vue'
import MainContent from './components/MainContent.vue'

export default {
  name: 'App',
  components: {
    MarkdownViewer,
    ActivityBar,
    Sidebar,
    MainContent
  },
  data() {
    return {
      activeActivity: 'chat',
      selectedSidebarItem: null
    }
  },
  computed: {
    activeActivityTitle() {
      const activityMap = {
        chat: '问答',
        search: '搜索',
        docs: '文档库',
        settings: '设置'
      };
      return activityMap[this.activeActivity] || '';
    }
  },
  methods: {
    handleActivityChange(id) {
      this.activeActivity = id;
      this.selectedSidebarItem = null; // 点击活动栏时重置侧边栏选择
    },
    selectSidebarItem(activity, item) {
      this.activeActivity = activity;
      this.selectedSidebarItem = item;
    },
    handleMenuChange(menu) {
      this.$store.commit('SET_SETTINGS_MENU', menu);
    }
  }
}
</script>

<style>
/* 新增Markdown内容样式 */
.markdown-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  box-sizing: border-box;
}

.markdown-content .markdown-viewer {
  overflow-y: auto;
  /* 隐藏滚动条 */
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.markdown-content .markdown-viewer::-webkit-scrollbar {
  display: none;
}

/* 全局固定：html, body, #app 禁止滚动 */
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100vh;          /* 与 100% 等效 */
  overflow: hidden;
}

/* 主布局容器：flex 行排列，占满视口 */
.vscode-layout {
  display: flex;
  height: 100vh;          /* 撑满视口 */
  background-color: #f5f7fa;
}



/* 核心区域容器 */
.main-content-container {
  flex: 1;                    /* 占据剩余全部宽度 */
  display: flex;
  flex-direction: column;
  overflow: hidden;           /* 防止主区域溢出 */
  padding: 20px;
  background-color: #f5f7fa;
}

/* Markdown内容容器 */
.markdown-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  box-sizing: border-box;
}

.markdown-content .markdown-viewer {
  overflow-y: auto;
  /* 隐藏滚动条 */
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.markdown-content .markdown-viewer::-webkit-scrollbar {
  display: none;
}
</style>