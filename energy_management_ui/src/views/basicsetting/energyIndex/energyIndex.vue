<template>
  <div class="app-container" style="padding:0">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="0"
             class="query-form">
      <el-row :gutter="24">
        <el-col :span="5">
          <el-form-item>
            <el-select v-model="queryParams.indexCategory" placeholder="请选择指标分类" clearable
                       size="small"
                       @change="handleQuery">
              <el-option
                  v-for="dict in indexCategoryOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item>
            <el-input
                v-model="queryParams.name"
                placeholder="请输入指标名称/编码"
                clearable
                size="small"
                @input="handleQuery"
                @clear="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item class="fr">
            <el-button
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="handleAdd"
                v-hasPermi="['energyindex:energyindex:add']"
            >新增
            </el-button>
            <el-button
                type="success"
                icon="el-icon-edit"
                size="mini"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['energyindex:energyindex:edit']"
            >修改
            </el-button>
            <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['energyindex:energyindex:remove']"
            >删除
            </el-button>
            <el-button
                type="info"
                icon="el-icon-upload2"
                size="mini"
                @click="handleImport"
            >导入
            </el-button>
            <el-button type="text"
                       icon="el-icon-setting"
                       @click="collectIndexSetting"
                       v-if="!statisticIndex">
              配置采集点
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-table v-loading="loading" :data="energyindexList" border
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="指标编码" align="center" prop="code"/>
      <el-table-column label="指标名称" align="center" prop="name"/>
      <el-table-column label="系统指标分类" align="center" prop="indexCategory"
                       :formatter="indexCategoryFormat"/>
      <el-table-column label="单位" align="center" prop="unitId" :formatter="unitIdFormat"/>
      <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['energyindex:energyindex:edit']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="dataStorage(scope.row)"
              v-hasPermi="['energyindex:energyindex:edit']"
              v-if="statisticIndex"
          >存储
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['energyindex:energyindex:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="paginationQuery"
    />

    <!-- 添加或修改指标信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="指标名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入指标名称"/>
        </el-form-item>
        <el-form-item label="指标编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入指标编码"/>
        </el-form-item>
        <el-form-item label="指标分类">
          <el-select v-model="form.indexCategory" placeholder="请选择指标分类" prop="indexCategory">
            <el-option
                v-for="dict in indexCategoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单位">
          <el-select v-model="form.unitId" placeholder="请选择单位">
            <el-option
                v-for="dict in unitIdOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="顺序号">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="设备">
          <el-select v-model="form.equipment" placeholder="请选择设备">
            <el-option
                v-for="dict in fcilityArchives"
                :key="dict.id"
                :label="dict.facilityName"
                :value="dict.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="能源品种">
          <el-select v-model="form.energyId" placeholder="请选择能源品种">
            <el-option
                v-for="dict in energyIdList"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-drawer title="配置指标存储" :visible.sync="indexStorageShow" direction="rtl"
               @open="showIndexStorageDialog"
               size="45%"
               :wrapperClosable="false">
      <IndexStorage ref="indexStorage"
                    :nodeId="currentNode===undefined?'':currentNode.id"></IndexStorage>
    </el-drawer>
    <!--选择采集指标-->
    <el-drawer title="选择采集指标" :visible.sync="collectIndexDialog" direction="rtl"
               @open="showCollectIndexDialog"
               size="45%">
      <CollectIndexSetting ref="collectIndexSetting"
                           @collectIndexConfirmSelect="collectIndexConfirmSelect"></CollectIndexSetting>
    </el-drawer>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" :destroy-on-close="true"
               width="400px">
      <el-upload
          ref="upload"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url + '?updateSupport=' + upload.updateSupport"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport"/>
          是否更新已经存在的用户数据
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addEnergyindex,
  delEnergyindex,
  exportEnergyindex,
  getEnergyindex,
  importTemplate,
  listEnergyindex,
  updateEnergyindex
} from "../../../api/basicsetting/energyindex";
import {getToken} from "@/utils/auth";
import {setNodeToIndex} from '@/api/basicsetting/modelNode'
import IndexStorage from "./indexStorage";
import CollectIndexSetting from "../nodeSetting/collectIndexSetting";
import {getPointFacility} from "@/api/comprehensiveStatistics/comprehensive";

