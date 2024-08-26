package com.dingzhuo.energy.basic.data.policy.service;

import com.dingzhuo.energy.basic.data.policy.domain.PolicyAnnex;
import java.util.List;

/**
 * 政策法规附件Service接口
 * 
 * @author liuli
 * @date 2020-04-24
 */
public interface IPolicyAnnexService 
{
    /**
     * 查询政策法规附件
     * 
     * @param id 政策法规附件ID
     * @return 政策法规附件
     */
    public PolicyAnnex selectPolicyAnnexById(String id);

    /**
     * 查询政策法规附件列表
     * 
     * @param policyAnnex 政策法规附件
     * @return 政策法规附件集合
     */
    public List<PolicyAnnex> selectPolicyAnnexList(PolicyAnnex policyAnnex);

    /**
     * 新增政策法规附件
     * 
     * @param policyAnnex 政策法规附件
     * @return 结果
     */
    public int insertPolicyAnnex(PolicyAnnex policyAnnex);

    /**
     * 修改政策法规附件
     * 
     * @param policyAnnex 政策法规附件
     * @return 结果
     */
    public int updatePolicyAnnex(PolicyAnnex policyAnnex);

    /**
     * 批量删除政策法规附件
     * 
     * @param ids 需要删除的政策法规附件ID
     * @return 结果
     */
    public int deletePolicyAnnexByIds(String[] ids);

    /**
     * 删除政策法规附件信息
     * 
     * @param id 政策法规附件ID
     * @return 结果
     */
    public int deletePolicyAnnexById(String id);
}
