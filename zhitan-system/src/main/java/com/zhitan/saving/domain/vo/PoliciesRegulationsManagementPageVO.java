package com.zhitan.saving.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zhitan.common.annotation.Excel;
import lombok.Data;

import java.util.Date;


/**
 * 政策法规page vo
 * @author Geoffrey
 * @date 2025/01/13
 */
@Data
public class PoliciesRegulationsManagementPageVO {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
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
     * 政策类型名称
     */
    private String typeName;


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
}