export default {
  name: 'EnergyIndex',
  components: {CollectIndexSetting, IndexStorage},
  props: ["indexType"],
  data() {
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
      // 指标信息表格数据
      energyindexList: [],
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
      energyIdList: [],//能源品种
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        indexCategory: undefined,
        nodeId: undefined,
        indexType: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "指标名称不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ], code: [
          {required: true, message: "指标编码不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '长度在 2 到 30 个字符', trigger: 'blur'},
          {pattern: /^[a-zA-Z][A-Za-z0-9#_-]+$/, message: '必须为数字、字母、#、- 或_ ，且首字符只能为字母'}
        ], indexCategory: [
          {required: true, message: "请选择指标分类", trigger: "blur"}
        ]
      },
      currentNode: undefined,
      indexCategoryDefaultVal: undefined,
      unitDefaultVal: undefined,
      indexStorageShow: false,
      statisticIndex: true,
      collectIndexDialog: false,
      fcilityArchives: [],
      upload: {
        open: false,
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/basicsetting/energyindex/importData"
      },
    };
  },
  created() {
    this.getDicts("sys_index_category").then(response => {
      this.indexCategoryOptions = response.data;
      this.indexCategoryDefaultVal = this.indexCategoryOptions.find(f => f.isDefault === 'Y');
    });
    this.getDicts("sys_unit").then(response => {
      this.unitIdOptions = response.data;
      this.unitDefaultVal = this.unitIdOptions.find(f => f.isDefault === 'Y');
    });
    this.statisticIndex = this.$props.indexType === 'STATISTIC';
    this.getDicts("energy_type").then(response => {
      this.energyIdList = response.data;
    });
  },
  methods: {
    paginationQuery() {
      this.getList(this.currentNode);
    },
    /** 查询指标信息列表 */
    getList(modelNode) {
      this.currentNode = modelNode;
      if (modelNode) {
        this.loading = true;
        this.queryParams.nodeId = modelNode.id;
        this.queryParams.indexType = this.$props.indexType;
        listEnergyindex(this.queryParams).then(response => {
          this.energyindexList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      } else {
        this.energyindexList = [];
      }
    },
    // 系统指标分类，1-能源类，2-产品类，3-能效类，4-经营类，5-其他字典翻译
    indexCategoryFormat(row, column) {
      return this.selectDictLabel(this.indexCategoryOptions, row.indexCategory);
    },
    // 单位字典翻译
    unitIdFormat(row, column) {
      return this.selectDictLabel(this.unitIdOptions, row.unitId);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        indexId: undefined,
        name: undefined,
        code: undefined,
        indexType: undefined,
        indexCategory: undefined,
        remark: undefined,
        unitId: undefined,
        orderNum: 0
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList(this.currentNode);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.indexId);
      this.names = selection.map(item => item.name);
      this.single = selection.length !== 1;
      this.multiple = !selection.length
    },
    getFacilityArchivesList() {
      getPointFacility().then(response => {
        this.fcilityArchives = response.data;
        console.log(this.fcilityArchives);
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getFacilityArchivesList();
      if (this.indexCategoryDefaultVal) {
        this.form.indexCategory = this.indexCategoryDefaultVal.dictValue;
      }

      if (this.unitDefaultVal) {
        this.form.unitId = this.unitDefaultVal.dictValue;
      }
      this.open = true;
      this.title = "添加指标信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getFacilityArchivesList();
      const indexId = row.indexId || this.ids;
      getEnergyindex(indexId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改" + response.data.name + "指标信息";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.indexId !== undefined) {
            updateEnergyindex(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList(this.currentNode);
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            this.form.indexType = this.$props.indexType;
            addEnergyindex(this.currentNode.id, this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList(this.currentNode);
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
      const indexIds = row.indexId || this.ids;
      const indexNames = row.name || this.names;
      this.$confirm('是否确认删除指标名为"' + indexNames + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return delEnergyindex(this.currentNode.id, indexIds);
      }).then(() => {
        this.getList(this.currentNode);
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有指标信息数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEnergyindex(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function () {
      });
    },
    dataStorage(row) {
      this.indexStorageShow = true;
      this.$nextTick(() => {
        this.$refs.indexStorage.getIndexStorage(row.indexId);
      })
    },
    showIndexStorageDialog() {
    },
    collectIndexSetting() {
      this.collectIndexDialog = true;
    },
    showCollectIndexDialog() {
      this.$nextTick(() => {
        this.$refs.collectIndexSetting.init(this.currentNode);
      })
    },
    collectIndexConfirmSelect(selectedIndex) {
      let indexIds = selectedIndex.map(item => item.indexId);
      setNodeToIndex(this.currentNode.id, indexIds, "COLLECT").then((response) => {
        if (response.code !== 200) {
          this.$message.error(response.msg);
        }
      }).then(() => {
        this.getList(this.currentNode);
      });
    },
    handleImport() {
      this.upload.title = "指标导入";
      this.upload.open = true;
    },// 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", {dangerouslyUseHTMLString: true});
      this.getList(this.currentNode);
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then(response => {
        this.download(response.msg);
      });
    },
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
