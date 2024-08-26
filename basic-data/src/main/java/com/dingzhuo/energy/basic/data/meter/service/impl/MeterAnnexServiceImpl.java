package com.dingzhuo.energy.basic.data.meter.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.meter.mapper.MeterAnnexMapper;
import com.dingzhuo.energy.basic.data.meter.domain.MeterAnnex;
import com.dingzhuo.energy.basic.data.meter.service.IMeterAnnexService;

/**
 * 计量器具档案附件Service业务层处理
 * 
 * @author zhaowei
 * @date 2020-02-14
 */
@Service
public class MeterAnnexServiceImpl implements IMeterAnnexService 
{
    @Autowired
    private MeterAnnexMapper meterAnnexMapper;

    /**
     * 查询计量器具档案附件
     * 
     * @param id 计量器具档案附件ID
     * @return 计量器具档案附件
     */
    @Override
    public MeterAnnex selectMeterAnnexById(String id)
    {
        return meterAnnexMapper.selectMeterAnnexById(id);
    }

    /**
     * 查询计量器具档案附件列表
     * 
     * @param meterAnnex 计量器具档案附件
     * @return 计量器具档案附件
     */
    @Override
    public List<MeterAnnex> selectMeterAnnexList(MeterAnnex meterAnnex)
    {
        return meterAnnexMapper.selectMeterAnnexList(meterAnnex);
    }

    /**
     * 新增计量器具档案附件
     * 
     * @param meterAnnex 计量器具档案附件
     * @return 结果
     */
    @Override
    public int insertMeterAnnex(MeterAnnex meterAnnex)
    {
        meterAnnex.setCreateTime(DateUtils.getNowDate());
        return meterAnnexMapper.insertMeterAnnex(meterAnnex);
    }

    /**
     * 修改计量器具档案附件
     * 
     * @param meterAnnex 计量器具档案附件
     * @return 结果
     */
    @Override
    public int updateMeterAnnex(MeterAnnex meterAnnex)
    {
        meterAnnex.setUpdateTime(DateUtils.getNowDate());
        return meterAnnexMapper.updateMeterAnnex(meterAnnex);
    }

    /**
     * 批量删除计量器具档案附件
     * 
     * @param ids 需要删除的计量器具档案附件ID
     * @return 结果
     */
    @Override
    public int deleteMeterAnnexByIds(String[] ids)
    {
        return meterAnnexMapper.deleteMeterAnnexByIds(ids);
    }

    /**
     * 删除计量器具档案附件信息
     * 
     * @param id 计量器具档案附件ID
     * @return 结果
     */
    @Override
    public int deleteMeterAnnexById(String id)
    {
        return meterAnnexMapper.deleteMeterAnnexById(id);
    }
}
