<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            <el-select v-model="modelData" placeholder="请选择模型"
                       style="width:220px"
                       size="small"
                       @change="changeModel">
              <el-option
                v-for="model in modelInfoOptions"
                :key="model.modelCode"
                :label="model.modelName"
                :value="model.modelCode"
              />
            </el-select>
           <!-- <el-button icon="el-icon-setting" circle title="管理模型" style="float:right;padding:8px"
                       @click="manageModel"></el-button>-->
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <el-main style="padding:0">
          <CollectionSetting ref="CollectionSetting"></CollectionSetting>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import CollectionSetting from "./indexCollectionSetting";
  import ModelNode from "../basicsetting/modelNode/modelNode";
  import {listModel} from "@/api/basicsetting/model";



  export default {
    components: {CollectionSetting, ModelNode},
    created() {
      listModel({isShow: 1}).then(response => {
        this.modelInfoOptions = response.data;
        if (this.modelInfoOptions.length > 0) {
          this.modelData = this.modelInfoOptions[0].modelCode;
          this.$refs.modelNode.getList(this.modelData);
        }
      });
    },

    data() {
      return {
        modelData: '',
        modelInfoOptions: []
      }
    },
    methods: {
      changeNode: function (node) {
        this.$refs.CollectionSetting.modelNodeChange(node);
      },
      manageModel: function () {
        this.$router.push('/model');
      },
      changeModel: function (item) {
        this.$refs.modelNode.getList(item);
      },
    }
  };
</script>
