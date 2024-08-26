package com.dingzhuo.energy.basic.data.workforce.service;

import com.dingzhuo.energy.basic.data.workforce.domain.Rostering;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringCopy;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringScheme;

import java.util.List;

/**
 * 排班表查询Service接口
 * 
 * @author liuli
 * @date 2020-05-13
 */
public interface IRosteringService 
{
    /**
     * 查询轮值方案名称
     * @return 轮值方案名称集合
     */
    public List<RosteringScheme> selectSchemeNameList();
    /**
     * 查询排班表查询
     * 
     * @param id 排班表查询ID
     * @return 排班表查询
     */
    public Rostering selectRosteringById(String id);

    /**
     * 查询排班表查询列表
     * 
     * @param rostering 排班表查询
     * @return 排班表查询集合
     */
    public List<Rostering> selectRosteringList(Rostering rostering);

    /**
     *
     * @return
     */
    public List<RosteringCopy> selectList(Rostering rostering);
    /**
     * 新增排班表查询

     * @return 结果
     */
    public int insertRostering(Rostering rostering );
    public void saveRostering(List<Rostering> dataItems);

    /**
     * 修改排班表查询
     * 
     * @param rostering 排班表查询
     * @return 结果
     */
    public int updateRostering(Rostering rostering);

    /**
     * 批量删除排班表查询
     * 
     * @param ids 需要删除的排班表查询ID
     * @return 结果
     */
    public int deleteRosteringByIds(String[] ids);

    /**
     * 删除排班表查询信息
     * 
     * @param id 排班表查询ID
     * @return 结果
     */
    public int deleteRosteringById(String id);


}
