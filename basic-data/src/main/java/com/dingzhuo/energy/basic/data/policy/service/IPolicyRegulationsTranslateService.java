package com.dingzhuo.energy.basic.data.policy.service;

import com.dingzhuo.energy.basic.data.policy.domain.PolicyRegulationsTranslate;

import java.util.List;

/**
 * 政策法规查询Service接口
 * 
 * @author liuli
 * @date 2020-04-24
 */
public interface IPolicyRegulationsTranslateService 
{
    /**
     * 查询政策法规查询
     * 
     * @param id 政策法规查询ID
     * @return 政策法规查询
     */
    public PolicyRegulationsTranslate selectPolicyRegulationsTranslateById(String id);

    /**
     * 查询政策法规查询列表
     * 
     * @param policyRegulationsTranslate 政策法规查询
     * @return 政策法规查询集合
     */
    public List<PolicyRegulationsTranslate> selectPolicyRegulationsTranslateList(PolicyRegulationsTranslate policyRegulationsTranslate);

    /**
     * 新增政策法规查询
     * 
     * @param policyRegulationsTranslate 政策法规查询
     * @return 结果
     */
    public int insertPolicyRegulationsTranslate(PolicyRegulationsTranslate policyRegulationsTranslate);

    /**
     * 修改政策法规查询
     * 
     * @param policyRegulationsTranslate 政策法规查询
     * @return 结果
     */
    public int updatePolicyRegulationsTranslate(PolicyRegulationsTranslate policyRegulationsTranslate);

    /**
     * 批量删除政策法规查询
     * 
     * @param ids 需要删除的政策法规查询ID
     * @return 结果
     */
    public int deletePolicyRegulationsTranslateByIds(String[] ids);

    /**
     * 删除政策法规查询信息
     * 
     * @param id 政策法规查询ID
     * @return 结果
     */
    public int deletePolicyRegulationsTranslateById(String id);

}
