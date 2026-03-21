<template>
  <div id="app">
    <div class="vscode-layout">
      <!-- 活动栏（固定不动） -->
      <ActivityBar @activity-change="handleActivityChange" />

      
      <!-- 侧边栏（内部可滚动） -->
      <Sidebar :active-activity="activeActivity" @item-click="selectSidebarItem" @menu-change="handleMenuChange" />
      
      <!-- 核心区域（内部可滚动） -->
      <div class="main-content-container">
        <MainContent :active-activity="activeActivity" :selected-sidebar-item="selectedSidebarItem" />
      </div>
    </div>
    
    <!-- 欢迎弹窗 -->
    <WelcomeDialog />
  </div>
</template>

<script>
import WelcomeDialog from './components/WelcomeDialog.vue'
import ActivityBar from './components/ActivityBar.vue'
import Sidebar from './components/Sidebar.vue'
import MainContent from './components/MainContent.vue'

export default {
  name: 'App',
  components: {
    WelcomeDialog,
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
      this.selectedSidebarItem = null;
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
  height: 100vh;
  overflow: hidden;
}

/* 主布局容器：flex 行排列，占满视口 */
.vscode-layout {
  display: flex;
  height: 100vh;
  background-color: #f5f7fa;
}

/* 核心区域容器 */
.main-content-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 20px;
  background-color: #f5f7fa;
}
</style>