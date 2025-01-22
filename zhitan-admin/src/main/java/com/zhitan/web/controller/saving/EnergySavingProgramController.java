package com.zhitan.web.controller.saving;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.saving.domain.dto.EnergySavingProgramDTO;
import com.zhitan.saving.domain.entity.EnergySavingProgram;
import com.zhitan.saving.domain.vo.EnergySavingProgramVO;
import com.zhitan.saving.service.IEnergySavingProgramService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 节能项目管理
 * Controller
 *
 * @author ZhiTan
 * @date 2024-12-26
 */
@RestController
@RequestMapping("/energySavingProject")
@Api(tags = "节能项目管理")
public class EnergySavingProgramController extends BaseController {
    @Resource
    private IEnergySavingProgramService energySavingProgramService;

    /**
     * 查询
     * 节能项目管理
     * 列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页列表")
    public TableDataInfo list(EnergySavingProgram energySavingProgram) {
        Page<EnergySavingProgramVO> list = energySavingProgramService.selectEnergySavingProgramList(energySavingProgram);
        return getDataTable(list);
    }


    /**
     * 获取
     * 节能项目管理
     * 详细信息
     */
    @ApiOperation(value = "查询详情")
    @GetMapping(value = "getById")
    public AjaxResult getInfo(@RequestParam("id") Long id) {
        return success(energySavingProgramService.selectEnergySavingProgramById(id));
    }

    /**
     * 新增
     * 节能项目管理
     */
    @ApiOperation(value = "新增")
    @PostMapping("add")
    public AjaxResult add(  @RequestBody EnergySavingProgramDTO dto) {
        return energySavingProgramService.insertEnergySavingProgram(dto);
    }

    /**
     * 修改
     * 节能项目管理
     */
    @ApiOperation(value = "更新")
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody EnergySavingProgramDTO dto) {
        return energySavingProgramService.updateEnergySavingProgram(dto);
    }

    /**
     * 删除
     * 节能项目管理
     */
    @DeleteMapping("del/{id}")
    @ApiOperation(value = "删除")
    public AjaxResult remove(@PathVariable  Long id) {
        return energySavingProgramService.deleteEnergySavingProgramById(id);
    }
}
