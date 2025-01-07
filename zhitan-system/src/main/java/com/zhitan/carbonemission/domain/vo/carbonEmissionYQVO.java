package com.zhitan.carbonemission.domain.vo;

import com.zhitan.common.annotation.Excel;
import lombok.Data;

@Data
public class carbonEmissionYQVO {

    @Excel(name = "日期")
    private String xAxis;
    @Excel(name = "碳排放量")
    private String yAxis;
    @Excel(name = "同比")
    private String YOY;
    @Excel(name = "环比")
    private String QOQ;


    public carbonEmissionYQVO() {
    }

    public carbonEmissionYQVO(String xAxis, String yAxis, String YOY, String QOQ) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.YOY = YOY;
        this.QOQ = QOQ;
    }
}
