<template>
  <elFrame :src="sqlApi" />
</template>
<script>
import { mapGetters } from 'vuex'
import elFrame from '@/components/Iframe/index.vue'
export default {
  name: 'Sql',
  components: { elFrame },
  computed: {
    ...mapGetters([
      'sqlApi'
    ])
  }
}
</script>
