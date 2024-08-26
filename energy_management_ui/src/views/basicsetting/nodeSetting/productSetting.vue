<template>
  <div class="app-container">
    <el-table border v-loading="loading" :data="productList" @select="handleSelectionChange"
              :header-cell-class-name="hideCheckAll"
              ref="productSettingTable">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="产品编号" align="center" prop="productsno"/>
      <el-table-column label="产品名称" align="center" prop="productname"/>
      <el-table-column label="计量单位" align="center" prop="muid" :formatter="muidFormat"/>
    </el-table>
  </div>
</template>

<script>
  import {listProduct} from "@/api/enerInfoManage/product";

  export default {
    name: "ProductSetting",
    data() {
      return {
        productList: [],
        selectedProduct: [],
        loading: false,
        muidOptions: []
      }
    },
    methods: {
      init(currentSelected) {
        this.selectedProduct = currentSelected;
        this.getList();
        this.getDicts("sys_unit").then(response => {
          this.muidOptions = response.data;
        });
      },
      getList() {
        this.loading = true;

        listProduct({}).then(response => {
          this.productList = response.rows;
          this.loading = false;
          this.$nextTick(function () {
            this.$refs.productSettingTable.data.forEach(rowData => {
              let exist = this.selectedProduct.filter(f => f.productid === rowData.productid).length;
              if (exist > 0) {
                this.$refs.productSettingTable.toggleRowSelection(rowData, true);
              }
            });
          });
        });
      },
      // 多选框选中数据
      handleSelectionChange(selection, row) {
        let rowStatus = selection.filter(f => f.productid === row.productid).length;
        if (rowStatus > 0) {
          this.selectedProduct.push({
            "productid": row.productid,
            "productsno": row.productsno,
            "productname": row.productname
          });
        } else {
          this.selectedProduct = this.selectedProduct.filter(f => f.productid !== row.productid);
        }

        this.$emit('productConfirmSelect', this.selectedProduct);
      },
      // 计量单位字典翻译
      muidFormat(row, column) {
        return this.selectDictLabel(this.muidOptions, row.muid);
      },
    }
  }
</script>

<style scoped>

</style>
