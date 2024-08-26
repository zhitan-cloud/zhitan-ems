<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'20%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            标准参数配置
          </div>
          <ModelNode ref="modelNode" @changeNode="changeNode" :showOpt="false" :modelCode="modelCode"></ModelNode>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <basicIndex ref="basicIndex"></basicIndex>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import basicIndex from "./basicIndex";
  import ModelNode from "../../basicsetting/modelNode/modelNode";

  export default {
    components: { ModelNode,basicIndex},
    created() {
      this.modelCode=this.$route.query.modelCode;
    },
    data() {
      return {
        modelCode:undefined,
        isCollapse: false,
      }
    },
    methods: {
      changeNode: function (node) {
        this.$refs.basicIndex.getList(node);
      },
      manageModel: function () {
        this.$router.push('/model');
      },
      changeModel: function (item) {
        this.$refs.modelNode.getList(item);
      },
      // 点击按钮，切换折叠与展开
      toggleCollapse () {
        this.isCollapse = !this.isCollapse
      }
    }
  };
</script>

<!--
<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" style="width: 20%">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>功能项</span>
            <span class="node-opt" style="float: right;">
              <el-link title="新增"  icon="el-icon-plus" @click="handleAdd"></el-link>
              <el-link title="修改" icon="el-icon-edit" @click="handleEdit"></el-link>
              <el-link title="删除"icon="el-icon-delete" @click="handleDel"></el-link>
            </span>
          </div>
          <el-menu default-active="2" class="el-menu-vertical-demo">
             <template v-for="(item,index) in parametersList">
              <el-menu-item v-on:click.native="changeNode(item.id,item.name)"  :max="max">
               &lt;!&ndash;<el-checkbox :key="item.id" @change="checked=>handleChange(checked,item.id)"></el-checkbox>&ndash;&gt;
                <span slot="title">{{item.name}}</span>
              </el-menu-item>
           </template>
          </el-menu>
        </el-card>
      </el-aside>
      <el-container>
        <el-main style="padding:0">
          <basicIndex ref="basicIndex"></basicIndex>
        </el-main>
      </el-container>
    </el-container>
    &lt;!&ndash; 添加或修改标准参数配置对话框 &ndash;&gt;
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="类别名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入类别名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类别编码" prop="code">
              <el-input v-model="form.code" placeholder="类别编码" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="是否报警" prop="showAlarm">
              <el-input v-model="form.showAlarm" placeholder="是否显示报警" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据类型" prop="dataType">
              <el-select v-model="form.dataType" placeholder="请选择数据类型">
                <el-option
                  v-for="dict in dataTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import basicIndex from "./basicIndex";
  import { listParameters, getParameters, addParameters, updateParameters,delParameters} from "@/api/basicSetup/parameters";
  export default {
    components: {basicIndex},
    data() {
      return {
        // 罩层遮
        loading: true,
        // 选中数组
        ids: [],
        id:undefined,
        max:1,
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // parameters表格数据
        parametersList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        opens: false,
        // 数据展示类型，1-实时数据，2-阶段数据字典
        dataTypeOptions: [],
        // 是否显示报警字典
        showAlarmOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          categoryName: undefined,
          categoryCode: undefined,
          dataType: undefined,
          showAlarm: undefined
        },
        // 表单参数
        form: {},

        // 表单校验
        rules: {
          name: [{ required: true, message: "标准参数类别名称不能为空", trigger: "blur" }],
          code: [{ required: true, message: "标准参数编码不能为空", trigger: "blur" }],
        },
      };
    },
    created() {
      this.getList();
      this.getDicts("data_type").then(response => {
        this.dataTypeOptions = response.data;
      });
    },
    methods: {
      getList(){
        this.loading = true;
        listParameters(this.queryParams).then(response => {
          this.parametersList = response.rows;
          this.$refs.basicIndex.getList(this.parametersList[0]);
          //this.total = response.total;
          this.loading = false;
        });
      },
      // 数据展示类型，1-实时数据，2-阶段数据字典翻译
      dataTypeFormat(row, column) {
        return this.selectDictLabel(this.dataTypeOptions, row.dataType);
      },
      // 是否显示报警字典翻译
      showAlarmFormat(row, column) {
        return this.selectDictLabel(this.showAlarmOptions, row.showAlarm);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          name: undefined,
          configInfo: undefined,
          code: undefined,
          dataType: undefined,
          showAlarm: undefined,
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

      handleChange(boolean,item){
        if(boolean){
          this.id =item;
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加标准参数";
      },
      handleEdit() {
        this.reset();
        const id =this.id;
        getParameters(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改标准参数";
        });
      },
      /** 删除按钮操作 */
      handleDel() {
        const ids =this.id;
        this.$confirm('是否确认删除标准参数编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delParameters(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 保存按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateParameters(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("保存成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addParameters(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("保存成功");
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
      changeNode(data) {
        this.$refs.basicIndex.getList(data);
      }
  }
  };
</script>
-->
