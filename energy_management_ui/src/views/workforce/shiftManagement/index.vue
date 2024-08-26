<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-aside class="left-content" :width="isCollapse?'0px':'25%'">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="height:32px">
            排班基础设置
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
              </el-tooltip>
            <span>{{ node.label }}</span>
          </span>
          </el-tree>
        </el-card>
      </el-aside>
      <el-container>
        <div style="cursor:pointer;" @click="toggleCollapse">
          <img src="@/assets/image/configureChart.png" style="width: 100%;height: 100%">
        </div>
        <el-main style="padding:0">
          <el-tabs @tab-click="handleClick">
            <el-tab-pane>
              <span slot="label"><i class="el-icon-pie-chart"></i> 班次设置</span>
              <RosteringShift ref="rostering_shift"></RosteringShift>
            </el-tab-pane>
            <el-tab-pane>
              <span slot="label"><i class="el-icon-pie-chart"></i> 值次设置</span>
              <RosteringDuty ref="rostering_duty"></RosteringDuty>
            </el-tab-pane>
            <el-tab-pane>
              <span slot="label"><i class="el-icon-pie-chart"></i> 轮值方案设置</span>
              <scheme ref="scheme"></scheme>
            </el-tab-pane>
          </el-tabs>
        </el-main>
      </el-container>
    </el-container>


  </div>
</template>

<script>

import {treeList} from "@/api/workforce/management"
import RosteringShift from "./rostering_shift";
import RosteringDuty from "../dutyManagement/rostering_duty";
import scheme from "../management/scheme";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  components: {RosteringShift, RosteringDuty, Treeselect, scheme},
  created() {
  },
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
          this.$refs.rostering_shift.modelNodeChange(this.modelNodeData[0]);
          this.$refs.rostering_duty.modelNodeChange(this.modelNodeData[0]);
          this.$refs.scheme.modelNodeChange(this.modelNodeData[0]);
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
      this.$refs.rostering_shift.modelNodeChange(data);
      this.$refs.rostering_duty.modelNodeChange(data);
      this.$refs.scheme.modelNodeChange(data);
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
