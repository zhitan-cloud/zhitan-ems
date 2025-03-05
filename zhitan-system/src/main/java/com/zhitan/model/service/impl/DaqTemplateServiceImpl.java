package com.zhitan.model.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.constant.Constants;
import com.zhitan.common.core.redis.RedisCache;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.model.domain.DaqTemplate;
import com.zhitan.model.mapper.DaqTemplateMapper;
import com.zhitan.model.service.IDaqTemplateService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 采集参数模板Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-08
 */
@Service
public class DaqTemplateServiceImpl implements IDaqTemplateService {
    @Resource
    private RedisCache cache;
    @Autowired
    private DaqTemplateMapper daqTemplateMapper;


    /**
     * 查询采集参数模板
     *
     * @param id 采集参数模板ID
     * @return 采集参数模板
     */
    @Override
    public DaqTemplate selectDaqTemplateById(String id) {
        return daqTemplateMapper.selectDaqTemplateById(id);
    }

    /**
     * 查询采集参数模板列表
     *
     * @param daqTemplate 采集参数模板
     * @return 采集参数模板
     */
    @Override
    public List<DaqTemplate> selectDaqTemplateList(DaqTemplate daqTemplate) {
        return daqTemplateMapper.selectDaqTemplateList(daqTemplate);
    }

    /**
     * 新增采集参数模板
     *
     * @param daqTemplate 采集参数模板
     * @return 结果
     */
    @Override
    public int insertDaqTemplate(DaqTemplate daqTemplate) {
        return daqTemplateMapper.insertDaqTemplate(daqTemplate);
    }

    /**
     * 修改采集参数模板
     *
     * @param daqTemplate 采集参数模板
     * @return 结果
     */
    @Override
    public int updateDaqTemplate(DaqTemplate daqTemplate) {
        return daqTemplateMapper.updateDaqTemplate(daqTemplate);
    }

    /**
     * 批量删除采集参数模板
     *
     * @param ids 需要删除的采集参数模板ID
     * @return 结果
     */
    @Override
    public int deleteDaqTemplateByIds(String[] ids) {
        return daqTemplateMapper.deleteDaqTemplateByIds(ids);
    }

    /**
     * 删除采集参数模板信息
     *
     * @param id 采集参数模板ID
     * @return 结果
     */
    @Override
    public int deleteDaqTemplateById(String id) {
        return daqTemplateMapper.deleteDaqTemplateById(id);
    }

    @Override
    public boolean dapHasExist(String code, String deviceType) {
        int count = daqTemplateMapper.dapHasExist(code, deviceType);
        return count > 0;
    }

    @Override
    public boolean dapHasExist(DaqTemplate daqTemplate) {
        int count = daqTemplateMapper.dapHasExistWhenUpdate(daqTemplate.getId(), daqTemplate.getCode(), daqTemplate.getDeviceType());
        return count > 0;
    }

    @Override
    public boolean dapCodeHasExist(String key, String deviceType) {
        int count = daqTemplateMapper.dapHasExist(key, deviceType);
        return count > 0;
    }
    @Override
    public boolean dapCodeHasExist(DaqTemplate daqTemplate) {
        int count = daqTemplateMapper.dapHasExistWhenUpdate(daqTemplate.getId(), daqTemplate.getGatewayKey(), daqTemplate.getDeviceType());
        return count > 0;
    }

    /**
     * 查询指标模板
     *
     * @return 结果
     */
    @Override
    public List<DaqTemplate> listTemplate() {
        Object cacheObject = cache.getCacheObject(Constants.DAQ_TEMPLATE_KEY);
        if (ObjectUtils.isNotEmpty(cacheObject)) {
            return JSONObject.parseArray(cacheObject.toString(), DaqTemplate.class);
        }

        List<DaqTemplate> daqTemplates = daqTemplateMapper.listTemplate();
        if (CollectionUtils.isEmpty(daqTemplates)) {
            return Collections.emptyList();
        }

        String jsonString = JSONObject.toJSONString(daqTemplates);
        cache.setCacheObject(Constants.DAQ_TEMPLATE_KEY, jsonString, 6, TimeUnit.HOURS);
        return daqTemplates;
    }

    /**
     * 查询指标模板map
     *
     * @return 结果
     */
    @Override
    public Map<String, List<DaqTemplate>> listTemplateMap() {
        Map<String, List<DaqTemplate>> map = new HashMap<>();
        List<DaqTemplate> daqTemplates = this.listTemplate();

        if (CollectionUtils.isNotEmpty(daqTemplates)) {
            return daqTemplates.stream().collect(Collectors.groupingBy(DaqTemplate::getCode));
        }
        return map;
    }

    @Override
    public Page<DaqTemplate> selectDaqTemplatePage(DaqTemplate daqTemplate, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<DaqTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(daqTemplate.getCode()),DaqTemplate::getCode,daqTemplate.getCode());
        wrapper.like(StringUtils.isNotEmpty(daqTemplate.getName()),DaqTemplate::getName,daqTemplate.getName());
        wrapper.eq(StringUtils.isNotEmpty(daqTemplate.getDeviceType()),DaqTemplate::getDeviceType,daqTemplate.getDeviceType());
        wrapper.orderByDesc(DaqTemplate::getCreateTime);
        Page<DaqTemplate> page = daqTemplateMapper.selectPage(new Page<DaqTemplate>(pageNum, pageSize), wrapper);
        return page;
    }
}
