package com.zhitan.dataitem.service.impl;

import com.zhitan.common.enums.TimeType;
import com.zhitan.dataitem.domain.StagseDataEntry;
import com.zhitan.dataitem.domain.vo.NodeIndexValueVO;
import com.zhitan.dataitem.mapper.DataItemMapper;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.mapper.EnergyIndexMapper;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.model.mapper.NodeIndexMapper;
import com.zhitan.realtimedata.domain.DataItem;
import com.zhitan.statisticalAnalysis.domain.dto.FlowChartsDTO;
import com.zhitan.statisticalAnalysis.domain.vo.FlowChartsItemVO;
import com.zhitan.statisticalAnalysis.domain.vo.FlowChartsVO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * stagseDataEntryService业务层处理
 *
 * @author sys
 * @date 2020-03-25
 */
@Service
@AllArgsConstructor
public class DataItemServiceImpl implements IDataItemService {

    @Resource
    private DataItemMapper dataItemMapper;
    @Resource
    private ModelNodeMapper modelNodeMapper;
    @Resource
    private EnergyIndexMapper energyIndexMapper;
    @Resource
    private NodeIndexMapper nodeIndexMapper;


    /**
     * 查询需要手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return 结果
     */
    @Override
    public List<StagseDataEntry> getSettingIndex(StagseDataEntry stagseDataEntry) {
        return dataItemMapper.getSettingIndex(stagseDataEntry);
    }

    /**
     * 查询修改手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return 结果
     */
    @Override
    public List<StagseDataEntry> getSettingEdit(StagseDataEntry stagseDataEntry) {
        return dataItemMapper.getSettingEdit(stagseDataEntry);
    }

    @Override
    public List<StagseDataEntry> stagseDataEntry(String nodeId, List<String> indexCodes, TimeType timeType, Date beginTime, Date endTime, String calcType) {
        return dataItemMapper.stagseDataByCode(nodeId, indexCodes, timeType, beginTime, endTime, calcType);
    }

