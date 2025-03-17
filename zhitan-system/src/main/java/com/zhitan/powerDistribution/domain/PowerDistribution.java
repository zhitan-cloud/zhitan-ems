package com.zhitan.powerDistribution.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 配电室实体类
 */
@Data
@TableName("power_distribution")
public class PowerDistribution extends BaseEntity {

    @TableId
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 负责人
     */
    private String principals;

    /**
     * 负责人电话
     */
    private String principalsTel;

    private String remark;
}
