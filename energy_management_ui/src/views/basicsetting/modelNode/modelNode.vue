<template>
  <div class="app-container" style="padding:0">
    <el-input
      placeholder="输入关键字进行过滤"
      prefix-icon="el-icon-search"
      v-model="filterText"
    >
    </el-input>

    <el-link
      icon="el-icon-plus"
      style="margin-top: 8px;width:100%"
      @click="addNode"
      v-if="nodeShowOpt && this.modelNodeData.length === 0"
      >添加根节点
    </el-link>
    <el-tree
      class="modelnode-tree"
      :data="modelNodeData"
      node-key="id"
      highlight-current
      :filter-node-method="filterNode"
      :default-expanded-keys="treeExpandData"
      :expand-on-click-node="false"
      empty-text=""
      ref="modelNodeTree"
      @node-drop="handleDrop"
      :allow-drop="allowDrop"
      @node-click="changeNode"
      :draggable="nodeShowOpt"
      accordion
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span v-if="nodeShowOpt" id="a">
          <el-tooltip
            v-if="node.label.length > 8"
            class="item"
            effect="dark"
            :content="node.label"
            placement="top-end"
          >
            <span>{{ node.label.slice(0, 8) + "..." }}</span>
          </el-tooltip>
          <span v-else id="b">{{ node.label }}</span>
        </span>
        <span v-else id="c">
          <el-tooltip
            v-if="node.label.length > 11"
            class="item"
            effect="dark"
            :content="node.label"
            placement="top-end"
          >
            <span>
              {{ node.label.slice(0, 11) + "..." }}
            </span>
          </el-tooltip>
          <span v-else id="d">{{ node.label }}</span>
        </span>
        <span class="node-opt" v-if="nodeShowOpt">
          <el-link
            title="新增下级节点"
            icon="el-icon-plus"
            @click="() => addNode(node, data)"
          >
          </el-link>
          <el-link
            title="修改节点"
            icon="el-icon-edit"
            @click="() => editNode(node, data)"
          >
          </el-link>
          <el-link
            title="删除节点"
            icon="el-icon-delete"
            @click="() => delNode(node, data)"
          >
          </el-link>
        </span>
      </span>
    </el-tree>
    <!-- 添加或修改模型节点对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="父级节点" v-if="addOpt">
          <span>{{ currentNode ? currentNode.name : "" }}</span>
        </el-form-item>
        <el-form-item label="指标节点编码" prop="code">
          <el-input
            v-if="addOpt"
            v-model="form.code"
            placeholder="请输入编码"
          />
          <span v-if="!addOpt">{{ form.code }}</span>
        </el-form-item>
        <el-form-item label="指标节点名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="节点类型" prop="nodeType">
          <el-select
            v-model="form.nodeCategory"
            placeholder="请选节点类型"
            prop="nodeCategory"
            clearable
          >
            <el-option
              v-for="dict in nodeCategoryOptions"
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
  </div>
</template>

<script>
import {
  addModelNode,
  delModelNode,
  getModelNode,
  hasEnergyIndex,
  modelNodeTree,
  updateModelNode,
  updateModelNodeOrder
} from "../../../api/basicsetting/modelNode";

