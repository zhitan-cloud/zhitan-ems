package com.zhitan.basicdata.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.domain.MeterImplementExcel;
import com.zhitan.basicdata.mapper.MeterImplementMapper;
import com.zhitan.basicdata.services.IMeterImplementService;
import com.zhitan.common.core.domain.entity.SysDictData;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.enums.IndexType;
import com.zhitan.common.exception.CustomException;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.gatewaysetting.domain.GatewaySetting;
import com.zhitan.gatewaysetting.mapper.GatewaySettingMapper;
import com.zhitan.model.domain.DaqTemplate;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.mapper.DaqTemplateMapper;
import com.zhitan.model.mapper.EnergyIndexMapper;
import com.zhitan.system.mapper.SysDictDataMapper;
import com.zhitan.system.service.impl.SysUserServiceImpl;
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
    private DaqTemplateMapper daqTemplateMapper;

    @Autowired
    private EnergyIndexMapper energyIndexMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private GatewaySettingMapper gatewaySettingMapper;

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

        final int i = meterImplementMapper.insertMeterImplement(meterImplement);

        DaqTemplate query = new DaqTemplate();
        query.setDeviceType(meterImplement.getMeterType());
        List<DaqTemplate> daqTemplates = daqTemplateMapper.selectDaqTemplateList(query);
        if (daqTemplates.isEmpty()) {
            return i;
        }

        List<EnergyIndex> energyIndices = new ArrayList<>();
        daqTemplates.forEach(daqTemplate -> {
            EnergyIndex energyIndex = new EnergyIndex();
            energyIndex.setIndexId(UUID.randomUUID().toString());
            energyIndex.setCode(meterImplement.getCode() + "_" + daqTemplate.getCode());
            energyIndex.setName(daqTemplate.getName());
            energyIndex.setUnitId(daqTemplate.getUnit());
            energyIndex.setIndexType(IndexType.COLLECT);
            energyIndex.setMeterId(meterImplement.getId());
            energyIndices.add(energyIndex);
        });

        energyIndexMapper.deleteIndexByMeterId(meterImplement.getId());
        energyIndexMapper.insertEnergyIndices(energyIndices);

        //判断是否关联网关信息，增加信息
        if (StringUtils.isNotBlank(meterImplement.getGatewayId())) {
            GatewaySetting gatewaySetting = new GatewaySetting();
            gatewaySetting.setId(meterImplement.getGatewayId());
            gatewaySetting.setDeviceNum(1);
            gatewaySetting.setPtNum(energyIndices.size());
            //更新数据
            gatewaySettingMapper.addNum(gatewaySetting);
        }

        return i;
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
        //判断之前是否已经绑定，如果已经绑定，将原网关关联的计量器具删除，并在新关联的网关内增加（计量器具数和测点数量）
        //之前未绑定，修改时绑定了：在关联的网关上增加
        MeterImplement beforeUpdate = meterImplementMapper.selectMeterImplementById(meterImplement.getId());

        if (StringUtils.isBlank(beforeUpdate.getGatewayId()) && StringUtils.isNotBlank(meterImplement.getGatewayId())) {
            //原网关没关联，修改时关联的新网关：直接增加
            //更新数据
            gatewaySettingMapper.addNum(getGatewaySetting(meterImplement));

        }else if (StringUtils.isNotBlank(beforeUpdate.getGatewayId())
                && StringUtils.isNotBlank(meterImplement.getGatewayId())
                && !beforeUpdate.getGatewayId().equals(meterImplement.getGatewayId())){
            //原网关关联，修改时换了网关：原网关数量扣减，新网关数量增加
            //更新原网关
            gatewaySettingMapper.subNum(getGatewaySetting(beforeUpdate));
            //更新新网关
            gatewaySettingMapper.addNum(getGatewaySetting(meterImplement));

        }else if (StringUtils.isNotBlank(beforeUpdate.getGatewayId()) && StringUtils.isBlank(meterImplement.getGatewayId())){
            //原来关联了网关，修改时删除掉了，扣减数量
            gatewaySettingMapper.subNum(getGatewaySetting(beforeUpdate));
        }

        return meterImplementMapper.updateMeterImplement(meterImplement);
    }

    /**
     * 根据关联的网关主键获取测点数量
     * @param meterImplement
     * @return
     */
    public GatewaySetting getGatewaySetting(MeterImplement meterImplement) {
        GatewaySetting gatewaySetting = new GatewaySetting();
        gatewaySetting.setId(meterImplement.getGatewayId());
        gatewaySetting.setDeviceNum(1);
        QueryWrapper<EnergyIndex> energyIndexQueryWrapper = new QueryWrapper<>();
        energyIndexQueryWrapper.eq("meter_id", meterImplement.getId());
        int ptNum=energyIndexMapper.selectList(energyIndexQueryWrapper).size();
        gatewaySetting.setPtNum(ptNum);
        gatewaySetting.setUpdateBy(meterImplement.getUpdateBy());
        return gatewaySetting;
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

    @Override
    public Page<MeterImplement> selectMeterImplementPage(MeterImplement meterImplement, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<MeterImplement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(meterImplement.getCode()),MeterImplement::getCode,meterImplement.getCode());
        queryWrapper.like(StringUtils.isNotEmpty(meterImplement.getMeterName()),MeterImplement::getMeterName,meterImplement.getMeterName());
        queryWrapper.eq(StringUtils.isNotEmpty(meterImplement.getMeterType()),MeterImplement::getMeterType,meterImplement.getMeterType());
        queryWrapper.orderByDesc(MeterImplement::getCreateTime);
        final Page<MeterImplement> meterImplementPage = meterImplementMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        return meterImplementPage;
    }
}
