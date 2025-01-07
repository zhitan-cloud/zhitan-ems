package com.zhitan.basicdata.services;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.domain.vo.EnergyTypeModel;

import java.util.List;

/**
 * energyService接口
 *
 * @author ruoyi
 * @date 2020-02-12
 */
public interface ISysEnergyService {
    /**
     * 查询energy
     *
     * @param
     * @return energy
     */
    SysEnergy selectSysEnergyById(Integer enerid);

    /**
     * 查询energy列表
     *
     * @param sysEnergy energy
     * @return energy集合
     */
    List<SysEnergy> selectSysEnergyList(SysEnergy sysEnergy);

    /**
     * 新增energy
     *
     * @param sysEnergy energy
     * @return 结果
     */
    int insertSysEnergy(SysEnergy sysEnergy);

    /**
     * 修改energy
     *
     * @param sysEnergy energy
     * @return 结果
     */
    int updateSysEnergy(SysEnergy sysEnergy);

    /**
     * 批量删除energy
     *
     * @param
     * @return 结果
     */
    int deleteSysEnergyByIds(Integer[] enerids);

    /**
     * 删除energy信息
     *
     * @param
     * @return 结果
     */
    int deleteSysEnergyById(Integer enerid);

    /**
     * 查询能源类型下拉框
     *
     * @param
     * @return 结果
     */
    List getenerclassname();

    /**
     * 查询能源类型id by enerclassname
     */
    Integer getEnerClassid(String enerclassname);

    /**
     * 查询一样的能源名称有几个 能源名称唯一校验
     */
    SysEnergy selectSameEnergyNameNum(String enername);

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

    /**
     * 查询所有能源类型信息
     *
     * @return 所有能源类型
     */
    List<EnergyTypeModel> listAllEnergyType();

    /**
     * 根据code查询能源信息
     *
     * @param code 编号
     * @return
     */
    EnergyTypeModel getEnergyTypeByCode(String code);

    Page<SysEnergy> selectSysEnergyPage(SysEnergy sysEnergy, Long pageNum, Long pageSize);

    SysEnergy selectSameEnergyCodeNum(String enersno);
}
