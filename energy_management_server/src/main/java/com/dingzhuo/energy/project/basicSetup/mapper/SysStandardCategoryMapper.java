package com.dingzhuo.energy.project.basicSetup.mapper;

import com.dingzhuo.energy.project.basicSetup.domain.SysStandardCategory;
import java.util.List;

/**
 * categoryMapper接口
 * 
 * @author ruoyi
 * @date 2020-02-12
 */
public interface SysStandardCategoryMapper 
{
    /**
     * 查询category
     * 
     * @param id categoryID
     * @return category
     */
    public SysStandardCategory selectSysStandardCategoryById(String id);

    /**
     * 查询category列表
     * 
     * @param sysStandardCategory category
     * @return category集合
     */
    public List<SysStandardCategory> selectSysStandardCategoryList(SysStandardCategory sysStandardCategory);

    /**
     * 新增category
     * 
     * @param sysStandardCategory category
     * @return 结果
     */
    public int insertSysStandardCategory(SysStandardCategory sysStandardCategory);

    /**
     * 修改category
     * 
     * @param sysStandardCategory category
     * @return 结果
     */
    public int updateSysStandardCategory(SysStandardCategory sysStandardCategory);

    /**
     * 删除category
     * 
     * @param id categoryID
     * @return 结果
     */
    public int deleteSysStandardCategoryById(String id);

    /**
     * 批量删除category
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysStandardCategoryByIds(String[] ids);
}
