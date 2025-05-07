<template>
  <JavaEdit height="500px" :value="form.content" :read-only="false" />
</template>

<script>
import JavaEdit from '@/components/JavaEdit'

export default {
  name: 'CodemirrorDemo',
  components: { JavaEdit },
  data() {
    return {
      form: {
        content: ''
      }
    }
  }
}
</script>
