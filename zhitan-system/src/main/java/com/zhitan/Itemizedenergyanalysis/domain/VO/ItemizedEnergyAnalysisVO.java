package com.zhitan.Itemizedenergyanalysis.domain.VO;

import com.zhitan.common.annotation.Excel;
import lombok.Data;

import java.util.List;

/**
 * 分项用能分析返回类
 *
 * @author 张
 */
@Data
public class ItemizedEnergyAnalysisVO {

    /**
     * 合计
     */
    private String total;
    /**
     * 最大值
     */
    private String max;
    /**
     * 最小值
     */
    private String min;
    /**
     * 平均值
     */
    private String avg;
    /**
     * 单位
     */
    private String unit;

    private List<ItemizedEnergyAnalysisItemVO> dataList;
}
