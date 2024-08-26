<template>
  <div v-html="svgHtml"/>
</template>

<script>
import {getConfigure, getLiveData} from "@/api/basicSetup/equipmentfile";

export default {
  props: ["nodeId"],
  created() {
    let nodeId = this.$props.nodeId;
    if (nodeId) {
      this.show(nodeId);
    }
  },
  data() {
    return {
      svgHtml: null,
      filePath: null,
      tags: null,
      tagCodes: [],
      valueItems: null,
      timer: null
    }
  },
  mounted() {
    clearInterval(this.timer);
    this.refreshData()
  },
  methods: {
    show(nodeId) {
      console.log(nodeId)
      this.tagCodes = [];
      this.filePath = '';
      getConfigure(nodeId).then(response => {
        this.tags = [];
        this.svgHtml = '';
        if (response.code === 200) {
          if (response.data) {
            this.filePath = process.env.VUE_APP_BASE_API + response.data.filePath;
            this.tags = response.data.infoList;
            this.getSvg();
            this.refresh();
          }
        } else {
          this.msgError(response.msg);
        }
      });
    },
    getSvg() {
      const xhr = new XMLHttpRequest();
      xhr.open("GET", this.filePath, true);
      xhr.send();
      xhr.addEventListener("load", () => {
        const resXML = xhr.responseXML;
        let svgDom = resXML.documentElement.cloneNode(true);
        let values = svgDom.getElementsByTagName('text');
        for (let i = 0; i < values.length; i++) {
          let tag = this.tags.filter(f => f.param === values[i].textContent);
          if (tag && tag.length > 0) {
            let tagCode = tag[0].tag;
            values[i].textContent = '';
            if (tagCode) {
              values[i].setAttribute("id", tagCode);
              this.tagCodes.push(tagCode)
            }
          }
        }

        let oSerializer = new XMLSerializer();
        this.svgHtml = oSerializer.serializeToString(svgDom);
      });
    },
    refreshData() {
      this.timer = setInterval(() => {
        this.refresh();
      }, 3000);
    },
    refresh() {
      if (this.tagCodes.length === 0) {
        return;
      }

      getLiveData(this.tagCodes).then(response => {
        if (response.code === 200) {
          if (response.data) {
            response.data.forEach(tagValue => {
              let value = '';
              if (tagValue.value) {
                value = parseFloat(tagValue.value).toFixed(2);
              } else {
                value = "0";
              }

              let el = document.getElementById(tagValue.tagCode);
              if (el) {
                el.textContent = value;
              }
            });
            //这块是单独加的 用来处理 没有指标或者指标没有结果的 都设置0
            let allText = document.getElementsByTagName("text");
            for (let i = 0; i < allText.length; i++) {
              // console.log(i + "textContent=" + allText[i].textContent);
              if (allText[i].textContent == null || allText[i].textContent == "") {
                allText[i].textContent = "0";
              }
            }
          }
        }
      });
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
  }
}
</script>
