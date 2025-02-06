package com.zhitan.saving.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.exception.ServiceException;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.PageUtils;
import com.zhitan.common.utils.bean.BeanUtils;
import com.zhitan.saving.domain.dto.EnergySavingProgramDTO;
import com.zhitan.saving.domain.entity.EnergySavingProgram;
import com.zhitan.saving.domain.vo.EnergySavingProgramVO;
import com.zhitan.saving.mapper.EnergySavingProgramMapper;
import com.zhitan.saving.service.IEnergySavingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 节能项目管理
 * Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-12-26
 */
@Service
public class EnergySavingProgramServiceImpl extends ServiceImpl<EnergySavingProgramMapper, EnergySavingProgram> implements IEnergySavingProgramService {
    @Resource
    private EnergySavingProgramMapper energySavingProgramMapper;

    /**
     * 查询
     * 节能项目管理
     *
     * @param id 节能项目管理
     *           主键
     * @return 节能项目管理
     */
    @Override
    public   EnergySavingProgram selectEnergySavingProgramById(Long id) {
        return energySavingProgramMapper.selectEnergySavingProgramById(id);
    }

    /**
     * 查询
     * 节能项目管理
     * 列表
     *
     * @param energySavingProgram 节能项目管理
     * @return 节能项目管理
     */
    @Override
    public Page<EnergySavingProgramVO> selectEnergySavingProgramList(  EnergySavingProgram energySavingProgram) {

        Page<  EnergySavingProgram> pageInfo = PageUtils.getPageInfo(  EnergySavingProgram.class);


        Page<  EnergySavingProgram> energySavingProgramPage = this.baseMapper.selectPage(pageInfo,
                new LambdaQueryWrapper<  EnergySavingProgram>().eq(EnergySavingProgram::getDel, CommonConst.DEL_FLAG_0)
//                        .eq(ObjectUtils.isNotEmpty(energySavingProgram.getEndTime()), EnergySavingProgram::getEndTime, energySavingProgram.getEndTime())
                        .orderByDesc(EnergySavingProgram::getCreateTime)
        );

        List<  EnergySavingProgram> energySavingProgramList = energySavingProgramPage.getRecords();

        List<EnergySavingProgramVO> voList = new ArrayList<>();
        energySavingProgramList.stream().forEach(indexInfo -> {
            EnergySavingProgramVO infoVO = new EnergySavingProgramVO();
            BeanUtils.copyProperties(indexInfo, infoVO);
            voList.add(infoVO);
        });

        Page<EnergySavingProgramVO> responsePage = PageUtils.getPageInfo(EnergySavingProgramVO.class);
        responsePage.setTotal(energySavingProgramPage.getTotal());
        responsePage.setRecords(voList);
        return responsePage;
    }

    /**
     * 新增
     * 节能项目管理
     *
     * @param dto
     * @return 结果
     * @throws ParseException
     */
    @Override
    @Transactional
    public AjaxResult insertEnergySavingProgram(EnergySavingProgramDTO dto) {
              EnergySavingProgram energySavingProgram = new   EnergySavingProgram();
            BeanUtils.copyProperties(dto, energySavingProgram);
            energySavingProgram.setCreateTime(DateUtils.getNowDate());
            energySavingProgram.setDel(0L);
            return AjaxResult.success(energySavingProgramMapper.insert(energySavingProgram));

    }


    /**
     * 修改
     * 节能项目管理
     *
     * @param dto
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult updateEnergySavingProgram(EnergySavingProgramDTO dto) {
          EnergySavingProgram vo = this.baseMapper.selectEnergySavingProgramById(dto.getId());
        if (vo == null) {
            throw new ServiceException("未找到对应实体");
        } else {
            BeanUtils.copyProperties(dto, vo);
            vo.setDel(CommonConst.DEL_FLAG_0.longValue());
            int i = energySavingProgramMapper.updateById(vo);
            return AjaxResult.success(i);
        }


    }

    /**
     * 删除
     * 节能项目管理
     * 信息
     *
     * @param id 节能项目管理
     *           主键
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult deleteEnergySavingProgramById(Long id) {

          EnergySavingProgram vo = this.baseMapper.selectEnergySavingProgramById(id);
        if (vo == null) {
            throw new ServiceException("未找到对应实体");
        } else {
            vo.setUpdateTime(DateUtils.getNowDate());
            vo.setDel(CommonConst.DEL_FLAG_1.longValue());
            int i = energySavingProgramMapper.updateById(vo);
            return AjaxResult.success(i);
        }

    }

}
