<template>
  <div class="app-container" style="    margin-top: -15px;    padding: 0;">
    <el-card class="index-storage-container"
             shadow="never">
      <el-form label-position="top" label-width="110px" :model="indexStorageData">
        <el-form-item style="margin-bottom: 0">
          <el-button size="mini" @click="saveIndexStorage" class="fr" type="primary">保存数据存储
          </el-button>
        </el-form-item>
        <el-form-item>
          <div slot="label">指标计算公式&nbsp;&nbsp;&nbsp;&nbsp;
            <el-button size="mini" @click="selectCalcFunction">选择计算函数</el-button>
          </div>
          <el-input type="textarea" rows="3" v-model="indexStorageData.indexFormula.formulaText"
                    id="formulaText"
                    @blur="parseFormula"></el-input>
        </el-form-item>
        <el-form-item>
          <el-table :data="indexStorageData.indexFormula.indexFormulaParams" style="width: 100%"
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
      <el-card class="box-card" header="数据存储">
        <el-form label-width="110px">
          <el-form-item v-for="period in indexStorageData.indexStorage">
            <div slot="label">{{getTimeLabel(period.timeType)}} 数据</div>
            <el-radio-group v-model="period.calcType">
              <el-radio-button label="CALC">自动计算</el-radio-button>
              <el-radio-button label="INPUT">手工录入</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </el-card>
      <el-drawer title="选择计算函数" :visible.sync="calcFunctionShow" direction="rtl"
                 @open="showCalcFunctionDialog"
                 size="35%">
        <CalcFunction ref="calcFunction" :showOpt="false"
                      @confirmCalcFunction="confirmCalcFunction"></CalcFunction>
      </el-drawer>
    </el-card>
  </div>
</template>
<script>
  import CalcFunction from "../function/index";
  import {
    getIndexStorage,
    getNodeIndex,
    parseFormula,
    saveIndexStorage
  } from "@/api/basicsetting/indexStorage";

  export default {
    name: "IndexStorage",
    props: ["nodeId"],
    components: {CalcFunction},
    data() {
      return {
        indexStorageData: undefined,
        calcFunctionShow: false,
        nodeIndex: [],
        indexId: ''
      }
    },
    created() {
      this.initData();
    },
    methods: {
      initData() {
        this.indexStorageData = {
          indexFormula: {
            id: '',
            indexId: '',
            formulaText: '',
            indexFormulaParams: [],
          },
          indexStorage: []
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
        this.indexStorageData.indexFormula.formulaText = result;
      },
      parseFormula() {
        parseFormula({"calcText": this.indexStorageData.indexFormula.formulaText}).then(
            response => {
              let newParamData = response.data;
              let paramData = this.indexStorageData.indexFormula.indexFormulaParams.filter(f => {
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

              this.indexStorageData.indexFormula.indexFormulaParams = paramData;
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
      saveIndexStorage() {
        saveIndexStorage(this.indexId, this.indexStorageData).then((response) => {
          if (response.code === 200) {
            this.$message.success("数据存储成功！");
          } else {
            this.$message.error("数据存储失败！");
          }
        });
      },
      getIndexStorage(indexId) {
        this.initData();
        this.indexId = indexId;
        getNodeIndex(this.nodeId).then((response) => {
          this.nodeIndex = response.data;
        });

        getIndexStorage(indexId).then(response => this.indexStorageData = response.data);
        // getCalcPeriod().then(response => this.indexStorageData.indexStorage = response.data);
      },
      getTimeLabel(timeType) {
        return this.selectTimeTypeLabel(timeType);
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

  .index-storage-container {
    height: calc(100vh - 60px);
    overflow: auto;
  }
</style>
