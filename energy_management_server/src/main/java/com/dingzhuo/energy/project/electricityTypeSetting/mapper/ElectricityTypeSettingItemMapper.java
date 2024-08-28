package com.dingzhuo.energy.project.electricityTypeSetting.mapper;


import com.dingzhuo.energy.project.electricityTypeSetting.domain.entity.ElectricityTypeSettingItem;

import java.util.List;

/**
 * 价格Mapper接口
 *
 * @author ruoyi
 * @date 2024-06-14
 */
public interface ElectricityTypeSettingItemMapper {

    /**
     * 根据规则id查询规则详情
     *
     * @param id 规则id
     * @return 结果
     */
    List<ElectricityTypeSettingItem> selectListByRuleId(String id);

    /**
     * 修改
     *
     * @param ruleDetails 规则详情
     */
    int updateRuleDetails(ElectricityTypeSettingItem ruleDetails);

    /**
     * 新增规则明细
     *
     * @param ruleDetails 规则明细
     * @return 结果
     */
    int insertRuleDetails(ElectricityTypeSettingItem ruleDetails);

    /**
     * 根据规则id删除规则明细
     *
     * @param ruleId 规则id
     */
    int deleteRulesByRuleId(String ruleId);
}
