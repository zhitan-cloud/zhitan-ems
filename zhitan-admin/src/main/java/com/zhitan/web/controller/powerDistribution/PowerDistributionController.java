package com.zhitan.web.controller.powerDistribution;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.services.IMeterImplementService;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.powerDistribution.domain.PowerDistribution;
import com.zhitan.powerDistribution.services.IPowerDistributionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/powerDistribution")
public class PowerDistributionController extends BaseController {

    @Resource
    private IPowerDistributionService powerDistributionService;

    @Resource
    private IMeterImplementService meterImplementService;

    /**
     * 配电室列表
     *
     * @param powerDistribution
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list(PowerDistribution powerDistribution) {
        List<PowerDistribution> list = powerDistributionService.selectPowerDistributionList(powerDistribution);
        return AjaxResult.success(list);
    }

    /**
     * 配电室列表分页查询
     *
     * @param powerDistribution
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public AjaxResult pageList(PowerDistribution powerDistribution,
                               @RequestParam(required = false, defaultValue = "1") int pageNo,
                               @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Page<PowerDistribution> page = new Page<>(pageNo, pageSize);
        IPage<PowerDistribution> pageList = powerDistributionService.pageList(powerDistribution, page);
        return AjaxResult.success(pageList);
    }

    /**
     * 查看配电室详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetail")
    public AjaxResult getDetail(@RequestParam("id") String id) {
        PowerDistribution powerDistribution = powerDistributionService.getById(id);
        return AjaxResult.success(powerDistribution);
    }

    /**
     * 新增配电室
     *
     * @param powerDistribution
     * @return
     */
    @PostMapping("/add")
    public AjaxResult addPowerDistribution(@RequestBody PowerDistribution powerDistribution) {
        List<PowerDistribution> checkList = powerDistributionService.list(new QueryWrapper<PowerDistribution>().eq("code", powerDistribution.getCode()));
        if (CollectionUtils.isNotEmpty(checkList)) {
            return AjaxResult.error("配电室编码不能重复，请重新输入");
        }
        return AjaxResult.success(powerDistributionService.save(powerDistribution));
    }

    /**
     * 编辑配电室
     *
     * @param powerDistribution
     * @return
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody PowerDistribution powerDistribution) {
        List<PowerDistribution> checkList = powerDistributionService.list(new QueryWrapper<PowerDistribution>().ne("id", powerDistribution.getId()).eq("code", powerDistribution.getCode()));
        if (CollectionUtils.isNotEmpty(checkList)) {
            return AjaxResult.error("配电室编码不能重复，请重新输入");
        }
        return AjaxResult.success(powerDistributionService.updateById(powerDistribution));
    }

    /**
     * 删除配电室
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/remove")
    public AjaxResult remove(@RequestParam("ids") String[] ids) {
        List<String> idList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        for (String id : ids) {
            List<MeterImplement> meterList = meterImplementService.listMeterImplByInstallLocation(id);
            if (CollectionUtils.isEmpty(meterList)) {
                idList.add(id);
            } else {
                errorList.add(id);
            }
        }
        if (CollectionUtils.isNotEmpty(errorList)) {
            return AjaxResult.error("此配电室已在计量器具中配置，不能删除");
        } else {
            return AjaxResult.success(powerDistributionService.deleteByIds(idList));
        }
    }

}
