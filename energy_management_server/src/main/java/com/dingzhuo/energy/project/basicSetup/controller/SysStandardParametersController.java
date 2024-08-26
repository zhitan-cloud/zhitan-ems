package com.dingzhuo.energy.project.basicSetup.controller;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.project.basicSetup.domain.SysStandardCategory;
import com.dingzhuo.energy.project.energyStatistics.domain.dataTimeSVG;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.project.basicSetup.domain.SysStandardParameters;
import com.dingzhuo.energy.project.basicSetup.service.ISysStandardParametersService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * parametersController
 *
 * @author ruoyi
 * @date 2020-02-12
 */
@RestController
@RequestMapping("/basicSetup/parameters")
public class SysStandardParametersController extends BaseController {
    @Autowired
    private ISysStandardParametersService sysStandardParametersService;
    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;

    /**
     * 查询parameters列表
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:parameters:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysStandardParameters sysStandardParameters) {
        startPage();
        List<SysStandardParameters> list = sysStandardParametersService.selectSysStandardParametersList(sysStandardParameters);
        return getDataTable(list);
    }

    /**
     * 导出parameters列表
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:parameters:export')")
    @Log(title = "parameters", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysStandardParameters sysStandardParameters) {
        List<SysStandardParameters> list = sysStandardParametersService.selectSysStandardParametersList(sysStandardParameters);
        ExcelUtil<SysStandardParameters> util = new ExcelUtil<SysStandardParameters>(SysStandardParameters.class);
        return util.exportExcel(list, "parameters");
    }

    /**
     * 获取parameters详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:parameters:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(sysStandardParametersService.selectSysStandardParametersById(id));
    }

    /**
     * 新增parameters
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:parameters:add')")
    @Log(title = "parameters", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysStandardParameters sysStandardParameters) {
        int msg = sysStandardParametersService.insertSysStandardParameters(sysStandardParameters);
        return toAjax(msg);
    }

    /**
     * 修改parameters
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:parameters:edit')")
    @Log(title = "parameters", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysStandardParameters sysStandardParameters) {
        SysStandardParameters standardParameters =sysStandardParametersService.selectSysStandardParametersById(sysStandardParameters.getId());
        int msg;
        if(standardParameters==null){
            msg = sysStandardParametersService.insertSysStandardParameters(sysStandardParameters);
        }else{
            msg=sysStandardParametersService.updateSysStandardParameters(sysStandardParameters);
        }
        return toAjax(msg);
    }

    /**
     * 删除parameters
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:parameters:remove')")
    @Log(title = "parameters", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(sysStandardParametersService.deleteSysStandardParametersByIds(ids));
    }
    @PreAuthorize("@ss.hasPermi('basicSetup:parameters:listSee')")
    @GetMapping("/listSee/{id}")
    public AjaxResult getListSee(@PathVariable("id") String id) {
        try {
            List tableList = new ArrayList();
            List<String> tagCodes = new ArrayList<String>();
            SysStandardParameters sysStandardParameters = sysStandardParametersService.getStandardParameters(id);
            if (sysStandardParameters != null) {
                JSONArray ar = JSONObject.parseArray(sysStandardParameters.getConfigInfo());
                for(int i=0;i<ar.size();i++){
                    JSONObject t =  ar.getJSONObject(i);
                    List<Map> list = JSONObject.parseArray(t.get("tableRow").toString(), Map.class);
                    for (Map item : list) {
                        for (int j = 1; j < item.keySet().size(); j++) {
                            tagCodes.add((String) item.get("colName"+j));
                        }
                    }
                }
                //tagCodes.add("DB01_A01");
                List<TagValue> tagValueslist= realtimeDatabaseService.retrieve(tagCodes);
                for(int i=0;i<ar.size();i++){
                    JSONObject t =  ar.getJSONObject(i);
                    Map tabMap = new HashMap();
                    tabMap.put("tableName",t.get("tableName"));
                    List<Map> listRow = JSONObject.parseArray(t.get("tableColumn").toString(), Map.class);
                    tabMap.put("tablecolumn",listRow);
                    List<Map> list = JSONObject.parseArray(t.get("tableRow").toString(), Map.class);//这里的第二个参数也可以用对应的自定义javaBean.class
                    List rowList = new ArrayList();
                    for (Map item : list) {
                        Map row= new HashMap();
                        for (int j = 0; j < item.values().size(); j++) {
                            /*if(item.get("colName"+j).toString().indexOf())*/
                            String code = "";
                            String code1=item.get("colName"+j).toString();
                            for (TagValue items:tagValueslist) {
                                String code2=items.getTagCode();
                                if(code2.equals(code1)){
                                    code = String.valueOf((Double)new BigDecimal(items.getValue()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
                                }else{
                                    code=item.get("colName"+j).toString();
                                }
                            }
                            row.put("colName"+j,code);
                        }
                        rowList.add(row);
                    }
                    tabMap.put("tablerowdata",rowList);
                    tableList.add(tabMap);
                }
            }


            return AjaxResult.success(tableList);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return AjaxResult.error("查询失败！");
        }
    }

}