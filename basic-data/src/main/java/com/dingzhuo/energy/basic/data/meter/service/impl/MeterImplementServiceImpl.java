package com.dingzhuo.energy.basic.data.meter.service.impl;

import com.dingzhuo.energy.basic.data.meter.domain.MeterImplement;
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplementExcel;
import com.dingzhuo.energy.basic.data.meter.mapper.MeterImplementMapper;
import com.dingzhuo.energy.basic.data.meter.service.IMeterImplementService;
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

import java.util.*;

/**
 * 计量器具档案维护Service业务层处理
 * 
 * @author zhaowei
 * @date 2020-02-12
 */
@Service
public class MeterImplementServiceImpl implements IMeterImplementService 
{
    @Autowired
    private MeterImplementMapper meterImplementMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    /**
     * 查询计量器具档案维护
     * 
     * @param id 计量器具档案维护ID
     * @return 计量器具档案维护
     */
    @Override
    public MeterImplement selectMeterImplementById(String id)
    {
        return meterImplementMapper.selectMeterImplementById(id);
    }

    /**
     * 查询计量器具档案维护
     *
     * @param meterImplement 计量器具档案维护 编号
     * @return 计量器具档案维护
     */
    @Override
    public MeterImplement selectMeterImplementByCode(MeterImplement meterImplement) {
        return meterImplementMapper.selectMeterImplementByCode(meterImplement);
    }

    /**
     * 查询计量器具档案维护列表
     * 
     * @param meterImplement 计量器具档案维护
     * @return 计量器具档案维护
     */
    @Override
    public List<MeterImplement> selectMeterImplementList(MeterImplement meterImplement)
    {
        return meterImplementMapper.selectMeterImplementList(meterImplement);
    }
    /**
     * 导出计量器具档案维护列表
     *
     * @param meterImplement 计量器具档案维护
     * @return 计量器具档案维护
     */
    @Override
    public List<MeterImplementExcel> exectMeterImplementList(MeterImplement meterImplement)
    {
        return meterImplementMapper.exectMeterImplementList(meterImplement);
    }

    /**
     * 新增计量器具档案维护
     * 
     * @param meterImplement 计量器具档案维护
     * @return 结果
     */
    @Override
    public int insertMeterImplement(MeterImplement meterImplement)
    {
        meterImplement.setCreateTime(DateUtils.getNowDate());
        return meterImplementMapper.insertMeterImplement(meterImplement);
    }

    /**
     * 修改计量器具档案维护
     * 
     * @param meterImplement 计量器具档案维护
     * @return 结果
     */
    @Override
    public int updateMeterImplement(MeterImplement meterImplement)
    {
        meterImplement.setUpdateTime(DateUtils.getNowDate());
        return meterImplementMapper.updateMeterImplement(meterImplement);
    }

    /**
     * 批量删除计量器具档案维护
     * 
     * @param ids 需要删除的计量器具档案维护ID
     * @return 结果
     */
    @Override
    public int deleteMeterImplementByIds(String[] ids)
    {
        return meterImplementMapper.deleteMeterImplementByIds(ids);
    }

    /**
     * 删除计量器具档案维护信息
     * 
     * @param id 计量器具档案维护ID
     * @return 结果
     */
    @Override
    public int deleteMeterImplementById(String id)
    {
        return meterImplementMapper.deleteMeterImplementById(id);
    }

    /**
     * Excel导入 计量器具档案维护信息
     *
     * @param meterImplementList  要导入的计量器具档案集合
     * @param loginUser 登录用户对象
     * @return 结果
     */
    @Override
    public String excelImpSave(List<MeterImplement> meterImplementList, LoginUser loginUser)
    {

        if (StringUtils.isNull(meterImplementList) || meterImplementList.size() == 0)
        {
            throw new CustomException("导入计量器具档案不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        //1、字典数据翻译
        //计量器具状态
        List<SysDictData> meterStatusList = dictDataMapper.selectDictDataByType("meter_status");
        Map<String,String> meterStatusMap = this.initDictMap(meterStatusList);
        //计量器具类型
        List<SysDictData> meterTypeList = dictDataMapper.selectDictDataByType(
                "sys_device_type");
        Map<String,String> meterTypeMap = this.initDictMap(meterTypeList);

        for (MeterImplement meterImplement : meterImplementList)
        {
            try
            {
                //  检定周期、提醒周期 定义的Integer类型，如果数据不匹配会默认为0
                meterImplement.setCheckCycle(meterImplement.getCheckCycle()==null?1:meterImplement.getCheckCycle());
                meterImplement.setReminderCycle(meterImplement.getReminderCycle()==null?1:meterImplement.getReminderCycle());
                // 2验证 编码是否存在
                MeterImplement chekcMeterImplement = meterImplementMapper.selectMeterImplementByCode(meterImplement);
                if (StringUtils.isNull(chekcMeterImplement))
                {
                    //设置主键
                    meterImplement.setId(UUID.randomUUID().toString());
                    //翻译器具状态 无或者值不对则设置为空字符串
                    String meterStatus= StringUtils.nvl(meterStatusMap.get(meterImplement.getMeterStatus()+""),"");
                    //判断状态是否正确
                    if(StringUtils.isEmpty(meterStatus))
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、编号 " + meterImplement.getCode() + " 的状态错误");
                        continue;
                    }
                    meterImplement.setMeterStatus(meterStatus);
                    //翻译器具种类  无或者值不对则设置为空字符串
                    String meterType= StringUtils.nvl(meterTypeMap.get(meterImplement.getMeterType()+""),"");
                    if(StringUtils.isEmpty(meterType))
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、编号 " + meterImplement.getCode() + " 的种类错误");
                        continue;
                    }
                    meterImplement.setMeterType(meterType);
                    //设置建立人和建立时间
                    meterImplement.setCreateBy(loginUser.getUsername());
                    meterImplement.setCreateTime(new Date());
                    //存储一条数据
                    this.insertMeterImplement(meterImplement);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、编号 " + meterImplement.getCode() + " 导入成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、编号 " + meterImplement.getCode() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、编号 " + meterImplement.getCode() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 根据id集合查询计量器具信息
     *
     * @param meterIdList 计量器具id
     * @return
     */
    @Override
    public List<MeterImplement> listMeterImplementByIds(List<String> meterIdList) {
        return meterImplementMapper.listMeterImplementByIds(meterIdList);
    }

    /**
     * 将字典类型 对应的 字典集合  存入map中使用
     *
     * @param sysDictDataList
     * @return
     */
    public Map initDictMap(List<SysDictData> sysDictDataList) {
        Map<String, String> map = new HashMap<String, String>();
        for (SysDictData sysDictData : sysDictDataList) {
            //存放 key=标签名字  value是 设置值
            map.put(sysDictData.getDictLabel(), sysDictData.getDictValue());
        }
        return map;
    }

}
