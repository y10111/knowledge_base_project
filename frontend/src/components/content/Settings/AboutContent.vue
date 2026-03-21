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
/* markdown-viewer {
  max-width: 800px;
  margin: 0 auto;
  padding: 50px;
} */
</style>
