package com.zhitan.knowledgeBase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBaseAddDTO;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBaseEditDTO;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBasePageDTO;
import com.zhitan.knowledgeBase.domain.entity.KnowledgeBase;
import com.zhitan.knowledgeBase.domain.vo.KnowledgeBaseDetailVO;
import com.zhitan.knowledgeBase.domain.vo.KnowledgeBasePageVO;

/**
 * 知识库(KnowledgeBase)表服务接口
 *
 * @author makejava
 * @since 2025-01-10 15:05:27
 */
public interface IKnowledgeBaseService extends IService<KnowledgeBase> {

    /**
     * 知识库-列表查询
     */
    Page<KnowledgeBasePageVO> getPageList(KnowledgeBasePageDTO pageDTO);

    /**
     * 知识库-查询详情
     */
    KnowledgeBaseDetailVO getDetail(Long id);

    /**
     * 知识库-新增
     */
    void add(KnowledgeBaseAddDTO addDTO);

    /**
     * 知识库-更新
     */
    void edit(KnowledgeBaseEditDTO editDTO);

    /**
     * 知识库-删除
     */
    void delete(Long id);
}
