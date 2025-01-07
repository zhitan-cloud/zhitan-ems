package com.zhitan.productoutput.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.productoutput.domain.ProductOutput;
import com.zhitan.productoutput.mapper.ProductOutputMapper;
import com.zhitan.productoutput.services.IProductOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品产量Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-10-08
 */
@Service
public class ProductOutputServiceImpl extends ServiceImpl<ProductOutputMapper, ProductOutput> implements IProductOutputService {
    @Autowired
    private ProductOutputMapper productOutputMapper;

    /**
     * 查询产品产量
     *
     * @param nodeId 产品产量主键
     * @return 产品产量
     */
    @Override
    public ProductOutput selectProductOutputById(String nodeId) {
        return productOutputMapper.selectProductOutputById(nodeId);
    }

    /**
     * 查询产品产量列表
     *
     * @param productOutput 产品产量
     * @return 产品产量
     */
    @Override
    public List<ProductOutput> selectProductOutputList(ProductOutput productOutput) {
        return productOutputMapper.selectProductOutputList(productOutput);
    }

    /**
     * 新增产品产量
     *
     * @param productOutput 产品产量
     * @return 结果
     */
    @Override
    public int insertProductOutput(ProductOutput productOutput) {
        productOutput.setCreateTime(DateUtils.getNowDate());
        return productOutputMapper.insertProductOutput(productOutput);
    }

    /**
     * 修改产品产量
     *
     * @param productOutput 产品产量
     * @return 结果
     */
    @Override
    public int updateProductOutput(ProductOutput productOutput) {
                productOutput.setUpdateTime(DateUtils.getNowDate());
        return productOutputMapper.updateProductOutput(productOutput);
    }

    /**
     * 批量删除产品产量
     *
     * @param nodeIds 需要删除的产品产量主键
     * @return 结果
     */
    @Override
    public int deleteProductOutputByIds(String[] nodeIds) {
        return productOutputMapper.deleteProductOutputByIds(nodeIds);
    }

    /**
     * 删除产品产量信息
     *
     * @param nodeId 产品产量主键
     * @return 结果
     */
    @Override
    public int deleteProductOutputById(String nodeId) {
        return productOutputMapper.deleteProductOutputById(nodeId);
    }

    @Override
    public Page<ProductOutput> selectProductOutputPage(ProductOutput productOutput, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<ProductOutput> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(productOutput.getName()),ProductOutput::getName,productOutput.getName());
        wrapper.between(StringUtils.isNotEmpty(productOutput.getBeginTime()),ProductOutput::getDataTime,productOutput.getBeginTime(),productOutput.getEndTime());
        wrapper.eq(StringUtils.isNotEmpty(productOutput.getDataType()),ProductOutput::getDataType,productOutput.getDataType());
        wrapper.like(StringUtils.isNotEmpty(productOutput.getDataTime()),ProductOutput::getDataTime,productOutput.getDataTime());
        wrapper.eq(StringUtils.isNotEmpty(productOutput.getProductType()),ProductOutput::getProductType,productOutput.getProductType());
        wrapper.eq(StringUtils.isNotEmpty(productOutput.getNodeId()),ProductOutput::getNodeId,productOutput.getNodeId());
        wrapper.orderByDesc(ProductOutput::getCreateTime);
        Page<ProductOutput> page = productOutputMapper.selectPage(new Page<ProductOutput>(pageNum, pageSize), wrapper);
        return page;
    }
}
