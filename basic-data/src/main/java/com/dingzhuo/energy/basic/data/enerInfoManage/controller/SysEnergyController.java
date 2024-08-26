package com.dingzhuo.energy.basic.data.enerInfoManage.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnerclass;
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
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnergy;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysEnergyService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

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
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:getenerclassname')")
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
    @GetMapping("/list")
    public TableDataInfo list(SysEnergy sysEnergy)
    {
        startPage();
        List<SysEnergy> list = sysEnergyService.selectSysEnergyList(sysEnergy);
        return getDataTable(list);
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
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:query')")
    @GetMapping(value = "/{enerid}")
    public AjaxResult getInfo(@PathVariable("enerid") Integer enerid)
    {
        SysEnergy sysEnergy = sysEnergyService.selectSysEnergyById(enerid);
        if(sysEnergy.getIsstorage()==0){
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
        String a = sysEnergy.getIsstorageString();
        if(a.equals("是")){
            sysEnergy.setIsstorage(0);
        }else {
            sysEnergy.setIsstorage(1);
        }
        String d = sysEnergy.getEnerclassname();
        Integer enerclassid = sysEnergyService.getEnerClassid(d);
        sysEnergy.setEnerclassid(enerclassid);
        //非空校验
        String name = sysEnergy.getEnername();
        if(name.length()>10){
            return AjaxResult.error("新增失败，能源名称超长！");
        }
        Integer nameNum = sysEnergyService.selectSameEnergyNameNum(name);
        if (nameNum==0){
            return toAjax(sysEnergyService.insertSysEnergy(sysEnergy));
        }
        return AjaxResult.error("新增失败，请检查能源名称是否重复！");
    }

    /**
     * 修改energy
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:edit')")
    @Log(title = "energy", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysEnergy sysEnergy)
    {
        String a = sysEnergy.getIsstorageString();
        if(a.equals("是")){
            sysEnergy.setIsstorage(0);
        }else {
            sysEnergy.setIsstorage(1);
        }
        String d = sysEnergy.getEnerclassname();
        Integer enerclassid = sysEnergyService.getEnerClassid(d);
        sysEnergy.setEnerclassid(enerclassid);
        //唯一校验
        Integer id = sysEnergy.getEnerid();
        String enerName = sysEnergy.getEnerclassname();
        if(enerName.length()>10){
            return AjaxResult.error("修改失败，能源名称超长！");
        }
        //  通过要改的能源名称查已有一样的能源名称有几个                  如果等于1就要通过id判断是不是改自己
        if(sysEnergyService.selectSameEnergyNameNum(enerName)==1&&id.equals(sysEnergyService.selectIdByName(enerName))){
            return toAjax(sysEnergyService.updateSysEnergy(sysEnergy));
            //                  如果=0 就说明这个能源名称可改
        }else if(sysEnergyService.selectSameEnergyNameNum(enerName)==0){
            return toAjax(sysEnergyService.updateSysEnergy(sysEnergy));
        }
        return AjaxResult.error("修改失败，能源名称重复！");
    }
    /**
     * 保存能源单价设置
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:updateEnergyPrice')")
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
    @PreAuthorize("@ss.hasPermi('enerInfoManage:energy:updateEnergyCoefficient')")
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
