
<template>
  <!-- eslint-disable vue/require-component-is -->
  <component v-bind="linkProps(to,menuNode)">
    <slot />
  </component>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  props: {
    to: {
      type: String,
      required: true
    },
    menuNode:{}
  },
  methods: {
    linkProps(url,menuNode) {
      if (isExternal(url)) {
        return {
          is: 'a',
          href: url,
          target: '_blank',
          rel: 'noopener'
        }
      }
      if(menuNode.pageQueryParameter==null || menuNode.pageQueryParameter==undefined ||menuNode.pageQueryParameter=="")
      {
        return {
          is: 'router-link',
          to: url
        }
      }else {
        let queryParaObj = JSON.parse(menuNode.pageQueryParameter);
        return {
          is: 'router-link',
          // to: {name:menuNode.name, params:queryParaObj}
          to: {path:url, query:queryParaObj}
        }
      }
    }
  }
}
</script>
