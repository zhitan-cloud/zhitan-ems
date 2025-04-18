package com.zhitan.alarm.services.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.HistoryAlarm;
import com.zhitan.alarm.domain.JkHistoryAlarm;
import com.zhitan.alarm.mapper.HistoryAlarmMapper;
import com.zhitan.alarm.services.IHistoryAlarmService;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.mapper.MeterImplementMapper;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.NodeIndex;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.EnergyIndexMapper;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.model.mapper.NodeIndexMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor

@Service
public class HistoryAlarmServiceImpl implements IHistoryAlarmService {

    @Resource
    private HistoryAlarmMapper historyAlarmMapper;

    @Resource
    private MeterImplementMapper meterImplementMapper;

    @Resource
    private ModelNodeMapper modelNodeMapper;
    @Resource
    private NodeIndexMapper nodeIndexMapper;
    @Resource
    private EnergyIndexMapper energyIndexMapper;


    @Override
    public List<HistoryAlarm> getHistoryAlarm(Date beginTime, Date endTime) {
        return null;
    }

    @Override
    public List<HistoryAlarm> getHistoryAlarm(Date beginTime, Date endTime, TimeType timeType) {
        return null;
    }

    @Override
    public List<HistoryAlarm> getHistoryAlarm(Date beginTime, Date endTime, String alarmType) {
        return null;
    }

    @Override
    public List<JkHistoryAlarm> selectJkHistoryAlarmList(JkHistoryAlarm jkHistoryAlarm) {
        return historyAlarmMapper.selectJkHistoryAlarmList(jkHistoryAlarm);
    }

    @Override
    public List<JkHistoryAlarm> selectJkHistoryAlarmListExcel(JkHistoryAlarm jkHistoryAlarm) {
        return historyAlarmMapper.selectJkHistoryAlarmListExcel(jkHistoryAlarm);
    }

    /**
     * 实时检测 功能 的多 sheet页  展示 组态图  测点 报警信息
     *
     * @param jkHistoryAlarm
     * @return
     */
    @Override
    public List<JkHistoryAlarm> selectHistoryAlarmNoteList(JkHistoryAlarm jkHistoryAlarm) {
        return historyAlarmMapper.selectHistoryAlarmNoteList(jkHistoryAlarm);
    }

    @Override
    public void updateHistoryAlarm(String alarmCode, HistoryAlarm historyAlarm) {
        historyAlarmMapper.updateHistoryAlarm(alarmCode, historyAlarm);
    }

    // 废弃
    @Override
    public Page<JkHistoryAlarm> selectJkHistoryAlarmPage(JkHistoryAlarm jkHistoryAlarm, Long pageNum, Long pageSize) {
        final Page<JkHistoryAlarm> jkHistoryAlarmPage = historyAlarmMapper.selectJkHistoryAlarmPage(jkHistoryAlarm, new Page<>(pageNum, pageSize));
        jkHistoryAlarmPage.getRecords().forEach(alarm -> {
            final String indexType = alarm.getIndexType();
            final String indexId = alarm.getIndexId();
            if ("COLLECT".equals(indexType) && StringUtils.isEmpty(alarm.getEnergyId())) {
                //根据nodeId和indexId 去查询计量器具
                EnergyIndex energyIndex = energyIndexMapper.selectEnergyIndexById(indexId);
                final MeterImplement meterImplement = meterImplementMapper.selectMeterImplementById(energyIndex.getMeterId());
                alarm.setEnergyId(meterImplement.getEnergyType());
            }
        });
        return jkHistoryAlarmPage;
    }

    /**
     * 获取历史报警分页数据
     *
     * @param historyAlarm
     * @return
     */
    @Override
    public List<JkHistoryAlarm> selectHistoryAlarmPageList(JkHistoryAlarm historyAlarm) {

        List<String> indexIdList = new ArrayList<>();
        if ("ALL".equals(historyAlarm.getEierarchyFlag())) {

            ModelNode modelNode = modelNodeMapper.selectModelNodeById(historyAlarm.getNodeId());
            List<ModelNodeIndexInfo> modelNodeIndexInfoList =
                    modelNodeMapper.getAllModelNodeIndexByAddress(modelNode.getModelCode(), modelNode.getAddress());
            if (StringUtils.isNotEmpty(historyAlarm.getIndexName())) {
                modelNodeIndexInfoList = modelNodeIndexInfoList.stream()
                        .filter(modelNodeIndexInfo -> modelNodeIndexInfo.getIndexName().contains(historyAlarm.getIndexName()))
                        .collect(Collectors.toList());
            }
            if (ObjectUtils.isNotEmpty(historyAlarm.getIndexType())) {
                modelNodeIndexInfoList = modelNodeIndexInfoList.stream()
                        .filter(modelNodeIndexInfo -> historyAlarm.getIndexType().equals(modelNodeIndexInfo.getIndexType()))
                        .collect(Collectors.toList());
            }
            indexIdList = modelNodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toList());

        } else {

            LambdaQueryWrapper<NodeIndex> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(NodeIndex::getNodeId, historyAlarm.getNodeId());
            List<NodeIndex> nodeIndexList = nodeIndexMapper.selectList(queryWrapper);
            List<String> allIndexIdList = nodeIndexList.stream().map(NodeIndex::getIndexId).collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(allIndexIdList)) {
                List<EnergyIndex> indexList = energyIndexMapper.selectEnergyIndexByIds(allIndexIdList);
                if (ObjectUtils.isNotEmpty(historyAlarm.getIndexName())) {
                    indexList = indexList.stream().filter(energyIndex -> energyIndex.getName().contains(historyAlarm.getIndexName())).collect(Collectors.toList());
                }
                if (ObjectUtils.isNotEmpty(historyAlarm.getIndexType())) {
                    indexList = indexList.stream().filter(energyIndex -> historyAlarm.getIndexType().equals(energyIndex.getIndexTypeCode())).collect(Collectors.toList());
                }
                indexIdList = indexList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            }
        }

        if (ObjectUtils.isEmpty(indexIdList)) {
            return new ArrayList<>();
        }

        //时间处理 如果不传时间默认查询当天的数据
        Date endTime = DateUtils.parseDate(historyAlarm.getEndTime());
        if (ObjectUtils.isEmpty(endTime)) {
            endTime = DateUtil.endOfDay(DateUtils.getNowDate());
        }
        Date beginTime = DateUtils.parseDate(historyAlarm.getEndTime());
        if (ObjectUtils.isEmpty(beginTime)) {
            beginTime = DateUtil.beginOfDay(DateUtils.getNowDate());
        }

        List<JkHistoryAlarm> historyAlarmList = historyAlarmMapper.getHistoryAlarmList(indexIdList, beginTime, endTime);
        return historyAlarmList;
    }

}
