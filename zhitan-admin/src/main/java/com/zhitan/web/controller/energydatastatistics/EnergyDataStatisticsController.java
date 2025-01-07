package com.zhitan.web.controller.energydatastatistics;

import com.zhitan.common.annotation.Anonymous;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;
import com.zhitan.energydata.service.IEnergyDataStatisticService;
import com.zhitan.energydata.vo.FactoryEnergyConsumptionVo;
import com.zhitan.energydata.vo.PurchaseConsumptionVo;
import com.zhitan.home.domain.vo.HomePeakValleyVO;
import com.zhitan.home.service.impl.IHomePageService;
import com.zhitan.peakvalley.domain.dto.PeakValleyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 恒邦能源数据综合分析
 *
 * @author ZhiTan
 * @date 2024-10-30
 */
@RestController
@RequestMapping("/energydatahomepage")
@Anonymous
public class EnergyDataStatisticsController extends BaseController
{

    @Autowired
    public IHomePageService homepageService;

    @Autowired
    private IEnergyDataStatisticService statisticService;

    /**
     * @description: 当日能耗统计
     * @return
     */
    @GetMapping("/energyConsumptionSummation/day")
    public AjaxResult energyConsumptionSummationDay() {
        try {
            String timeType = "DAY";
            String modelCode = "COMPREHENSIVE_CODE";
            return AjaxResult.success(homepageService.energyConsumptionSummation(timeType,modelCode));
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    /**
     * 电同环比分析
     * @param timeType:month、year
     * @return
     */
    @GetMapping("/energyConsumptionSummation")
    public AjaxResult energyConsumptionSummation(String timeType) {
        try {
            String modelCode = "COMPREHENSIVE_CODE";
            return AjaxResult.success(statisticService.energyConsumptionSummation(timeType,modelCode));
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    /**
     * @description: 各厂区当月能耗统计
     * @return
     */
    @GetMapping("/factoryEnergyConsumption")
    public AjaxResult factoryEnergyConsumption() {
        try {
            String timeType = "MONTH";
            String modelCode = "COMPREHENSIVE_CODE";
            FactoryEnergyConsumptionVo vo=statisticService.factoryEnergyConsumption(timeType,modelCode);
            return AjaxResult.success(vo);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    /**
     * 用电趋势分析---按年：往前推12个月，每个月统计
     * @return
     */
    @GetMapping("/segmentAnalysis/year")
    public AjaxResult segmentAnalysisMonth() {
        PeakValleyDTO dto =new PeakValleyDTO();
        dto.setModelCode("COMPREHENSIVE_CODE");
        dto.setTimeType("YEAR");
        dto.setQueryTime(new Date());
        return AjaxResult.success(statisticService.segmentAnalysisMonth(dto));
    }


    /**
     * 用电趋势统计---按月
     * @return
     */
    @GetMapping("/segmentAnalysis/month")
    public AjaxResult segmentAnalysisDay() {
        PeakValleyDTO dto =new PeakValleyDTO();
        dto.setModelCode("COMPREHENSIVE_CODE");
        dto.setTimeType("MONTH");
        dto.setQueryTime(new Date());
        return AjaxResult.success(statisticService.segmentAnalysisDay(dto));
    }   

    /**
     * 用电趋势统计---按日
     * @return
     */
    @GetMapping("/segmentAnalysis/day")
    public AjaxResult segmentAnalysisHour() {
        PeakValleyDTO dto =new PeakValleyDTO();
        dto.setModelCode("COMPREHENSIVE_CODE");
        dto.setTimeType("DAY");
        dto.setQueryTime(new Date());
        return AjaxResult.success(statisticService.segmentAnalysisHour(dto));
    }

    /**
     * @description: 购入和消耗对比
     * @return
     */
    @GetMapping("/purchaseConsumption")
    public AjaxResult purchaseConsumption() {
        String timeType="MONTH";
        String modelCode = "COMPREHENSIVE_CODE";
        List<PurchaseConsumptionVo> consumptionAnalysisVO = statisticService.purchaseConsumption(modelCode,timeType);
        return AjaxResult.success(consumptionAnalysisVO);
    }

    /**
     * @description: 科室能耗排名
     * @return
     */
    @GetMapping("/energyConsumptionRanking")
    public AjaxResult energyConsumptionRanking(String timeType) {
        String modelCode = "COMPREHENSIVE_CODE";
        List<RankingEnergyData> consumptionAnalysisVO = statisticService.energyConsumptionRanking(modelCode,timeType);
        return AjaxResult.success(consumptionAnalysisVO);
    }

    /**
     * @description: 能源成本占比
     * @return
     */
    @GetMapping("/costProp")
    public AjaxResult costProp(String timeType) {
        String modelCode = "COMPREHENSIVE_CODE";
        List<PurchaseConsumptionVo> consumptionAnalysisVO = statisticService.costProp(modelCode, timeType);
        return AjaxResult.success(consumptionAnalysisVO);

    }

    /**
     * @description: 尖峰平谷占比
     * @param timeType
     */
    @GetMapping("/peakValley")
    public AjaxResult peakValley(String timeType) {
        try {
            String modelcode = "COMPREHENSIVE_CODE";
            List<HomePeakValleyVO> vo = homepageService.peakValley(timeType,modelcode);
            return AjaxResult.success(vo);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
}
