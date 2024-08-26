package com.dingzhuo.energy.data.monitoring.alarm.domain;

public enum AlarmJudgeDirection {
  /**
   * 大于
   */
  G(">"),
  /**
   * 大于等于
   */
  GE(">="),
  /**
   * 小于
   */
  L("<"),
  /**
   * 小于等于
   */
  LE("<="),
  /**
   * 等于
   */
  E("=");

  private String code;

  AlarmJudgeDirection(String code) {
    this.code = code;
  }

  public static AlarmJudgeDirection value(String code) {
    switch (code) {
      case ">":
        return AlarmJudgeDirection.G;
      case ">=":
        return AlarmJudgeDirection.GE;
      case "<":
        return AlarmJudgeDirection.L;
      case "<=":
        return AlarmJudgeDirection.LE;
      case "=":
        return AlarmJudgeDirection.E;
      default:
        return AlarmJudgeDirection.E;
    }
  }
}
