package com.dingzhuo.energy.project.plannedOutput.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.plannedOutput.domain.PlanEnergy;
import com.dingzhuo.energy.project.plannedOutput.domain.PlannedOutput;
import com.dingzhuo.energy.project.plannedOutput.mapper.PlanEnergyMapper;
import com.dingzhuo.energy.project.plannedOutput.service.IPlanEnergyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author sys
 * @date 2020-12-17
 */
@Service
public class PlanEnergyServiceImpl implements IPlanEnergyService
{
    private Logger logger = LogManager.getLogger(PlanEnergyServiceImpl.class);
    @Autowired
    private PlanEnergyMapper planEnergyMapper;

    /**
     * 查询列表
     */
    @Override
    public List<PlanEnergy> selectPlanEnergyList(List<Integer> indexIds,TimeType timeType, Date dataTime)
    {
        if (indexIds.size() !=0) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return planEnergyMapper.selectPlanEnergyList(indexIds,timeCode);
        }
        return Collections.emptyList();
    }

    public List<PlanEnergy> PlanEnergyList(List<Integer> indexIds, TimeType timeType, Date dataTime,String type)
    {
        if (indexIds.size() !=0) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return planEnergyMapper.PlanEnergyList(indexIds, timeCode,type);
        }
        return Collections.emptyList();
    }


    /**
     * 新增修改
     */
    @Override
    public void insertPlanEnergy(List<PlanEnergy> datas) {
        List<List<PlanEnergy>> splitDatas = splitList(datas, 100);
        if (splitDatas != null) {
            splitDatas.parallelStream().forEach(dataPart -> {
                /*try {
                    plannedOutputMapper.saveDataList(dataPart);
                } catch (Exception ex) {
                    logger.error("批量保存数据失败", ex);*/
                dataPart.parallelStream().forEach(dataItem -> {
                    try {
                        planEnergyMapper.save(dataItem);
                    } catch (Exception singleEx) {
                        logger.error("单个指标数据保存失败！【" + dataItem + "】", singleEx);
                    }
                });
                /* }*/
            });
        }
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
