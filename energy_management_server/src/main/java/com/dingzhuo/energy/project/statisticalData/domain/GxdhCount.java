package com.dingzhuo.energy.project.statisticalData.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 工序单耗统计功能对象 gxdh_count
 * 
 * @author zhaow
 * @date 2020-12-26
 */
public class GxdhCount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置主键 */
    private String id;

    /** 产品类型 */
    @Excel(name = "产品类型")
    private String repProductType;

    /** 电指标 */
    @Excel(name = "电指标")
    private String electricIndex;

    /** 煤气指标 */
    @Excel(name = "煤气指标")
    private String gasIndex;

    /** 蒸汽指标 */
    @Excel(name = "蒸汽指标")
    private String steamIndex;

    /** 能源品种 */
    @Excel(name = "能源品种")
    private String varietyType;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setRepProductType(String repProductType) 
    {
        this.repProductType = repProductType;
    }

    public String getRepProductType() 
    {
        return repProductType;
    }
    public void setElectricIndex(String electricIndex) 
    {
        this.electricIndex = electricIndex;
    }

    public String getElectricIndex() 
    {
        return electricIndex;
    }
    public void setGasIndex(String gasIndex) 
    {
        this.gasIndex = gasIndex;
    }

    public String getGasIndex() 
    {
        return gasIndex;
    }
    public void setSteamIndex(String steamIndex) 
    {
        this.steamIndex = steamIndex;
    }

    public String getSteamIndex() 
    {
        return steamIndex;
    }
    public void setVarietyType(String varietyType) 
    {
        this.varietyType = varietyType;
    }

    public String getVarietyType() 
    {
        return varietyType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("repProductType", getRepProductType())
            .append("electricIndex", getElectricIndex())
            .append("gasIndex", getGasIndex())
            .append("steamIndex", getSteamIndex())
            .append("varietyType", getVarietyType())
            .toString();
    }
}
