package com.dingzhuo.energy.data.model.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;


/**
 * 指标信息对象 energy_index
 *
 * @author fanxinfu
 * @date 2020-02-14
 */
@ApiModel(value = "指标信息")
public class EnergyIndex extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @ApiModelProperty(value = "主键")
  private String indexId;

  /**
   * 指标名称
   */
  @ApiModelProperty(value = "指标名称")
  @Excel(name = "指标名称", prompt = "必填")
  private String name;

  /**
   * 指标编码
   */
  @ApiModelProperty(value = "指标编码")
  @Excel(name = "指标编码", prompt = "必填")
  private String code;

  /**
   * 系统指标类型
   */
  @ApiModelProperty(value = "系统指标类型")
  private IndexType indexType;

  @Excel(name = "指标类型", combo = "COLLECT,STATISTIC")
  @ApiModelProperty(value = "")
  private String indexTypeCode;

  /**
   * 指标分类
   */
  @Excel(name = "指标分类")
  @ApiModelProperty(value = "指标分类")
  private String indexCategory;

  /**
   * 单位主键
   */
  @Excel(name = "单位主键")
  @ApiModelProperty(value = "单位主键")
  private String unitId;

  @Excel(name = "所属节点", prompt = "必填")
  @ApiModelProperty(value = "所属节点")
  private String nodeId;

  @ApiModelProperty(value = "表计id")
  private String meterId;
  @ApiModelProperty(value = "顺序号")
  @Excel(name = "顺序号")
  private int orderNum;

  @ApiModelProperty(value = "")
  private String equipment;
  @ApiModelProperty(value = "id")
  private String energyId;

  @Transient
  private  String meterName;
  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setIndexType(IndexType indexType) {
    this.indexType = indexType;
    if (StringUtils.isBlank(this.indexTypeCode)) {
      this.indexTypeCode = indexType.name();
    }
  }

  public IndexType getIndexType() {
    if (indexType == null && StringUtils.isNotBlank(this.indexTypeCode)) {
      return IndexType.valueOf(indexTypeCode);
    }

    return indexType;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getUnitId() {
    return unitId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("indexId", getIndexId())
        .append("name", getName())
        .append("code", getCode())
        .append("indexType", getIndexType())
        .append("remark", getRemark())
        .append("unitId", getUnitId())
        .toString();
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getMeterId() {
    return meterId;
  }

  public void setMeterId(String meterId) {
    this.meterId = meterId;
  }

  public String getIndexCategory() {
    return indexCategory;
  }

  public void setIndexCategory(String indexCategory) {
    this.indexCategory = indexCategory;
  }

  public String getIndexTypeCode() {
    if (StringUtils.isBlank(indexTypeCode) && this.indexType != null) {
      return this.indexType.name();
    }
    return indexTypeCode;
  }

  public void setIndexTypeCode(String indexTypeCode) {
    this.indexTypeCode = indexTypeCode;
    if (this.indexType == null) {
      this.indexType = IndexType.valueOf(indexTypeCode);
    }
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public String getEquipment() {
    return equipment;
  }

  public void setEquipment(String equipment) {
    this.equipment = equipment;
  }

  public String getEnergyId() {
    return energyId;
  }

  public void setEnergyId(String energyId) {
    this.energyId = energyId;
  }

  public String getMeterName() {
    return meterName;
  }

  public void setMeterName(String meterName) {
    this.meterName = meterName;
  }
}
