<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="产品类型" prop="repProductType">
          <el-select v-model="queryParams.repProductType" placeholder="请选择产品类型" clearable size="small">
            <el-option
              v-for="dict in repProductTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="年度" prop="selectYear">
          <el-select v-model="queryParams.selectYear" placeholder="请选择年度" clearable size="small">
            <el-option
              v-for="dict in selectYearOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="gxdhcountList" >
      <el-table-column label="能源品种" align="center" prop="varietyType" min-width="100px"/>
      <el-table-column label="1月" align="center">
        <el-table-column label="电" align="center" prop="a1"/>
        <el-table-column label="煤汽" align="center" prop="a2"/>
        <el-table-column label="蒸汽" align="center" prop="a3"/>
      </el-table-column>
      <el-table-column label="2月" align="center">
        <el-table-column label="电" align="center" prop="a4"/>
        <el-table-column label="煤汽" align="center" prop="a5"/>
        <el-table-column label="蒸汽" align="center" prop="a6"/>
      </el-table-column>
      <el-table-column label="3月" align="center">
        <el-table-column label="电" align="center" prop="a7"/>
        <el-table-column label="煤汽" align="center" prop="a8"/>
        <el-table-column label="蒸汽" align="center" prop="a9"/>
      </el-table-column>
      <el-table-column label="4月" align="center">
        <el-table-column label="电" align="center" prop="a10"/>
        <el-table-column label="煤汽" align="center" prop="a11"/>
        <el-table-column label="蒸汽" align="center" prop="a12"/>
      </el-table-column>
      <el-table-column label="5月" align="center">
        <el-table-column label="电" align="center" prop="a13"/>
        <el-table-column label="煤汽" align="center" prop="a14"/>
        <el-table-column label="蒸汽" align="center" prop="a15"/>
      </el-table-column>
      <el-table-column label="6月" align="center">
        <el-table-column label="电" align="center" prop="a16"/>
        <el-table-column label="煤汽" align="center" prop="a17"/>
        <el-table-column label="蒸汽" align="center" prop="a18"/>
      </el-table-column>
      <el-table-column label="7月" align="center">
        <el-table-column label="电" align="center" prop="a19"/>
        <el-table-column label="煤汽" align="center" prop="a20"/>
        <el-table-column label="蒸汽" align="center" prop="a21"/>
      </el-table-column>
      <el-table-column label="8月" align="center">
        <el-table-column label="电" align="center" prop="a22"/>
        <el-table-column label="煤汽" align="center" prop="a23"/>
        <el-table-column label="蒸汽" align="center" prop="a24"/>
      </el-table-column>
      <el-table-column label="9月" align="center">
        <el-table-column label="电" align="center" prop="a25"/>
        <el-table-column label="煤汽" align="center" prop="a26"/>
        <el-table-column label="蒸汽" align="center" prop="a27"/>
      </el-table-column>
      <el-table-column label="10月" align="center">
        <el-table-column label="电" align="center" prop="a28"/>
        <el-table-column label="煤汽" align="center" prop="a29"/>
        <el-table-column label="蒸汽" align="center" prop="a30"/>
      </el-table-column>
      <el-table-column label="11月" align="center">
        <el-table-column label="电" align="center" prop="a31"/>
        <el-table-column label="煤汽" align="center" prop="a32"/>
        <el-table-column label="蒸汽" align="center" prop="a33"/>
      </el-table-column>
      <el-table-column label="12月" align="center">
        <el-table-column label="电" align="center" prop="a34"/>
        <el-table-column label="煤汽" align="center" prop="a35"/>
        <el-table-column label="蒸汽" align="center" prop="a36"/>
      </el-table-column>
    </el-table>


  </div>
</template>

<script>
import { listGxdhcountIndex } from "@/api/statisticalData/gxdhcount";

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
      // 工序单耗统计功能表格数据
      gxdhcountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 配置主键字典
      idOptions: [],
      // 产品类型字典
      repProductTypeOptions: [],
      // 下拉年度选择
      selectYearOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        repProductType: "JT",
        selectYear: "2020",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
            }
    };
  },
  created() {
    this.getList();
    this.getDicts("rep_product_type").then(response => {
      this.repProductTypeOptions = response.data;
    });
    this.getDicts("selectyear").then(response => {
      this.selectYearOptions = response.data;
    });
  },
  methods: {
    /** 查询工序单耗统计功能列表 */
    getList() {
      this.loading = true;
      listGxdhcountIndex(this.queryParams).then(response => {
        this.gxdhcountList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        repProductType: undefined,
        electricIndex: undefined,
        gasIndex: undefined,
        steamIndex: undefined,
        varietyType: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
  }
};
</script>
