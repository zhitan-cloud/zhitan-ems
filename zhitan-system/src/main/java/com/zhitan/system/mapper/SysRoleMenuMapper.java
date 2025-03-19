package com.zhitan.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.system.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

/**
 * 角色与菜单关联表 数据层
 * 
 * @author zhitan
 */
 public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu>
{
    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
     int checkMenuExistRole(Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
     int deleteRoleMenuByRoleId(Long roleId);

    /**
     * 批量删除角色菜单关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     int deleteRoleMenu(@Param("ids") Long[] ids);

    /**
     * 批量新增角色菜单信息
     * 
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
     int batchRoleMenu(@Param("list") List<SysRoleMenu> roleMenuList);
}
