package com.zhitan.saving.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.saving.domain.entity.PoliciesRegulationsManagement;
import com.zhitan.saving.domain.dto.PoliciesRegulationsDTO;
import com.zhitan.saving.domain.dto.PoliciesRegulationsManagementPageDTO;
import com.zhitan.saving.domain.vo.PoliciesRegulationsManagementDetailVO;
import com.zhitan.saving.domain.vo.PoliciesRegulationsManagementPageVO;

/**
 * 政策法规Service接口
 *
 * @author ZhiTan
 * @date 2024-12-26
 */
public interface IPoliciesRegulationsManagementService extends IService<PoliciesRegulationsManagement> {

    /**
     * 政策法规-列表查询
     */
    Page<PoliciesRegulationsManagementPageVO> getPageList(PoliciesRegulationsManagementPageDTO pageDTO);

    /**
     * 政策法规-查询详情
     */
    PoliciesRegulationsManagementDetailVO getDetail(Long id);

    /**
     * 政策法规-新增
     */
    void add(PoliciesRegulationsDTO addDTO);

    /**
     * 政策法规-更新
     */
    void edit(PoliciesRegulationsDTO editDTO);

    /**
     * 政策法规-删除
     */
    void delete(Long id);
}
