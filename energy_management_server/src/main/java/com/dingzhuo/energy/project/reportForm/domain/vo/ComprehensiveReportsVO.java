package com.dingzhuo.energy.project.reportForm.domain.vo;


import lombok.Data;

import java.util.List;

/**
 * 综合报表返回类
 *
 * @Author: Zhujw
 * @Date: 2023/3/16
 */
@Data
public class ComprehensiveReportsVO {

    /**
     * MC用电总量
     */
    private List<ComprehensiveReportsItem> reportsItemList;

    /**
     * 综合能耗折线图
     */
    private List<ComprehensiveReportsItem> chart;
}
