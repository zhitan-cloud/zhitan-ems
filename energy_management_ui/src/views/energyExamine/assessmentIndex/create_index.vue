<template>
  <div class="app-container">
    <span>{{modeNode?modeNode.label:''}}</span>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px" class="query-form">
      <el-form-item>
        <el-radio-group v-model="queryParams.eierarchyFlag">
          <el-radio label="B" style="margin-right: 10px!important;" onselect="true">当前单元</el-radio>
          <el-radio label="ALL">包含下级</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="selectList">查询</el-button>
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="openImportIndexTable()"
            v-hasPermi="['energyExamine:assessmentIndex:add']"
          >添加</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
    </el-row>

    <el-table v-loading="loading" :data="assessmentIndexList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单位名称" align="center" prop="modename"/>
      <el-table-column label="指标名称" align="center" prop="indexname"/>
      <el-table-column label="计量单位" align="center" prop="jldw"/>
      <el-table-column label="考核依据" align="center">
        <el-table-column label="能耗计划" align="center" prop="plan" :formatter="planFormat"/>
        <el-table-column label="能效对标" align="center" prop="benchmarking" :formatter="benchmarkingFormat"/>
        <el-table-column label="能源双控" align="center" prop="dualControl" :formatter="dualControlFormat"/>
        <el-table-column label="指定标准" align="center" prop="customStandard" :formatter="customStandardFormat"/>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">

        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['energyExamine:assessmentIndex:edit']"
          >考核依据</el-button>


          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['energyExamine:assessmentIndex:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用能考核标准对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="单位名称" prop="modename">
              <el-input v-model="form.modename" readonly="readonly" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指标名称" prop="indexname">
              <el-input v-model="form.indexname"  readonly="readonly"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位" prop="jldw">
              <el-input v-model="form.jldw"  readonly="readonly"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="" label-width="30px">
              <el-checkbox v-model="form.plan" name="plan">能耗计划值作为参考依据</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label=""  label-width="30px">
              <el-checkbox v-model="form.benchmarking" name="benchmarking" >能耗对标值作为参考依据</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
              <el-form-item label="标杆范围" >
                <el-select v-model="form.benchRange" placeholder="请选择标杆范围">
                  <el-option
                    v-for="dict in benchRangeOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标杆类型"  >
            <el-select v-model="form.benchType" placeholder="请选择标杆类型">
              <el-option
                v-for="dict in benchTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label=""  label-width="30px">
            <el-checkbox v-model="form.dualControl" name="dualControl">能源双控值作为参考依据</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label=""  label-width="30px">
            <el-checkbox v-model="form.customStandard" >指定标准作为参考依据</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label=""  label-width="10px">
              <el-input v-model="form.customVal" size="mini"/>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <addIndexTable ref="addIndex"   />

  </div>
</template>

<script>
  import { listAssessmentIndex,listAssessmentIndexModel , getAssessmentIndex, delAssessmentIndex, addAssessmentIndex, updateAssessmentIndex, exportAssessmentIndex } from "@/api/energyExamine/assessmentIndex";
  import addIndexTable from "./addIndexTable";
  export default {
    name: "implement",
    components: { addIndexTable },
    data() {
      return {
        //模型节点
        modeNode:'',
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 用能考核标准表格数据
        assessmentIndexList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 主键id字典
        idOptions: [],
        // 模型节点id字典
        modeNodeIdOptions: [],
        // 指标id字典
        indexIdOptions: [],
        // 是否能耗计划 Y  N字典
        planOptions: [],
        // 是否对标 Y  N字典
        benchmarkingOptions: [],
        // 标杆范围字典项字典
        benchRangeOptions: [],
        // 标杆类型字典项字典
        benchTypeOptions: [],
        // 是否能源双控 Y N字典
        dualControlOptions: [],
        // 是否自定义标准 Y  N字典
        customStandardOptions: [],
        // 指定自定义标准数值字典
        customValOptions: [],
        // 查询参数
        queryParams: {
          eierarchyFlag:"B",
          pageNum: 1,
          pageSize: 10,
        },
        // 表单参数
        form: {},
        setIndexWindow:{
          title:"添加指标",
          open:false,
          indexList:[]
        },
        // 表单校验
        rules: {
          customVal: [
            { required: true, pattern:/^\+?[1-9]\d*$/,message: "指定标准值只能是数字", trigger: "blur" }
          ],
        }
      };
    },
    created() {
      this.getList();

      this.getDicts("benchmarkingRange").then(response => {
        this.benchRangeOptions = response.data;
      });
      this.getDicts("benchmarkingType").then(response => {
        this.benchTypeOptions = response.data;
      });

    },
    methods: {
      modelNodeChange(modelNode) {
        if(modelNode)
        {
          this.modeNode=modelNode;
          this.getList();
        }
        console.log("lable="+modelNode.label);
        console.log("id="+modelNode.id);
      },
      /** 查询用能考核标准列表 */
      getList() {
        this.loading = true;
        listAssessmentIndexModel(this.modeNode.id,this.queryParams).then(response => {
          this.assessmentIndexList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },

      // 通用符号格式化
      planFormat(row, column) {
        return row.plan?"√":"X";
      },
      benchmarkingFormat(row, column) {
        return row.benchmarking?"√":"X";
      },
      dualControlFormat(row, column) {
        return row.dualControl?"√":"X";
      },
      customStandardFormat(row, column) {
        return row.customStandard?"√  "+row.customVal:"X";
      },
      /**     // 是否对标 Y  N字典翻译
      benchmarkingFormat(row, column) {
        return this.selectDictLabel(this.benchmarkingOptions, row.benchmarking);
      },

      // 是否能源双控 Y N字典翻译
      dualControlFormat(row, column) {
        return this.selectDictLabel(this.dualControlOptions, row.dualControl);
      },
      // 是否自定义标准 Y  N字典翻译
      customStandardFormat(row, column) {
        return this.selectDictLabel(this.customStandardOptions, row.customStandard);
      },
   */
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          modeNodeId: undefined,
          indexId: undefined,
          plan: undefined,
          benchmarking: undefined,
          benchRange: undefined,
          benchType: undefined,
          dualControl: undefined,
          customStandard: undefined,
          customVal: undefined
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加用能考核标准";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getAssessmentIndex(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "指标考核依据设置";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateAssessmentIndex(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addAssessmentIndex(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const indexname = row.indexname;
        this.$confirm('是否确认删除指标为"' + indexname + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAssessmentIndex(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有用能考核标准数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportAssessmentIndex(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      /** 打开指标表弹窗 */
      openImportIndexTable() {
        this.$refs.addIndex.show(this.modeNode);
      },
      //查询功能
      selectList()
      {
        this.getList();
      }
    }
  };
</script>
