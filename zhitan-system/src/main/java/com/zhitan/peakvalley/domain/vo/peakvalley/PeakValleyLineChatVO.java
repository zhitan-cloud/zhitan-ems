package com.zhitan.peakvalley.domain.vo.peakvalley;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * description 尖峰平谷柱状图
 *
 * @author hmj
 * @date 2024-09-24 16:32
 */
@Getter
@Setter
public class PeakValleyLineChatVO {
    @ApiModelProperty(value = "2024-09-24 00", name = "时间")
    private String xdata;
    @ApiModelProperty(value = "0", name = "尖")
    private BigDecimal ytip;
    @ApiModelProperty(value = "0", name = "峰")
    private BigDecimal ypeak;
    @ApiModelProperty(value = "0", name = "平")
    private BigDecimal yflat;
    @ApiModelProperty(value = "0", name = "谷")
    private BigDecimal ytrough;
}
