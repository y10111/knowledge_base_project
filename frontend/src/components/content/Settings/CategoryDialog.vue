<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="500px"
    @close="handleClose"
  >
    <el-form :model="form" :rules="rules" ref="categoryForm" label-width="100px">
      <el-form-item label="分类名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入分类名称"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'CategoryDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '新建分类'
    },
    form: {
      type: Object,
      default: () => ({
        id: null,
        name: '',
        parentId: null
      })
    },
    rules: {
      type: Object,
      default: () => ({
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ]
      })
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(value) {
        this.$emit('update:visible', value)
      }
    }
  },
  methods: {
    handleClose() {
      this.$refs.categoryForm.resetFields()
      this.$emit('close')
    },
    handleSave() {
      this.$refs.categoryForm.validate((valid) => {
        if (valid) {
          this.$emit('save')
        }
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>