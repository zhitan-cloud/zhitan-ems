package com.dingzhuo.energy.basic.data.workforce.service.impl;

import com.dingzhuo.energy.basic.data.workforce.domain.RosteringShift;
import com.dingzhuo.energy.basic.data.workforce.domain.workForceTreeObject;
import com.dingzhuo.energy.basic.data.workforce.mapper.RosteringShiftMapper;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringShiftService;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.project.system.domain.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

/**
 * 排班管理设置Service业务层处理
 * 
 * @author liuli
 * @date 2020-05-12
 */
@Service
public class RosteringShiftServiceImpl implements IRosteringShiftService 
{
    @Autowired
    private RosteringShiftMapper rosteringShiftMapper;
    @Override
    public List<SysDept> treeList() {
        return rosteringShiftMapper.treeList();
    }
    @Override
    public List<workForceTreeObject> buildModelNodeTree(List<SysDept> modelNodes) {
        List<SysDept> modelNodeTree = modelNodes.stream()
                .filter(f -> f.getParentId().toString().equals("0"))
                .collect(Collectors.toList());
        for (SysDept modelNode : modelNodeTree) {
            List<SysDept> children = modelNodes.stream()
                    .filter(f -> equalsAnyIgnoreCase(f.getParentId().toString(), modelNode.getDeptId().toString()))
                    .collect(Collectors.toList());
            buildTree(modelNode, children, modelNodes);
        }
        return modelNodeTree.stream().map(workForceTreeObject::new).collect(Collectors.toList());
    }
    private void buildTree(SysDept parent, List<SysDept> children, List<SysDept> modelNodes) {
        parent.setChildren(children);

        for (SysDept modelNode : children) {
            List<SysDept> tmp = modelNodes.stream()
                    .filter(f -> equalsAnyIgnoreCase(f.getParentId().toString(), modelNode.getDeptId().toString()))
                    .collect(Collectors.toList());
            if (!tmp.isEmpty()) {
                buildTree(modelNode, tmp, modelNodes);
            }
        }
    }
    /**
     * 查询排班管理设置
     * 
     * @param id 排班管理设置ID
     * @return 排班管理设置
     */
    @Override
    public RosteringShift selectRosteringShiftById(String id)
    {
        return rosteringShiftMapper.selectRosteringShiftById(id);
    }

    /**
     * 查询排班管理设置列表
     * 
     * @param rosteringShift 排班管理设置
     * @return 排班管理设置
     */
    @Override
    public List<RosteringShift> selectRosteringShiftList(RosteringShift rosteringShift)
    {
        return rosteringShiftMapper.selectRosteringShiftList(rosteringShift);
    }

    /**
     * 新增排班管理设置
     * 
     * @param rosteringShift 排班管理设置
     * @return 结果
     */
    @Override
    public int insertRosteringShift(RosteringShift rosteringShift)
    {
        return rosteringShiftMapper.insertRosteringShift(rosteringShift);
    }

    /**
     * 修改排班管理设置
     * 
     * @param rosteringShift 排班管理设置
     * @return 结果
     */
    @Override
    public int updateRosteringShift(RosteringShift rosteringShift)
    {
        rosteringShift.setUpdateTime(DateUtils.getNowDate());
        return rosteringShiftMapper.updateRosteringShift(rosteringShift);
    }

    /**
     * 批量删除排班管理设置
     * 
     * @param ids 需要删除的排班管理设置ID
     * @return 结果
     */
    @Override
    public int deleteRosteringShiftByIds(String[] ids)
    {
        return rosteringShiftMapper.deleteRosteringShiftByIds(ids);
    }

    /**
     * 删除排班管理设置信息
     * 
     * @param id 排班管理设置ID
     * @return 结果
     */
    @Override
    public int deleteRosteringShiftById(String id)
    {
        return rosteringShiftMapper.deleteRosteringShiftById(id);
    }
}
