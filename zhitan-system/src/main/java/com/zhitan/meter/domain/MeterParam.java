package com.zhitan.meter.domain;

import io.swagger.annotations.ApiModel;

import java.util.Objects;

/**
 * @ClassName: MeterParam
 * @Author: Yarry
 * @CreateTime: 2024-09-28 11-32-01
 * @Description: TODO
 * @Version: 1.0
 * @Since: JDK1.8
 */

@ApiModel(value = "查询配置信息")
public class MeterParam {
    private static final long serialVersionUID = 1L;

    private String id;

    private String meterName;

    private String indexId;

    private String code;

    private String name;

    public MeterParam() {
    }

    public MeterParam(String id, String meterName, String indexId, String code, String name) {
        this.id = id;
        this.meterName = meterName;
        this.indexId = indexId;
        this.code = code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MeterParam{" +
                "id='" + id + '\'' +
                ", meterName='" + meterName + '\'' +
                ", indexId='" + indexId + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterParam that = (MeterParam) o;
        return Objects.equals(id, that.id) && Objects.equals(meterName, that.meterName) && Objects.equals(indexId, that.indexId) && Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, meterName, indexId, code, name);
    }
}
