<template>
  <div class="markdown-viewer" v-html="renderedContent"></div>
</template>

<script>
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'

export default {
  name: 'MarkdownViewer',
  props: {
    content: {
      type: String,
      default: ''
    },
    src: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      markdownContent: ''
    }
  },
  computed: {
    renderedContent() {
      return marked(this.markdownContent)
    }
  },
  watch: {
    content: {
      immediate: true,
      handler(val) {
        if (val) {
          this.markdownContent = val
        }
      }
    },
    src: {
      immediate: true,
      handler(val) {
        if (val) {
          this.loadMarkdownFile(val)
        }
      }
    }
  },
  mounted() {
    this.highlightCode()
  },
  updated() {
    this.highlightCode()
  },
  methods: {
    async loadMarkdownFile(url) {
      try {
        const response = await fetch(url)
        this.markdownContent = await response.text()
      } catch (error) {
        console.error('加载Markdown文件失败:', error)
        this.markdownContent = '加载失败'
      }
    },
    highlightCode() {
      this.$nextTick(() => {
        const blocks = this.$el.querySelectorAll('pre code')
        blocks.forEach((block) => {
          hljs.highlightBlock(block)
        })
      })
    }
  }
}
</script>

<style scoped>
/* 主容器样式 */
.markdown-viewer {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  flex: 1;
  width: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 15px;
  line-height: 1.7;
  color: #333333;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

/* 隐藏滚动条 */
.markdown-viewer::-webkit-scrollbar {
  display: none;
}

.markdown-viewer {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

/* 标题样式 */
.markdown-viewer >>> h1,
.markdown-viewer >>> h2,
.markdown-viewer >>> h3,
.markdown-viewer >>> h4,
.markdown-viewer >>> h5,
.markdown-viewer >>> h6 {
  margin-top: 28px;
  margin-bottom: 16px;
  font-weight: 700;
  line-height: 1.3;
  color: #1a1a1a;
  letter-spacing: -0.02em;
}

.markdown-viewer >>> h1 {
  font-size: 2.2em;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 0.4em;
  margin-top: 0;
  color: #000000;
}

.markdown-viewer >>> h2 {
  font-size: 1.7em;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 0.3em;
  color: #1a1a1a;
}

.markdown-viewer >>> h3 {
  font-size: 1.4em;
  color: #2a2a2a;
}

.markdown-viewer >>> h4 {
  font-size: 1.2em;
  color: #333333;
}

.markdown-viewer >>> h5 {
  font-size: 1.1em;
  color: #444444;
}

.markdown-viewer >>> h6 {
  font-size: 1em;
  color: #666666;
  font-weight: 600;
}

/* 段落样式 */
.markdown-viewer >>> p {
  margin-top: 0;
  margin-bottom: 18px;
  line-height: 1.7;
  text-align: justify;
  color: #333333;
}

/* 强调文本 */
.markdown-viewer >>> strong {
  font-weight: 700;
  color: #000000;
  background: linear-gradient(transparent 60%, #fff3cd 60%);
  padding: 0 2px;
  border-radius: 2px;
}

/* 代码样式 */
.markdown-viewer >>> code {
  background-color: #f6f8fa;
  border: 1px solid #eaecef;
  border-radius: 4px;
  font-size: 85%;
  margin: 0;
  padding: 0.2em 0.5em;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  color: #d73a49;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.markdown-viewer >>> pre {
  background-color: #f8f9fa;
  border: 1px solid #eaecef;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.5;
  overflow: auto;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.markdown-viewer >>> pre code {
  background-color: transparent;
  border: 0;
  display: inline;
  line-height: inherit;
  margin: 0;
  overflow: visible;
  padding: 0;
  word-wrap: normal;
  color: #333333;
  box-shadow: none;
}

/* 引用样式 */
.markdown-viewer >>> blockquote {
  border-left: 4px solid #409EFF;
  color: #555555;
  padding: 15px 20px;
  margin: 0 0 20px 0;
  font-style: italic;
  background-color: #f8f9fa;
  border-radius: 0 4px 4px 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

/* 列表样式 */
.markdown-viewer >>> ul,
.markdown-viewer >>> ol {
  margin-top: 0;
  margin-bottom: 18px;
  padding-left: 2.5em;
}

.markdown-viewer >>> li {
  margin: 0.4em 0;
  position: relative;
}

.markdown-viewer >>> ul {
  list-style-type: none;
}

.markdown-viewer >>> ul li::before {
  content: '•';
  color: #409EFF;
  font-weight: bold;
  position: absolute;
  left: -1.2em;
}

.markdown-viewer >>> ul ul {
  margin-bottom: 0;
}

.markdown-viewer >>> ul ul li::before {
  content: '◦';
  color: #666666;
}

.markdown-viewer >>> ol {
  list-style-type: decimal;
}

.markdown-viewer >>> ol ol {
  list-style-type: lower-alpha;
}

/* 链接样式 */
.markdown-viewer >>> a {
  color: #409EFF;
  text-decoration: none;
  transition: all 0.3s ease;
  border-bottom: 1px solid rgba(64, 158, 255, 0.3);
  padding-bottom: 2px;
}

.markdown-viewer >>> a:hover {
  color: #1890ff;
  border-bottom-color: #1890ff;
  text-decoration: none;
  background: linear-gradient(transparent 60%, rgba(64, 158, 255, 0.1) 60%);
  border-radius: 2px;
}

/* 表格样式 */
.markdown-viewer >>> table {
  border-collapse: collapse;
  border-spacing: 0;
  display: block;
  overflow: auto;
  width: 100%;
  margin-bottom: 20px;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  overflow: hidden;
}

.markdown-viewer >>> table th,
.markdown-viewer >>> table td {
  border: 1px solid #eaecef;
  padding: 12px 16px;
  text-align: left;
  transition: background-color 0.2s ease;
}

.markdown-viewer >>> table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333333;
  border-bottom: 2px solid #eaecef;
}

.markdown-viewer >>> table tr:nth-child(2n) {
  background-color: #f8f9fa;
}

.markdown-viewer >>> table tr:hover {
  background-color: #f0f7ff;
}

/* 图片样式 */
.markdown-viewer >>> img {
  max-width: 100%;
  box-sizing: content-box;
  background-color: #fff;
  border-radius: 8px;
  margin: 20px 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

.markdown-viewer >>> img:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

/* 分割线样式 */
.markdown-viewer >>> hr {
  background: linear-gradient(to right, transparent, #eaecef, transparent);
  border: 0;
  height: 2px;
  margin: 30px 0;
  border-radius: 1px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .markdown-viewer {
    padding: 20px;
    font-size: 14px;
  }
  
  .markdown-viewer >>> h1 {
    font-size: 1.8em;
  }
  
  .markdown-viewer >>> h2 {
    font-size: 1.5em;
  }
  
  .markdown-viewer >>> h3 {
    font-size: 1.3em;
  }
}

/* 滚动条样式 */
.markdown-viewer::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.markdown-viewer::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.markdown-viewer::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.markdown-viewer::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 动画效果 */
.markdown-viewer {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>