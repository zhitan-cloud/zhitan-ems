package com.zhitan.web.controller.basicdata;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.domain.MeterImplementExcel;
import com.zhitan.basicdata.services.IMeterImplementService;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.framework.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * 计量器具档案维护Controller
 *
 * @author zhaowei
 * @date 2020-02-12
 */
@RestController
@RequestMapping("/meter/implement")
@Api(value = "计量器具管理",tags = {"计量器具管理"})
public class MeterImplementController extends BaseController
{
    @Autowired
    private IMeterImplementService meterImplementService;

    @Autowired
    private TokenService tokenService;


    /**
     * 查询计量器具档案维护列表
     */
    @PreAuthorize("@ss.hasPermi('meter:implement:list')")
    @GetMapping("/list")
    @ApiOperation(value = "计量器具列表")
    public TableDataInfo list(MeterImplement meterImplement,Long pageNum, Long pageSize)
    {
        Page<MeterImplement> list = meterImplementService.selectMeterImplementPage(meterImplement,pageNum,pageSize);
        return getDataTable(list);
    }

    /**
     * 导出计量器具档案维护列表
     */
    @PreAuthorize("@ss.hasPermi('meter:implement:export')")
    @Log(title = "计量器具档案维护", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation(value = "计量器具列表导出")
    public AjaxResult export(MeterImplement meterImplement)
    {
//        List<MeterImplement> list = meterImplementService.selectMeterImplementList(meterImplement);
        //使用专用的导出调用方法，这里面将 数据进行转化， 状态、种类 转换成 中文，用函数实现   通过数据字典转换
        //MeterImplementExcel  导出 Excel专用对象
        List<MeterImplementExcel> list = meterImplementService.exectMeterImplementList(meterImplement);
        ExcelUtil<MeterImplementExcel> util = new ExcelUtil<MeterImplementExcel>(MeterImplementExcel.class);
        return util.exportExcel(list, "implement");
    }

    /**
     * 获取计量器具档案维护详细信息
     */
    @PreAuthorize("@ss.hasPermi('meter:implement:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取计量器具相信信息")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(meterImplementService.selectMeterImplementById(id));
    }

    /**
     * 新增计量器具档案维护
     */
    @PreAuthorize("@ss.hasPermi('meter:implement:add')")
    @Log(title = "计量器具档案维护", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增计量器具")
    public AjaxResult add(@RequestBody MeterImplement meterImplement)
    {
        MeterImplement check = meterImplementService.selectMeterImplementByCode(meterImplement);
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        //编号唯一性检测
        if(check!=null && check.getCode()!=null && check.getCode().length()>0)
        {
           return  AjaxResult.error(check.getCode()+"编码已存在!");
        }else
        {
            meterImplement.setId(UUID.randomUUID().toString());
            meterImplement.setCreateBy(loginUser.getUsername());
           return toAjax(meterImplementService.insertMeterImplement(meterImplement));
        }
    }

    /**
     * 修改计量器具档案维护
     */
    @PreAuthorize("@ss.hasPermi('meter:implement:edit')")
    @Log(title = "计量器具档案维护", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "编辑计量器具")
    public AjaxResult edit(@RequestBody MeterImplement meterImplement)
    {
        //更新时的编码 唯一约束 要判断 id不等于自己，且 code存在重复的，要先去掉自己，否则 自己的修改也报错
        MeterImplement check = meterImplementService.selectMeterImplementByCode(meterImplement);
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        //编号唯一性检测
        if(check!=null && check.getCode()!=null && check.getCode().length()>0)
        {
            return  AjaxResult.error(check.getCode()+"编码已存在!");
        }else
        {
            meterImplement.setUpdateBy(loginUser.getUsername());
            return  toAjax(meterImplementService.updateMeterImplement(meterImplement));
        }
    }

    /**
     * 删除计量器具档案维护
     */
    @PreAuthorize("@ss.hasPermi('meter:implement:remove')")
    @Log(title = "计量器具档案维护", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation(value = "删除计量器具")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(meterImplementService.deleteMeterImplementByIds(ids));
    }

    @Log(title = "计量器具档案维护", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('meter:implement:import')")
    @PostMapping("/importData")
    @ApiOperation(value = "计量器具导入")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<MeterImplement> util = new ExcelUtil<MeterImplement>(MeterImplement.class);
        List<MeterImplement> meterList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String message = meterImplementService.excelImpSave(meterList,loginUser);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    @ApiOperation(value = "计量器具导出模板")
    public AjaxResult importTemplate()
    {
        ExcelUtil<MeterImplementExcel> util = new ExcelUtil<MeterImplementExcel>(MeterImplementExcel.class);
        return util.importTemplateExcel("计量器具档案数据");
    }
}
