<!--<template>-->
<!--  -->
<!--</template>-->
<template>
  <div class="app-container" style="padding: 0">
    <el-container class="split-container">
      <el-container>
        <el-main style="padding:0">
<!--          <el-tabs v-model="activeName" @tab-click="handleClick">-->
<!--            <el-tab-pane label="检测图" name="first">用户管理</el-tab-pane>-->
<!--            <el-tab-pane label="报表" name="second">配置管理</el-tab-pane>-->
<!--            <el-tab-pane label="趋势图" name="third">角色管理</el-tab-pane>-->
<!--            <el-tab-pane label="报警记录" name="fourth">定时任务补偿</el-tab-pane>-->
<!--          </el-tabs>-->
          <svg-icon v-bind:iconClass="imgShow.realName" className='icon'></svg-icon>
        </el-main>
<!--        <el-main style="padding:0">-->
<!--          <realTimeSetting ref="realTimeSetting"></realTimeSetting>-->
<!--        </el-main>-->
<!--        <svg-icon v-bind:iconClass="imgShow.realName" className='icon'></svg-icon>-->
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
  .icon {
    width: 970px;
    height: 550px;
  }
</style>
<script>
    import {
        listEquipmentfile,
    } from "@/api/basicSetup/equipmentfile";
    export default {
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        data() {
            var choiceIndex;
            var Indexid;
            var realName = "2020-03-12-275a44966506b783f033058f9e0e3472";
            return {
                //右边菜单栏
                activeName: '实时监测',
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
                //
                imgShow:[
                    realName=""
                ],
                // 组态图表格数据
                equipmentfileList: [],
                //测点表格数据
                equipmentfilecdList: [
                    choiceIndex = ""
                ],
                // 弹出层标题
                title: "",
                // 是否显示弹出层
                open: false,
                footopen: false,
                addImgOpen: false,
                uploadOpen: false,
                indexLoading: false,
                //组态图选择指标
                statisticIndexDialog: false,
                //组态图测点弹出层
                imgcdopen: false,
                // ID字典
                fileidOptions: [],
                // 文件名称字典
                filenameOptions: [],
                // 文件编号字典
                filenoOptions: [],
                // 操作人字典
                opomanOptions: [],
                // 操作时间字典
                opotimeOptions: [],
                // 文件路径字典
                filepathOptions: [],
                // 备注字典
                noteOptions: [],
                settingStatisticIndexList: [],
                // 表单参数
                form: {},
                // 表单校验
                rules: {
                    filename: [
                        {required: true, message: "文件名称不能为空", trigger: "blur"}
                    ], fileno: [
                        {required: true, message: "文件编号不能为空", trigger: "blur"}
                    ],
                },
                filterText: '',
                svgData:[{
                    id:1,
                    label:"生产企业",
                    children:[{
                        id:2,
                        label:"水系统",
                    },{
                        id:3,
                        label: "电系统",
                    }],
                }],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
            };
        },
        created() {
            this.getList();
            this.imgShow.realName = "dian";
        },
        methods: {
            //通过
            handleClick(tab, event) {
                console.log(tab, event);
            },
            //切换组态图查看
            updateImg(row){
                var pos = row.filepath.lastIndexOf('/');
                var str = row.filepath.substr(pos+1);
                this.imgShow.realName = str.substring(0,str.length-4);
                console.log(this.imgShow.realName)
            },
            //通过关键字进行过滤组态图
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            /** 查询组态图列表 */
            getList() {
                this.loading = true;
                listEquipmentfile(this.queryParams).then(response => {
                    this.equipmentfileList = response.rows;
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
                    fileid: undefined,
                    filename: undefined,
                    fileno: undefined,
                    opoman: undefined,
                    opotime: undefined,
                    filepath: undefined,
                    note: undefined,
                    img: undefined
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
        }
    };
</script>
