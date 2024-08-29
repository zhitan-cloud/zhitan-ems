package com.dingzhuo.energy.project.electricityTypeSetting.service.impl;

import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingAddVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingItemAddVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingItemUpdateVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingUpdateVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.entity.ElectricityTypeSetting;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.entity.ElectricityTypeSettingItem;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.enums.ElectricityTypeEnum;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingItemQueryVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingItemVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingPageListVO;
import com.dingzhuo.energy.project.electricityTypeSetting.mapper.ElectricityTypeSettingItemMapper;
import com.dingzhuo.energy.project.electricityTypeSetting.mapper.ElectricityTypeSettingMapper;
import com.dingzhuo.energy.project.electricityTypeSetting.service.IElectricityTypeSettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 计费规则Service业务层处理
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Service
public class ElectricityTypeSettingServiceImpl implements IElectricityTypeSettingService {

    @Resource
    private ElectricityTypeSettingMapper rulesMapper;
    @Resource
    private ElectricityTypeSettingItemMapper ruleDetailsMapper;


    /**
     * 查询计费规则列表
     */
    @Override
    public List<ElectricityTypeSettingPageListVO> selectPageList(String name) {
        List<ElectricityTypeSettingPageListVO> rulesPageListVOList = rulesMapper.selectList(name);
        if (CollectionUtils.isEmpty(rulesPageListVOList)) {
            return rulesPageListVOList;
        }
        Date maxEffectiveDate = new Date();
        Date date = new Date();

        for (ElectricityTypeSettingPageListVO vo : rulesPageListVOList) {
            Date effectiveDate = vo.getEffectiveDate();
            // 判断并设置 effective 属性
            if (effectiveDate.before(date) && effectiveDate.after(maxEffectiveDate)) {
                maxEffectiveDate = effectiveDate;
                vo.setEffective(true);
            }
        }
        return rulesPageListVOList;
    }

    /**
     * 获取计费规则详情
     */
    @Override
    public ElectricityTypeSettingItemQueryVO getRuleDetail(String id) {
        ElectricityTypeSettingItemQueryVO rulesDetailQueryResponse = new ElectricityTypeSettingItemQueryVO();

        ElectricityTypeSetting rules = rulesMapper.selectById(id);
        if (ObjectUtils.isEmpty(rules)) {
            return rulesDetailQueryResponse;
        }
        BeanUtils.copyProperties(rules, rulesDetailQueryResponse);

        List<ElectricityTypeSettingItem> detailsList = ruleDetailsMapper.selectListByRuleId(id);
        if (ObjectUtils.isNotEmpty(detailsList)) {
            List<ElectricityTypeSettingItemVO> ruleDetailList = new ArrayList<>();
            for (ElectricityTypeSettingItem ruleDetails : detailsList) {
                ElectricityTypeSettingItemVO ruleDetailsResponse = new ElectricityTypeSettingItemVO();
                BeanUtils.copyProperties(ruleDetails, ruleDetailsResponse);
                ruleDetailsResponse.setTypeDesc(ElectricityTypeEnum.getNameByType(ruleDetails.getType()));
                ruleDetailList.add(ruleDetailsResponse);
            }
            rulesDetailQueryResponse.setRuleDetailList(ruleDetailList);
        }
        return rulesDetailQueryResponse;
    }

    /**
     * 修改计费策略
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRule(ElectricityTypeSettingUpdateVO request) {
        ElectricityTypeSetting rules = rulesMapper.selectById(request.getId());
        if (ObjectUtils.isEmpty(rules)) {
            throw new RuntimeException("计费规则不存在");
        }

        // 判断当前生效时间不能小于当前日期
        Date now = new Date();
        Date inputDate = request.getEffectiveDate();
        if (inputDate.before(now) || inputDate.equals(now)) {
            throw new RuntimeException("传入时间早于或等于当前时间: " + inputDate);
        }

        List<ElectricityTypeSettingItemUpdateVO> ruleDetailList = request.getRuleDetailList();

        if (ruleDetailList.size() != 48) {
            throw new RuntimeException("收费时间段必须是48条");
        }

        BeanUtils.copyProperties(request, rules);
        rulesMapper.updateRules(rules);

        for (ElectricityTypeSettingItemUpdateVO ruleDetailsRequest : ruleDetailList) {
            ElectricityTypeSettingItem ruleDetails = new ElectricityTypeSettingItem();

            ruleDetails.setId(ruleDetailsRequest.getId());
            ruleDetails.setType(ruleDetailsRequest.getType());
            if (ObjectUtils.isNotEmpty(ruleDetailsRequest.getRemark())) {
                ruleDetails.setRemark(ruleDetailsRequest.getRemark());
            }
            ruleDetailsMapper.updateRuleDetails(ruleDetails);
        }
    }

    /**
     * 新增计费策略
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRule(ElectricityTypeSettingAddVO request) {
        Date effectiveDate = request.getEffectiveDate();
        Date now = new Date();
        if (effectiveDate.before(now) || effectiveDate.equals(now)) {
            throw new RuntimeException("传入时间早于或等于当前时间");
        }

        List<ElectricityTypeSettingItemAddVO> ruleDetailList = request.getRuleDetailList();

        if (ruleDetailList.size() != 48) {
            throw new RuntimeException("收费时间段必须是48条!");
        }
        List<Integer> timePeriodList = ruleDetailList.stream().distinct().map(ElectricityTypeSettingItemAddVO::getTimePeriod).collect(Collectors.toList());
        if (timePeriodList.size() != ruleDetailList.size()) {
            throw new RuntimeException("收费时间段不能重复!");
        }
        Date createTime = new Date();

        ElectricityTypeSetting rules = new ElectricityTypeSetting();
        BeanUtils.copyProperties(request, rules);
        rules.setId(UUID.randomUUID().toString());
        rules.setCreateTime(createTime);
        rulesMapper.insertRules(rules);

        for (ElectricityTypeSettingItemAddVO ruleDetailsAddRequest : ruleDetailList) {
            ElectricityTypeSettingItem ruleDetails = new ElectricityTypeSettingItem();
            ruleDetails.setRuleId(rules.getId());
            BeanUtils.copyProperties(ruleDetailsAddRequest, ruleDetails);
            ruleDetails.setId(UUID.randomUUID().toString());
            ruleDetails.setCreateTime(createTime);
            ruleDetailsMapper.insertRuleDetails(ruleDetails);
        }
    }

    /**
     * 删除计费策略
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delRule(String id) {
        ElectricityTypeSetting rules = rulesMapper.selectById(id);
        if (ObjectUtils.isEmpty(rules)) {
            throw new RuntimeException("计费规则不存在");
        }

        rulesMapper.deleteRulesById(id);
        ruleDetailsMapper.deleteRulesByRuleId(id);
    }
}
