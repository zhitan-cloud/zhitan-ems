package com.zhitan.model.domain.vo;


import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 模型节点与点位关系对象
 *
 * @Author: Zhujw
 * @Date: 2023/3/2
 */
@Data
public class ModelNodeIndexInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型编码
     */
    private String modelCode;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点编码
     */
    private String nodeCode;
    /**
     * 名称
     */
    private String nodeName;

    /**
     * 点位id
     */
    private String indexId;
    /**
     * 点位名称
     */
    private String indexName;
    /**
     * 点位编码
     */
    private String indexCode;

    /**
     * 能源类型
     */
    private String energyId;
    /**
     * 点位类型
     */
    private String indexType;

}
