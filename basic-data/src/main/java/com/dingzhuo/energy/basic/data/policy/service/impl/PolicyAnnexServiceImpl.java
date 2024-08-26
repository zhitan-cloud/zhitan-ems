package com.dingzhuo.energy.basic.data.policy.service.impl;

import com.dingzhuo.energy.basic.data.policy.domain.PolicyAnnex;
import com.dingzhuo.energy.basic.data.policy.mapper.PolicyAnnexMapper;
import com.dingzhuo.energy.basic.data.policy.service.IPolicyAnnexService;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 政策法规附件Service业务层处理
 * 
 * @author liuli
 * @date 2020-04-24
 */
@Service
public class PolicyAnnexServiceImpl implements IPolicyAnnexService 
{
    @Autowired
    private PolicyAnnexMapper policyAnnexMapper;

    /**
     * 查询政策法规附件
     * 
     * @param id 政策法规附件ID
     * @return 政策法规附件
     */
    @Override
    public PolicyAnnex selectPolicyAnnexById(String id)
    {
        return policyAnnexMapper.selectPolicyAnnexById(id);
    }

    /**
     * 查询政策法规附件列表
     * 
     * @param policyAnnex 政策法规附件
     * @return 政策法规附件
     */
    @Override
    public List<PolicyAnnex> selectPolicyAnnexList(PolicyAnnex policyAnnex)
    {
        return policyAnnexMapper.selectPolicyAnnexList(policyAnnex);
    }

    /**
     * 新增政策法规附件
     * 
     * @param policyAnnex 政策法规附件
     * @return 结果
     */
    @Override
    public int insertPolicyAnnex(PolicyAnnex policyAnnex)
    {
        policyAnnex.setCreateTime(DateUtils.getNowDate());
        return policyAnnexMapper.insertPolicyAnnex(policyAnnex);
    }

    /**
     * 修改政策法规附件
     * 
     * @param policyAnnex 政策法规附件
     * @return 结果
     */
    @Override
    public int updatePolicyAnnex(PolicyAnnex policyAnnex)
    {
        policyAnnex.setUpdateTime(DateUtils.getNowDate());
        return policyAnnexMapper.updatePolicyAnnex(policyAnnex);
    }

    /**
     * 批量删除政策法规附件
     * 
     * @param ids 需要删除的政策法规附件ID
     * @return 结果
     */
    @Override
    public int deletePolicyAnnexByIds(String[] ids)
    {
        return policyAnnexMapper.deletePolicyAnnexByIds(ids);
    }

    /**
     * 删除政策法规附件信息
     * 
     * @param id 政策法规附件ID
     * @return 结果
     */
    @Override
    public int deletePolicyAnnexById(String id)
    {
        return policyAnnexMapper.deletePolicyAnnexById(id);
    }
}
