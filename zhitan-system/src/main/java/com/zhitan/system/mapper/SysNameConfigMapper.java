package com.zhitan.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.system.domain.SysNameConfig;

import java.util.List;

/**
 * 系统名称配置Mapper接口
 *
 * @author ZhiTan
 * @date 2024-11-05
 */
public interface SysNameConfigMapper extends BaseMapper<SysNameConfig> {
    /**
     * 查询系统名称配置
     *
     * @param systemName 系统名称配置主键
     * @return 系统名称配置
     */
    public SysNameConfig selectSysNameConfigBySystemName(String systemName);

    /**
     * 查询系统名称配置列表
     *
     * @param sysNameConfig 系统名称配置
     * @return 系统名称配置集合
     */
    public List<SysNameConfig> selectSysNameConfigList(SysNameConfig sysNameConfig);

    /**
     * 新增系统名称配置
     *
     * @param sysNameConfig 系统名称配置
     * @return 结果
     */
    public int insertSysNameConfig(SysNameConfig sysNameConfig);

    /**
     * 修改系统名称配置
     *
     * @param sysNameConfig 系统名称配置
     * @return 结果
     */
    public int updateSysNameConfig(SysNameConfig sysNameConfig);

    /**
     * 删除系统名称配置
     *
     * @param systemName 系统名称配置主键
     * @return 结果
     */
    public int deleteSysNameConfigBySystemName(String systemName);

    /**
     * 批量删除系统名称配置
     *
     * @param systemNames 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNameConfigBySystemNames(String[] systemNames);
}
