package com.dingzhuo.energy.project.plannedOutput.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.service.impl.PeriodDataServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.plannedOutput.mapper.PlannedOutputMapper;
import com.dingzhuo.energy.project.plannedOutput.domain.PlannedOutput;
import com.dingzhuo.energy.project.plannedOutput.service.IPlannedOutputService;

/**
 * plannedOutputService业务层处理
 * 
 * @author sys
 * @date 2020-12-16
 */
@Service
public class PlannedOutputServiceImpl implements IPlannedOutputService 
{
    private Logger logger = LogManager.getLogger(PlannedOutputServiceImpl.class);
    @Autowired
    private PlannedOutputMapper plannedOutputMapper;

    /**
     * 查询plannedOutput列表
     * 
     * @param
     * @return plannedOutput
     */
    @Override
    public List<PlannedOutput> selectPlannedOutputList(List<Integer> indexIds, TimeType timeType,Date dataTime,String palnType)
    {
        if (indexIds.size() !=0) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return plannedOutputMapper.selectPlannedOutputList(indexIds, timeCode,palnType);
        }
        return Collections.emptyList();
    }
    public List<PlannedOutput> selectPlanList(List<Integer> indexIds){
        if (indexIds.size() !=0) {
            return plannedOutputMapper.selectPlanList(indexIds);
        }
        return Collections.emptyList();
    }
    /**
     * 新增plannedOutput
     *
     * @return 结果
     */
    /*@Override
    public int insertPlannedOutput(PlannedOutput plannedOutput)
    {
        return plannedOutputMapper.insertPlannedOutput(plannedOutput);
    }*/
    @Override
    public void insertPlannedOutput(List<PlannedOutput> datas) {
        List<List<PlannedOutput>> splitDatas = splitList(datas, 100);
        if (splitDatas != null) {
            splitDatas.parallelStream().forEach(dataPart -> {
                /*try {
                    plannedOutputMapper.saveDataList(dataPart);
                } catch (Exception ex) {
                    logger.error("批量保存数据失败", ex);*/
                    dataPart.parallelStream().forEach(dataItem -> {
                        try {
                            plannedOutputMapper.save(dataItem);
                        } catch (Exception singleEx) {
                            logger.error("单个指标数据保存失败！【" + dataItem + "】", singleEx);
                        }
                    });
               /* }*/
            });
        }
    }
    /**
     * 修改plannedOutput
     * 
     * @param plannedOutput plannedOutput
     * @return 结果
     */
    @Override
    public int updatePlannedOutput(PlannedOutput plannedOutput)
    {
        return plannedOutputMapper.updatePlannedOutput(plannedOutput);
    }
    public static <E> List<List<E>> splitList(List<E> targetList, Integer splitSize) {
        if (targetList == null) {
            return Collections.emptyList();
        }

        int size = targetList.size();
        List<List<E>> resultList = new ArrayList<>();
        if (size <= splitSize) {
            resultList.add(targetList);
        } else {
            for (int i = 0; i < size; i += splitSize) {
                //用于限制最后一部分size小于splitSize的list
                int limit = i + splitSize;
                if (limit > size) {
                    limit = size;
                }
                resultList.add(targetList.subList(i, limit));
            }
        }
        return resultList;
    }
}
