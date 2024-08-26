<template>
  <div class="app-container" style="padding-top: 0">
    <el-form label-position="top" label-width="110px" :model="deviceFormlaData">
      <el-form-item style="margin-bottom: 0">
        <el-button size="mini" @click="saveDeviceStorage" class="fr" type="primary">保存数据存储
        </el-button>
      </el-form-item>
      <el-form-item>
        <div slot="label">指标计算公式&nbsp;&nbsp;&nbsp;&nbsp;
          <el-button size="mini" @click="selectCalcFunction">选择计算函数</el-button>
        </div>
        <el-input type="textarea" rows="3" v-model="deviceFormlaData.deviceFormula.formulaText"
                  id="formulaText"
                  @blur="parseFormula"></el-input>
      </el-form-item>
      <el-form-item>
        <el-table :data="deviceFormlaData.deviceFormula.deviceFormulaParams" style="width: 100%"
                  border>
          <el-table-column label="参数名" prop="paramName" width="200"></el-table-column>
          <el-table-column label="参数值" prop="paramValue">
            <template slot-scope="scope">
              <el-autocomplete popper-class="my-autocomplete"
                               v-model="scope.row['paramValue']"
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
      </el-form-item>
    </el-form>
    <el-drawer title="选择计算函数" :visible.sync="calcFunctionShow" direction="rtl"
               @open="showCalcFunctionDialog"
               size="35%">
      <CalcFunction ref="calcFunction" :showOpt="false"
                    @confirmCalcFunction="confirmCalcFunction"></CalcFunction>
    </el-drawer>
  </div>
</template>
<style type="text/css">
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
<script>
  import CalcFunction from "../function/index";
  import {
    getNodeIndex,//这个是获取当前模型节点下的所有采集点或指标
    parseFormula,//这个是计算公式解析方法
  } from "@/api/basicsetting/indexStorage";
  import {
    getDeviceStorage,//这个是获取当前 模型节点下   指定状态下的   设备状态公式配置信息
    saveDeviceStorage,//这个是保存配置方法，会替换掉
  } from "@/api/basicsetting/deviceStorage";
  const  deviceCbk=undefined;
  export default {
    name: "IndexStorage",
    props: ["nodeId"],
    components: {CalcFunction},
    data() {
      return {
        deviceFormlaData: undefined,
        calcFunctionShow: false,
        nodeIndex: [],
        stateId: ''
      }
    },
    created() {
      this.initData();
    },
    methods: {
      initData() {
        this.deviceFormlaData = {
          deviceFormula: {
            id: '',
            deviceId: '',
            stateId: '',
            formulaText: '',
            calcText: '',
            deviceFormulaParams: [],
          }
        }
      },
      showCalcFunctionDialog() {
      },
      selectCalcFunction() {
        this.calcFunctionShow = true;
      },
      confirmCalcFunction(funcText) {
        this.calcFunctionShow = false;
        const elInput = document.getElementById('formulaText'); //根据id选择器选中对象
        const startPos = elInput.selectionStart;// input 第0个字符到选中的字符
        const endPos = elInput.selectionEnd;// 选中的字符到最后的字符
        if (startPos === undefined || endPos === undefined) {
          return;
        }
        const txt = elInput.value;
        // 将表情添加到选中的光标位置
        const result = txt.substring(0, startPos) + funcText + txt.substring(endPos);
        elInput.value = result;// 赋值给input的value
        // 重新定义光标位置
        elInput.focus();
        elInput.selectionStart = startPos + funcText.length;
        elInput.selectionEnd = startPos + funcText.length;
        this.deviceFormlaData.deviceFormula.formulaText = result;
      },
      parseFormula() {
        parseFormula({"calcText": this.deviceFormlaData.deviceFormula.formulaText}).then(
          response => {
            let newParamData = response.data;
            let paramData = this.deviceFormlaData.deviceFormula.deviceFormulaParams.filter(f => {
              let exist = newParamData.find(param => param === f.paramName);
              return exist && exist.length > 0;
            });

            newParamData.forEach(item => {
              let exist = paramData.find(param => param.paramName === item);
              if (!exist || exist.length === 0) {
                paramData.push({
                  "paramName": item,
                  "paramValue": ''
                })
              }
            });

            this.deviceFormlaData.deviceFormula.deviceFormulaParams = paramData;
          });
      },
      querySearch(queryString, cb) {
        let nodeIndex = this.nodeIndex;
        let results = queryString ? nodeIndex.filter((index) => {
          return index.code.toLowerCase().indexOf(queryString.toLowerCase()) === 0 ||
            index.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        }) : nodeIndex;
        // 调用 callback 返回建议列表的数据
        let values = [];
        results.forEach(item => {
          values.push({
            value: item.code,
            name: item.name
          })
        });
        cb(values);
      },
      saveDeviceStorage() {
        saveDeviceStorage(this.nodeId, this.stateId,this.deviceFormlaData).then((response) => {
          if (response.code === 200) {
            this.$message.success("数据存储成功！");
            getDeviceStorage(this.nodeId,this.stateId).then(response => this.deviceFormlaData = response.data);
            this.deviceCbk();
          } else {
            this.$message.error("数据存储失败！");
          }
        });
      },
      getIndexStorage(stateId,cbk) {
        this.deviceCbk = cbk;
        this.initData();
        this.stateId = stateId;
        getNodeIndex(this.nodeId).then((response) => {
          this.nodeIndex = response.data;
        });
        getDeviceStorage(this.nodeId,stateId).then(response => this.deviceFormlaData = response.data);
      }
    }
  }
</script>

<style scoped>

</style>
