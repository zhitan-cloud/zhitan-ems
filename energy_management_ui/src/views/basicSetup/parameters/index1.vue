<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="168px">
      <el-form-item label="标准参数类别名称" prop="categoryName">
        <el-input
          v-model="queryParams.categoryName"
          placeholder="请输入标准参数类别名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标准参数类别编码" prop="categoryCode">
        <el-input
          v-model="queryParams.categoryCode"
          placeholder="请输入标准参数类别编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据展示类型" prop="dataType">
        <el-select v-model="queryParams.dataType" placeholder="请选择数据展示类型" clearable size="small">
          <el-option
            v-for="dict in dataTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['basicSetup:category:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['basicSetup:category:edit']"
        >修改</el-button>
      </el-col>
    </el-row>

    <template>
      <el-table v-loading="loading" :data="categoryList" style="width: 100%"  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" prop="id"/>
        <el-table-column type="expand" >
          <template slot-scope="props">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body" style="width: 90%;border: 1px solid #ddd;margin-bottom: 20px"
                 v-for="(item,index) in parametersList"
                 v-if="item.categoryId === props.row.id">
            <thead class="has-gutter" style="background: #e7eaec">
            <tr class="">
              <th colspan="1" rowspan="1" class="el-table_4_column_24  is-center is-leaf">
                <div class="cell">名称</div>
              </th>
              <th colspan="1" rowspan="1" class="el-table_4_column_25  is-center is-leaf" v-for="(items,indexs) in JSON.parse(item.name)" v-bind:key="items.id">
                <div class="cell">{{items.name}}</div>
              </th>
              <th colspan="1" rowspan="1" class="el-table_4_column_26  is-center is-leaf">
                <div class="cell">操作</div>
              </th>
              <th class="gutter" style="width: 0px; display: none;"></th>
            </tr>
            </thead>
            <tbody>
            <tr class="el-table__row expanded" v-for="(items,indexs) in JSON.parse(item.configInfo)" v-bind:key="items.id" style="border: 1px solid #ddd">
              <td rowspan="1" colspan="1" class="el-table_1_column_3 is-center " >
                <div class="cell">{{items.value}}</div>
              </td>
              <td rowspan="1" colspan="1" class="el-table_1_column_3 is-center " >
                <div class="cell">测试1</div>
              </td>
              <td rowspan="1" colspan="1" class="el-table_1_column_3 is-center " >
                <div class="cell">测试2</div>
              </td>
              <td rowspan="1" colspan="1" class="el-table_1_column_7 is-center small-padding fixed-width">
                <div class="cell">
                  <button type="button" class="el-button el-button&#45;&#45;text el-button&#45;&#45;mini" @click="handleRevise(item.id)">
                    <i class="el-icon-edit"></i>
                    <span>修改</span>
                  </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
          </template>
        </el-table-column>
        <el-table-column label="标准参数类别名称" align="center" prop="categoryName"/>
        <el-table-column label="标准参数类别编码" align="center" prop="categoryCode"/>
        <el-table-column label="数据展示类型" align="center" prop="dataType" :formatter="dataTypeFormat"/>
        <el-table-column label="是否显示报警" align="center" prop="showAlarm"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleSave(scope.row)"
              v-hasPermi="['basicSetup:parameters:add']"
            >新增</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['basicSetup:category:edit']"
            >修改</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改标准参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="类别名称" prop="categoryName">
              <el-input v-model="form.categoryName" placeholder="请输入类别名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类别编码" prop="categoryCode">
              <el-input v-model="form.categoryCode" placeholder="类别编码" />
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
    <!--&lt;!&ndash; 添加或修改basicSetup对话框 &ndash;&gt;-->
    <el-dialog :title="title" :visible.sync="opens" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row  v-for='(form,index) in lists' v-bind:key='form.id'>
          <el-col :span="15">
            <el-form-item label="标题" prop="names">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-button type="button" plain @click="addEl">添加</el-button>
            <el-button type="button" @click="del(index)">删除</el-button>
          </el-col>
        </el-row>
        <el-row  v-for='(form,index) in list' v-bind:key='form.id'>
          <el-col :span="15">
            <el-form-item label="值" prop="value">
              <el-input v-model="form.value" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-button type="button" plain @click="addE2">添加</el-button>
            <el-button type="button" @click="del2(index)">删除</el-button>
          </el-col>
        </el-row>
        <el-input type="hidden"  v-model="form.categoryId"></el-input>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormC">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listParameters, getParameters, addParameters, updateParameters} from "@/api/basicSetup/parameters";
  import { listCategory, getCategory,  addCategory, updateCategory } from "@/api/basicSetup/category";
  export default {
    data() {
      return {
        // 罩层遮
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // parameters表格数据
        parametersList: [],
        categoryList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        opens: false,
        // 是否显示报警字典
        idOptions: [],
        // 标准参数类别名称字典
        nameOptions: [],
        // 设计内容字典
        configInfoOptions: [],
        // 设计内容字典
        createByOptions: [],
        // 设计内容字典
        createTimeOptions: [],
        // 设计内容字典
        updateByOptions: [],
        // 设计内容字典
        updateTimeOptions: [],
        // 设计内容字典
        remarkOptions: [],
        // 标准参数类别编码字典
        codeOptions: [],
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
          showAlarm: undefined,
          categoryId: undefined
        },
        // 表单参数
        form: {},
        lists: [
          { name: ""}
        ],
        list: [
          {value: ""}
        ],
        // 表单校验
        rules: {
          id: [{ required: true, message: "是否显示报警不能为空", trigger: "blur" }],
          name: [{ required: true, message: "标准参数类别名称不能为空", trigger: "blur" }], }
      };
    },
    created() {
      this.getList();
      this.getLists();
      this.getDicts("${column.dictType}").then(response => {
        this.idOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.nameOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.configInfoOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.createByOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.createTimeOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.updateByOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.updateTimeOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.remarkOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.codeOptions = response.data;
      });
      this.getDicts("data_type").then(response => {
        this.dataTypeOptions = response.data;
      });
      this.getDicts("${column.dictType}").then(response => {
        this.showAlarmOptions = response.data;
      });
    },
    methods: {
      /** 查询parameters列表 */
      getList() {
        this.loading = true;
        listCategory(this.queryParams).then(response => {
          this.categoryList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      getLists(){
        this.loading = true;
        listParameters(this.queryParams).then(response => {
          this.parametersList = response.rows;
          //this.total = response.total;
          this.loading = false;
        });
      },
      // 标准参数类别名称字典翻译
      idFormat(row, column) {
        return this.selectDictLabel(this.idOptions, row.id);
      },
      // 标准参数类别名称字典翻译
      nameFormat(row, column) {
        return this.selectDictLabel(this.nameOptions, row.name);
      },
      // 设计内容字典翻译
      configInfoFormat(row, column) {
        return this.selectDictLabel(this.configInfoOptions, row.configInfo);
      },
      // 设计内容字典翻译
      createByFormat(row, column) {
        return this.selectDictLabel(this.createByOptions, row.createBy);
      },
      // 设计内容字典翻译
      createTimeFormat(row, column) {
        return this.selectDictLabel(this.createTimeOptions, row.createTime);
      },
      // 设计内容字典翻译
      updateByFormat(row, column) {
        return this.selectDictLabel(this.updateByOptions, row.updateBy);
      },
      // 设计内容字典翻译
      updateTimeFormat(row, column) {
        return this.selectDictLabel(this.updateTimeOptions, row.updateTime);
      },
      // 设计内容字典翻译
      remarkFormat(row, column) {
        return this.selectDictLabel(this.remarkOptions, row.remark);
      },
      // 标准参数类别编码字典翻译
      codeFormat(row, column) {
        return this.selectDictLabel(this.codeOptions, row.code);
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
        this.opens = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          name: undefined,
          configInfo: undefined,
          createBy: undefined,
          createTime: undefined,
          updateBy: undefined,
          updateTime: undefined,
          remark: undefined,
          code: undefined,
          dataType: undefined,
          showAlarm: undefined,
          categoryName: undefined,
          categoryCode: undefined
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
        this.title = "添加标准参数类别";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCategory(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改标准参数类别";
        });
      },
      handleSave (row) {
        this.opens = true;
        this.reset();
        const id = row.id || this.ids
        this.form.categoryId=id;
        this.opens = true;
        this.title = "新增标准参数";
      },

      handleRevise(row){
        this.opens = true;
        this.reset();
        getParameters(row).then(response => {
          this.form = response.data;
          this.list= JSON.parse(this.form.configInfo);
          this.lists= JSON.parse(this.form.name);
          console.log(response.data);
          this.opens = true;
          this.title = "修改标准参数";
        });
      },
      /** 提交按钮 */
      submitFormC: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              this.form.name=JSON.stringify(this.lists);
              this.form.configInfo=JSON.stringify(this.list);
              updateParameters(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.opens = false;
                  this.getList();
                  this.getLists();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              this.form.name=JSON.stringify(this.lists);
              this.form.configInfo=JSON.stringify(this.list);
              addParameters(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.opens = false;
                  this.getList();
                  this.getLists();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateCategory(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addCategory(this.form).then(response => {
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
      addEl: function () {
        let cope = {
          name: " ",
        }
        this.lists.push(cope);
      },
      del: function(index) {
        this.lists.splice(index, 1);
      },
      addE2: function () {
        let cope = {
          value: " ",
        }
        this.list.push(cope);
      },
      del2: function(index) {
        this.list.splice(index, 1);
      },
    }
  };
</script>
