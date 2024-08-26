<p align="center">
	<img alt="logo" src="https://zhitancloud.com/static/img/zt_logo02.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">zhitan-EMS</h1>
<h4 align="center">智碳能源管理平台</h4>


#### 介绍
通过物联网技术，采集企业水、电、气、热等能耗数据，帮企业建立能源管理体系，找到跑冒滴漏，从而为企业节能提供依据。
进一步为企业实现碳跟踪、碳盘查、碳交易、谈汇报的全生命过程。 为中国碳达峰-碳中和做出贡献。

框架：基于SpringBoot的若依框架 易读易懂、界面简洁美观。
关系数据库：influxdb
时序数据库：mysql 或者 postgredb
中间件：redis等

#### 业务架构
![输入图片说明](readme/业务架构.png)
#### 技术架构【未来规划】
![输入图片说明](readme/技术架构.png)
#### 亮点功能
![输入图片说明](readme/亮点功能.png)
#### 功能规划
<table border="0" cellpadding="0" cellspacing="0" width="447" style="border-collapse:
 collapse;table-layout:fixed;width:335pt">
 <colgroup><col class="xl106" width="116" style="mso-width-source:userset;mso-width-alt:4454;
 width:87pt">
 <col class="xl106" width="171" style="mso-width-source:userset;mso-width-alt:6553;
 width:128pt">
 <col class="xl106" width="80" span="2" style="mso-width-source:userset;mso-width-alt:
 3072;width:60pt">
 </colgroup><tbody><tr height="21" style="height:16.0pt">
  <td height="21" class="xl107" width="116" style="height:16.0pt;width:87pt">模块</td>
  <td class="xl107" width="171" style="border-left:none;width:128pt">二级模块</td>
  <td class="xl107" width="80" style="border-left:none;width:60pt">是否完成</td>
  <td class="xl107" width="80" style="border-left:none;width:60pt">计划完成</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none">首页</td>
  <td class="xl108" style="border-top:none;border-left:none">首页看板</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="2" height="42" class="xl108" style="height:32.0pt;border-top:none">实时数据监测</td>
  <td class="xl108" style="border-top:none;border-left:none">实时数据</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">历史数据</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="3" height="63" class="xl108" style="height:48.0pt;border-top:none">综合指标分析</td>
  <td class="xl108" style="border-top:none;border-left:none">综合指标分析（日）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">综合指标分析（月）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">综合指标分析（年）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="3" height="63" class="xl108" style="height:48.0pt;border-top:none">重点设备分析</td>
  <td class="xl108" style="border-top:none;border-left:none">重点设备分析（日）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">重点设备分析（月）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">重点设备分析（年）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="3" height="63" class="xl108" style="height:48.0pt;border-top:none">工序能耗分析</td>
  <td class="xl108" style="border-top:none;border-left:none">工序能耗分析（日）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">工序能耗分析（月）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">工序能耗分析（年）</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="2" height="42" class="xl108" style="height:32.0pt;border-top:none">尖峰平谷统计</td>
  <td class="xl108" style="border-top:none;border-left:none">尖峰平谷配置</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">尖峰平谷数据</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="5" height="105" class="xl108" style="height:80.0pt;border-top:none">能耗对比分析</td>
  <td class="xl108" style="border-top:none;border-left:none">电同比分析</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">电环比分析</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">水同比分析</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">水环比分析</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">能耗指标对比</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="2" height="42" class="xl108" style="height:32.0pt;border-top:none">智能报警管理</td>
  <td class="xl108" style="border-top:none;border-left:none">实时报警</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">报警统计</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="2" height="42" class="xl108" style="height:32.0pt;border-top:none">能耗深度分析</td>
  <td class="xl108" style="border-top:none;border-left:none">能耗产消统计</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">能耗平衡分析</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="5" height="105" class="xl108" style="height:80.0pt;border-top:none">能源计划实际</td>
  <td class="xl108" style="border-top:none;border-left:none">计划产量</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">实际产量</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">用能预测</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">能源消耗计划</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">计划与实际对比分析</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none">能源辅助录入</td>
  <td class="xl108" style="border-top:none;border-left:none">阶段数据录入</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="2" height="42" class="xl108" style="height:32.0pt;border-top:none">用能考核管理</td>
  <td class="xl108" style="border-top:none;border-left:none">用能考核标准</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">阶段考核统计</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="3" height="63" class="xl108" style="height:48.0pt;border-top:none">能源对标管理</td>
  <td class="xl108" style="border-top:none;border-left:none">标杆管理</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">实时对标</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">阶段性对标</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="2" height="42" class="xl108" style="height:32.0pt;border-top:none">节能项目管理</td>
  <td class="xl108" style="border-top:none;border-left:none">节能分析报告</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">节能项目管理</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none">综合报表管理</td>
  <td class="xl108" style="border-top:none;border-left:none">能源品种报表</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td rowspan="3" height="63" class="xl108" style="height:48.0pt;border-top:none">系统管理</td>
  <td class="xl108" style="border-top:none;border-left:none">用户管理</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">部门管理</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <tr height="21" style="height:16.0pt">
  <td height="21" class="xl108" style="height:16.0pt;border-top:none;border-left:
  none">岗位管理</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
  <td class="xl108" style="border-top:none;border-left:none">　</td>
 </tr>
 <!--[if supportMisalignedColumns]-->
 <tr height="0" style="display:none">
  <td width="116" style="width:87pt"></td>
  <td width="171" style="width:128pt"></td>
  <td width="80" style="width:60pt"></td>
  <td width="80" style="width:60pt"></td>
 </tr>
 <!--[endif]-->
</tbody></table>

#### 使用说明

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

