<template>
  <div class="app-container" style="padding: 0">
    <div slot="header" class="clearfix">
      <el-button style="float: right; padding: 8px; margin-left:8px" type="text"
                 icon="el-icon-setting"
                 @click="saveSetting">
        保存配置
      </el-button>
      <el-button style="float: right; padding: 8px; margin-left:8px" type="text"
                 icon="el-icon-setting"
                 @click="reset()">
        重新选择底图
      </el-button>
    </div>
    <el-row>
      <el-col :span="18">
        <el-upload
            v-if="filePath==='空节点'"
            class="configure-upload"
            drag
            ref="upload"
            :limit="1"
            :headers="uploadData.headers"
            :action="uploadData.url"
            :with-credentials="true"
            :on-success="handleFileSuccess"
            :show-file-list="false">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <div id="svgHtml" v-if="filePath !== '空节点'">
          <div v-html="svgHtml"/>
        </div>
      </el-col>
      <el-col :span="6" style="height: calc(100vh - 165px); overflow: auto">
        <el-table :data="tags" border>
          <el-table-column label="参数" align="center" prop="param"/>
          <el-table-column label="采集点" align="center" prop="tag">
            <template slot-scope="scope">
              <el-autocomplete popper-class="my-autocomplete"
                               v-model="scope.row['tag']"
                               :fetch-suggestions="querySearch"
                               placeholder="请输入指标编码">
                <template slot-scope="{ item }">
                  <div class="name">{{ item.value }}</div>
                  <span class="addr">{{ item.name }}</span>
                </template>
              </el-autocomplete>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {
  getAllCollectTag,
  getConfigure,
  saveSetting,
  updateEquipmentfile
} from "@/api/basicSetup/equipmentfile";
import {getToken} from "@/utils/auth";

export default {
  name: "ConfigureView",
  data() {
    return {
      currentNode: '',
      form: {
        nodeId: '',
        fileName: '',
        svgType:''
      },
      filePath: '空节点',
      svgHtml: '',
      tags: [],
      uploadData: {
        headers: {
          Authorization: "Bearer " + getToken()
        },
        url: process.env.VUE_APP_BASE_API + "/basicSetup/equipmentfile/upload"
      },
    }
  },
  methods: {
    changeModelNode(modelNode) {
      this.currentNode = modelNode;
      this.filePath = '空节点';
      getConfigure(modelNode.id).then(response => {
        this.tags = [];
        this.svgHtml = '';
        if (response.code === 200) {
          if (response.data) {
            this.filePath = process.env.VUE_APP_BASE_API + response.data.filePath;
            this.tags = response.data.infoList;
            this.getSvg();
          }
        } else {
          this.msgError(response.msg);
        }
      });
    },
    // 初始化svg
    getSvg() {
      /* 创建xhr对象 */
      const xhr = new XMLHttpRequest();
      xhr.open("GET", this.filePath, true);
      xhr.send();
      /* 监听xhr对象 */
      xhr.addEventListener("load", () => {
        this.svgHtml = xhr.responseText;
        let values = xhr.responseXML.getElementsByTagName('text');
        let tagTemps =[];
        for (let i = 0; i < values.length; i++) {
          if (values[i].getAttribute("id") != undefined)
            tagTemps.push({
              "param": values[i].textContent,
              "tag": "",
              "tagType":"COLLECT"
            });
        }
        console.log(this.tags.length,tagTemps.length);
        if (this.tags.length === 0 || this.tags.length!=tagTemps.length) {
          this.tags=[];
          this.tags=tagTemps;
          console.log("222",this.tags.length,tagTemps.length);
        }
      });
    },
    //上传成功后
    handleFileSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.$refs.upload.clearFiles();
        this.form.nodeId = this.currentNode.id;
        this.form.filePath = response.msg;
        this.form.svgType = 'COLLECT';
        updateEquipmentfile(this.form).then(result => {
          if (result.code === 200) {
            this.filePath = process.env.VUE_APP_BASE_API + response.msg;
            this.tags=[];
            this.getSvg();
          } else {
            this.msgError(result.msg);
          }
        });
      } else {
        this.msgError(response.msg);
      }
    },
    querySearch(queryString, cb) {
      if (queryString) {
        getAllCollectTag({codeOrName: queryString, indexType: 'COLLECT'}).then(response => {
          // 调用 callback 返回建议列表的数据
          let result = response.data;
          let values = [];
          result.forEach(item => {
            values.push({
              value: item.code,
              name: item.name
            })
          });
          cb(values);
        });
      }
    },
    saveSetting() {
      saveSetting(this.currentNode.id, this.tags).then(response => {
        if (response.code === 200) {
          this.$message.success(response.msg);
        } else {
          this.$message.error(response.msg);
        }
      });
    },
    reset() {
      this.filePath = '空节点';
    }
  }
}
</script>
<style scoped lang="scss">
.el-autocomplete {
  width: 100%;
}

.my-autocomplete li {
  line-height: normal;
  padding: 7px;
}

.my-autocomplete li .name {
  text-overflow: ellipsis;
  overflow: hidden;
}

.my-autocomplete li .addr {
  font-size: 12px;
  color: #b4b4b4;
}

.my-autocomplete li .highlighted .addr {
  color: #ddd;
}
</style>
