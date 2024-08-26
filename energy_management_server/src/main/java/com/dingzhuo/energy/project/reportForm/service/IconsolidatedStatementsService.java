package com.dingzhuo.energy.project.reportForm.service;

import com.dingzhuo.energy.project.reportForm.domain.consolidatedStatements;
import java.util.List;

/**
 * 焦化工序综合报表Service接口
 * 
 * @author sys
 * @date 2021-01-15
 */
public interface IconsolidatedStatementsService 
{
    /**
     * 查询焦化工序综合报表
     * 
     * @param id 焦化工序综合报表ID
     * @return 焦化工序综合报表
     */
    public consolidatedStatements selectconsolidatedStatementsById(String id);

    /**
     * 查询焦化工序综合报表列表
     * 
     * @param consolidatedStatements 焦化工序综合报表
     * @return 焦化工序综合报表集合
     */
    public List<consolidatedStatements> selectconsolidatedStatementsList(consolidatedStatements consolidatedStatements);

    /**
     * 新增焦化工序综合报表
     * 
     * @param consolidatedStatements 焦化工序综合报表
     * @return 结果
     */
    public int insertconsolidatedStatements(consolidatedStatements consolidatedStatements);

    /**
     * 修改焦化工序综合报表
     * 
     * @param consolidatedStatements 焦化工序综合报表
     * @return 结果
     */
    public int updateconsolidatedStatements(consolidatedStatements consolidatedStatements);

    /**
     * 批量删除焦化工序综合报表
     * 
     * @param ids 需要删除的焦化工序综合报表ID
     * @return 结果
     */
    public int deleteconsolidatedStatementsByIds(String[] ids);

    /**
     * 删除焦化工序综合报表信息
     * 
     * @param id 焦化工序综合报表ID
     * @return 结果
     */
    public int deleteconsolidatedStatementsById(String id);
}
