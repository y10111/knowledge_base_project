<template>
  <markdown-viewer :src="'/docs/init.md'" />
</template>

<script>
import MarkdownViewer from '../../MarkdownViewer.vue'

export default {
  name: 'AboutContent',
  components: {
    MarkdownViewer
  }
}
</script>

<style scoped>
markdown-viewer {
  height: 100%;
  padding: 20px;
  overflow-y: auto;
  line-height: 1.6;
}
</style>
