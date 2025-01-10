package com.zhitan.knowledgeBase.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.constant.MessageConstant;
import com.zhitan.common.exception.ServiceException;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBaseAddDTO;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBaseEditDTO;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBasePageDTO;
import com.zhitan.knowledgeBase.domain.entity.KnowledgeBase;
import com.zhitan.knowledgeBase.domain.entity.KnowledgeBaseFile;
import com.zhitan.knowledgeBase.domain.enums.EnergyTypeEnum;
import com.zhitan.knowledgeBase.domain.vo.KnowledgeBaseDetailVO;
import com.zhitan.knowledgeBase.domain.vo.KnowledgeBasePageVO;
import com.zhitan.knowledgeBase.mapper.KnowledgeBaseFileMapper;
import com.zhitan.knowledgeBase.mapper.KnowledgeBaseMapper;
import com.zhitan.knowledgeBase.service.IKnowledgeBaseService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库(KnowledgeBase)表服务实现类
 *
 * @author makejava
 * @since 2025-01-10 15:05:28
 */
@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements IKnowledgeBaseService {

    @Resource
    private KnowledgeBaseFileMapper fileMapper;


    /**
     * 知识库-列表查询
     */
    public Page<KnowledgeBasePageVO> getPageList(KnowledgeBasePageDTO pageDTO) {
        Page<KnowledgeBasePageVO> voPage = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        // 分页查询
        Page<KnowledgeBase> knowledgeBasePage = baseMapper.selectPage(new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize()),
                Wrappers.<KnowledgeBase>lambdaQuery()
                        .like(ObjectUtils.isNotEmpty(pageDTO.getTitle()), KnowledgeBase::getTitle, pageDTO.getTitle())
                        .eq(ObjectUtils.isNotEmpty(pageDTO.getType()), KnowledgeBase::getType, pageDTO.getType())
                        .eq(KnowledgeBase::getDelFlag, false).orderByDesc(KnowledgeBase::getCreateTime));

        List<KnowledgeBase> records = knowledgeBasePage.getRecords();
        if (records.isEmpty()){
            return voPage;
        }
        // 数据转换
        voPage.setRecords(records.stream().map(item -> {
            KnowledgeBasePageVO pageVO = new KnowledgeBasePageVO();
            BeanUtil.copyProperties(item, pageVO);
            pageVO.setTypeDesc(EnergyTypeEnum.getDescByCode(item.getType()));
            return pageVO;
        }).collect(Collectors.toList()));
        voPage.setTotal(knowledgeBasePage.getTotal());
        return voPage;
    }

    /**
     * 知识库-查询详情
     */
    public KnowledgeBaseDetailVO getDetail(Long id) {
        KnowledgeBaseDetailVO vo = new KnowledgeBaseDetailVO();

        // 查询数据
        KnowledgeBase knowledgeBase = baseMapper.selectById(id);
        if (knowledgeBase != null){
            // 数据转换
            BeanUtil.copyProperties(knowledgeBase, vo);
            vo.setUrl(fileMapper.selectList(Wrappers.<KnowledgeBaseFile>lambdaQuery()
                    .select(KnowledgeBaseFile::getUrl)
                    .eq(KnowledgeBaseFile::getKnowledgeBaseId, id))
                    .stream().map(KnowledgeBaseFile::getUrl).collect(Collectors.toList()));
        }
        return vo;
    }

    /**
     * 知识库-新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(KnowledgeBaseAddDTO addDTO) {
        // 插入数据
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        BeanUtil.copyProperties(addDTO, knowledgeBase);
        baseMapper.insert(knowledgeBase);

        // 插入文件
        if (ObjectUtils.isNotEmpty(addDTO.getUrl())){
            addDTO.getUrl().forEach(url->{
                KnowledgeBaseFile file = new KnowledgeBaseFile();
                file.setKnowledgeBaseId(knowledgeBase.getId());
                file.setUrl(url);
                fileMapper.insert(file);
            });
        }
    }

    /**
     * 知识库-更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void edit(KnowledgeBaseEditDTO editDTO) {
        // 查询数据
        KnowledgeBase knowledgeBase = baseMapper.selectById(editDTO.getId());
        if (ObjectUtils.isEmpty(knowledgeBase)){
            throw new ServiceException(MessageConstant.KNOWLEDGE_BASE_NOT_EXIST);
        }
        // 更新数据
        BeanUtils.copyProperties(editDTO, knowledgeBase);
        baseMapper.updateById(knowledgeBase);

        // 删除文件
        fileMapper.delete(Wrappers.<KnowledgeBaseFile>lambdaQuery()
                .eq(KnowledgeBaseFile::getKnowledgeBaseId, knowledgeBase.getId()));

        // 插入文件
        if (ObjectUtils.isNotEmpty(editDTO.getUrl())){
            editDTO.getUrl().forEach(url->{
                KnowledgeBaseFile file = new KnowledgeBaseFile();
                file.setKnowledgeBaseId(knowledgeBase.getId());
                file.setUrl(url);
                fileMapper.insert(file);
            });
        }
    }

    /**
     * 知识库-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 查询数据
        KnowledgeBase knowledgeBase = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(knowledgeBase)){
            throw new ServiceException(MessageConstant.KNOWLEDGE_BASE_NOT_EXIST);
        }
        // 删除数据
        knowledgeBase.setDelFlag(true);
        baseMapper.updateById(knowledgeBase);

        // 删除文件
        fileMapper.delete(Wrappers.<KnowledgeBaseFile>lambdaQuery()
                .eq(KnowledgeBaseFile::getKnowledgeBaseId, id));
    }
}
