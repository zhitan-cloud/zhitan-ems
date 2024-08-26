package com.dingzhuo.energy.dataservice.data.influxdb;

import com.jsoniter.annotation.JsonProperty;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author fanxinfu
 */
@Measurement(name = "daq")
public class RtdbResult implements Serializable {
  private static final long serialVersionUID = -6608775093489743678L;
  @Column(name = "time")
  @JsonProperty(value = "dataTime", decoder = DecodeInstant.class)
  private Instant time;
  @Column(name = "code", tag = true)
  @JsonProperty("tagCode")
  private String tagCode;
  @Column(name = "value")
  @JsonProperty("value")
  private Double value;
  @Column(name = "quality")
  @JsonProperty("quality")
  private int quality;

  public Instant getTime() {
    return time;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  public String getTagCode() {
    return tagCode;
  }

  public void setTagCode(String tagCode) {
    this.tagCode = tagCode;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public int getQuality() {
    return quality;
  }

  public void setQuality(int quality) {
    this.quality = quality;
  }

  @Override
  public String toString() {
    return "RtdbResult{" +
        "time=" + time +
        ", tagCode='" + tagCode + '\'' +
        ", value=" + value +
        ", quality=" + quality +
        '}';
  }
}
