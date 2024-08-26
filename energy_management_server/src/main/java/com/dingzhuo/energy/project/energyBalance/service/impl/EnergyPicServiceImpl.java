package com.dingzhuo.energy.project.energyBalance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;
import com.dingzhuo.energy.project.energyBalance.mapper.EnergyPicMapper;
import com.dingzhuo.energy.project.energyBalance.service.IEnergyPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工序单耗统计功能Service业务层处理
 * 
 * @author zhaow
 * @date 2020-12-26
 */
@Service
public class EnergyPicServiceImpl implements IEnergyPicService
{
    @Autowired
    private EnergyPicMapper energyPicMapper;

    /**
     * 查询工序单耗统计功能报表的指标数据列表
     *
     * @param energyPic 工序单耗统计功能报表的指标数据
     * @return 工序单耗统计功能集合
     */
    public List<EnergyPic> selectEnergyPicList(EnergyPic energyPic){

        //查询数据
        List<EnergyPic> queryEnergyPiclRes = energyPicMapper.selectEnergyPicList(energyPic);
        //返回信息
        List<EnergyPic> resList = new ArrayList<>();
        //按 模型的address做个分组 其实 按 指标所在模型名字分组也可以
        Map<String, List<EnergyPic>> addRessMap = queryEnergyPiclRes.stream().collect(Collectors.groupingBy(EnergyPic::getAddress, Collectors.toList()));
        //遍历分组，因为一个模型下就固定四个指标 产出、消耗、外供、损耗 ，将他们横起来做成一个 bean
        addRessMap.forEach((k,v)->{
            EnergyPic tPic = new EnergyPic();
            for(EnergyPic codePic:v)
            {
                String code = codePic.getCode();
                tPic.setModename(codePic.getModename());
                if(code.lastIndexOf("_CC")==code.length()-3)//根据指标的编码判定 是那种 值（产出、消耗等等），底层没有属性支持，先用这个
                {
                    tPic.setA1(codePic.getA1());
                }
                if(code.lastIndexOf("_XH")==code.length()-3)
                {
                    tPic.setA2(codePic.getA1());
                }
                if(code.lastIndexOf("_WG")==code.length()-3)
                {
                    tPic.setA3(codePic.getA1());
                }
                if(code.lastIndexOf("_SH")==code.length()-3)
                {
                    tPic.setA4(codePic.getA1());
                }
            }
            resList.add(tPic);
        });
        return resList;
    }

    private String calculationCpdh(String nyxhl,String cpcl)
    {
        try{
            if(nyxhl.equals("--")||cpcl.equals("--"))
            {
                return "--";
            }else{
                double fz = Double.parseDouble(nyxhl);
                double fm = Double.parseDouble(cpcl);
                return String.format("%.2f", fz/fm);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return "--";
        }
    }
}
