package com.zhitan.web.controller.energyMonitor;

import com.zhitan.common.annotation.Log;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.energyMonitor.domain.EnergyUnitToDevice;
import com.zhitan.energyMonitor.domain.vo.ListElectricLoadVO;
import com.zhitan.energyMonitor.service.IElectricLoadService;
import com.zhitan.energyMonitor.service.IEnergyUnitToDeviceService;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.service.IEnergyIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 负荷分析
 * @Author: yxw
 * @Date: 2022-04-24
 * @Version: V1.2
 */
@Api(tags = "负荷分析")
@RestController
@RequestMapping("/loadAnalysis")
@Slf4j
public class ElectricLoadController extends BaseController {
    @Autowired
    private IEnergyUnitToDeviceService energyUnitToDeviceService;
    @Autowired
    private IElectricLoadService electricLoadService;
    @Autowired
    private IEnergyIndexService energyIndexService;

    /**
     * 根据电表id获取负荷分析数据
     *
     * @param energyUnitId 用能单元id
     * @param meterId      电表id
     * @param timeType     时间类型 DAY/MONTH/YEAR
     * @param timeCode     时间值 与时间类型对应：2022-03-21/2022-03/2022
     * @return
     */
    @Log(title = "根据电表id获取负荷分析数据")
    @ApiOperation(value = "根据电表id获取负荷分析数据", notes = "根据电表id获取负荷分析数据")
    @GetMapping(value = "/detail")
    public AjaxResult list(@RequestParam(name = "energyUnitId") String energyUnitId,
                           @RequestParam(name = "meterId") String meterId,
                           @RequestParam(name = "timeType") String timeType,
                           @RequestParam(name = "timeCode") String timeCode) {
        EnergyIndex energyIndex = energyIndexService.getDeviceIndexByCode(energyUnitId, meterId, CommonConst.TAG_CODE_ZYGGL);

        EnergyUnitToDevice energyUnitToDevice = energyUnitToDeviceService.getEnergyUnitToDeviceById(energyUnitId, meterId);
        ListElectricLoadVO vo = electricLoadService.list(timeType, timeCode, energyIndex, energyUnitToDevice);
        return AjaxResult.success(vo);
    }

}