    /**
     * 根据indexId与时间范围查询dataitem合计信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    @Override
    public BigDecimal getDataItemTimeRangeValueByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds) {
        return dataItemMapper.getDataItemTimeRangeValueByIndexIds(beginTime, endTime, timeType, indexIds);
    }

    /**
     * 根据indexId与时间范围查询dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    @Override
    public List<DataItem> getDataItemTimeRangeInforByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds) {
        return dataItemMapper.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, timeType, indexIds);
    }

    /**
     * 根据indexId与时间编码查询点位值合计
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    @Override
    public List<DataItem> getDataItemInforByIndexIds(String timeCode, List<String> indexIds) {
        return dataItemMapper.getDataItemInforByIndexIds(timeCode, indexIds);
    }

    /**
     * 根据indexId与时间编码查询合计值
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    @Override
    public BigDecimal getDataItemValueByIndexIds(String timeCode, List<String> indexIds) {
        return dataItemMapper.getDataItemValueByIndexIds(timeCode, indexIds);
    }

    /**
     * 根据indexId与时间范围查询小时的dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    @Override
    public List<DataItem> getDataItemHourInforByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds) {
        return dataItemMapper.getDataItemHourInforByIndexIds(beginTime, endTime, timeType, indexIds);
    }

    /**
     * 获取能流图形分析
     *
     * @param dto 请求参数
     * @return 结果
     */
    @Override
    public FlowChartsVO getFlowCharts(FlowChartsDTO dto) {
        FlowChartsVO flowChartsVO = new FlowChartsVO();
        // 父节点id
        String nodeId = dto.getNodeId();
        String energyType = dto.getEnergyType();
        LocalDate queryTime = dto.getQueryTime();
        TimeType timeType = dto.getTimeType();

        // 获取节点信息
        ModelNode modelNode = modelNodeMapper.selectModelNodeById(nodeId);
        if (ObjectUtils.isEmpty(modelNode)) {
            return flowChartsVO;
        }
        // 获取查询时间
        Map<String, LocalDateTime> dateTimeMap = getDataItemByIndexId(timeType, queryTime);

        // 获取节点和点位的累积量
        List<NodeIndexValueVO> parentDataItemList = modelNodeMapper.getDataItemByNodeId(nodeId, energyType, timeType, dateTimeMap);

        // 获取子节点下的点位的累积量
        List<NodeIndexValueVO> childDataItemList = modelNodeMapper.getDataItemByParentNodeId(nodeId, energyType, timeType, dateTimeMap);

        // 获取父节点下的能耗数据总和
        if (ObjectUtils.isNotEmpty(parentDataItemList)) {
            // 父节点下的能耗数据总和
            BigDecimal totalValue = parentDataItemList.stream().map(NodeIndexValueVO::getValue)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 总累积量
            flowChartsVO.setTotalAccumulatedAmount(totalValue);
        }
        // 获取子节点下的能耗数据
        if (ObjectUtils.isNotEmpty(childDataItemList)) {
            // 子节点下的能耗数据总和
            BigDecimal childTotalValue = childDataItemList.stream().map(NodeIndexValueVO::getValue)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 子节点累积量
            flowChartsVO.setChildNodeAccumulatedAmount(childTotalValue);

            // 根据子节点id分组
            Map<String, List<NodeIndexValueVO>> voMap = childDataItemList.stream()
                    .collect(Collectors.groupingBy(NodeIndexValueVO::getNodeId));
            List<FlowChartsItemVO> itemList = new ArrayList<>();

            for (String childNodeId : voMap.keySet()) {
                FlowChartsItemVO vo = new FlowChartsItemVO();
                vo.setSource(modelNode.getName());
                List<NodeIndexValueVO> valueList = voMap.getOrDefault(childNodeId, Collections.emptyList());
                if (ObjectUtils.isNotEmpty(valueList)) {
                    // 各个子节点的能耗数据总和
                    BigDecimal value = valueList.stream().map(NodeIndexValueVO::getValue)
                            .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    valueList.stream().findFirst().ifPresent(nodeIndexValueVO -> vo.setTarget(nodeIndexValueVO.getNodeName()));
                    vo.setValue(value);
                }
                itemList.add(vo);
            }
            flowChartsVO.setItemVOList(itemList);
        }
        return flowChartsVO;
    }

    /**
     * 根据indexId查询能耗数据
     */
    private Map<String, LocalDateTime> getDataItemByIndexId(TimeType timeType, LocalDate queryTime) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        LocalDate startDate;
        LocalDate endDate;
        switch (timeType) {
            case DAY:
                // 当天的开始时间
                startTime = LocalDateTime.of(queryTime, LocalTime.MIN);
                // 当天的结束时间
                endTime = LocalDateTime.of(queryTime, LocalTime.MAX);
                break;
            case MONTH:
                // 当月的开始时间
                startDate = queryTime.with(TemporalAdjusters.firstDayOfMonth());
                startTime = LocalDateTime.of(startDate, LocalTime.MIN);
                // 当月的结束时间
                endDate = queryTime.with(TemporalAdjusters.lastDayOfMonth());
                endTime = LocalDateTime.of(endDate, LocalTime.MAX);
                break;
            case YEAR:
                // 当年的开始时间
                startDate = queryTime.with(TemporalAdjusters.firstDayOfYear());
                startTime = LocalDateTime.of(startDate, LocalTime.MIN);
                // 当年的结束时间
                endDate = queryTime.with(TemporalAdjusters.lastDayOfYear());
                endTime = LocalDateTime.of(endDate, LocalTime.MAX);
                break;
            default:
                // 当天的开始时间
                startTime = LocalDateTime.of(queryTime, LocalTime.MIN);
                // 当天的结束时间
                endTime = LocalDateTime.of(queryTime, LocalTime.MAX);
                break;
        }
        Map<String, LocalDateTime> localDateTimeMap = new HashMap<>();
        localDateTimeMap.put("startTime", startTime);
        localDateTimeMap.put("endTime", endTime);
        return localDateTimeMap;
    }
}
