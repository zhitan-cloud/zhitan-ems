package com.zhitan.web.controller.saving;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.saving.domain.dto.PoliciesRegulationsDTO;
import com.zhitan.saving.domain.dto.PoliciesRegulationsManagementPageDTO;
import com.zhitan.saving.domain.vo.PoliciesRegulationsManagementDetailVO;
import com.zhitan.saving.domain.vo.PoliciesRegulationsManagementPageVO;
import com.zhitan.saving.service.IPoliciesRegulationsManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 政策法规Controller
 *
 * @author ZhiTan
 * @date 2024-12-26
 */
@RestController
@Api(tags = "政策法规管理")
@RequestMapping("/policiesRegulations")
public class PoliciesRegulationsManagementController extends BaseController {

    @Resource
    private IPoliciesRegulationsManagementService policiesRegulationsService;


    /**
     * 政策法规-列表查询
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页列表")
    public TableDataInfo page(PoliciesRegulationsManagementPageDTO pageDTO) {
        Page<PoliciesRegulationsManagementPageVO> responsePage = policiesRegulationsService.getPageList(pageDTO);
        return getDataTable(responsePage);
    }

    /**
     * 政策法规-查询详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "查询详情")
    public AjaxResult page(@RequestParam("id") Long id) {
        PoliciesRegulationsManagementDetailVO responsePage = policiesRegulationsService.getDetail(id);
        return success(responsePage);
    }

    /**
     * 政策法规-新增
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody @Validated PoliciesRegulationsDTO addDTO) {
        policiesRegulationsService.add(addDTO);
        return success();
    }

    /**
     * 政策法规-更新
     */
    @PostMapping("/edit")
    @ApiOperation(value = "更新")
    public AjaxResult edit(@RequestBody @Validated PoliciesRegulationsDTO editDTO) {
        policiesRegulationsService.edit(editDTO);
        return success();
    }

    /**
     * 政策法规-删除
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@PathVariable("id") Long id) {
        policiesRegulationsService.delete(id);
        return success();
    }
}
