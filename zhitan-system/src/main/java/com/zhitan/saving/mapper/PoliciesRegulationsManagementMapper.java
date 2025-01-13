package com.zhitan.saving.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.annotation.DataSource;
import com.zhitan.common.enums.DataSourceType;
import com.zhitan.saving.domain.dto.PoliciesRegulationsManagementPageDTO;
import com.zhitan.saving.domain.entity.PoliciesRegulationsManagement;
import com.zhitan.saving.domain.vo.PoliciesRegulationsManagementPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 政策法规Mapper接口
 *
 * @author ZhiTan
 * @date 2024-12-26
 */
@Mapper
@DataSource(value = DataSourceType.MASTER)
public interface PoliciesRegulationsManagementMapper extends BaseMapper<PoliciesRegulationsManagement> {


    /**
     * 政策法规-列表分页查询
     */
    Page<PoliciesRegulationsManagementPageVO> getPageList(Page<PoliciesRegulationsManagementPageVO> pageInfo, @Param("dto") PoliciesRegulationsManagementPageDTO pageDTO);


}
