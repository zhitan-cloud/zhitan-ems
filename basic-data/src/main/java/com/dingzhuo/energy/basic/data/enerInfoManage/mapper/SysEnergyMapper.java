package com.dingzhuo.energy.basic.data.enerInfoManage.mapper;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnergy;
import java.util.List;

/**
 * energyMapper接口
 *
 * @author ruoyi
 * @date 2020-02-12
 */
public interface SysEnergyMapper
{
    /**
     * 查询能源类型下拉框
     *
     * @param
     * @return 结果
     */
    List getenerclassname();
    /**
     * 查询energy
     *
     * @param
     * @return energy
     */
    public SysEnergy selectSysEnergyById(Integer enerid);

    /**
     * 查询energy列表
     *
     * @param sysEnergy energy
     * @return energy集合
     */
    public List<SysEnergy> selectSysEnergyList(SysEnergy sysEnergy);

    /**
     * 新增energy
     *
     * @param sysEnergy energy
     * @return 结果
     */
    public int insertSysEnergy(SysEnergy sysEnergy);

    /**
     * 修改energy
     *
     * @param sysEnergy energy
     * @return 结果
     */
    public int updateSysEnergy(SysEnergy sysEnergy);

    /**
     * 删除energy
     *
     * @param
     * @return 结果
     */
    public int deleteSysEnergyById(Integer enerid);

    /**
     * 批量删除energy
     *
     * @param
     * @return 结果
     */
    public int deleteSysEnergyByIds(Integer[] enerids);
    /**
     * 通过能源类型名称查询 能源类型id
     */
    Integer getEnerClassid(String enerclassname);
    /**
     * 查询一样的能源名称有几个 能源名称唯一校验
     */
    int selectSameEnergyNameNum(String enername);
    /**
     * 修改的时候查询一样能源名称的id
     */
    Integer selectIdByName(String enername);
    /**
     * 通过能源id查询单价信息
     */
    Integer getPriceCountByEnerid(SysEnergy sysEnergy);
    /**
     * 增加单价信息
     */
    Integer insertEnergyPrice(SysEnergy sysEnergy);
    /**
     * 修改单价信息
     */
    Integer updateEnergyPrice(SysEnergy sysEnergy);
    /**
     * 通过能源id查折标系数num
     */
    Integer getCoefficientCountByEnerid(Integer enerid);
    /**
     * 增加折标系数信息
     */
    Integer insertEnergyCoefficient(SysEnergy sysEnergy);
    /**
     * 修改折标系数信息
     */
    Integer updateEnergyCoefficient(SysEnergy sysEnergy);
}
