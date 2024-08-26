package com.dingzhuo.energy.data.model.mapper;

import com.dingzhuo.energy.data.model.domain.ModelInfo;
import java.util.List;

/**
 * 模型Mapper接口
 * 
 * @author fanxinfu
 * @date 2020-02-17
 */
public interface ModelInfoMapper 
{
    /**
     * 查询模型
     * 
     * @param modelCode 模型ID
     * @return 模型
     */
    ModelInfo selectModelInfoById(String modelCode);

    /**
     * 查询模型列表
     * 
     * @param modelInfo 模型
     * @return 模型集合
     */
    List<ModelInfo> selectModelInfoList(ModelInfo modelInfo);

    /**
     * 新增模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    int insertModelInfo(ModelInfo modelInfo);

    /**
     * 修改模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    int updateModelInfo(ModelInfo modelInfo);

    /**
     * 删除模型
     * 
     * @param modelCode 模型ID
     * @return 结果
     */
    int deleteModelInfoById(String modelCode);

    /**
     * 批量删除模型
     * 
     * @param modelCodes 需要删除的数据ID
     * @return 结果
     */
    int deleteModelInfoByCode(String modelCodes);
}
