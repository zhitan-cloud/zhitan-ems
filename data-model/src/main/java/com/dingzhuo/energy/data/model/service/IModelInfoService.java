package com.dingzhuo.energy.data.model.service;

import com.dingzhuo.energy.data.model.domain.ModelInfo;
import com.dingzhuo.energy.data.model.domain.vo.PointDataVO;

import java.util.List;

/**
 * 模型Service接口
 * 
 * @author fanxinfu
 * @date 2020-02-17
 */
public interface IModelInfoService 
{
    /**
     * 查询模型
     * 
     * @param modelCode 模型ID
     * @return 模型
     */
    public ModelInfo selectModelInfoById(String modelCode);

    /**
     * 查询模型列表
     * 
     * @param modelInfo 模型
     * @return 模型集合
     */
    public List<ModelInfo> selectModelInfoList(ModelInfo modelInfo);

    /**
     * 新增模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    public int insertModelInfo(ModelInfo modelInfo);

    /**
     * 修改模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    public int updateModelInfo(ModelInfo modelInfo);

    /**
     * 批量删除模型
     * 
     * @param modelCode 需要删除的模型ID
     * @return 结果
     */
    public int deleteModelInfoByCode(String modelCode);

    /**
     * 删除模型信息
     * 
     * @param modelCode 模型ID
     * @return 结果
     */
    public int deleteModelInfoById(String modelCode);

    /**
     * 根据模型id查询对应点位信息
     *
     * @param modelId 查询模型id
     * @return
     */
    List<PointDataVO> listEnergyIndexByModelId(String modelId);
}
