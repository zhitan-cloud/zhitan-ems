package com.dingzhuo.energy.basic.data.policy.service;


import com.dingzhuo.energy.basic.data.policy.domain.PolicyRegulations;

import java.util.List;

/**
 * 政策法规维护Service接口
 * 
 * @author liuli
 * @date 2020-04-23
 */
public interface IPolicyRegulationsService 
{
    /**
     * 查询政策法规维护
     * 
     * @param id 政策法规维护ID
     * @return 政策法规维护
     */
    public PolicyRegulations selectPolicyRegulationsById(String id);

    /**
     * 查询政策法规维护列表
     * 
     * @param policyRegulations 政策法规维护
     * @return 政策法规维护集合
     */
    public List<PolicyRegulations> selectPolicyRegulationsList(PolicyRegulations policyRegulations);

    /**
     * 新增政策法规维护
     * 
     * @param policyRegulations 政策法规维护
     * @return 结果
     */
    public int insertPolicyRegulations(PolicyRegulations policyRegulations);

    /**
     * 修改政策法规维护
     * 
     * @param policyRegulations 政策法规维护
     * @return 结果
     */
    public int updatePolicyRegulations(PolicyRegulations policyRegulations);

    /**
     * 批量删除政策法规维护
     * 
     * @param ids 需要删除的政策法规维护ID
     * @return 结果
     */
    public int deletePolicyRegulationsByIds(String[] ids);

    /**
     * 删除政策法规维护信息
     * 
     * @param id 政策法规维护ID
     * @return 结果
     */
    public int deletePolicyRegulationsById(String id);

}
