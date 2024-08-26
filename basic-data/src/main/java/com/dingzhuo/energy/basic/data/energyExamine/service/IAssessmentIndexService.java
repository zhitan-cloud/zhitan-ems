package com.dingzhuo.energy.basic.data.energyExamine.service;

import com.dingzhuo.energy.basic.data.energyExamine.domain.AssessmentIndex;
import java.util.List;

/**
 * 用能考核标准Service接口
 * 
 * @author zhaow
 * @date 2020-12-22
 */
public interface IAssessmentIndexService 
{
    /**
     * 查询用能考核标准
     * 
     * @param id 用能考核标准ID
     * @return 用能考核标准
     */
    public AssessmentIndex selectAssessmentIndexById(String id);

    /**
     * 查询用能考核标准列表
     * 
     * @param assessmentIndex 用能考核标准
     * @return 用能考核标准集合
     */
    public List<AssessmentIndex> selectAssessmentIndexList(AssessmentIndex assessmentIndex);

    /**
     * 查询用能考核标准列表根据模型ID
     *
     * @param assessmentIndex 用能考核标准
     * @return 用能考核标准集合
     */
    public List<AssessmentIndex> selectAssessmentIndexListByMode(AssessmentIndex assessmentIndex);

    /**
     * 新增用能考核标准
     * 
     * @param assessmentIndex 用能考核标准
     * @return 结果
     */
    public int insertAssessmentIndex(AssessmentIndex assessmentIndex);

    /**
     * 修改用能考核标准
     * 
     * @param assessmentIndex 用能考核标准
     * @return 结果
     */
    public int updateAssessmentIndex(AssessmentIndex assessmentIndex);

    /**
     * 批量删除用能考核标准
     * 
     * @param ids 需要删除的用能考核标准ID
     * @return 结果
     */
    public int deleteAssessmentIndexByIds(String[] ids);

    /**
     * 删除用能考核标准信息
     * 
     * @param id 用能考核标准ID
     * @return 结果
     */
    public int deleteAssessmentIndexById(String id);

    /**
     * 批量新增模型考核指标
     *
     * @param assessmentIndexList 考核指标列表
     * @return 结果
     */
    public int batchAssessIndex(List<AssessmentIndex> assessmentIndexList);
}
