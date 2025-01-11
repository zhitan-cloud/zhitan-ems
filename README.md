<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">企业能源管理平台</h1>
<h4 align="center">基于SpringBoot和若依框架开发</h4>
<p align="center">
    <a href='https://gitee.com/ustcyc/zhitan-ems/stargazers'><img src='https://gitee.com/ustcyc/zhitan-ems/badge/star.svg?theme=dark' alt='star'></img></a>
	<a href="https://gitee.com/y_project/RuoYi/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

## 介绍
通过物联网技术，采集企业水、电、气、热等能耗数据，帮企业建立能源管理体系，找到跑冒滴漏，从而为企业节能提供依据。
进一步为企业实现碳跟踪、碳盘查、碳交易、谈汇报的全生命过程。 为中国碳达峰-碳中和做出贡献。
针对客户场景：政府、园区、企业、工矿、公共建筑等。

已同步更新到github仓库：https://github.com/Andy-Yin/zhitan-ems

## 框架：

基于SpringBoot的若依框架 易读易懂、界面简洁美观。   
关系数据库：mysql 或者 postgredb   
时序数据库：influxdb 2.7+  
中间件：redis，mq  
VUE版本：VUE 3

## 数据驱动的计算模型和业务模型配置
支持动态点位配置、数据模型、业务模型等。 

## 在线体验

- guestUser/guestUser  

演示地址：https://demo-ems.zhitancloud.com/  

## 亮点功能
![输入图片说明](readme/亮点功能.png)

## 功能列表
1.  首页看板
2.  实时数据监测  
    2.1.  实时数据查看  
    2.2.  组态图分析（svg 图绑定）
3. 历史数据分析
4. 综合指标分析  
   4.1.  综合指标分析（日）  
   4.2.  综合指标分析（月）  
   4.3.  综合指标分析（年）
5. 重点设备分析  
   5.1.  重点设备分析（日）  
   5.2.  重点设备分析（月）    
   5.3.  重点设备分析（年）
6. 工序能耗分析  
   6.1.  工序能耗分析（日）  
   6.2.  工序能耗分析（月）    
   6.3.  工序能耗分析（年）
7. 尖峰平谷分析  
   7.1.  尖峰平谷配置  
   7.2.  尖峰平谷数据
8. 能耗对比分析（各能源品种）  
   8.1.  电同环比分析      
   8.2.  水同环比分析
9. 智能报警  
   9.1.  报警分析        
   9.2.  报警配置
10. 单耗分析
11. 计划与实绩
12. 用能考核
13. 用能对标
14. 数据补录
15. 节能项目管理
16. 能源平衡分析
17. 能源对标分析
18. 模型配置管理（计算模型等）
19. 基础数据管理（字典、能源类型等）
20. 系统管理（用户、角色、权限等）

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
![输入图片说明](readme/技术架构.png)

## 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request