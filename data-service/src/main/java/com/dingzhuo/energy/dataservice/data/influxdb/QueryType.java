package com.dingzhuo.energy.dataservice.data.influxdb;

/**
 * @author 智碳未来
 * <p>
 * 获取实时数据方式.
 */
public enum QueryType {
  /**
   * 原始
   */
  FULL,
  /**
   * 插值
   */
  MEAN,
  /**
   * 最小
   */
  MIN,
  /**
   * 最大
   */
  MAX,
  /**
   * 最早
   */
  FIRST,
  /**
   * 最后
   */
  LAST,
  /**
   * 中位数
   */
  MEDIAN,
  /**
   * 求和
   */
  SUM,
  /**
   * 积分
   */
  INTEGRAL
}
