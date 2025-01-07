package com.zhitan.web.controller.meter;

import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.R;
import com.zhitan.meter.domain.MeterConfig;
import com.zhitan.meter.domain.MeterImplements;
import com.zhitan.meter.domain.MeterParam;
import com.zhitan.meter.services.IMetersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: MeterController
 * @Author:
 * @CreateTime: 2024-09-20 15-33-37
 * @Description: 采集数据生成器
 * @Version: 1.0
 * @Since: JDK1.8
 */

@Slf4j
@Api("采集计量表数据管理")
@RestController
@RequestMapping("/meters")
public class MeterController extends BaseController {

    @Resource
    private IMetersService iMeterservice;




    @ApiOperation("获取计量表数据")
    @GetMapping("/listMeterData")
    public R<List<MeterImplements>> listMeterData(@RequestParam String meterType){
        log.info("开始获取计量表数据......");
        List<MeterImplements> meterImplementList = iMeterservice.listMeterData(meterType);
        if (meterImplementList != null || meterImplementList .size() > 0) {
            return R.ok(meterImplementList);
        } else {
            return R.fail("获取计量表数据失败");
        }
    }

    @ApiOperation("获取点位配置信息")
    @GetMapping("/listConfigurationData")
    public R<List<MeterParam>> listConfigurationData(@RequestParam String meterType, @RequestParam String indexType) {

        log.info("开始获取点位配置信息......");
        List<MeterParam> meterParams = iMeterservice.listConfigurationData(meterType,indexType);
        if (meterParams != null || meterParams .size() > 0) {
            return (R.ok(meterParams));
        } else {
            return R.fail("获取点位配置信息失败");
        }

    }

    @ApiOperation("根据index_type获取点位配置信息")
    @GetMapping("/listConfigData")
    public R<List<MeterConfig>> listConfigData(@RequestParam String indexType) {


        log.info("开始获取点位配置信息......");
        List<MeterConfig> meterConfigList = iMeterservice.listConfigData(indexType);
        if (meterConfigList != null || meterConfigList.size() > 0) {
            return R.ok(meterConfigList);
        } else {
            return R.fail("获取点位配置信息失败");
        }
    }


    @ApiOperation("新增保存配置数据")
    @GetMapping("/addConfigurationData")
    public R<T> addConfigurationData(){
        log.info("执行新增保存配置数据......");

        return R.ok();
    }



}
