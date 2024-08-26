<template>
  <!-- 导入表 -->
  <el-dialog :title="title" :visible.sync="open"  :destroy-on-close="true" :append-to-body="true" width="800px" top="5vh">
    <!--
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item>
        <el-button icon="el-icon-refresh" size="mini" @click="createIndex">生成指标</el-button>
      </el-form-item>
    </el-form>-->


    <el-row>
      <el-table v-loading="loading" :data="impIndexList" @selection-change="handleSelectionChange" stripe max-height="350">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="指标编码" align="center" prop="code"  />
        <el-table-column label="指标名称" align="center" prop="name"  />
      </el-table>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handsave">保 存</el-button>
      <el-button @click="handclose">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listIndex,addIndex } from "@/api/energyExamine/addIndex";

export default {
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
      qjcode:"",
      //  指标  表格数据
      impIndexList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 主键字典
      idOptions: [],
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
  },
  methods: {
    // 显示弹框
    show(modeNode) {
      this.modeNode=modeNode;
      this.reset();
      this.getList();
      this.title="添加"+this.modeNode.label+"考核指标";
      this.open = true;
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
    // 查询  指标
    getList() {
      listIndex("STATISTIC",this.modeNode.id).then(response => {
        this.impIndexList = response.data;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.indexId)
    },
    /** 保存按钮操作 */
    handsave()
    {
      if(this.ids==null || this.ids.length<=0){
        this.msgSuccess("请选择指标!");
        return;
      }
      addIndex(this.ids,this.modeNode.id).then(response => {
        if(response!=null && response.code=="200")
        {
          this.msgSuccess(response.msg);
          this.open=false;
        }else{
          this.msgSuccess(response.msg);
        }
      });
    },
    /** 关闭按钮操作 */
    handclose()
    {
      this.open=false;
    },

  }
};

</script>
