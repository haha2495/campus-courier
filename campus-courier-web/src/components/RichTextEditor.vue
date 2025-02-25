<template>
  <div class="rich-text-editor">
    <div class="editor-container">
      <Toolbar
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        mode="default"
        :style="{ borderBottom: '1px solid #ccc' }"
      />
      <Editor
        :defaultConfig="editorConfig"
        :value="modelValue"
        mode="default"
        :style="{ height: height + 'px' }"
        @onCreated="handleCreated"
        @onChange="handleChange"
      />
    </div>
  </div>
</template>

<script setup>
import { shallowRef, onBeforeUnmount, watch } from 'vue'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  height: {
    type: Number,
    default: 300
  },
  placeholder: {
    type: String,
    default: '请输入内容...'
  },
  uploadUrl: {
    type: String,
    default: '/api/file/upload'
  },
  uploadField: {
    type: String,
    default: 'file'
  },
  maxSize: {
    type: Number,
    default: 5 // 默认5MB
  },
  maxFiles: {
    type: Number,
    default: 10
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 编辑器配置
const toolbarConfig = {
  excludeKeys: [
    'group-video',
    'group-code',
    'todo',
    'group-justify',
    'group-indent',
    'group-more-style'
  ]
}

const editorConfig = {
  placeholder: props.placeholder,
  autoFocus: false,
  MENU_CONF: {
    uploadImage: {
      server: props.uploadUrl,
      fieldName: props.uploadField,
      maxFileSize: props.maxSize * 1024 * 1024,
      maxNumberOfFiles: props.maxFiles,
      allowedFileTypes: ['image/*'],
      customInsert(res, insertFn) {
        const url = res.data
        insertFn(url)
      },
      onError(file, err, res) {
        ElMessage.error(`图片上传失败：${err.message || '未知错误'}`)
      }
    }
  }
}

// 编辑器创建完成时的回调
const handleCreated = (editor) => {
  editorRef.value = editor
  // 确保在编辑器创建完成后设置初始值
  if (props.modelValue) {
    editor.setHtml(props.modelValue)
  }
  
  // 设置编辑器容器样式
  const textArea = editor.getEditableContainer()
  if (textArea) {
    textArea.style.whiteSpace = 'pre-wrap'
    textArea.style.wordWrap = 'break-word'
    textArea.style.wordBreak = 'break-all'
    textArea.style.overflowWrap = 'break-word'
  }
}

// 编辑器内容变化时的回调
const handleChange = (editor) => {
  const html = editor.getHtml()
  emit('update:modelValue', html)
  emit('change', html)
}

// 监听 modelValue 变化，更新编辑器内容
watch(() => props.modelValue, (newValue) => {
  if (editorRef.value && newValue !== editorRef.value.getHtml()) {
    editorRef.value.setHtml(newValue || '')
  }
}, { immediate: true })

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>

<style lang="scss" scoped>
.rich-text-editor {
  .editor-container {
    border: 1px solid #ccc;
    border-radius: 4px;
    z-index: 500;
    
    :deep(.w-e-toolbar) {
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;
      border-bottom: 1px solid #ccc;
    }
    
    :deep(.w-e-text-container) {
      border-bottom-left-radius: 4px;
      border-bottom-right-radius: 4px;
      overflow: hidden;

      .w-e-scroll {
        overflow-y: auto !important;
        overflow-x: hidden !important;
      }
    }

    :deep(.w-e-text-placeholder) {
      z-index: 1;
    }

    :deep(.w-e-text) {
      p, div {
        white-space: pre-wrap !important;
        word-wrap: break-word !important;
        word-break: break-all !important;
        overflow-wrap: break-word !important;
      }
    }
  }
}
</style> 