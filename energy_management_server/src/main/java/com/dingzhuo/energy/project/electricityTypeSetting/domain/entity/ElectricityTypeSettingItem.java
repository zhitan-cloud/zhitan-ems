package com.dingzhuo.energy.project.electricityTypeSetting.domain.entity;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Data
public class ElectricityTypeSettingItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 计费规则id
     */
    @Excel(name = "计费规则id")
    private String ruleId;

    /**
     * 费用类型时间段
     */
    @Excel(name = "费用类型时间段", readConverterExp = "1=0:00")
    private Integer timePeriod;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @Excel(name = "电费类型 0:尖 1:峰 2:平 3:谷 4:深谷")
    private String type;

    /**
     * 组织架构id
     */
    @Excel(name = "组织架构id")
    private Long deptId;

    /**
     * 删除标记，默认0：未删除，2 删除
     */
    private String delFlag;

}
