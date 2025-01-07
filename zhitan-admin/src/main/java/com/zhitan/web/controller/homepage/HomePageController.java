package com.zhitan.web.controller.homepage;

import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;
import com.zhitan.home.domain.vo.HomeEnergyConsumptionTrendVO;
import com.zhitan.home.domain.vo.HomePeakValleyVO;
import com.zhitan.home.service.impl.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HomePageController
 *
 * @author hmj
 * @date 2024-10-08
 */
@RestController
@RequestMapping("/homepage")
public class HomePageController extends BaseController {

    @Autowired
    public IHomePageService homepageService;
   /**
    * @description: 全厂能耗统计
    * @param timeType
    * @return 
    * @author: hmj
    * @date: 2024/10/8 13:41
    */
   @GetMapping("/energyConsumptionSummation")
   public AjaxResult energyConsumptionSummation(String timeType) {
       try {
           String modelcode = "Composite_Indicators";
           return AjaxResult.success(homepageService.energyConsumptionSummation(timeType,modelcode));
       } catch (Exception ex) {
           logger.error("获取出错！", ex);
           return AjaxResult.error("获取出错!");
       }
   }


    /**
     * @description: 能耗趋势
     * @param timeType
     * @return
     * @author: hmj
     * @date: 2024/10/8 13:41
     */
    @GetMapping("/energyConsumptionTrend")
    public AjaxResult energyConsumptionTrend(String timeType) {
        try {
            String modelcode = "Composite_Indicators";
            HomeEnergyConsumptionTrendVO vo = homepageService.energyConsumptionTrend(timeType,modelcode);

            return AjaxResult.success(vo);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    /**
     * @description: 科室能耗排名
     * @param timeType
     * @return
     * @author: hmj
     * @date: 2024/10/8 13:41
     */
    @GetMapping("/energyConsumptionRanking")
    public AjaxResult energyConsumptionRanking(String timeType) {
        try {
            String modelcode = "Composite_Indicators";
            List<RankingEnergyData> consumptionAnalysisVO = homepageService.energyConsumptionRanking(modelcode,timeType);
            return AjaxResult.success(consumptionAnalysisVO);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    /**
     * @description: 峰平谷占比
     * @param timeType
     * @return
     * @author: hmj
     * @date: 2024/10/8 13:41
     */
    @GetMapping("/peakValley")
    public AjaxResult peakValley(String timeType) {
        try {
//            String modelcode = "Composite_Indicators";
            String modelcode = "PEAK_VALLEY";
            List<HomePeakValleyVO> vo = homepageService.peakValley(timeType,modelcode);
            return AjaxResult.success(vo);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
}
