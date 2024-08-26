package com.dingzhuo.energy.project.govReports.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * GovReportsVo对象 data_item_pub
 *
 * @author zw
 * @date 2022-04-07
 */
@Data
public class DataItemPubVo {

    private static final long serialVersionUID = 1L;

    /**
     * 指标主键
     */
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
     * 所属期
     */
    @Excel(name = "上报所属期")
    private String timeCode;

    /**
     * 上报值
     */
    @Excel(name = "上报值")
    private Double value;

    /**
     * 原始值
     */
    @Excel(name = "原始值")
    private Double originValue;

    /**
     * 上限限值
     */
    @Excel(name = "上限限值")
    private Double reportUpperLimit;

    /**
     * 上限替换值
     */
    @Excel(name = "上限替换值")
    private Double reportUpperReplace;

    /**
     * 下限限值
     */
    @Excel(name = "下限限值")
    private Double reportLowerLimit;

    /**
     * 下限替换值
     */
    @Excel(name = "下限替换值")
    private Double reportLowerReplace;



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("indexId", getIndexId())
                .append("timeCode", getTimeCode())
                .append("value", getValue())
                .append("reportUpperLimit", getReportUpperLimit())
                .append("reportUpperReplace", getReportUpperReplace())
                .append("reportLowerLimit", getReportLowerLimit())
                .append("reportLowerReplace", getReportLowerReplace())
                .append("originValue", getOriginValue())
                .toString();
    }
}
