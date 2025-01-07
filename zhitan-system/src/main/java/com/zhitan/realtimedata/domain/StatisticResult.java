package com.zhitan.realtimedata.domain;

import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import com.zhitan.common.utils.DateUtils;
import lombok.Data;

import java.util.Date;

@Data
public class StatisticResult extends BaseEntity {
  /**
   * 指标主键
   */
  @Excel(name = "指标主键")
  private String indexId;
  /**
   * 指标编码
   */
  @Excel(name = "指标编码")
  private String indexCode;
  /**
   * 指标名称
   */
  @Excel(name = "指标名称")
  private String indexName;
  /**
   * 单位主键
   */
  @Excel(name = "单位主键")
  private String unitId;
  /**
   * 数据时间
   */
  @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
  private Date dataTime;
  /**
   * 本期值
   */
  @Excel(name = "本期值")
  private double currentValue;
  /**
   * 上期值
   */
  @Excel(name = "上期值")
  private double previousValue;
  /**
   * 同期值
   */
  @Excel(name = "同期值")
  private double lastYearValue;
  /**
   * 最大值
   */
  @Excel(name = "最大值")
  private double maxValue;
  /**
   * 最小值
   */
  @Excel(name = "最小值")
  private double minValue;
  /**
   * 平均值
   */
  @Excel(name = "平均值")
  private double avgValue;
  //时间
  private String formatdate;

  public void setFormatdate(String formatdate) {
    this.formatdate = DateUtils.parseDateToStr(formatdate,this.dataTime);
  }

  /**
   * 同比.
   */
  public double getYoy() {
    if (lastYearValue != 0) {
      return (currentValue - lastYearValue) / lastYearValue * 100;
    }
    return 0;
  }

  /**
   * 环比.
   * @return
   */
  public double getQoq() {
    if (previousValue != 0) {
      return (currentValue - previousValue) / previousValue * 100;
    }
    return 0;
  }

}
