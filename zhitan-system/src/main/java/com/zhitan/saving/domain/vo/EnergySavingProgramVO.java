package com.zhitan.saving.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zhitan.saving.domain.entity.EnergySavingProgram;

/**
 * 节能项目管理 VO
 * @author Geoffrey
 * @date 2025/01/13
 */
public class EnergySavingProgramVO extends EnergySavingProgram {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
}