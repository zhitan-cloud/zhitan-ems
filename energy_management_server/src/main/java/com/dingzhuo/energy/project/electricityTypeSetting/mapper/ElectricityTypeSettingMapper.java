package com.dingzhuo.energy.project.electricityTypeSetting.mapper;

import com.dingzhuo.energy.project.electricityTypeSetting.domain.entity.ElectricityTypeSetting;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingPageListVO;

import java.util.List;

/**
 * 计费规则Mapper接口
 *
 * @author ruoyi
 * @date 2024-06-14
 */
public interface ElectricityTypeSettingMapper {

    /**
     * 查询列表
     */
    List<ElectricityTypeSettingPageListVO> selectList(String name);

    /**
     * 根据id查询详情
     *
     * @param id id
     * @return 结构
     */
    ElectricityTypeSetting selectById(String id);

    /**
     * 更新
     *
     * @param rules 规则信息
     * @return 结果
     */
    int updateRules(ElectricityTypeSetting rules);

    /**
     * 新增规则
     *
     * @param rules 规则
     * @return 结果
     */
    int insertRules(ElectricityTypeSetting rules);

    /**
     * 删除计费规则
     *
     * @param id id
     * @return 结果
     */
    int deleteRulesById(String id);
}
