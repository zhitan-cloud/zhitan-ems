package com.dingzhuo.energy.project.govReports.controller;

import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.govReports.domain.DataItemPub;
import com.dingzhuo.energy.project.govReports.domain.DataItemPubVo;
import com.dingzhuo.energy.project.govReports.service.IDataItemPubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GovReportsController
 *
 * @author zy
 * @date 2022-04-06
 */
@RestController
@RequestMapping("/govReports/govReports")
public class DataItemPubController extends BaseController
{
    @Autowired
    private IDataItemPubService dataItemPubService;

    /**
     * 查询GovReports列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "timeCode",required = false) String timeCode,@RequestParam(value = "timeType",required = false) String timeType)
    {
        if(StringUtils.isNotEmpty(timeCode)){
            if("DAY".equals(timeType)){
                timeCode="D"+timeCode;
            }
            if("MONTH".equals(timeType)){
                timeCode="M"+timeCode;
            }
        }
        DataItemPub dip = new DataItemPub();
        dip.setTimeCode(timeCode);
        startPage();
        List<DataItemPubVo> list = dataItemPubService.selectDataItemPubInfoList(dip);
        return getDataTable(list);
    }

    /**
     * 导出GovReports列表
     */
    @Log(title = "GovReports", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DataItemPub dataItemPub)
    {
        List<DataItemPub> list = dataItemPubService.selectDataItemPubList(dataItemPub);
        ExcelUtil<DataItemPub> util = new ExcelUtil<DataItemPub>(DataItemPub.class);
        return util.exportExcel(list, "govReports");
    }

//    /**
//     * 获取GovReports详细信息
//     */
//    @GetMapping(value = "/{indexId}")
//    public AjaxResult getInfo(@PathVariable("indexId") String indexId)
//    {
//        return AjaxResult.success(dataItemPubService.selectDataItemPubById(indexId));
//    }

    /**
     * 新增GovReports
     */
    @Log(title = "GovReports", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DataItemPub dataItemPub)
    {
        return toAjax(dataItemPubService.insertDataItemPub(dataItemPub));
    }

    /**
     * 修改GovReports
     */
    @Log(title = "GovReports", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DataItemPub dataItemPub)
    {
        return toAjax(dataItemPubService.updateDataItemPub(dataItemPub));
    }

    /**
     * 删除GovReports
     */
    @Log(title = "GovReports", businessType = BusinessType.DELETE)
	@DeleteMapping("/{indexIds}")
    public AjaxResult remove(@PathVariable String[] indexIds)
    {
        return toAjax(dataItemPubService.deleteDataItemPubByIds(indexIds));
    }
}
