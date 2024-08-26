package com.dingzhuo.energy.project.basicSetup.service.impl;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.basicSetup.mapper.SysStandardParametersMapper;
import com.dingzhuo.energy.project.basicSetup.domain.SysStandardParameters;
import com.dingzhuo.energy.project.basicSetup.service.ISysStandardParametersService;

/**
 * parametersService业务层处理
 *
 * @author ruoyi
 * @date 2020-02-12
 */
@Service
public class SysStandardParametersServiceImpl implements ISysStandardParametersService
{
    @Autowired
    private SysStandardParametersMapper sysStandardParametersMapper;

    /**
     * 查询parameters
     *
     * @param id parametersID
     * @return parameters
     */
    @Override
    public SysStandardParameters selectSysStandardParametersById(String id)
    {
        return sysStandardParametersMapper.selectSysStandardParametersById(id);
    }
   /* public List<SysStandardParameters> getStandardParameters(){
        return sysStandardParametersMapper.getStandardParameters();
    }*/
   public SysStandardParameters getStandardParameters(String id){
       return sysStandardParametersMapper.getStandardParameters(id);
   }
    /**
     * 查询parameters列表
     *
     * @param sysStandardParameters parameters
     * @return parameters
     */
    @Override
    public List<SysStandardParameters> selectSysStandardParametersList(SysStandardParameters sysStandardParameters)
    {
        return sysStandardParametersMapper.selectSysStandardParametersList(sysStandardParameters);
    }

    /**
     * 新增parameters
     *
     * @param sysStandardParameters parameters
     * @return 结果
     */
    @Override
    public int insertSysStandardParameters(SysStandardParameters sysStandardParameters) {
        /*sysStandardParameters.setId(UUID.randomUUID().toString())*/;
        sysStandardParameters.setCreateBy(SecurityUtils.getUsername());
        sysStandardParameters.setCreateTime(DateUtils.getNowDate());
        return sysStandardParametersMapper.insertSysStandardParameters(sysStandardParameters);
    }

    /**
     * 修改parameters
     *
     * @param sysStandardParameters parameters
     * @return 结果
     */
    @Override
    public int updateSysStandardParameters(SysStandardParameters sysStandardParameters){
        sysStandardParameters.setUpdateBy(SecurityUtils.getUsername());
        sysStandardParameters.setUpdateTime(DateUtils.getNowDate());
        return sysStandardParametersMapper.updateSysStandardParameters(sysStandardParameters);
    }

    /**
     * 批量删除parameters
     *
     * @param ids 需要删除的parametersID
     * @return 结果
     */
    @Override
    public int deleteSysStandardParametersByIds(String[] ids)
    {
        return sysStandardParametersMapper.deleteSysStandardParametersByIds(ids);
    }

    /**
     * 删除parameters信息
     *
     * @param id parametersID
     * @return 结果
     */
    @Override
    public int deleteSysStandardParametersById(String id)
    {
        return sysStandardParametersMapper.deleteSysStandardParametersById(id);
    }

}