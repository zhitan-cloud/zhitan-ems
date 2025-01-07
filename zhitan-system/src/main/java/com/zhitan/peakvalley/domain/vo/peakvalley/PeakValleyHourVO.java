package com.zhitan.peakvalley.domain.vo.peakvalley;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author hmj
 * @date 2024-09-24 15:59
 */
@Getter
@Setter
public class PeakValleyHourVO {
    List<PeakValleyHourDataVO> dataList;
    List<PeakValleyLineChatVO> lineChat;
    PeakValleyPieChatVO pieChat;
}
