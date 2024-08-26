package com.dingzhuo.energy.basic.data.energyExamine.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 用能考核标准对象 assessmentIndex
 * 
 * @author zhaow
 * @date 2020-12-22
 */
public class AssessmentIndex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String id;

    /** 模型节点id */
    private String modeNodeId;

    /** 指标id */
    private String indexId;

    /** 是否能耗计划 Y  N */
    private boolean plan;

    /** 是否对标 Y  N */
    private boolean benchmarking;

    /** 标杆范围字典项 */
    private String benchRange;

    /** 标杆类型字典项 */
    private String benchType;

    /** 是否能源双控 Y N */
    private boolean dualControl;

    /** 是否自定义标准 Y  N */
    private boolean customStandard;

    /** 指定自定义标准数值 */
    private Double customVal;

    /**模型名称**/
    private String modename;

    /**指标名称**/
    private String indexname;

    /**计量单位**/
    private String jldw;
    /**使用状态 Y使用  N停用**/
    private String state;
    //查询本级、全部的标志
    private String eierarchyFlag;

    public String getEierarchyFlag(){
        return  this.eierarchyFlag;
    }
    public void setEierarchyFlag(String eierarchyFlag)
    {
        this.eierarchyFlag =eierarchyFlag;
    }

    public String getModename() {
        return modename;
    }

    public void setModename(String modename) {
        this.modename = modename;
    }

    public String getIndexname() {
        return indexname;
    }

    public void setIndexname(String indexname) {
        this.indexname = indexname;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public AssessmentIndex() {
    }

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setModeNodeId(String modeNodeId) 
    {
        this.modeNodeId = modeNodeId;
    }

    public String getModeNodeId() 
    {
        return modeNodeId;
    }
    public void setIndexId(String indexId) 
    {
        this.indexId = indexId;
    }

    public String getIndexId() 
    {
        return indexId;
    }
    public void setPlan(boolean plan)
    {
        this.plan = plan;
    }

    public boolean getPlan()
    {
        return plan;
    }
    public void setBenchmarking(boolean benchmarking)
    {
        this.benchmarking = benchmarking;
    }

    public boolean getBenchmarking()
    {
        return benchmarking;
    }
    public void setBenchRange(String benchRange)
    {
        this.benchRange = benchRange;
    }

    public String getBenchRange()
    {
        return benchRange;
    }
    public void setBenchType(String benchType)
    {
        this.benchType = benchType;
    }

    public String getBenchType()
    {
        return benchType;
    }
    public void setDualControl(boolean dualControl)
    {
        this.dualControl = dualControl;
    }

    public boolean getDualControl()
    {
        return dualControl;
    }
    public void setCustomStandard(boolean customStandard)
    {
        this.customStandard = customStandard;
    }

    public boolean getCustomStandard()
    {
        return customStandard;
    }
    public void setCustomVal(Double customVal)
    {
        this.customVal = customVal;
    }

    public Double getCustomVal()
    {
        return customVal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("modeNodeId", getModeNodeId())
            .append("indexId", getIndexId())
            .append("plan", getPlan())
            .append("benchmarking", getBenchmarking())
            .append("benchRange", getBenchRange())
            .append("benchType", getBenchType())
            .append("dualControl", getDualControl())
            .append("customStandard", getCustomStandard())
            .append("customVal", getCustomVal())
            .toString();
    }
}
