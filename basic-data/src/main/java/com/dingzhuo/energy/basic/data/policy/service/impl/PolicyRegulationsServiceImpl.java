package com.dingzhuo.energy.basic.data.policy.service.impl;


import com.dingzhuo.energy.basic.data.policy.domain.PolicyRegulations;
import com.dingzhuo.energy.basic.data.policy.mapper.PolicyRegulationsMapper;
import com.dingzhuo.energy.basic.data.policy.service.IPolicyRegulationsService;
import com.dingzhuo.energy.project.system.mapper.SysDictDataMapper;
import com.dingzhuo.energy.project.system.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 政策法规维护Service业务层处理
 * 
 * @author liuli
 * @date 2020-04-23
 */
@Service
public class PolicyRegulationsServiceImpl implements IPolicyRegulationsService 
{
    @Autowired
    private PolicyRegulationsMapper policyRegulationsMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    /**
     * 查询政策法规维护
     * 
     * @param id 政策法规维护ID
     * @return 政策法规维护
     */
    @Override
    public PolicyRegulations selectPolicyRegulationsById(String id)
    {
        return policyRegulationsMapper.selectPolicyRegulationsById(id);
    }

    /**
     * 查询政策法规维护列表
     * 
     * @param policyRegulations 政策法规维护
     * @return 政策法规维护
     */
    @Override
    public List<PolicyRegulations> selectPolicyRegulationsList(PolicyRegulations policyRegulations)
    {
        return policyRegulationsMapper.selectPolicyRegulationsList(policyRegulations);
    }
    /**
     * 新增政策法规维护
     * 
     * @param policyRegulations 政策法规维护
     * @return 结果
     */
    @Override
    public int insertPolicyRegulations(PolicyRegulations policyRegulations)
    {
        return policyRegulationsMapper.insertPolicyRegulations(policyRegulations);
    }

    /**
     * 修改政策法规维护
     * 
     * @param policyRegulations 政策法规维护
     * @return 结果
     */
    @Override
    public int updatePolicyRegulations(PolicyRegulations policyRegulations)
    {
        return policyRegulationsMapper.updatePolicyRegulations(policyRegulations);
    }

    /**
     * 批量删除政策法规维护
     * 
     * @param ids 需要删除的政策法规维护ID
     * @return 结果
     */
    @Override
    public int deletePolicyRegulationsByIds(String[] ids)
    {
        return policyRegulationsMapper.deletePolicyRegulationsByIds(ids);
    }

    /**
     * 删除政策法规维护信息
     * 
     * @param id 政策法规维护ID
     * @return 结果
     */
    @Override
    public int deletePolicyRegulationsById(String id)
    {
        return policyRegulationsMapper.deletePolicyRegulationsById(id);
    }

}
