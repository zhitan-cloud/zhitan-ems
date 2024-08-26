package com.dingzhuo.energy.project.basicSetup.service.impl;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.basicSetup.mapper.SysStandardCategoryMapper;
import com.dingzhuo.energy.project.basicSetup.domain.SysStandardCategory;
import com.dingzhuo.energy.project.basicSetup.service.ISysStandardCategoryService;

/**
 * categoryService业务层处理
 * 
 * @author ruoyi
 * @date 2020-02-12
 */
@Service
public class SysStandardCategoryServiceImpl implements ISysStandardCategoryService 
{
    @Autowired
    private SysStandardCategoryMapper sysStandardCategoryMapper;

    /**
     * 查询category
     * 
     * @param id categoryID
     * @return category
     */
    @Override
    public SysStandardCategory selectSysStandardCategoryById(String id)
    {
        return sysStandardCategoryMapper.selectSysStandardCategoryById(id);
    }

    /**
     * 查询category列表
     * 
     * @param sysStandardCategory category
     * @return category
     */
    @Override
    public List<SysStandardCategory> selectSysStandardCategoryList(SysStandardCategory sysStandardCategory)
    {
        return sysStandardCategoryMapper.selectSysStandardCategoryList(sysStandardCategory);
    }

    /**
     * 新增category
     * 
     * @param sysStandardCategory category
     * @return 结果
     */
    @Override
    public int insertSysStandardCategory(SysStandardCategory sysStandardCategory)
    {
        //sysStandardCategory.setId(UUID.randomUUID().toString());
        sysStandardCategory.setCreateBy(SecurityUtils.getUsername());
        sysStandardCategory.setCreateTime(DateUtils.getNowDate());
        return sysStandardCategoryMapper.insertSysStandardCategory(sysStandardCategory);
    }

    /**
     * 修改category
     * 
     * @param sysStandardCategory category
     * @return 结果
     */
    @Override
    public int updateSysStandardCategory(SysStandardCategory sysStandardCategory)
    {
        sysStandardCategory.setUpdateBy(SecurityUtils.getUsername());
        sysStandardCategory.setUpdateTime(DateUtils.getNowDate());
        return sysStandardCategoryMapper.updateSysStandardCategory(sysStandardCategory);
    }

    /**
     * 批量删除category
     * 
     * @param ids 需要删除的categoryID
     * @return 结果
     */
    @Override
    public int deleteSysStandardCategoryByIds(String[] ids)
    {
        return sysStandardCategoryMapper.deleteSysStandardCategoryByIds(ids);
    }

    /**
     * 删除category信息
     * 
     * @param id categoryID
     * @return 结果
     */
    @Override
    public int deleteSysStandardCategoryById(String id)
    {
        return sysStandardCategoryMapper.deleteSysStandardCategoryById(id);
    }
}
