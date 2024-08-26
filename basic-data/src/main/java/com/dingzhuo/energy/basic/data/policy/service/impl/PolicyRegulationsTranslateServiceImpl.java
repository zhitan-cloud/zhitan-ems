package com.dingzhuo.energy.basic.data.policy.service.impl;

import com.dingzhuo.energy.basic.data.policy.domain.PolicyRegulationsTranslate;
import com.dingzhuo.energy.basic.data.policy.mapper.PolicyRegulationsTranslateMapper;
import com.dingzhuo.energy.basic.data.policy.service.IPolicyRegulationsTranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 政策法规查询Service业务层处理
 * 
 * @author liuli
 * @date 2020-04-24
 */
@Service
public class PolicyRegulationsTranslateServiceImpl implements IPolicyRegulationsTranslateService 
{
    @Autowired
    private PolicyRegulationsTranslateMapper policyRegulationsTranslateMapper;

    /**
     * 查询政策法规查询
     * 
     * @param id 政策法规查询ID
     * @return 政策法规查询
     */
    @Override
    public PolicyRegulationsTranslate selectPolicyRegulationsTranslateById(String id)
    {
        return policyRegulationsTranslateMapper.selectPolicyRegulationsTranslateById(id);
    }

    /**
     * 查询政策法规查询列表
     * 
     * @param policyRegulationsTranslate 政策法规查询
     * @return 政策法规查询
     */
    @Override
    public List<PolicyRegulationsTranslate> selectPolicyRegulationsTranslateList(PolicyRegulationsTranslate policyRegulationsTranslate)
    {
        return policyRegulationsTranslateMapper.selectPolicyRegulationsTranslateList(policyRegulationsTranslate);
    }

    /**
     * 新增政策法规查询
     * 
     * @param policyRegulationsTranslate 政策法规查询
     * @return 结果
     */
    @Override
    public int insertPolicyRegulationsTranslate(PolicyRegulationsTranslate policyRegulationsTranslate)
    {
        return policyRegulationsTranslateMapper.insertPolicyRegulationsTranslate(policyRegulationsTranslate);
    }

    /**
     * 修改政策法规查询
     * 
     * @param policyRegulationsTranslate 政策法规查询
     * @return 结果
     */
    @Override
    public int updatePolicyRegulationsTranslate(PolicyRegulationsTranslate policyRegulationsTranslate)
    {
        return policyRegulationsTranslateMapper.updatePolicyRegulationsTranslate(policyRegulationsTranslate);
    }

    /**
     * 批量删除政策法规查询
     * 
     * @param ids 需要删除的政策法规查询ID
     * @return 结果
     */
    @Override
    public int deletePolicyRegulationsTranslateByIds(String[] ids)
    {
        return policyRegulationsTranslateMapper.deletePolicyRegulationsTranslateByIds(ids);
    }

    /**
     * 删除政策法规查询信息
     * 
     * @param id 政策法规查询ID
     * @return 结果
     */
    @Override
    public int deletePolicyRegulationsTranslateById(String id)
    {
        return policyRegulationsTranslateMapper.deletePolicyRegulationsTranslateById(id);
    }


}
