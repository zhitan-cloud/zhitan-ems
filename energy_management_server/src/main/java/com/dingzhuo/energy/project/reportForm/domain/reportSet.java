package com.dingzhuo.energy.project.reportForm.domain;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: report_set
 * @Author: jeecg-boot
 * @Date:   2022-04-01
 * @Version: V1.0
 */
@Data
public class reportSet implements Serializable {
	/**id*/
    @ApiModelProperty(value = "id")
    private String id;
	/**indexId*/
    @ApiModelProperty(value = "indexId")
    private String indexId;
	/**时间类型：DAY/MONTH*/
    @ApiModelProperty(value = "时间类型：DAY/MONTH")
    private String dateType;
    /**
     * 时间类型显示字符串
     */
    private String dateTypeShow;
	/**上限值*/
    @ApiModelProperty(value = "上限值")
    private String limitValUp;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
	/**createBy*/
    @ApiModelProperty(value = "createBy")
    private String createBy;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private Date updateTime;
	/**updateBy*/
    @ApiModelProperty(value = "updateBy")
    private String updateBy;
	/**下限值*/
    @ApiModelProperty(value = "下限值")
    private String limitValDown;
	/**nodeId*/
    @ApiModelProperty(value = "nodeId")
    private String nodeId;
	/**上限替换值*/
    @ApiModelProperty(value = "上限替换值")
    private String limitReplaceValUp;
	/**下限替换值*/
    @ApiModelProperty(value = "下限替换值")
    private String limitReplaceValDown;

    /** 是否启用限值替换*/
    @ApiModelProperty(value = "是否启用限值替换")
    private Integer enableLimitValue;
}
