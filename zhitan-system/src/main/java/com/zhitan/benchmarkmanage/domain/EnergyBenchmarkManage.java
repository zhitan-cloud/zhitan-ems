package com.zhitan.benchmarkmanage.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 标杆值管理对象 energy_benchmark_manage
 *
 * @author ZhiTan
 * @date 2024-11-12
 */
@TableName("energy_benchmark_manage")
@Data
public class EnergyBenchmarkManage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** uuid主键 */
    private String id;

    /** 标杆编码 */
    @Excel(name = "标杆编码")
    private String code;

    /** 标杆类型 */
    @Excel(name = "标杆类型")
    private String type;

    /** 标杆等级 */
    @Excel(name = "标杆等级")
    private String grade;

    /** 标杆值 */
    @Excel(name = "标杆值")
    private String value;

    /** 国标编号 */
    @Excel(name = "国标编号")
    private String nationalNum;

}
