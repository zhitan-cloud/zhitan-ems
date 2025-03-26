package com.zhitan.Itemizedenergyanalysis.domain.VO;

import com.zhitan.common.annotation.Excel;
import lombok.Data;

/**
 *
 */
@Data
public class ItemizedEnergyAnalysisItemVO {
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 合计
     */
    private Double total;
    @Excel(name = "1时/1日/1月")
    private Double value0;
    @Excel(name = "2时/2日/2月")
    private Double value1;
    @Excel(name = "3时/3日/3月")
    private Double value2;
    @Excel(name = "4时/4日/4月")
    private Double value3;
    @Excel(name = "5时/5日/5月日")
    private Double value4;
    @Excel(name = "6时/6日/6月日")
    private Double value5;
    @Excel(name = "7时/7日/7月日")
    private Double value6;
    @Excel(name = "8时/8日/8月日")
    private Double value7;
    @Excel(name = "9时/9日/9月日")
    private Double value8;
    @Excel(name = "10时/10日/10月日")
    private Double value9;
    @Excel(name = "11时/11日/11月日")
    private Double value10;
    @Excel(name = "12时/12日/12月")
    private Double value11;
    @Excel(name = "13时/13日")
    private Double value12;
    @Excel(name = "14时/14日")
    private Double value13;
    @Excel(name = "15时/15日")
    private Double value14;
    @Excel(name = "16时/16日")
    private Double value15;
    @Excel(name = "17时/17日")
    private Double value16;
    @Excel(name = "18时/18日")
    private Double value17;
    @Excel(name = "19时/19日")
    private Double value18;
    @Excel(name = "20时/20日")
    private Double value19;
    @Excel(name = "21时/21日")
    private Double value20;
    @Excel(name = "22时/22日")
    private Double value21;
    @Excel(name = "23时/23日")
    private Double value22;
    @Excel(name = "24时/24日")
    private Double value23;
    @Excel(name = "25日")
    private Double value24;
    @Excel(name = "26日")
    private Double value25;
    @Excel(name = "27日")
    private Double value26;
    @Excel(name = "28日")
    private Double value27;
    @Excel(name = "29日")
    private Double value28;
    @Excel(name = "30日")
    private Double value29;
    @Excel(name = "31日")
    private Double value30;
}
