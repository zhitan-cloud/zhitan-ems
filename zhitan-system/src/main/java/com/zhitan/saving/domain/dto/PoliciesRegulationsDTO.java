package com.zhitan.saving.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Geoffrey
 * @date 2025/01/13
 */
@Data
@ApiModel(value = "政策管理新增DTO")
public class PoliciesRegulationsDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 政策标题
     */
    @NotBlank(message = "请输入政策标题")
    private String title;

    /**
     * 政策类型
     */
    @NotNull(message = "请选择政策类型")
    private Integer type;

    /**
     * 印发部门
     */
    @NotBlank(message = "请输入印发部门")
    private String dept;

    /**
     * 印发时间
     */
    @NotNull(message = "请选择印发时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date issuingTime;

    /**
     * 文件地址
     */
    @NotBlank(message = "请上传文件")
    private String address;



}
