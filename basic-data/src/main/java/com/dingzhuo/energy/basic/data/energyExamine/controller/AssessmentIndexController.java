package com.dingzhuo.energy.basic.data.energyExamine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
import com.dingzhuo.energy.basic.data.energyExamine.domain.AssessmentIndex;
import com.dingzhuo.energy.basic.data.energyExamine.service.IAssessmentIndexService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 用能考核标准Controller
 * 
 * @author zhaow
 * @date 2020-12-22
 */
@RestController
@RequestMapping("/energyExamine/assessmentIndex")
public class AssessmentIndexController extends BaseController
{
    @Autowired
    private IAssessmentIndexService assessmentIndexService;

    /**
     * 查询用能考核标准列表
     */
    @PreAuthorize("@ss.hasPermi('energyExamine:assessmentIndex:list')")
    @GetMapping("/list")
    public TableDataInfo list(AssessmentIndex assessmentIndex)
    {
        startPage();
        List<AssessmentIndex> list = assessmentIndexService.selectAssessmentIndexList(assessmentIndex);
        return getDataTable(list);
    }

    /**
     * 查询用能考核标准列表根据模型ID
     */
    @PreAuthorize("@ss.hasPermi('energyExamine:assessmentIndex:listindex')")
    @GetMapping("/list/{modeNodeId}")
    public TableDataInfo listindex(@PathVariable String modeNodeId,AssessmentIndex assessmentIndex)
    {
        assessmentIndex.setModeNodeId(modeNodeId);
        startPage();
        List<AssessmentIndex> list = assessmentIndexService.selectAssessmentIndexListByMode(assessmentIndex);
        return getDataTable(list);
    }

    /**
     * 导出用能考核标准列表
     */
    @PreAuthorize("@ss.hasPermi('energyExamine:assessmentIndex:export')")
    @Log(title = "用能考核标准", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AssessmentIndex assessmentIndex)
    {
        List<AssessmentIndex> list = assessmentIndexService.selectAssessmentIndexList(assessmentIndex);
        ExcelUtil<AssessmentIndex> util = new ExcelUtil<AssessmentIndex>(AssessmentIndex.class);
        return util.exportExcel(list, "assessmentIndex");
    }

    /**
     * 获取用能考核标准详细信息
     */
    @PreAuthorize("@ss.hasPermi('energyExamine:assessmentIndex:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(assessmentIndexService.selectAssessmentIndexById(id));
    }

    /**
     * 新增用能考核标准
     */
    @PreAuthorize("@ss.hasPermi('energyExamine:assessmentIndex:add')")
    @Log(title = "用能考核标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AssessmentIndex assessmentIndex)
    {
        return toAjax(assessmentIndexService.insertAssessmentIndex(assessmentIndex));
    }

    /**
     * 修改用能考核标准
     */
    @PreAuthorize("@ss.hasPermi('energyExamine:assessmentIndex:edit')")
    @Log(title = "用能考核标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AssessmentIndex assessmentIndex)
    {
        return toAjax(assessmentIndexService.updateAssessmentIndex(assessmentIndex));
    }

    /**
     * 删除用能考核标准
     */
    @PreAuthorize("@ss.hasPermi('energyExamine:assessmentIndex:remove')")
    @Log(title = "用能考核标准", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(assessmentIndexService.deleteAssessmentIndexByIds(ids));
    }

    /**
     * 添加模型考核节点的指标
     */
    @Log(title = "添加模型考核节点的指标", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addIndex/{modeNodeId}")
    public AjaxResult edit(@PathVariable String modeNodeId, @RequestBody String[] ids) {
        List<AssessmentIndex> assessmentIndexList = new ArrayList<AssessmentIndex>();
        if(ids!=null && modeNodeId!=null && modeNodeId.trim().length()>0)
        {
            for(String indexId:ids)
            {
                AssessmentIndex assessMentIdex = new AssessmentIndex();
                assessMentIdex.setId(UUID.randomUUID().toString());
                assessMentIdex.setIndexId(indexId);
                assessMentIdex.setModeNodeId(modeNodeId);
                assessmentIndexList.add(assessMentIdex);
            }
        }
        AjaxResult ajax = toAjax(assessmentIndexService.batchAssessIndex(assessmentIndexList));
        return ajax;
    }
}
