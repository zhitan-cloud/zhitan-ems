<template>
  <!-- 导入表 -->
  <el-dialog :title="title" :visible.sync="open"  :destroy-on-close="true" :append-to-body="true" width="800px" top="5vh">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item>
        <el-button icon="el-icon-refresh" size="mini" @click="createIndex">生成指标</el-button>
      </el-form-item>
    </el-form>


    <el-row>
      <el-table v-loading="loading" :data="impIndexList" stripe max-height="350">
        <el-table-column label="指标编码" align="center" prop="code"  />
        <el-table-column label="指标名称" align="center" prop="name"  />
        <el-table-column label="指标类型" align="center" prop="indexType" :formatter="indexTypeFormat" />
        <el-table-column label="单位" align="center" prop="unitId" :formatter="unitIdFormat" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['meter:annex:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
<!--      <pagination-->
<!--        v-show="total>0"-->
<!--        :total="total"-->
<!--        :page.sync="queryParams.pageNum"-->
<!--        :limit.sync="queryParams.pageSize"-->
<!--        @pagination="getList"-->
<!--      />-->
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handclose">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listIndex,addIndex,delIndex } from "@/api/meter/index";
let meterid="";
// 通用下载方法

export default {
  data() {
    return {
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
      qjcode:"",
      // 计量器具档案  指标  表格数据
      impIndexList: [],
      // 计量器具档案  指标的 单位集合
      unitIdList: [],
      // 计量器具档案 指标的 系统指标类型
      indexTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 主键字典
      idOptions: [],
      // 关联meter_implement.id字典
      implementIdOptions: [],
      // 文件名字典
      fileNameOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        implementId:'',
        tableName: undefined
      },
    };
  },
  created() {
    this.getDicts("sys_unit").then(response => {
      this.unitIdList = response.data;
    });
    this.getDicts("sys_index_type").then(response => {
      this.indexTypeList = response.data;
    });
  },
  methods: {
    // 显示弹框
    show(impid,code) {
      this.reset();
      this.qjcode = code;
      this.queryParams.implementId = impid;
      this.meterid = impid;
      this.getList();
      this.title='编号 '+code+' 指标';
      this.open = true;
    },
    // 指标类型字典翻译
    indexTypeFormat(row, column) {
      return this.selectDictLabel(this.indexTypeList, row.indexType);
    },
    // 单位 字典翻译
    unitIdFormat(row, column) {
      return this.selectDictLabel(this.unitIdList, row.unitId);
    },
    // 表单重置
    reset() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        implementId:'',
        tableName: undefined
      };
      //清空上传组件的文件列表
      this.resetForm("queryForm");
    },
    // 查询 计量器具的指标
    getList() {
      listIndex(this.queryParams.implementId).then(response => {
        this.impIndexList = response.data;
        this.loading = false;
      });
    },
    // 生成计量器具指标
    createIndex() {
      let aa = this.meterid;
      console.log("aa==="+aa);
      this.$confirm('是否确认重新生成器具编码为"' + this.qjcode + '"的指标吗?重新生成后所有指标会还原到模板状态', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
          return addIndex(aa);
      }).then(() => {
        this.getList();
        this.msgSuccess("指标生成成功");
      }).catch(function() {});
    },
    /** 关闭按钮操作 */
    handclose()
    {
      this.open=false;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除指标编码为"' + row.code + '"的指标吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delIndex(row.indexId);
      }).then(() => {
        this.getList();
        this.msgSuccess("指标删除成功");
      }).catch(function() {});
    },
  }
};

</script>
