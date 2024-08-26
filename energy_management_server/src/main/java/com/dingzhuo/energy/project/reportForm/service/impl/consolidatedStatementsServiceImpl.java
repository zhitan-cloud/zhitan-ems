package com.dingzhuo.energy.project.reportForm.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.reportForm.mapper.consolidatedStatementsMapper;
import com.dingzhuo.energy.project.reportForm.domain.consolidatedStatements;
import com.dingzhuo.energy.project.reportForm.service.IconsolidatedStatementsService;

/**
 * 焦化工序综合报表Service业务层处理
 * 
 * @author sys
 * @date 2021-01-15
 */
@Service
public class consolidatedStatementsServiceImpl implements IconsolidatedStatementsService 
{
    @Autowired
    private consolidatedStatementsMapper consolidatedStatementsMapper;

    /**
     * 查询焦化工序综合报表
     * 
     * @param id 焦化工序综合报表ID
     * @return 焦化工序综合报表
     */
    @Override
    public consolidatedStatements selectconsolidatedStatementsById(String id)
    {
        return consolidatedStatementsMapper.selectconsolidatedStatementsById(id);
    }

    /**
     * 查询焦化工序综合报表列表
     * 
     * @param consolidatedStatements 焦化工序综合报表
     * @return 焦化工序综合报表
     */
    @Override
    public List<consolidatedStatements> selectconsolidatedStatementsList(consolidatedStatements consolidatedStatements)
    {
        return consolidatedStatementsMapper.selectconsolidatedStatementsList(consolidatedStatements);
    }

    /**
     * 新增焦化工序综合报表
     * 
     * @param consolidatedStatements 焦化工序综合报表
     * @return 结果
     */
    @Override
    public int insertconsolidatedStatements(consolidatedStatements consolidatedStatements)
    {
        consolidatedStatements.setCreateTime(DateUtils.getNowDate());
        return consolidatedStatementsMapper.insertconsolidatedStatements(consolidatedStatements);
    }

    /**
     * 修改焦化工序综合报表
     * 
     * @param consolidatedStatements 焦化工序综合报表
     * @return 结果
     */
    @Override
    public int updateconsolidatedStatements(consolidatedStatements consolidatedStatements)
    {
        consolidatedStatements.setUpdateTime(DateUtils.getNowDate());
        return consolidatedStatementsMapper.updateconsolidatedStatements(consolidatedStatements);
    }

    /**
     * 批量删除焦化工序综合报表
     * 
     * @param ids 需要删除的焦化工序综合报表ID
     * @return 结果
     */
    @Override
    public int deleteconsolidatedStatementsByIds(String[] ids)
    {
        return consolidatedStatementsMapper.deleteconsolidatedStatementsByIds(ids);
    }

    /**
     * 删除焦化工序综合报表信息
     * 
     * @param id 焦化工序综合报表ID
     * @return 结果
     */
    @Override
    public int deleteconsolidatedStatementsById(String id)
    {
        return consolidatedStatementsMapper.deleteconsolidatedStatementsById(id);
    }
}
