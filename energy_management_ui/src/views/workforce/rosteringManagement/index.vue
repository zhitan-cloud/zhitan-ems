<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'25%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            排班基础查询
          </div>
          <el-input
              placeholder="输入关键字进行过滤"
              prefix-icon="el-icon-search"
              v-model="filterText">
          </el-input>
          <el-tree
              class="modelnode-tree"
              :data="modelNodeData"
              node-key="id"
              highlight-current
              :expand-on-click-node="false"
              empty-text=""
              ref="modelNodeTree"
              :filter-node-method="filterNode"
              :default-expanded-keys="treeExpandData"
              @node-click="changeNode"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
            <el-tooltip class="item" effect="dark" :content="node.label" placement="top-end">
              <span>{{ node.label.length > 4 ? node.label.slice(0, 4) + '...' : node.label }}</span>
                  <span class="tree-text-node-common">{{ node.label }}</span>
              </el-tooltip>
          </span>
          </el-tree>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <scheduling_basis ref="scheduling_basis" style="padding:10px"></scheduling_basis>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

import {treeList} from "@/api/workforce/management"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import scheduling_basis from "./scheduling_basis";

export default {
  components: {Treeselect, scheduling_basis},
  watch: {
    filterText(val) {
      this.$refs.modelNodeTree.filter(val);
    }
  },
  data() {
    return {
      filterText: '',
      // 表格树数据
      modelNodeData: [],
      // 部门名称
      deptName: undefined,
      // 查询参数
      queryParams: {
        deptName: undefined,
        status: undefined
      },
      treeExpandData: [],
      activeName: '',
      modelCode: undefined,
      isCollapse: false,
    }
  },
  created() {
    this.getList();
  },
  methods: {
    filterNode(value, data) {
      if (!value) {
        return true;
      }
      return data.label.indexOf(value) !== -1;
    },

    getList() {
      treeList().then(response => {
        this.modelNodeData = response.data;
        this.loading = false;
        if (this.modelNodeData.length > 0) {
          this.treeExpandData.push(this.modelNodeData[0].id);
          this.$emit("changeNode", this.modelNodeData[0]);
          this.$refs.scheduling_basis.modelNodeChange(this.modelNodeData[0]);
        } else {
          this.$emit("changeNode", '');
        }
        this.$nextTick(() => {
          if (this.modelNodeData.length > 0) {
            this.$refs.modelNodeTree.setCurrentKey(this.modelNodeData[0].id);
          }
        })
      });
    },
    changeNode(data, node, ev) {
      this.$emit("changeNode", data);
      this.$refs.scheduling_basis.modelNodeChange(data);
    },
    handleClick(tab, event) {
      this.activeName = tab.name;
    },
    // 点击按钮，切换折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
    },
  }
};
</script>
