package com.dingzhuo.energy.basic.data.meter.service;

import com.dingzhuo.energy.basic.data.meter.domain.MeterAnnex;
import java.util.List;

/**
 * 计量器具档案附件Service接口
 * 
 * @author zhaowei
 * @date 2020-02-14
 */
public interface IMeterAnnexService 
{
    /**
     * 查询计量器具档案附件
     * 
     * @param id 计量器具档案附件ID
     * @return 计量器具档案附件
     */
    public MeterAnnex selectMeterAnnexById(String id);

    /**
     * 查询计量器具档案附件列表
     * 
     * @param meterAnnex 计量器具档案附件
     * @return 计量器具档案附件集合
     */
    public List<MeterAnnex> selectMeterAnnexList(MeterAnnex meterAnnex);

    /**
     * 新增计量器具档案附件
     * 
     * @param meterAnnex 计量器具档案附件
     * @return 结果
     */
    public int insertMeterAnnex(MeterAnnex meterAnnex);

    /**
     * 修改计量器具档案附件
     * 
     * @param meterAnnex 计量器具档案附件
     * @return 结果
     */
    public int updateMeterAnnex(MeterAnnex meterAnnex);

    /**
     * 批量删除计量器具档案附件
     * 
     * @param ids 需要删除的计量器具档案附件ID
     * @return 结果
     */
    public int deleteMeterAnnexByIds(String[] ids);

    /**
     * 删除计量器具档案附件信息
     * 
     * @param id 计量器具档案附件ID
     * @return 结果
     */
    public int deleteMeterAnnexById(String id);
}
