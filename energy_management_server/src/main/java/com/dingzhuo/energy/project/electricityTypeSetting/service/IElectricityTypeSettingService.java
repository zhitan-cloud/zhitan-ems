package com.dingzhuo.energy.project.electricityTypeSetting.service;


import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingAddVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingUpdateVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingItemQueryVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingPageListVO;

import java.util.List;

/**
 * 计费规则Service接口
 *
 * @author ruoyi
 * @date 2024-06-14
 */
public interface IElectricityTypeSettingService {

    /**
     * 查询计费规则列表
     */
    List<ElectricityTypeSettingPageListVO> selectPageList(String name);

    /**
     * 获取计费规则详情
     */
    ElectricityTypeSettingItemQueryVO getRuleDetail(String id);

    /**
     * 修改计费策略
     */
    void updateRule(ElectricityTypeSettingUpdateVO request);

    /**
     * 新增计费策略
     */
    void addRule(ElectricityTypeSettingAddVO request);

    /**
     * 删除计费策略
     */
    void delRule(String id);
}
