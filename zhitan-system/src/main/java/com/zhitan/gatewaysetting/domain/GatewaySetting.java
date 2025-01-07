package com.zhitan.gatewaysetting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 网关配置信息对象 gateway_setting
 *
 * @author ZhiTan
 * @date 2024-10-30
 */
@TableName("gateway_setting")
@Data
public class GatewaySetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** UUID主键 */
    private String id;

    /** 网关编号 */
    @Excel(name = "网关编号")
    private String gatewayNum;

    /** 网关名称 */
    @Excel(name = "网关名称")
    private String gatewayName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specsModel;

    /** 安装位置 */
    @Excel(name = "安装位置")
    private String installLocation;

    /** ip地址 */
    @Excel(name = "IP地址")
    private String ipAdd;

    /** 运行状态：未知、离线、在线 */
    @Excel(name = "运行状态")
    private String runStatus;

    /** 心跳时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "心跳时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hbtTime;

    /** 计量器具数量 */
    @Excel(name = "计量器具数量")
    private Integer deviceNum;

    /** 监测点数量 */
    @Excel(name = "采集点数量")
    private Integer ptNum;

}
