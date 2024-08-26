package com.dingzhuo.energy.basic.data.energyExamine.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.energyExamine.mapper.AssessmentIndexMapper;
import com.dingzhuo.energy.basic.data.energyExamine.domain.AssessmentIndex;
import com.dingzhuo.energy.basic.data.energyExamine.service.IAssessmentIndexService;

/**
 * 用能考核标准Service业务层处理
 * 
 * @author zhaow
 * @date 2020-12-22
 */
@Service
public class AssessmentIndexServiceImpl implements IAssessmentIndexService 
{
    @Autowired
    private AssessmentIndexMapper assessmentIndexMapper;

    /**
     * 查询用能考核标准
     * 
     * @param id 用能考核标准ID
     * @return 用能考核标准
     */
    @Override
    public AssessmentIndex selectAssessmentIndexById(String id)
    {
        return assessmentIndexMapper.selectAssessmentIndexById(id);
    }

    /**
     * 查询用能考核标准列表
     * 
     * @param assessmentIndex 用能考核标准
     * @return 用能考核标准
     */
    @Override
    public List<AssessmentIndex> selectAssessmentIndexList(AssessmentIndex assessmentIndex)
    {
        return assessmentIndexMapper.selectAssessmentIndexList(assessmentIndex);
    }

    /**
     * 查询用能考核标准列表根据模型ID
     *
     * @param assessmentIndex 用能考核标准
     * @return 用能考核标准集合
     */
    public List<AssessmentIndex> selectAssessmentIndexListByMode(AssessmentIndex assessmentIndex)
    {
        return assessmentIndexMapper.selectAssessmentIndexListByMode(assessmentIndex);
    }

    /**
     * 新增用能考核标准
     * 
     * @param assessmentIndex 用能考核标准
     * @return 结果
     */
    @Override
    public int insertAssessmentIndex(AssessmentIndex assessmentIndex)
    {
        return assessmentIndexMapper.insertAssessmentIndex(assessmentIndex);
    }

    /**
     * 修改用能考核标准
     * 
     * @param assessmentIndex 用能考核标准
     * @return 结果
     */
    @Override
    public int updateAssessmentIndex(AssessmentIndex assessmentIndex)
    {
        return assessmentIndexMapper.updateAssessmentIndex(assessmentIndex);
    }

    /**
     * 批量删除用能考核标准
     * 
     * @param ids 需要删除的用能考核标准ID
     * @return 结果
     */
    @Override
    public int deleteAssessmentIndexByIds(String[] ids)
    {
        return assessmentIndexMapper.deleteAssessmentIndexByIds(ids);
    }

    /**
     * 删除用能考核标准信息
     * 
     * @param id 用能考核标准ID
     * @return 结果
     */
    @Override
    public int deleteAssessmentIndexById(String id)
    {
        return assessmentIndexMapper.deleteAssessmentIndexById(id);
    }

    /**
     * 批量新增模型考核指标
     *
     * @param assessmentIndexList 考核指标列表
     * @return 结果
     */
    @Override
    public int batchAssessIndex(List<AssessmentIndex> assessmentIndexList){
        return assessmentIndexMapper.batchAssessIndex(assessmentIndexList);
    }
}
