package com.zhitan.web.controller.auxiliaryinput;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.energyIndicators.domain.EnergyIndicators;
import com.zhitan.energyIndicators.service.IEnergyIndicatorsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * 能源指标Controller
 *
 * @author ZhiTan
 * @date 2024-10-25
 */
@RestController
public class EnergyIndicatorsController extends BaseController
{
    @Resource
    private IEnergyIndicatorsService energyIndicatorsService;

    /**
     * 查询能源指标列表
     */
    @PreAuthorize("@ss.hasPermi('system:energyIndicators:list')")
    @GetMapping("/energyIndicators/list")
    public TableDataInfo energyIndicatorsPage(EnergyIndicators energyIndicators, @RequestParam Long pageNum, @RequestParam Long pageSize)
    {
//        startPage();
        Page<EnergyIndicators> list = energyIndicatorsService.selectEnergyIndicatorsPage(energyIndicators,pageNum,pageSize);
        return getDataTable(list);
    }


    /**
     * 导出能源指标列表
     */
    @PreAuthorize("@ss.hasPermi('system:energyIndicators:export')")
    @Log(title = "能源指标", businessType = BusinessType.EXPORT)
    @PostMapping("/energyIndicators/export")
    public void export(HttpServletResponse response, EnergyIndicators energyIndicators)
    {
        List<EnergyIndicators> list = energyIndicatorsService.selectEnergyIndicatorsList(energyIndicators);
        ExcelUtil<EnergyIndicators> util = new ExcelUtil<EnergyIndicators>(EnergyIndicators.class);
        util.exportExcel(response, list, "能源指标数据");
    }

    /**
     * 获取能源指标详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:energyIndicators:query')")
    @GetMapping(value = "/energyIndicators/{energyIndicatorsId}")
    public AjaxResult getInfo(@PathVariable("energyIndicatorsId") String energyIndicatorsId)
    {
        return success(energyIndicatorsService.selectEnergyIndicatorsByNodeId(energyIndicatorsId));
    }

    /**
     * 新增能源指标
     */
    @PreAuthorize("@ss.hasPermi('system:energyIndicators:add')")
    @Log(title = "能源指标", businessType = BusinessType.INSERT)
    @PostMapping(value = "/energyIndicators")
    public AjaxResult add(@RequestBody EnergyIndicators energyIndicators)
    {

        //todo 校验是否重复，如果重复就更新、如果不重复就新增.
        // 校验重复规则：nodeId+timeType+dataTime+energyType+indicatorsType
        EnergyIndicators queryParam = new EnergyIndicators();
        queryParam.setNodeId(energyIndicators.getNodeId());
        queryParam.setDataTime(energyIndicators.getDataTime());
        queryParam.setTimeType(energyIndicators.getTimeType());
        queryParam.setEnergyType(energyIndicators.getEnergyType());
        queryParam.setIndicatorsType(energyIndicators.getIndicatorsType());
        List<EnergyIndicators> list = energyIndicatorsService.selectEnergyIndicatorsList(queryParam);

        //判断查询到的数据,不为空说明已经存在：更新数据。为空说明不存在：新增
        if  (list!=null && !list.isEmpty()){
            return AjaxResult.error("当前节点已经添加过该指标类型");
        }else {
            energyIndicators.setEnergyIndicatorsId(UUID.randomUUID().toString());
            return toAjax(energyIndicatorsService.insertEnergyIndicators(energyIndicators));
        }
    }

    /**
     * 修改能源指标
     */
    @PreAuthorize("@ss.hasPermi('system:energyIndicators:edit')")
    @Log(title = "能源指标", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/energyIndicators")
    public AjaxResult edit(@RequestBody EnergyIndicators energyIndicators)
    {
        return toAjax(energyIndicatorsService.updateEnergyIndicators(energyIndicators));
    }

    /**
     * 删除能源指标
     */
    @PreAuthorize("@ss.hasPermi('system:energyIndicators:remove')")
    @Log(title = "能源指标", businessType = BusinessType.DELETE)
	@DeleteMapping("/energyIndicators/{energyIndicatorsIds}")
    public AjaxResult remove(@PathVariable String[] energyIndicatorsIds)
    {
        return toAjax(energyIndicatorsService.deleteEnergyIndicatorsByEnergyIndicatorsIds(energyIndicatorsIds));
    }
}
