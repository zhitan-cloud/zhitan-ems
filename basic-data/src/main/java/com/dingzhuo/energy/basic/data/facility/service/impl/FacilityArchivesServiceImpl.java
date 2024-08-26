package com.dingzhuo.energy.basic.data.facility.service.impl;

import java.util.*;

import com.dingzhuo.energy.common.exception.CustomException;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.project.system.domain.SysDictData;
import com.dingzhuo.energy.project.system.mapper.SysDictDataMapper;
import com.dingzhuo.energy.project.system.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.facility.mapper.FacilityArchivesMapper;
import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.basic.data.facility.service.IFacilityArchivesService;

/**
 * 设备档案Service业务层处理
 *
 * @author zhaowei
 * @date 2020-02-24
 */
@Service
public class FacilityArchivesServiceImpl implements IFacilityArchivesService
{
    @Autowired
    private FacilityArchivesMapper facilityArchivesMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);


    /**
     * 查询设备档案
     *
     * @param id 设备档案ID
     * @return 设备档案
     */
    @Override
    public FacilityArchives selectFacilityArchivesById(String id)
    {
        return facilityArchivesMapper.selectFacilityArchivesById(id);
    }

    /**
     * 查询设备档案根据code
     *
     * @param facilityArchives 设备档案code
     * @return 设备档案
     */
    @Override
    public FacilityArchives selectFacilityArchivesByCode(FacilityArchives facilityArchives)
    {
        return facilityArchivesMapper.selectFacilityArchivesByCode(facilityArchives);
    }

    /**
     * 查询设备档案列表
     *
     * @param facilityArchives 设备档案
     * @return 设备档案
     */
    @Override
    public List<FacilityArchives> selectFacilityArchivesList(FacilityArchives facilityArchives)
    {
        return facilityArchivesMapper.selectFacilityArchivesList(facilityArchives);
    }

    /**
     * 查询设备档案列表导出列表
     *
     * @param facilityArchives 设备档案
     * @return 设备档案
     */
    @Override
    public List<FacilityArchives> excelFacilityArchivesList(FacilityArchives facilityArchives)
    {
        return facilityArchivesMapper.excelFacilityArchivesList(facilityArchives);
    }

    /**
     * 新增设备档案
     *
     * @param facilityArchives 设备档案
     * @return 结果
     */
    @Override
    public int insertFacilityArchives(FacilityArchives facilityArchives)
    {
        facilityArchives.setCreateTime(DateUtils.getNowDate());
        return facilityArchivesMapper.insertFacilityArchives(facilityArchives);
    }

    /**
     * 修改设备档案
     *
     * @param facilityArchives 设备档案
     * @return 结果
     */
    @Override
    public int updateFacilityArchives(FacilityArchives facilityArchives)
    {
        facilityArchives.setUpdateTime(DateUtils.getNowDate());
        return facilityArchivesMapper.updateFacilityArchives(facilityArchives);
    }

    /**
     * 批量删除设备档案
     *
     * @param ids 需要删除的设备档案ID
     * @return 结果
     */
    @Override
    public int deleteFacilityArchivesByIds(String[] ids)
    {
        return facilityArchivesMapper.deleteFacilityArchivesByIds(ids);
    }

    /**
     * 删除设备档案信息
     *
     * @param id 设备档案ID
     * @return 结果
     */
    @Override
    public int deleteFacilityArchivesById(String id)
    {
        return facilityArchivesMapper.deleteFacilityArchivesById(id);
    }


    /**
     * 批量检定恢复设备档案
     *
     * @param ids 需要删除的设备档案ID
     * @return 结果
     */
    @Override
    public int resetFacilityArchivesByIds(String[] ids)
    {
        return facilityArchivesMapper.resetFacilityArchivesByIds(ids);
    }

   /* *//**
     * 检定恢复设备档案信息
     *
     * @param id 设备档案ID
     * @return 结果
     *//*
    @Override
    public int resetFacilityArchivesById(String[] id)
    {
        return facilityArchivesMapper.resetFacilityArchivesById(id);
    }
*/
    /**
     * Excel导入 设备档案维护信息
     *
     * @param facilityArchivesList  要导入的设备档案集合
     * @param loginUser 登录用户对象
     * @return 结果
     */
    @Override
    public String excelImpSave(List<FacilityArchives> facilityArchivesList, LoginUser loginUser)
    {

        if (StringUtils.isNull(facilityArchivesList) || facilityArchivesList.size() == 0)
        {
            throw new CustomException("导入设备档案不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        //1、字典数据翻译
        // 设备类型
        List<SysDictData> facilityTypeList = dictDataMapper.selectDictDataByType("facility_type");
        Map<String,String> facilityTypeMap = this.initDictMap(facilityTypeList);
        // 使用分厂
        List<SysDictData> facilityBranchList = dictDataMapper.selectDictDataByType(
                "branch_factory");
        Map<String,String> facilityBranchMap = this.initDictMap(facilityBranchList);
        //能耗等级
        List<SysDictData> facilityGradeList = dictDataMapper.selectDictDataByType(
                "facility_grade");
        Map<String,String> facilityGradeMap = this.initDictMap(facilityGradeList);
        for (FacilityArchives facilityArchives : facilityArchivesList)
        {
            try
            {

                //  检定周期、提醒周期 定义的Integer类型，如果数据不匹配会默认为0
                facilityArchives.setCheckCycle(facilityArchives.getCheckCycle()==null?1:facilityArchives.getCheckCycle());
                facilityArchives.setReminderCycle(facilityArchives.getReminderCycle()==null?1:facilityArchives.getReminderCycle());
                // 2验证 编码是否存在
                FacilityArchives chekcFacilityImplement = facilityArchivesMapper.selectFacilityArchivesByCode(facilityArchives);
                if (StringUtils.isNull(chekcFacilityImplement))
                {
                    //设置主键
                    facilityArchives.setId(UUID.randomUUID().toString());
                    //翻译 设备类型 无或者值不对则设置为空字符串
                    String facilityType= StringUtils.nvl(facilityTypeMap.get(facilityArchives.getFacilityType()+""),"");
                    //判断状态是否正确
                    if(StringUtils.isEmpty(facilityType))
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、编号 " + facilityArchives.getCode() + " 的设备类型错误");
                        continue;
                    }
                    facilityArchives.setFacilityType(facilityType+"");
                    //翻译 使用分厂  无或者值不对则设置为空字符串
                    String branchFactory= StringUtils.nvl(facilityBranchMap.get(facilityArchives.getBranchFactory()+""),"");
                    if(StringUtils.isEmpty(branchFactory))
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、编号 " + facilityArchives.getCode() + " 的使用分厂错误");
                        continue;
                    }
                    facilityArchives.setBranchFactory(branchFactory);

                    //翻译 能耗等级  无或者值不对则设置为空字符串
                    String facilityGrade= StringUtils.nvl(facilityGradeMap.get(facilityArchives.getFacilityGrade()+""),"");
                    if(StringUtils.isEmpty(facilityGrade))
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、编号 " + facilityArchives.getCode() + " 的能耗等级错误");
                        continue;
                    }
                    facilityArchives.setFacilityGrade(facilityGrade);

                    //设置建立人和建立时间
                    facilityArchives.setCreateBy(loginUser.getUsername());
                    facilityArchives.setCreateTime(new Date());
                    //存储一条数据
                    this.insertFacilityArchives(facilityArchives);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、编号 " + facilityArchives.getCode() + " 导入成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、编号 " + facilityArchives.getCode() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、编号 " + facilityArchives.getCode() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 将字典类型 对应的 字典集合  存入map中使用
     * @param sysDictDataList
     * @return
     */
    public Map initDictMap(List<SysDictData> sysDictDataList)
    {
        Map<String,String> map = new HashMap<String,String>();
        for(SysDictData sysDictData : sysDictDataList)
        {
            //存放 key=标签名字  value是 设置值
            map.put(sysDictData.getDictLabel(),sysDictData.getDictValue());
        }
        return map;
    }
}
