package com.zhitan.web.controller.knowledgeBase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBaseAddDTO;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBaseEditDTO;
import com.zhitan.knowledgeBase.domain.dto.KnowledgeBasePageDTO;
import com.zhitan.knowledgeBase.domain.vo.KnowledgeBaseDetailVO;
import com.zhitan.knowledgeBase.domain.vo.KnowledgeBasePageVO;
import com.zhitan.knowledgeBase.service.IKnowledgeBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 知识库(KnowledgeBase)表控制层
 *
 * @author makejava
 * @since 2025-01-10 15:05:26
 */
@RestController
@Api(tags = "知识库管理")
@RequestMapping("/knowledgeBase")
public class KnowledgeBaseController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private IKnowledgeBaseService IKnowledgeBaseService;

    /**
     * 知识库-列表查询
     */
    @GetMapping("/page")
    @ApiOperation(value = "知识库-列表查询")
    public TableDataInfo page(KnowledgeBasePageDTO pageDTO) {
        Page<KnowledgeBasePageVO> responsePage = IKnowledgeBaseService.getPageList(pageDTO);
        return getDataTable(responsePage);
    }

    /**
     * 知识库-查询详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "知识库-查询详情")
    public AjaxResult page(@RequestParam("id") Long id) {
        KnowledgeBaseDetailVO responsePage = IKnowledgeBaseService.getDetail(id);
        return success(responsePage);
    }

    /**
     * 知识库-新增
     */
    @PostMapping("/add")
    @ApiOperation(value = "知识库-新增")
    public AjaxResult add(@RequestBody @Validated KnowledgeBaseAddDTO addDTO) {
        IKnowledgeBaseService.add(addDTO);
        return success();
    }

    /**
     * 知识库-更新
     */
    @PostMapping("/edit")
    @ApiOperation(value = "知识库-更新")
    public AjaxResult edit(@RequestBody @Validated KnowledgeBaseEditDTO editDTO) {
        IKnowledgeBaseService.edit(editDTO);
        return success();
    }

    /**
     * 知识库-删除
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "知识库-删除")
    public AjaxResult delete(@PathVariable("id") Long id) {
        IKnowledgeBaseService.delete(id);
        return success();
    }
}