export default {
  name: "ModelNode",
  props: ["modelCode", "showOpt", "auth", "isSelectFirstLeaf"],
  watch: {
    filterText(val) {
      this.$refs.modelNodeTree.filter(val);
    }
  },
  data() {
    return {
      filterText: "",
      // 遮罩层
      loading: true,
      // 模型节点表格数据
      modelNodeData: [],
      nodeCategoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      currentNode: undefined,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "编码不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "长度在 2 到 20 个字符",
            trigger: "blur"
          },
          {
            pattern: /^[a-zA-Z][A-Za-z0-9_-]+$/,
            message: "必须为数字、字母、- 或_ ，且首字符只能为字母"
          }
        ],
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" },
          { min: 2, max: 15, message: "长度在 2 到 15 个字符", trigger: "blur" }
        ]
      },
      addOpt: true,
      currentSelectNode: undefined,
      treeExpandData: [],
      currentModelCode: "",
      nodeCategoryDefaultVal: "",
      nodeShowOpt: true,
      withAuth: false,
      isFirstLeafNode: false // 选中第一个叶子结点
    };
  },
  created() {
    console.log("this.$props", this.$props);
    let modelCode = this.$props.modelCode;
    if (this.$props.auth !== undefined) {
      this.withAuth = this.$props.auth;
    }

    if (modelCode) {
      this.getList(modelCode);
    }

    this.nodeShowOpt =
      this.$props.showOpt !== undefined ? this.$props.showOpt : true;
    this.isFirstLeafNode =
      this.$props.isSelectFirstLeaf !== undefined
        ? this.$props.isSelectFirstLeaf
        : false;
  },
  methods: {
    filterNode(value, data) {
      if (!value) {
        return true;
      }
      return data.label.indexOf(value) !== -1;
    },
    /** 查询模型节点列表 */
    getList(modelCode) {
      console.log("查询模型节点列表", modelCode, this.modelCode);
      this.currentModelCode = modelCode;
      this.loading = true;

      this.getDicts("sys_node_category").then(response => {
        this.nodeCategoryOptions = response.data;
        this.nodeCategoryDefaultVal = this.nodeCategoryOptions.find(
          f => f.isDefault === "Y"
        );
      });

      modelNodeTree({ modelCode: modelCode }, this.withAuth).then(response => {
        this.modelNodeData = response.data;
        this.loading = false;
        let chooseNode = null;
        if (this.modelNodeData.length > 0) {
          // 选中第一个叶子结点
          if (
            this.modelNodeData[0].children &&
            this.modelNodeData[0].children.length !== 0 &&
            this.isFirstLeafNode
          ) {
            if (
              this.modelNodeData[0].children[0].children &&
              this.modelNodeData[0].children[0].children.length !== 0
            ) {
              chooseNode = this.modelNodeData[0].children[0].children[0];
            } else {
              chooseNode = this.modelNodeData[0].children[0];
            }
          } else {
            chooseNode = this.modelNodeData[0];
          }

          this.treeExpandData.push(chooseNode.id);
          this.$emit("changeNode", chooseNode);
          this.$nextTick(() => {
            this.$refs.modelNodeTree.setCurrentKey(chooseNode.id);
          });
        } else {
          this.$emit("changeNode", "");
        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.currentNode = undefined;
      this.form = {
        nodeId: undefined,
        code: undefined,
        name: undefined,
        parentId: undefined,
        address: undefined,
        modelCode: undefined,
        nodeCategory: undefined
      };
      this.resetForm("form");
    },
    addNode(node, data) {
      this.reset();
      this.addOpt = true;
      if (data) {
        this.currentSelectNode = data;
        getModelNode(data.id).then(response => {
          this.open = true;
          this.title = "添加模型节点";
          this.currentNode = response.data;
        });
      } else {
        this.open = true;
      }
    },
    /** 修改按钮操作 */
    editNode(node, data) {
      this.currentSelectNode = data;
      this.reset();
      this.addOpt = false;
      getModelNode(data.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改模型节点";
      });
    },
    delNode(node, data) {
      if (data.children && data.children.length > 0) {
        this.msgWarning("包含子节点，不能进行删除！");
        return;
      }

      hasEnergyIndex(data.id).then(response => {
        if (response.data) {
          this.msgWarning("当前节点下存在指标，不能进行删除！");
        } else {
          this.$confirm('是否确认删除名为"' + data.label + '"的节点?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(function() {
              return delModelNode(data.id);
            })
            .then(() => {
              let parent = node.parent;
              let children = parent.data.children || parent.data;
              let index = children.findIndex(d => d.id === data.id);
              children.splice(index, 1);

              this.$nextTick(() => {
                if (parent.data && parent.data.id) {
                  this.$emit("changeNode", parent.data);
                } else {
                  this.$emit("changeNode", null);
                }
              });

              this.msgSuccess("删除成功");
            })
            .catch(function() {});
        }
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.nodeId !== undefined) {
            updateModelNode(this.form).then(response => {
              if (response.code === 200) {
                this.currentSelectNode.label = this.form.name;
                this.msgSuccess("修改成功");
                this.open = false;
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            this.form.parentId = this.currentNode
              ? this.currentNode.nodeId
              : "";
            this.form.address =
              (this.currentNode ? this.currentNode.address : "") +
              this.form.code +
              ",";
            this.form.modelCode = this.currentModelCode;
            addModelNode(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                const addedNode = response.data;
                const newChild = {
                  id: addedNode.nodeId,
                  label: addedNode.name,
                  children: []
                };
                if (this.currentSelectNode) {
                  if (!this.currentSelectNode.children) {
                    this.$set(this.currentSelectNode, "children", []);
                  }
                  this.currentSelectNode.children.push(newChild);
                } else {
                  this.modelNodeData.push(newChild);
                }

                this.treeExpandData.push(addedNode.nodeId);
                this.$nextTick(() => {
                  this.$refs.modelNodeTree.setCurrentNode(newChild);
                  this.$emit("changeNode", newChild);
                });
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    allowDrop(draggingNode, dropNode, ev) {
      return dropNode.level !== 1;
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      let updateData = {
        nodeId: draggingNode.data.id,
        parentId: dropNode.parent.data.id,
        children: []
      };
      for (let item of dropNode.parent.childNodes) {
        updateData.children.push(item.data.id);
      }
      updateModelNodeOrder(updateData).then();
    },
    changeNode(data, node, ev) {
      this.$emit("changeNode", data);
    }
  }
};
</script>

<style lang="scss" scoped>
::v-deep {
  .el-tree-node__content {
    margin-top: 10px;
    height: 35px;
  }
  // .is-current {
  //   .el-tree-node__content {
  //     border: 1px solid red;
  //   }
  // }
  .el-tree--highlight-current
    .el-tree-node.is-current
    > .el-tree-node__content {
    background-color: #f7f7f7;
    border: 1px solid #38bcbf;
  }
}
</style>
