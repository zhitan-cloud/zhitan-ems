package com.zhitan.saving.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;


/**
 * 政策法规对象
 * @author Geoffrey
 * @date 2025/01/13
 */
@Data
@TableName(value = "policies_regulations_management")
@ApiModel(value = "PoliciesRegulationsManagement", description = "政策法规对象")
public class PoliciesRegulationsManagement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 政策标题
     */
    @Excel(name = "政策标题")
    private String title;

    /**
     * 政策类型
     */
    @Excel(name = "政策类型")
    private String type;

    /**
     * 印发部门
     */
    @Excel(name = "印发部门")
    private String dept;

    /**
     * 印发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "印发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issuingTime;

    /**
     * 文件地址
     */
    @Excel(name = "文件地址")
    private String url;

    /**
     * 删除标志（0：正常；1：删除）
     */
    private Integer delFlag;
}
