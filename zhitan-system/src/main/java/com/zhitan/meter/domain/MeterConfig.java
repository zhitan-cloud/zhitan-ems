package com.zhitan.meter.domain;

import com.zhitan.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: MeterConfiguration
 * @Author: Yarry
 * @CreateTime: 2024-09-23 13-44-42
 * @Description: TODO
 * @Version: 1.0
 * @Since: JDK1.8
 */

@ApiModel(value = "点位配置信息")
public class MeterConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 表名
     */
    @ApiModelProperty(value = "表名")
    private String tableName;

    /**
     * 点位code
     */
    @ApiModelProperty(value = "点位code")
    private String code;

    /**
     * 采集类型【0：采集类，1计算类】
     */
    @ApiModelProperty(value = "采集类型")
    private String indexType;


    /**
     * 表头值
     */
    @ApiModelProperty(value = "表头值")
    private String tableValue;

    /**
     * 步长最小值
     */
    @ApiModelProperty(value = "步长最小值")
    private String stepMin;

    /**
     * 步长最大值
     */
    @ApiModelProperty(value = "步长最大值")
    private String stepMax;


    /**
     * 最小值
     */
    @ApiModelProperty(value = "最小值")
    private String minValue;

    /**
     * 最大值
     */
    @ApiModelProperty(value = "最大值")
    private String maxValue;

    public MeterConfig() {
    }

    public MeterConfig(String id, String tableName, String code, String indexType, String tableValue, String stepMin, String stepMax, String minValue, String maxValue) {
        this.id = id;
        this.tableName = tableName;
        this.code = code;
        this.indexType = indexType;
        this.tableValue = tableValue;
        this.stepMin = stepMin;
        this.stepMax = stepMax;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public MeterConfig(String code) {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getTableValue() {
        return tableValue;
    }

    public void setTableValue(String tableValue) {
        this.tableValue = tableValue;
    }

    public String getStepMin() {
        return stepMin;
    }

    public void setStepMin(String stepMin) {
        this.stepMin = stepMin;
    }

    public String getStepMax() {
        return stepMax;
    }

    public void setStepMax(String stepMax) {
        this.stepMax = stepMax;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return "MeterConfig{" +
                "id='" + id + '\'' +
                ", tableName='" + tableName + '\'' +
                ", code='" + code + '\'' +
                ", indexType='" + indexType + '\'' +
                ", tableValue='" + tableValue + '\'' +
                ", stepMin='" + stepMin + '\'' +
                ", stepMax='" + stepMax + '\'' +
                ", minValue='" + minValue + '\'' +
                ", maxValue='" + maxValue + '\'' +
                '}';
    }
}
