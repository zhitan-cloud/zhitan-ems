package com.dingzhuo.compute.engine.config;

import org.kohsuke.args4j.Option;

public class EngineArgOption {

  @Option(name = "-c", usage = "指标计算引擎")
  private boolean calc;
  @Option(name = "-a", usage = "指标报警引擎")
  private boolean alarm;
  @Option(name = "-d", usage = "设备检测引擎")
  private boolean device;
  @Option(name = "-r", usage = "重算引擎")
  private boolean recast;

  public boolean isCalc() {
    return calc;
  }

  public void setCalc(boolean calc) {
    this.calc = calc;
  }

  public boolean isAlarm() {
    return alarm;
  }

  public void setAlarm(boolean alarm) {
    this.alarm = alarm;
  }

  public boolean isDevice() {
    return device;
  }

  public void setDevice(boolean device) {
    this.device = device;
  }

  public void setRecast(boolean recast) {
    this.recast = recast;
  }

  public boolean isRecast() {
    return recast;
  }
}
