package com.zhitan.powerDistribution.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.powerDistribution.domain.PowerDistribution;

import java.util.List;

public interface IPowerDistributionService extends IService<PowerDistribution> {
    List<PowerDistribution> selectPowerDistributionList(PowerDistribution powerDistribution);

    IPage<PowerDistribution> pageList(PowerDistribution powerDistribution, Page<PowerDistribution> page);

    int deleteByIds(List<String> ids);
}
