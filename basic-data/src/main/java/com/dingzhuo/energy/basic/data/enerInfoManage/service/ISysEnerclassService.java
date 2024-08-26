package com.dingzhuo.energy.basic.data.enerInfoManage.service;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnerclass;
import java.util.List;

/**
 * 能源品种设置Service接口
 *
 * @author ruoyi
 * @date 2020-02-10
 */
public interface ISysEnerclassService
{
    /**
     * 查询能源品种设置
     *
     * @param enerclassid 能源品种设置ID
     * @return 能源品种设置
     */
    public SysEnerclass selectSysEnerclassById(Integer enerclassid);

    /**
     * 查询能源品种设置列表
     *
     * @param sysEnerclass 能源品种设置
     * @return 能源品种设置集合
     */
    public List<SysEnerclass> selectSysEnerclassList(SysEnerclass sysEnerclass);

    /**
     * 新增能源品种设置
     *
     * @param sysEnerclass 能源品种设置
     * @return 结果
     */
    public int insertSysEnerclass(SysEnerclass sysEnerclass);

    /**
     * 修改能源品种设置
     *
     * @param sysEnerclass 能源品种设置
     * @return 结果
     */
    public int updateSysEnerclass(SysEnerclass sysEnerclass);

    /**
     * 批量删除能源品种设置
     *
     * @param enerclassids 需要删除的能源品种设置ID
     * @return 结果
     */
    public int deleteSysEnerclassByIds(Integer[] enerclassids);

    /**
     * 删除能源品种设置信息
     *
     * @param enerclassid 能源品种设置ID
     * @return 结果
     */
    public int deleteSysEnerclassById(Integer enerclassid);
    /**
     * 查询一样的能源名称有几个 能源名称唯一校验
     */
    int selectSameEnergyNameNum(String enerclassname);
    /**
     * 修改的时候查询一样能源名称的id
     */
    Integer selectIdByName(String enerclassname);
}
