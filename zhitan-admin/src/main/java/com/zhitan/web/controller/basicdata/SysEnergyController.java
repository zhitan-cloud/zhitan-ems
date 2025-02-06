package com.zhitan.web.controller.basicdata;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.SysEnerclass;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.services.ISysEnergyService;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.constant.UserConstants;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * energyController
 *
 * @author ruoyi
 * @date 2020-02-12
 */
@RestController
@RequestMapping("/enerInfoManage/energy")
public class SysEnergyController extends BaseController
{
    @Autowired
    private ISysEnergyService sysEnergyService;

    /**
     * 查询enerclassname能源类型名称下拉框
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:list')")
    @GetMapping("/getenerclassname")
    public AjaxResult list()
    {
        List<SysEnerclass> s = sysEnergyService.getenerclassname();
        return AjaxResult.success(s);
    }

    /**
     * 查询energy列表
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:list')")
    @GetMapping("/page")
    public TableDataInfo page(SysEnergy sysEnergy, Long pageNum, Long pageSize)
    {
        Page<SysEnergy> list = sysEnergyService.selectSysEnergyPage(sysEnergy,pageNum,pageSize);
        return getDataTable(list);
    }

    /**
     * 查询energy列表
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:list')")
    @GetMapping("/list")
    public AjaxResult list(SysEnergy sysEnergy)
    {
        List<SysEnergy> list = sysEnergyService.selectSysEnergyList(sysEnergy);
        return AjaxResult.success(list);
    }

    /**
     * 导出energy列表
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:export')")
    @Log(title = "energy", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysEnergy sysEnergy)
    {
        List<SysEnergy> list = sysEnergyService.selectSysEnergyList(sysEnergy);
        ExcelUtil<SysEnergy> util = new ExcelUtil<SysEnergy>(SysEnergy.class);
        return util.exportExcel(list, "energy");
    }

    /**
     * 获取energy详细信息
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:list')")
    @GetMapping(value = "/{enerid}")
    public AjaxResult getInfo(@PathVariable("enerid") Integer enerid)
    {
        SysEnergy sysEnergy = sysEnergyService.selectSysEnergyById(enerid);
        if (UserConstants.YES.equals(sysEnergy.getIsstorage())) {
            sysEnergy.setIsstorageString("是");
        }else{
            sysEnergy.setIsstorageString("否");
        }
        return AjaxResult.success(sysEnergy);
    }

    /**
     * 新增energy
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:add')")
    @Log(title = "energy", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysEnergy sysEnergy)
    {
        //非空校验
        String name = sysEnergy.getEnername();
        if(name.length()>10){
            return AjaxResult.error("新增失败，能源名称超长！");
        }
        SysEnergy nameNum = sysEnergyService.selectSameEnergyNameNum(name);
        SysEnergy codeNum = sysEnergyService.selectSameEnergyCodeNum(sysEnergy.getEnersno());
        if (null != nameNum){
            return AjaxResult.error("新增失败，请检查能源名称是否重复！");
        }
        if (null != codeNum){
            return AjaxResult.error("新增失败，请检查能源编号是否重复！");
        }
        return toAjax(sysEnergyService.insertSysEnergy(sysEnergy));
    }

    /**
     * 修改energy
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:edit')")
    @Log(title = "energy", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysEnergy sysEnergy)
    {
        String enerName = sysEnergy.getEnername();
        //唯一校验
        Integer id = sysEnergy.getEnerid();
        if(enerName.length()>10){
            return AjaxResult.error("修改失败，能源名称超长！");
        }
        SysEnergy nameNum = sysEnergyService.selectSameEnergyNameNum(enerName);
        SysEnergy codeNum = sysEnergyService.selectSameEnergyCodeNum(sysEnergy.getEnersno());

        if (null != nameNum && nameNum.getEnerid() != sysEnergy.getEnerid()){
            return AjaxResult.error("修改失败，请检查能源名称是否重复！");
        }
        if (null != codeNum && codeNum.getEnerid() != sysEnergy.getEnerid()){
            return AjaxResult.error("修改失败，请检查能源编号是否重复！");
        }
        return toAjax(sysEnergyService.updateSysEnergy(sysEnergy));
    }
    /**
     * 保存能源单价设置
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:edit')")
    @Log(title = "energy", businessType = BusinessType.UPDATE)
    @PutMapping("/updateEnergyPrice")
    public AjaxResult updateEnergyPrice(@RequestBody(required = false) SysEnergy sysEnergy) throws ParseException {
        Integer enerid = sysEnergy.getEnerid();
        if(sysEnergy.getExecdate()==null){
            return AjaxResult.error("执行日期不能为空！");
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = df.parse(dateString);
        if(sysEnergy.getExecdate().before(now)){
            return AjaxResult.error("执行日期以过！");
        }
        if((sysEnergyService.getPriceCountByEnerid(sysEnergy))==0){
            return toAjax(sysEnergyService.insertEnergyPrice(sysEnergy));
        }else if((sysEnergyService.getPriceCountByEnerid(sysEnergy))==1){
            return toAjax(sysEnergyService.updateEnergyPrice(sysEnergy));
        }
            return AjaxResult.error("保存失败！");
    }
    /**
     * 保存能源折标系数设置
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:edit')")
    @Log(title = "energy", businessType = BusinessType.UPDATE)
    @PutMapping("/updateEnergyCoefficient")
    public AjaxResult updateEnergyCoefficient(@RequestBody(required = false) SysEnergy sysEnergy) throws ParseException {
        Integer enerid = sysEnergy.getEnerid();
        if(sysEnergy.getCoefficientexecdate()==null){
            return AjaxResult.error("执行日期不能为空！");
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = df.parse(dateString);
        if(sysEnergy.getCoefficientexecdate().before(now)){
            return AjaxResult.error("执行日期以过！");
        }
        Integer count = sysEnergyService.getCoefficientCountByEnerid(enerid);
        if(count==0){
            return toAjax(sysEnergyService.insertEnergyCoefficient(sysEnergy));
        }else if(count==1){
            return toAjax(sysEnergyService.updateEnergyCoefficient(sysEnergy));
        }
        return AjaxResult.error("保存失败！");
    }

    /**
     * 删除energy
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:remove')")
    @Log(title = "energy", businessType = BusinessType.DELETE)
	@DeleteMapping("/{enerids}")
    public AjaxResult remove(@PathVariable Integer[] enerids)
    {
        return toAjax(sysEnergyService.deleteSysEnergyByIds(enerids));
    }
}
