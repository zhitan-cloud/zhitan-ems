package com.zhitan.energyMonitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description: 用能单元关联的平台模板中仪表的界面逻辑关系 不含有 采集、计算信息
 * @Author: jeecg-boot
 * @Date:   2022-01-26
 * @Version: V1.0
 */
@Data
@TableName("energy_unit_to_device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="energy_unit_to_device对象", description="用能单元关联的平台模板中仪表的界面逻辑关系 不含有 采集、计算信息")
public class EnergyUnitToDevice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "新主键")
    @TableId(type = IdType.ASSIGN_ID)
    private String newId;

	/**主键*/
    @ApiModelProperty(value = "主键")
    private String id;

	/**关联仪表名称*/
	@Excel(name = "关联仪表名称", width = 15)
    @ApiModelProperty(value = "关联仪表名称")
    private String name;

	/**关联仪表编码*/
	@Excel(name = "关联仪表编码", width = 15)
    @ApiModelProperty(value = "关联仪表编码")
    private String code;

	/**能源类型（水表、电表、气表等）*/
	@Excel(name = "能源类型（水表、电表、气表等）", width = 15)
    @ApiModelProperty(value = "能源类型（水表、电表、气表等）")
    private String deviceType;

	/**用能单元主键*/
	@Excel(name = "用能单元主键", width = 15)
    @ApiModelProperty(value = "用能单元主键")
    private String energyUnitId;

	/**租户主键*/
	@Excel(name = "租户主键", width = 15)
    @ApiModelProperty(value = "租户主键")
    private Integer tenantId;

    @ApiModelProperty(value = "参与计量")
    private String partMeasurement;

    @ApiModelProperty(value = "安装位置")
    private String installPosition;

    @ApiModelProperty(value = "分摊比例")
    private BigDecimal shareRatio;
}
