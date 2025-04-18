<div align="center">
    <img src="readme/logo-chinese.png" alt="输入图片说明" height="150" width="150">
</div>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">智碳能源管理系统</h1>
<p align="center">基于SpringBoot和若依框架开发</p>
<p align="center">能碳平台后台展示&接口已完全开源，代码完备，功能齐全，运行正常</p>
<p align="center"><span style="color: red;">通过本项目，学习者可以掌握能源管理行业的功能和业务，以及技术架构。</span></p>

<p align="center">
    <a href='https://gitee.com/liulingling1993/zhitan-ems/stargazers'><img src='https://gitee.com/liulingling1993/zhitan-ems/badge/star.svg?theme=dark' alt='star'></img></a>
    <a href='https://gitee.com/liulingling1993/zhitan-ems/members'><img src='https://gitee.com/liulingling1993/zhitan-ems/badge/fork.svg?theme=dark' alt='fork'></img></a>
</p>

## 介绍
能源管理系统，采集企业水、电、气、热等能耗数据，帮企业建立能源管理体系，找到跑冒滴漏，从而为企业节能提供依据。
进一步为企业实现碳跟踪、碳盘查、碳交易、碳汇报的全生命过程。 为中国碳达峰-碳中和做出贡献。

针对客户场景：政府、园区、企业、工矿、公共建筑等。

## 【注意】完整能碳管理平台包含三个部分，<span style="color: red;">本仓库只包含能碳平台后台管理端</span>
1. 能碳平台后台管理端：<span style="color: red;">也即本项目能碳平台后台展示部分，代码完备，运行正常。通过本项目，学习者可以掌握能源管理行业的功能和业务，以及技术架构。</span>
2. 数据采集程序：也即mqtt➡️时序库功能，请参考[我们另一个仓库（点击超链接），MQTT采集网关](https://github.com/zhitan-cloud/zhitan-gateway)；或者可参考thingsjs等知名项目，或者自己用netty自己实现。
3. 数据清洗服务：也即时序库➡️关系库，学习者可以使用java自带的XXL job等计划任务工具自己按照业务功能，来实现数据清洗服务。

## 文档--均在wiki目录
### github wiki地址：https://github.com/zhitan-cloud/zhitan-ems/wiki
### gitee wiki地址：https://gitee.com/liulingling1993/zhitan-ems/wikis/


## 在线体验

#### guestUser/guest@123456
#### 演示地址：https://demo-ems.zhitancloud.com/

## 开源协议
zhitan-ems是基于 [AGPL](https://www.gnu.org/licenses/agpl-3.0.en.html) 和 [ZTPL](./LICENSE) 双协议的开源软件。

## 社区版-功能列表
1.  首页看板 **已完成**
2.  实时数据监测 **已完成**  
    2.1.  实时数据查看 **已完成**  
    2.2.  组态图分析（svg 图绑定）**已完成**
3. 历史点位分析 **已完成**
4. 综合指标分析 **已完成**  
   4.1.  综合指标分析（日）**已完成**  
   4.2.  综合指标分析（月）**已完成**  
   4.3.  综合指标分析（年）**已完成**
5. 重点设备分析  **已完成**    
   5.1.  重点设备分析（日）**已完成**  
   5.2.  重点设备分析（月）**已完成**    
   5.3.  重点设备分析（年）**已完成**
6. 工序能耗分析 **已完成**  
   6.1.  工序能耗分析（日）**已完成**  
   6.2.  工序能耗分析（月）**已完成**    
   6.3.  工序能耗分析（年）**已完成**
7. 尖峰平谷分析 **已完成**  
   7.1.  尖峰平谷配置 **已完成**  
   7.2.  尖峰平谷数据 **已完成**
8. 能耗对比分析（各能源品种）**已完成**  
   8.1.  电同环比分析 **已完成**      
   8.2.  水同环比分析 **已完成**       
   8.3.  其他能源品种分析 **已完成**
9. 智能报警 **已完成**  
   9.1.  报警分析 **已完成**        
   9.2.  报警配置 **已完成**
10. 数据补录 **已完成**
11. 节能项目管理 **已完成**
12. 知识库 **已完成**
13. 设备档案管理 **已完成**
14. 模型配置管理（计算模型等） **已完成**
15. 基础数据管理（字典、能源类型等） **已完成**
16. 系统管理（用户、角色、权限等） **已完成**
17. 建筑能耗分析（支路、分项） **已完成**

## 增强版-功能列表
1. 单耗分析
2. 计划与实绩
3. 用能考核
4. 用能对标
5. 建筑能耗空调节能
6. 能源平衡分析
7. 能源对标分析
8. 空压机重点设备节能内容
9. 碳盘查、碳路径、碳排放因子因子相关内容
10. 源网荷储（微电网）相关功能
11. 低碳园区相关
12. 自定义报表
13. 能源成本分析
14. 光伏运维监控
15. 充电运营平台



## 框架：

基于SpringBoot的若依框架 易读易懂、界面简洁美观。支持深色&浅色两种风格切换（演示系统右上角）   
关系数据库：postgresql（mysql需自行适配，mysql性能太差了）   
时序数据库：influxdb 2.7+  
中间件：redis  
VUE版本：VUE 3

## 特色：数据驱动的计算模型和业务模型配置
1. 精心设计的展示界面。
 ![输入图片说明](readme/img/0-index.png)
2. 支持动态点位配置。
![输入图片说明](readme/img/指标配置.png)
2.  支持计算公式。
![输入图片说明](readme/img/计算公式.png)
3.  支持模型配置、包括数据模型、业务模型。
![输入图片说明](readme/img/模型配置.png)
4.  复杂项目底层代码&数据结构完全不需要变化，可快速交付实施。
5.  已完成标准化的网关&电表等采集设备对接，快速为客户软硬件一体化交付。


## 亮点功能
![输入图片说明](readme/亮点功能.png)

## UI展示（平台分深色和浅色两种风格切换）

    登录页面
![输入图片说明](readme/img/1-登录页.png)

    首页-浅色 
![输入图片说明](readme/img/2-1-首页-浅色.png)

    首页-深色 
![输入图片说明](readme/img/2-2-首页-深色.png)

    实时监测-浅色 
![输入图片说明](readme/img/3-1-能源实时监测-浅色.png)

    实时监测-深色 
![输入图片说明](readme/img/3-2-能源实时监测-深色.png)

    尖峰平谷-浅色 
![输入图片说明](readme/img/4-1-峰平谷时段统计-浅色.png)

    尖峰平谷-深色 
![输入图片说明](readme/img/4-2-峰平谷时段统计-深色.png)

    区域能耗-浅色 
![输入图片说明](readme/img/5-1-区域能耗分析-浅色.png)

    区域能耗-深色 
![输入图片说明](readme/img/5-2-区域能耗分析-深色.png)

## 业务架构
![输入图片说明](readme/业务架构.png)

## 技术架构
![输入图片说明](readme/技术架构-1.png)
![输入图片说明](readme/技术架构.png)

## 沟通交流

应很多开发者私信，我创建了QQ群交流，群号：，请备注：ems+姓名。
<p align="center">
  <img src="readme/img/qq.png" width=50% height=50%>
</p>

扫码添加微信交流，加微信请备注：ems+姓名。

<p align="center">
  <img src="readme/img/image-yc.png" width=50% height=50%>
</p>

## 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
