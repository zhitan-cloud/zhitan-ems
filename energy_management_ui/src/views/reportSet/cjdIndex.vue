<template>
  <div class="app-container item-style" style="padding:0">

    <!--表格-->
    <el-form :rules="model.rules" :model="model" ref="form">
      <el-table v-loading="loading" :data="model.energyindexList"  height="570">
        <el-table-column label="表具名称" align="center" prop="meterName"/>
        <el-table-column label="采集点名称" align="center" prop="indexName"/>
        <el-table-column label="采集点编码" align="center" prop="indexCode"/>
        <el-table-column label="操作" align="center" >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="setting(scope.row)"
              v-hasPermi="['basicsetting:template:edit']"
            >设置
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!--报警界面start-->
    <el-dialog :title="settingModel.title" :visible.sync="settingModel.open" width="800px" :close-on-click-modal="false"
               @close='closeYjDialog'>
      <el-form ref="settingForm" :model="settingModel" :rules="settingModel.rules" label-width="80px">
        <el-table v-loading="loading" :data="settingModel.settingOptions" border >
          <el-table-column label="时间类型" align="center" prop="dateTypeShow"/>
          <el-table-column label="上限值" align="center" prop="limitValUp" >
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.limitValUp" size="small" controls-position="right" placeholder="请输入值"
                               clearable></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="上限替换值" align="center" prop="limitReplaceValUp" >
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.limitReplaceValUp" size="small" controls-position="right" placeholder="请输入值"
                               clearable></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="下限值" align="center" prop="limitValDown" >
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.limitValDown" size="small" controls-position="right" placeholder="请输入值"
                               clearable></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="下限替换值" align="center" prop="limitReplaceValDown" >
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.limitReplaceValDown" size="small" controls-position="right" placeholder="请输入值"
                               clearable></el-input-number>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="settingSubmitForm">确 定</el-button>
        <el-button @click="yjSetCancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--预警界面end-->
  </div>
</template>

<script>
import {
  listSet,
  getSettingIndex,
  updateDialogForm,
} from "@/api/reportSet/index"
import {listLimitType} from "@/api/basicsetting/limitType"

const myModelNode = undefined;
export default {
  name: 'EnergyIndex',
  data() {
    const checkNum = (rule, value, callback) => {
      let reg = /(^([-]?)[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^([-]?)(0){1}$)|(^([-]?)[0-9]\.[0-9]([0-9])?$)/;
      if (!reg.test(value) && value != "") {
        callback(new Error('数字可2位小数'));
      } else {
        callback();
      }
    };
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      names: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      //table
      // 表单参数
      form: {
        supplier2: "",
        type: "",
        desc: "",
        input: "",
        min: "",
        max: ""
      },
      //配置表使用模型
      settingModel: {
        title: "",
        open: false,
        settingOptions: [
          // {
          //   interval:",
          //   limit:"",
          //   limit_value:"",
          // },
        ],
        // 时段数组
        timeSlotOptions: [],
        limitTypeOptions: [],
        //报警级别
        alarmLevelOptions: [],
        //响应
        resposeList: [],
        stateById: "",
        rules: {
          numRule: [
            {type: "number", validator: checkNum, trigger: "change"},
          ],
        },
      },
      model: {
        // 指标信息表格数据
        energyindexList: [],
        rules: {
          numRule: [
            {type: "number", validator: checkNum, trigger: "change"},
            // {pattern: /^\d+(\.\d{0,2})?$/, message: '数字可2位小数', trigger: "change"}
          ],
        },
      },
      //主表选中的启停状态数组
      startStopOptions: [],

      editModeEnabled: true,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 主键字典
      indexIdOptions: [],
      // 指标名称字典
      nameOptions: [],
      // 指标编码字典
      codeOptions: [],
      // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典
      indexCategoryOptions: [],
      // 备注字典
      remarkOptions: [],
      // 单位字典
      unitIdOptions: [],
      //报警级别
      alarmLevel: "",
      // 默认值
      dictDefaultVal: undefined,
      // 查询参数
      hbCount: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        code: undefined,
        indexCategory: undefined,
        nodeId: undefined,
        //指标index
        indexType: "COLLECT"
      },
      currentNode: undefined,
      indexCategoryDefaultVal: undefined,
      unitDefaultVal: undefined
    };
  },
  created() {
    this.getDicts("warn_time_slot").then(response => {
      this.settingModel.timeSlotOptions = response.data;
    });
    listLimitType(this.queryParams).then(response => {
      this.hbCount = response.rows.length;
      this.settingModel.limitTypeOptions = response.rows;
    });
    this.getDicts("alarm_type").then(response => {
      this.settingModel.alarmTypeOptions = response.data;
    });
    this.getDicts("alarm_level").then(response => {
      this.settingModel.alarmLevelOptions = response.data;
      this.dictDefaultVal = this.settingModel.alarmLevelOptions.find(f => f.isDefault === 'Y');
    });
  },
  methods: {
    /** 查询指标信息列表 */
    setModelNode(modelNode) {
      this.myModelNode = modelNode;
      this.currentNode = modelNode;
      this.getList();
    },
    /** 查询指标信息列表 */
    getList() {
      if (this.myModelNode) {
        this.loading = true;
        this.queryParams.nodeId = this.myModelNode.id;
        //接口
        getSettingIndex(this.queryParams).then(response => {
          this.model.energyindexList = response.data;
          this.total = response.total;
          this.loading = false;
        });
      } else {
        this.energyindexList = [];
      }
    },
    yjSetCancel() {
      this.settingModel.open = false;
      this.settingModel.settingOptions = [];
    },
    //编辑
    setting(e) {
      listSet({indexId: e.indexId, nodeId: this.currentNode.id}).then(response => {
        this.settingModel.settingOptions = response.rows;
        this.settingModel.open = true;
        this.settingModel.title = "指标编号：" + e.indexCode;
      });
    },
    //表单提交
    settingSubmitForm() {
      console.log(JSON.stringify(this.settingModel.settingOptions));
      this.$refs["settingForm"].validate(valid => {
        if (valid) {
          updateDialogForm(this.settingModel.settingOptions || "").then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              this.open = false;
              this.settingModel.settingOptions = [];
              this.settingModel.open = false;
            } else {
              this.msgError(response.msg);
            }
          });
          this.settingModel.open = false;
        }
      });
    },
    closeYjDialog() {
      this.settingModel.settingOptions = [];
      return false;
    },
  }
};
</script>
<style>
.item-style .el-form-item__content {
  margin-left: 0px !important;
}
</style>
