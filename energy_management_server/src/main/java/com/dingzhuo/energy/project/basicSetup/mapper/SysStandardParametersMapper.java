package com.dingzhuo.energy.project.basicSetup.mapper;

import com.dingzhuo.energy.project.basicSetup.domain.SysStandardParameters;
import java.util.List;

/**
 * parametersMapper接口
 *
 * @author ruoyi
 * @date 2020-02-12
 */
public interface SysStandardParametersMapper
{
    /**
     * 查询parameters
     *
     * @param id parametersID
     * @return parameters
     */
    public SysStandardParameters selectSysStandardParametersById(String id);
    /*public List<SysStandardParameters> getStandardParameters();*/
    public SysStandardParameters getStandardParameters(String id);
    /**
     * 查询parameters列表
     *
     * @param sysStandardParameters parameters
     * @return parameters集合
     */
    public List<SysStandardParameters> selectSysStandardParametersList(SysStandardParameters sysStandardParameters);

    /**
     * 新增parameters
     *
     * @param sysStandardParameters parameters
     * @return 结果
     */
    public int insertSysStandardParameters(SysStandardParameters sysStandardParameters);

    /**
     * 修改parameters
     *
     * @param sysStandardParameters parameters
     * @return 结果
     */
    public int updateSysStandardParameters(SysStandardParameters sysStandardParameters);

    /**
     * 删除parameters
     *
     * @param id parametersID
     * @return 结果
     */
    public int deleteSysStandardParametersById(String id);

    /**
     * 批量删除parameters
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysStandardParametersByIds(String[] ids);

}