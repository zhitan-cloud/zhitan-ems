package com.zhitan.costmanagement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.costmanagement.domain.CostPriceTacticsItem;
import com.zhitan.costmanagement.domain.vo.CostPriceTacticsVo;
import com.zhitan.costmanagement.mapper.CostPriceTacticsItemMapper;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysItem;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysScheme;
import com.zhitan.spikesandvalleys.domain.vo.SpikesAndValleysSchemeVo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhitan.costmanagement.mapper.CostPriceTacticsMapper;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.zhitan.costmanagement.service.ICostPriceTacticsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 成本策略Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@Service
public class CostPriceTacticsServiceImpl extends ServiceImpl<CostPriceTacticsMapper, CostPriceTactics> implements ICostPriceTacticsService {
    @Autowired
    private CostPriceTacticsMapper costPriceTacticsMapper;
    @Autowired
    private CostPriceTacticsItemMapper costPriceTacticsItemMapper;


    /**
     * 查询成本策略
     *
     * @param id 成本策略主键
     * @return 成本策略
     */
    @Override
    public CostPriceTactics selectCostPriceTacticsById(String id) {
        CostPriceTacticsVo result = new CostPriceTacticsVo();
        CostPriceTactics info = costPriceTacticsMapper.selectCostPriceTacticsById(id);
        BeanUtil.copyProperties(info,result);
        LambdaQueryWrapper<CostPriceTacticsItem> queryWrapperItem = new LambdaQueryWrapper<>();
        queryWrapperItem.eq(CostPriceTacticsItem::getTacticsId,id);
        List<CostPriceTacticsItem> itemListOld = costPriceTacticsItemMapper.selectList(queryWrapperItem);
        result.setItemList(itemListOld);
        return result ;
    }

    /**
     * 查询成本策略列表
     *
     * @param costPriceTactics 成本策略
     * @return 成本策略
     */
    @Override
    public Page<CostPriceTacticsVo> selectCostPriceTacticsList(CostPriceTactics costPriceTactics, Long pageNum, Long pageSize) {

        LambdaQueryWrapper<CostPriceTactics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(costPriceTactics.getTacticsName()),CostPriceTactics::getTacticsName,costPriceTactics.getTacticsName());
        queryWrapper.like(StringUtils.isNotEmpty(costPriceTactics.getTacticsNumber()),CostPriceTactics::getTacticsNumber,costPriceTactics.getTacticsNumber());

        queryWrapper.eq(StringUtils.isNotEmpty(costPriceTactics.getType()),CostPriceTactics::getType,costPriceTactics.getType());
        queryWrapper.orderByDesc(CostPriceTactics::getCreateTime);
        Page<CostPriceTactics> page = costPriceTacticsMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
        IPage aa =  page.convert(s->{
            CostPriceTacticsVo v = new CostPriceTacticsVo();
            BeanUtil.copyProperties(s, v);
            LambdaQueryWrapper<CostPriceTacticsItem> queryWrapperItem = new LambdaQueryWrapper<>();
            queryWrapperItem.eq(CostPriceTacticsItem::getTacticsId,s.getId());
            List<CostPriceTacticsItem> itemListOld = costPriceTacticsItemMapper.selectList(queryWrapperItem);
            v.setItemList(itemListOld);
            return v;
        });

        return (Page<CostPriceTacticsVo>) aa;
    }

    @Override
    public List<CostPriceTacticsVo> selectCostPriceTacticsListAll() {
        return costPriceTacticsMapper.selectCostPriceTacticsListAll();
    }

    /**
     * 新增成本策略
     *
     * @param costPriceTacticsVo 成本策略
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCostPriceTactics(CostPriceTacticsVo costPriceTacticsVo) throws Exception {

        CostPriceTactics search = costPriceTacticsMapper.selectOne(new QueryWrapper<CostPriceTactics>().eq("tactics_number",costPriceTacticsVo.getTacticsNumber()));
        if (search!=null){
            throw new Exception("该策略编码已存在！");
        }
        costPriceTacticsVo.setCreateTime(DateUtils.getNowDate());
        costPriceTacticsVo.setId(UUID.randomUUID().toString());
        CostPriceTactics costPriceTactics = new CostPriceTactics();
        BeanUtil.copyProperties(costPriceTacticsVo,costPriceTactics);
        int result =  costPriceTacticsMapper.insert(costPriceTactics);
        //插入子表
        List<CostPriceTacticsItem> itemList =  costPriceTacticsVo.getItemList();
        itemList.stream().forEach(s->{
            s.setId(UUID.randomUUID().toString());
            s.setCreateTime(DateUtils.getNowDate());
            s.setCreateBy(costPriceTacticsVo.getCreateBy());
            s.setTacticsId(costPriceTacticsVo.getId());
            costPriceTacticsItemMapper.insert(s);
        });

        return result;
    }

    /**
     * 修改成本策略
     *
     * @param costPriceTacticsVo 成本策略
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCostPriceTactics(CostPriceTacticsVo costPriceTacticsVo) {
        costPriceTacticsVo.setUpdateTime(DateUtils.getNowDate());
        CostPriceTactics costPriceTactics = new CostPriceTactics();
        BeanUtil.copyProperties(costPriceTacticsVo,costPriceTactics);
        int result = costPriceTacticsMapper.updateCostPriceTactics(costPriceTactics);
        //删除子表
        LambdaQueryWrapper<CostPriceTacticsItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CostPriceTacticsItem::getTacticsId,costPriceTactics.getId());
        List<CostPriceTacticsItem> itemListOld = costPriceTacticsItemMapper.selectList(queryWrapper);
        List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
        String[] ids = idList.toArray(new String[idList.size()]);
        costPriceTacticsItemMapper.deleteCostPriceTacticsItemByIds(ids);
        //插入子表
        List<CostPriceTacticsItem> itemList =  costPriceTacticsVo.getItemList();
        itemList.stream().forEach(s->{
            s.setId(UUID.randomUUID().toString());
            s.setCreateTime(DateUtils.getNowDate());
            s.setCreateBy(costPriceTacticsVo.getCreateBy());
            s.setTacticsId(costPriceTacticsVo.getId());
            costPriceTacticsItemMapper.insert(s);
        });
        return result;
    }

    /**
     * 批量删除成本策略
     *
     * @param ids 需要删除的成本策略主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCostPriceTacticsByIds(String[] ids) {
        //删除子表
        List<String> list = Arrays.asList(ids);
        list.stream().forEach(item->{
            LambdaQueryWrapper<CostPriceTacticsItem> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CostPriceTacticsItem::getTacticsId,item);
            List<CostPriceTacticsItem> itemListOld = costPriceTacticsItemMapper.selectList(queryWrapper);
            List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
            String[] itemIds = idList.toArray(new String[idList.size()]);
            costPriceTacticsItemMapper.deleteCostPriceTacticsItemByIds(itemIds);
        });

        return costPriceTacticsMapper.deleteCostPriceTacticsByIds(ids);
    }

    /**
     * 删除成本策略信息
     *
     * @param id 成本策略主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCostPriceTacticsById(String id) {
        LambdaQueryWrapper<CostPriceTacticsItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CostPriceTacticsItem::getTacticsId,id);
        List<CostPriceTacticsItem> itemListOld = costPriceTacticsItemMapper.selectList(queryWrapper);
        List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
        String[] itemIds = idList.toArray(new String[idList.size()]);
        costPriceTacticsItemMapper.deleteCostPriceTacticsItemByIds(itemIds);
        return costPriceTacticsMapper.deleteCostPriceTacticsById(id);
    }
}
