<template>
  <div class="json-editor">
    <textarea ref="textarea" />
  </div>
</template>

<script>
import CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
// 替换主题这里需修改名称
import 'codemirror/theme/idea.css'
import 'codemirror/mode/clike/clike'
export default {
  props: {
    value: {
      type: String,
      required: true
    },
    height: {
      type: String,
      required: true
    },
    readOnly: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  data() {
    return {
      editor: false
    }
  },
  watch: {
    value(value) {
      const editorValue = this.editor.getValue()
      if (value !== editorValue) {
        this.editor.setValue(this.value)
      }
    },
    height(value) {
      this.editor.setSize('auto', this.height)
    }
  },
  mounted() {
    this.editor = CodeMirror.fromTextArea(this.$refs.textarea, {
      line: true,
      theme: 'idea', // 主题
      tabSize: 4, // 制表符的宽度
      indentUnit: 2, // 一个块应该缩进多少个空格（无论这在编辑语言中意味着什么）。默认值为 2。
      firstLineNumber: 1, // 从哪个数字开始计算行数。默认值为 1。
      readOnly: this.readOnly, // 只读
      autorefresh: true,
      smartIndent: true, // 上下文缩进
      lineNumbers: true, // 是否显示行号
      styleActiveLine: true, // 高亮选中行
      viewportMargin: Infinity, // 处理高度自适应时搭配使用
      showCursorWhenSelecting: true, // 当选择处于活动状态时是否应绘制游标
      mode: 'text/x-java', // 脚本类型
      cursorHeight: 0.9,
      lint: true,
      lineWrapping: true
    })
    this.editor.setSize('auto', this.height)
    this.editor.setValue(this.value)
  },
  methods: {
    getValue() {
      return this.editor.getValue()
    }
  }
}
</script>

<style scoped>
  .json-editor{
    height: 100%;
    margin-bottom: 10px;
  }
  .json-editor >>> .CodeMirror {
    font-size: 14px;
    font-weight:normal
  }
  .json-editor >>> .CodeMirror-scroll{
  }
  .json-editor >>> .cm-s-rubyblue span.cm-string {
    color: #F08047;
  }
</style>
