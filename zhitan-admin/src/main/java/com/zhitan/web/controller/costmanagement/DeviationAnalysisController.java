package com.zhitan.web.controller.costmanagement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.costmanagement.domain.CostElectricityInput;
import com.zhitan.costmanagement.domain.vo.*;
import com.zhitan.costmanagement.service.DeviationAnalysisService;
import com.zhitan.costmanagement.service.ICostElectricityInputService;
import com.zhitan.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ZhiTan
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/cost/statistics")
public class DeviationAnalysisController extends BaseController
{
    @Resource
    private ICostElectricityInputService costElectricityInputService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DeviationAnalysisService deviationAnalysisService;
    /**
     * 查询成本策略列表
     */

    @GetMapping("/statistics")
    public AjaxResult statistics(DeviationAnalysisDTO dto) throws ParseException {
        Map<String,Object>result = new HashMap<>();
        StatisticsDataOutItem  statisticsDataItem = deviationAnalysisService.getStatisticsDataItem(dto);
        ElectricityDataOutItem electricityDataItem = deviationAnalysisService.getElectricityDataItem(dto);
        result.put("CostElectricityData",electricityDataItem);
        result.put("CostStatisticsData",statisticsDataItem);

        return AjaxResult.success(result);
    }
    @GetMapping("/test")
    public AjaxResult test(DeviationAnalysisDTO dto) throws ParseException {
        deviationAnalysisService.getStatisticsInfoList(dto);
        return AjaxResult.success(deviationAnalysisService.getStatisticsInfoList2(dto));
    }

    @GetMapping("/getStatisticsInfoList")
    public AjaxResult getStatisticsInfoList(DeviationAnalysisDTO dto) throws ParseException {

        return AjaxResult.success(deviationAnalysisService.getStatisticsInfoList(dto));
    }

    @GetMapping("/getStatisticsInfoEcharts")
    public AjaxResult echarts(DeviationAnalysisDTO dto) throws ParseException {
        Map<String,Object>result = new HashMap<>();


        result = deviationAnalysisService.getEcharst(dto);

        return AjaxResult.success(result);
    }
}
