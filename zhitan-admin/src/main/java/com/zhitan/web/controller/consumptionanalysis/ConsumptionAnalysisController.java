package com.zhitan.web.controller.consumptionanalysis;

import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.services.ISysEnergyService;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.consumptionanalysis.domain.dto.ConsumptionAnalysisDTO;
import com.zhitan.consumptionanalysis.domain.vo.ConsumptionAnalysisVO;
import com.zhitan.consumptionanalysis.domain.vo.ProductEnergyAnalysisVO;
import com.zhitan.consumptionanalysis.domain.vo.RankingDataVO;
import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;
import com.zhitan.consumptionanalysis.service.IConsumptionAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 统计分析 控制层
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@Api(tags = "综合能耗分析")
@RestController
@RequestMapping("/consumptionanalysis")
public class ConsumptionAnalysisController extends BaseController {

    @Autowired
    private IConsumptionAnalysisService consumptionAnalysisService;

    @Autowired
    private ISysEnergyService sysEnergyService;


    @GetMapping("/getByArea")
    @ApiOperation(value = "科室能耗分析列表", notes = "科室能耗分析列表")
    public AjaxResult getByArea(@Validated ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = consumptionAnalysisService.getByArea(dto);
        return AjaxResult.success(consumptionAnalysisVO);
    }

    @PostMapping("/energyExport")
    @ApiOperation(value = "科室能耗分析导出", notes = "科室能耗分析导出")
    public void energyExport(HttpServletResponse response,  ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = consumptionAnalysisService.getByArea(dto);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("能耗分析");

        final SysEnergy sysEnergy = sysEnergyService.selectSameEnergyCodeNum(dto.getEnergyType());

        String[]  columnNames = new String[] {
            "本期时间", 
            "本期耗" + sysEnergy.getEnername() + "(" + sysEnergy.getMuid()+ ")",
            "同期时间","同期耗" + sysEnergy.getEnername() + "(" + sysEnergy.getMuid()+ ")",
            "YOY".equals( dto.getAnalysisType())?"同比(%)":"环比(%)"
        };


        // 定义数据
        List<String[]> data = new ArrayList<>();
        data.add(columnNames);
        consumptionAnalysisVO.getDataList().forEach(vo->{

            final String[] strings = new String[]{vo.getCurrentTime(),vo.getCurrentValue()+"", vo.getCompareTime(),vo.getCompareValue()+"",vo.getRatio()+""};
            data.add(strings);
        });

        // 写入数据
        int rowNum = 0;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String field : rowData) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(field);
            }
        }

        try {
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("能耗分析导出异常");
        } finally {
            IOUtils.closeQuietly(workbook);
        }

    }

    @GetMapping("/getByDepartment")
    @ApiOperation(value = "根据科室获取能耗排名", notes = "根据科室获取能耗排名")
    public AjaxResult getByDepartment(@Validated ConsumptionAnalysisDTO dto) {
        List<RankingDataVO> consumptionAnalysisVO = consumptionAnalysisService.getByDepartment(dto);
        return AjaxResult.success(consumptionAnalysisVO);
    }


    @GetMapping("/getComprehensiveEnergy")
    @ApiOperation(value = "根据节点获取综合能耗列表", notes = "根据节点获取综合能耗列表")
    public AjaxResult getComprehensiveEnergy(@Validated ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = consumptionAnalysisService.getComprehensiveEnergy(dto);
        return AjaxResult.success(consumptionAnalysisVO);
    }

    @GetMapping("/getYOY")
    @ApiOperation(value = "根据节点获取综合能耗同比环比", notes = "根据节点获取综合能耗同比环比")
    public AjaxResult getYOY(@Validated ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = consumptionAnalysisService.getYOY(dto);
        return AjaxResult.success(consumptionAnalysisVO);
    }

    @GetMapping("/getEnergyRanking")
    @ApiOperation(value = "根据节点获取综合能耗排名", notes = "根据节点获取综合能耗排名")
    public AjaxResult getEnergyRanking(@Validated ConsumptionAnalysisDTO dto) {
        List<RankingEnergyData> consumptionAnalysisVO = consumptionAnalysisService.getEnergyRanking(dto);
        return AjaxResult.success(consumptionAnalysisVO);
    }

    @PostMapping("/comprehensiveEnergyExport")
    @ApiOperation(value = "综合能耗分析导出", notes = "综合能耗分析导出")
    public void comprehensiveEnergyExport(HttpServletResponse response, ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = consumptionAnalysisService.getComprehensiveEnergy(dto);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("综合能耗分析");
        String[]  columnNames = new String[] {
                "日期",
                "综合能耗量(tce)"
        };
        // 定义数据
        List<String[]> data = new ArrayList<>();
        data.add(columnNames);
        consumptionAnalysisVO.getDataList().forEach(vo->{
            final String[] strings = new String[]{vo.getCurrentTime(),vo.getCurrentValue()+""};
            data.add(strings);
        });
        // 写入数据
        int rowNum = 0;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String field : rowData) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(field);
            }
        }
        try {
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("综合能耗分析导出异常");
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    @GetMapping("/getPlanAndProdCount")
    @ApiOperation(value = "根据节点和日期获取平均产量和计划量", notes = "根据节点和日期获取平均产量和计划量")
    public AjaxResult getPlanAndProdCount(@Validated ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = consumptionAnalysisService.getPlanAndProdCount(dto);
        ConsumptionAnalysisVO consumptionAnalysisServiceByArea = consumptionAnalysisService.getByArea(dto);
        consumptionAnalysisServiceByArea.setProdCount(consumptionAnalysisVO.getProdCount());
        consumptionAnalysisServiceByArea.setPlanCount(consumptionAnalysisVO.getPlanCount());
        return AjaxResult.success(consumptionAnalysisServiceByArea);
    }

    @GetMapping("/getProdEnergy")
    @ApiOperation(value = "根据节点和日期获取产品单耗", notes = "根据节点和日期获取产品单耗")
    public AjaxResult getProdEnergy(@Validated ConsumptionAnalysisDTO dto) {
        ProductEnergyAnalysisVO productEnergyAnalysisVO = consumptionAnalysisService.getProdEnergy(dto);
        return AjaxResult.success(productEnergyAnalysisVO);
    }
}
