package com.zhitan.spikesandvalleys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.zhitan.alarm.domain.LimitType;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysItem;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysScheme;
import com.zhitan.spikesandvalleys.domain.vo.SpikesAndValleysSchemeVo;
import com.zhitan.spikesandvalleys.mapper.SpikesAndValleysItemMapper;
import com.zhitan.spikesandvalleys.mapper.SpikesAndValleysSchemeMapper;
import com.zhitan.spikesandvalleys.service.ISpikesAndValleysSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 尖峰平谷时间段明细Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-10-29
 */
@Service
public class SpikesAndValleysSchemeServiceImpl extends ServiceImpl<SpikesAndValleysSchemeMapper, SpikesAndValleysScheme> implements ISpikesAndValleysSchemeService {
    @Autowired
    private SpikesAndValleysSchemeMapper spikesAndValleysSchemeMapper;
    @Autowired
    private SpikesAndValleysItemMapper spikesAndValleysItemMapper;
    /**
     * 查询尖峰平谷时间段明细
     *
     * @param id 尖峰平谷时间段明细主键
     * @return 尖峰平谷时间段明细
     */
    @Override
    public SpikesAndValleysSchemeVo selectSpikesAndValleysSchemeById(String id) {
        SpikesAndValleysSchemeVo result = new SpikesAndValleysSchemeVo();
        SpikesAndValleysScheme info = spikesAndValleysSchemeMapper.selectSpikesAndValleysSchemeById(id);
        BeanUtil.copyProperties(info,result);
        LambdaQueryWrapper<SpikesAndValleysItem> queryWrapperItem = new LambdaQueryWrapper<>();
        queryWrapperItem.eq(SpikesAndValleysItem::getSchemeId,id);
        List<SpikesAndValleysItem> itemListOld = spikesAndValleysItemMapper.selectList(queryWrapperItem);
        result.setItemList(itemListOld);
        return result ;
    }

    /**
     * 查询尖峰平谷时间段明细列表
     *
     * @param spikesAndValleysScheme 尖峰平谷时间段明细
     * @return 尖峰平谷时间段明细
     */
    @Override
    public Page<SpikesAndValleysSchemeVo>  selectSpikesAndValleysSchemeList(SpikesAndValleysScheme spikesAndValleysScheme,Long pageNum, Long pageSize) {

        LambdaQueryWrapper<SpikesAndValleysScheme> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(spikesAndValleysScheme.getSchemeName()),SpikesAndValleysScheme::getSchemeName,spikesAndValleysScheme.getSchemeName());
        queryWrapper.eq(StringUtils.isNotEmpty(spikesAndValleysScheme.getType()),SpikesAndValleysScheme::getType,spikesAndValleysScheme.getType());
        queryWrapper.orderByDesc(SpikesAndValleysScheme::getCreateTime);
        Page<SpikesAndValleysScheme> page = spikesAndValleysSchemeMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
            IPage aa =  page.convert(s->{
            SpikesAndValleysSchemeVo v = new SpikesAndValleysSchemeVo();
            BeanUtil.copyProperties(s, v);
                LambdaQueryWrapper<SpikesAndValleysItem> queryWrapperItem = new LambdaQueryWrapper<>();
                queryWrapperItem.eq(SpikesAndValleysItem::getSchemeId,s.getId());
                List<SpikesAndValleysItem> itemListOld = spikesAndValleysItemMapper.selectList(queryWrapperItem);
                v.setItemList(itemListOld);
            return v;
        });

    return (Page<SpikesAndValleysSchemeVo>) aa;
    }

    /**
     * 新增尖峰平谷时间段明细
     *
     * @param spikesAndValleysSchemeVo 尖峰平谷时间段明细
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSpikesAndValleysScheme(SpikesAndValleysSchemeVo spikesAndValleysSchemeVo) {
        spikesAndValleysSchemeVo.setCreateTime(DateUtils.getNowDate());
        spikesAndValleysSchemeVo.setId(UUID.randomUUID().toString());
        SpikesAndValleysScheme spikesAndValleysScheme = new SpikesAndValleysScheme();
        BeanUtil.copyProperties(spikesAndValleysSchemeVo,spikesAndValleysScheme);
       int result =  spikesAndValleysSchemeMapper.insert(spikesAndValleysScheme);
        //插入子表
        List<SpikesAndValleysItem> itemList =  spikesAndValleysSchemeVo.getItemList();
        itemList.stream().forEach(s->{
            s.setId(UUID.randomUUID().toString());
            s.setCreateTime(DateUtils.getNowDate());
            s.setCreateBy(spikesAndValleysSchemeVo.getCreateBy());
            s.setSchemeId(spikesAndValleysScheme.getId());
            spikesAndValleysItemMapper.insert(s);
        });

        return result;
    }

    /**
     * 修改尖峰平谷时间段明细
     *
     * @param spikesAndValleysSchemeVo 尖峰平谷时间段明细
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSpikesAndValleysScheme(SpikesAndValleysSchemeVo spikesAndValleysSchemeVo) {
        spikesAndValleysSchemeVo.setUpdateTime(DateUtils.getNowDate());
        SpikesAndValleysScheme spikesAndValleysScheme = new SpikesAndValleysScheme();
        BeanUtil.copyProperties(spikesAndValleysSchemeVo,spikesAndValleysScheme);
        int result = spikesAndValleysSchemeMapper.updateSpikesAndValleysScheme(spikesAndValleysScheme);
        //删除子表
        LambdaQueryWrapper<SpikesAndValleysItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SpikesAndValleysItem::getSchemeId,spikesAndValleysScheme.getId());
        List<SpikesAndValleysItem> itemListOld = spikesAndValleysItemMapper.selectList(queryWrapper);
        List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
        String[] ids = idList.toArray(new String[idList.size()]);
        spikesAndValleysItemMapper.deleteSpikesAndValleysItemByIds(ids);
        //插入子表
        List<SpikesAndValleysItem> itemList =  spikesAndValleysSchemeVo.getItemList();
        itemList.stream().forEach(s->{
            s.setId(UUID.randomUUID().toString());
            s.setCreateTime(DateUtils.getNowDate());
            s.setCreateBy(spikesAndValleysSchemeVo.getCreateBy());
            s.setSchemeId(spikesAndValleysScheme.getId());
            spikesAndValleysItemMapper.insert(s);
        });
        return result;
    }

    /**
     * 批量删除尖峰平谷时间段明细
     *
     * @param ids 需要删除的尖峰平谷时间段明细主键
     * @return 结果
     */
    @Override
    public int deleteSpikesAndValleysSchemeByIds(String[] ids) {
        //删除子表
        List<String> list = Arrays.asList(ids);
        list.stream().forEach(item->{
            LambdaQueryWrapper<SpikesAndValleysItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SpikesAndValleysItem::getSchemeId,item);
            List<SpikesAndValleysItem> itemListOld = spikesAndValleysItemMapper.selectList(queryWrapper);
            List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
            String[] itemIds = idList.toArray(new String[idList.size()]);
            spikesAndValleysItemMapper.deleteSpikesAndValleysItemByIds(itemIds);
        });

        return spikesAndValleysSchemeMapper.deleteSpikesAndValleysSchemeByIds(ids);
    }

    /**
     * 删除尖峰平谷时间段明细信息
     *
     * @param id 尖峰平谷时间段明细主键
     * @return 结果
     */
    @Override
    public int deleteSpikesAndValleysSchemeById(String id) {
        return spikesAndValleysSchemeMapper.deleteSpikesAndValleysSchemeById(id);
    }
}
