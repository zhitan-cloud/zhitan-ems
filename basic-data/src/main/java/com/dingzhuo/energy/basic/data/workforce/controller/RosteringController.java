package com.dingzhuo.energy.basic.data.workforce.controller;

import com.dingzhuo.energy.basic.data.workforce.domain.Rostering;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringCopy;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringScheme;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringService;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 排班表查询Controller
 * 
 * @author liuli
 * @date 2020-05-13
 */
@RestController
@RequestMapping("/workforce/rosteringManagement")
public class RosteringController extends BaseController
{
    @Autowired
    private IRosteringService rosteringService;
    /**
     *查询轮值方案名称集合
     */
    @PreAuthorize("@ss.hasPermi('workforce:rosteringManagement:schemeNamelist')")
    @GetMapping("/schemeNamelist")
    public TableDataInfo schemeNamelist(Rostering rostering) {
        startPage();
        List<RosteringScheme> list = rosteringService.selectSchemeNameList();
        return getDataTable(list);
    }

    /**
     * 查询排班表查询列表
     */
    @PreAuthorize("@ss.hasPermi('workforce:rosteringManagement:list')")
    @GetMapping("/list")
    public TableDataInfo list(Rostering rostering)
    {
        startPage();
        List<Rostering> list = rosteringService.selectRosteringList(rostering);
        return getDataTable(list);
    }

    /**
     * 导出排班表查询列表
     */
    @PreAuthorize("@ss.hasPermi('workforce:rosteringManagement:export')")
    @Log(title = "排班表查询", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Rostering rostering)
    {
        List<Rostering> list = rosteringService.selectRosteringList(rostering);
        ExcelUtil<Rostering> util = new ExcelUtil<Rostering>(Rostering.class);
        return util.exportExcel(list, "rosteringManagement");
    }

    /**
     * 获取排班表查询详细信息
     */
    @PreAuthorize("@ss.hasPermi('workforce:rosteringManagement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rosteringService.selectRosteringById(id));
    }

    /**
     * 新增排班表查询（生成）
     */
    @PreAuthorize("@ss.hasPermi('workforce:rosteringManagement:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Rostering rostering) throws ParseException {
        //根据轮值方案id查询班次id、值次id等list
        List<RosteringCopy> list=rosteringService.selectList(rostering);
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //建一个新的list存放insert的list
        List<Rostering> dataItems = new ArrayList<>();
        //定义一个count来完成code（编码）的自增
        int count=1;
        //定义从前台获取的开始时间 startdate为开始时间
        String startdate=sdf.format(rostering.getBeginTimes());
       /* Long timenum =(rostering.getEndTimes().getTime()-rostering.getBeginTimes().getTime())/(60*60*24*1000);//时间差*/
        for(RosteringCopy b:list){
            Date loopstarttime=sdf.parse(startdate);
            long long3 =loopstarttime.getTime();
            long long4= rostering.getEndTimes().getTime();
            if(long3 > long4){//循环后的时间>从页面获取的结束时间则停止循环
                break;
            }else {
                startdate=sdf.format(loopstarttime.getTime());
                for (RosteringCopy a:list) {
                    Rostering rosterings =new Rostering();
                    rosterings.setId(UUID.randomUUID().toString());
                    String code=String.valueOf(count);
                    rosterings.setCode(code);
                    rosterings.setName("排班"+code);
                    rosterings.setSchemeId(a.getSchemeId());
                    rosterings.setShiftId(a.getShiftId());
                    rosterings.setDutyId(a.getDutyId());
                    rosterings.setDepartMemberId(rostering.getDepartMemberId());
                    rosterings.setCreateBy(SecurityUtils.getUsername());
                    rosterings.setCreateTime(DateUtils.getNowDate());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String bengintime=startdate +" "+a.getStartTime();//bengintime为拼接完的开始时间
                    Date looptime=sdf.parse(startdate);//循环后的开始时间
                    Date endtime =rostering.getEndTimes();//从前台获取的结束时间
                    long long1 =looptime.getTime();
                    long long2= endtime.getTime();
                    if(long1 > long2){//若当前时间>从前台获取的结束时间则结束循环
                        break;
                    }else {
                        if(a.getIsCrossDay().equals("Y")) {//是否跨天
                            Date starttime = sdf.parse(startdate);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(starttime);
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            Date looptimes=sdf.parse(startdate);//循环后的开始时间
                            Date endtimes =rostering.getEndTimes();//从前台获取的结束时间
                            long long5 =looptime.getTime();
                            long long6= endtime.getTime();
                            Date beginTimes = df.parse(bengintime);
                            String enddate=startdate+" "+a.getEnddTime();
                            Date endTimes = df.parse(enddate);
                            rosterings.setBeginTimes(beginTimes);
                            rosterings.setEndTimes(endTimes);
                            startdate=sdf.format(calendar.getTime());//给开始时间赋值跨天后的时间
                        }else{
                            Date beginTimes = df.parse(bengintime);
                            String enddate=startdate+" "+a.getEnddTime();
                            Date endTimes = df.parse(enddate);
                            rosterings.setBeginTimes(beginTimes);
                            rosterings.setEndTimes(endTimes);
                        }
                    }
                    dataItems.add(rosterings);
                    count ++;
                }
            }
        }

        this.rosteringService.saveRostering(dataItems);
        return AjaxResult.success("保存成功！");
    }

    /**
     * 修改排班表查询
     */
    @PreAuthorize("@ss.hasPermi('workforce:rosteringManagement:edit')")
    @Log(title = "排班表查询", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Rostering rostering)
    {
        rostering.setUpdateBy(SecurityUtils.getUsername());
        rostering.setUpdateTime(DateUtils.getNowDate());
        return toAjax(rosteringService.updateRostering(rostering));
    }

    /**
     * 删除排班表查询
     */
    @PreAuthorize("@ss.hasPermi('workforce:rosteringManagement:remove')")
    @Log(title = "排班表查询", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rosteringService.deleteRosteringByIds(ids));
    }
    public static <E> List<List<E>> splitList(List<E> targetList, Integer splitSize) {
        if (targetList == null) {
            return new ArrayList<>();
        }

        int size = targetList.size();
        List<List<E>> resultList = new ArrayList<>();
        if (size <= splitSize) {
            resultList.add(targetList);
        } else {
            for (int i = 0; i < size; i += splitSize) {
                //用于限制最后一部分size小于splitSize的list
                int limit = i + splitSize;
                if (limit > size) {
                    limit = size;
                }
                resultList.add(targetList.subList(i, limit));
            }
        }
        return resultList;
    }
}
