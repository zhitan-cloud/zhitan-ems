<template>
  <div>
    <basic-container>
      <span>{{ selectedName }} 权限配置，选择模型：</span>
      <el-select v-model="modelData" placeholder="请选择模型"
                 style="width:220px"
                 size="small"
                 @change="changeModel">
        <el-option
            v-for="model in modelInfoOptions"
            :key="model.modelCode"
            :label="model.modelName"
            :value="model.modelCode"
        />
      </el-select>
      <el-button type="primary" class="fr" @click="saveSetting">保存权限</el-button>
    </basic-container>
    <basic-container style="margin-top: 16px" :bodyStyle="bodyStyle">
      <el-input
          placeholder="输入关键字进行过滤"
          prefix-icon="el-icon-search"
          v-model="filterText">
      </el-input>
      <el-tree
          class="modelnode-tree"
          node-key="id"
          highlight-current
          empty-text="没有查询到数据"
          show-checkbox
          :data="modelNodeData"
          :filter-node-method="filterNode"
          :default-expanded-keys="treeExpandData"
          :expand-on-click-node="false"
          :check-strictly="strictly"
          @check-change="checkChange"
          ref="dataAuthTree">
         <span class="custom-tree-node" slot-scope="{ node, data }">
              <el-tooltip class="item" effect="dark" :content="node.label" placement="top-end">
                 <span>{{ node.label.length > 4 ? node.label.slice(0, 4) + '...' : node.label }}</span>
                  <span class="tree-text-node-common">{{ node.label }}</span>
              </el-tooltip>
        <span class="node-opt" v-if="data.children && data.children.length>0">
          <el-button
              type="text"
              size="mini"
              @click="() => checkAllChildren(data)">
            全选
          </el-button>
          <el-button
              type="text"
              size="mini"
              @click="() => uncheckAllChildren(data)">
            取消全选
          </el-button>
        </span>
      </span>
      </el-tree>
    </basic-container>
  </div>
</template>

<script>
import {listModel} from "../../../api/basicsetting/model";
import {modelNodeTree} from "../../../api/basicsetting/modelNode";
import {selectSettingAuth, setDataAuth} from "../../../api/dataAuth/authSetting";
import mixins from "@/layout/mixin/getHeight";

export default {
  name: "dataAuthModelNode",
  mixins: [mixins],
  watch: {
    filterText(val) {
      this.$refs.modelNodeTree.filter(val);
    }
  },
  created() {
    listModel({isShow: 1}).then(response => {
      this.modelInfoOptions = response.data;
      if (this.modelInfoOptions.length > 0) {
        this.modelData = this.modelInfoOptions[0].modelCode;
        this.changeModel(this.modelData);
      }
    });
  },
  data() {
    return {
      modelData: null,
      modelInfoOptions: [],
      modelNodeData: [],
      treeExpandData: [],
      filterText: null,
      userOrRoleId: null,
      authType: 'user',
      settingAuthIds: [],
      selectedName: '',
      currentModelCode: '',
      strictly: true,
      currentCheckedIds: []
    }
  },
  methods: {
    setCharts() {
      this.bodyStyle.height = window.innerHeight - 205 + 'px';
    },
    changeModel(modelCode) {
      this.loading = true;
      this.currentModelCode = modelCode;
      modelNodeTree({modelCode: modelCode}).then(response => {
        this.modelNodeData = response.data;
        this.loading = false;
        if (this.modelNodeData.length > 0) {
          this.treeExpandData.push(this.modelNodeData[0].id);
        }
        this.$nextTick(() => {
          if (this.modelNodeData.length > 0) {
            this.$refs.dataAuthTree.setCurrentKey(this.modelNodeData[0].id);
          }
        })
      });

      this.getAuthSetting();
    },
    changeSelected(id, name, authType) {
      this.userOrRoleId = id;
      this.selectedName = name;
      this.authType = authType;
      this.getAuthSetting();
    },
    filterNode(value, data) {
      if (!value) {
        return true;
      }
      return data.label.indexOf(value) !== -1;
    },
    getAuthSetting() {
      if (this.userOrRoleId) {
        selectSettingAuth(this.userOrRoleId, this.currentModelCode, this.authType)
            .then(response => {
              if (response.code === 200) {
                this.currentCheckedIds = response.data;
                this.$refs.dataAuthTree.setCheckedKeys(this.currentCheckedIds);
              }
            });
      } else {
        this.currentCheckedIds = []
        this.$refs.dataAuthTree.setCheckedKeys([]);
      }
    },
    saveSetting() {
      if (!this.userOrRoleId) {
        debugger
        this.$message.warning("请选择" + (this.authType === 'user' ? '用户' : '角色'))
        return;
      }

      let checkedNodes = this.$refs.dataAuthTree.getCheckedNodes(false, true);
      let ids = checkedNodes.map(node => node.id);
      setDataAuth(this.userOrRoleId, this.currentModelCode, this.authType, ids)
          .then(response => {
            if (response.code === 200) {
              this.$message.success("设置成功！");
            } else {
              this.$message.error("设置失败！")
            }
          });
    },
    getAllChildren(node) {
      const childNodes = node.root ? node.root.children : node.children;
      if (childNodes && childNodes.length > 0) {
        childNodes.forEach(child => {
          if (this.currentCheckedIds.indexOf(child.id) < 0) {
            this.currentCheckedIds.push(child.id);
          }
          if (child.children && child.children.length > 0) {
            this.getAllChildren(child);
          }
        });
      }
    },
    removeChildren(node) {
      const childNodes = node.root ? node.root.children : node.children;
      if (childNodes && childNodes.length > 0) {
        childNodes.forEach(child => {
          this.currentCheckedIds.splice(this.currentCheckedIds.indexOf(child.id), 1);
          if (child.children && child.children.length > 0) {
            this.removeChildren(child);
          }
        });
      }
    },
    checkAllChildren(node) {
      if (this.currentCheckedIds.indexOf(node.id) < 0) {
        this.currentCheckedIds.push(node.id);
      }
      this.getAllChildren(node);
      this.$refs.dataAuthTree.setCheckedKeys(this.currentCheckedIds);
    },
    uncheckAllChildren(node) {
      this.currentCheckedIds.splice(this.currentCheckedIds.indexOf(node.id), 1);
      this.removeChildren(node);
      this.$refs.dataAuthTree.setCheckedKeys(this.currentCheckedIds);
    },
    checkChange(node, currentState) {
      if (currentState) {
        if (this.currentCheckedIds.indexOf(node.id) < 0) {
          this.currentCheckedIds.push(node.id);
        }
      } else {
        this.currentCheckedIds.splice(this.currentCheckedIds.indexOf(node.id), 1);
      }
    }
  }
}
</script>
