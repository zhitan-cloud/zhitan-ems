package com.dingzhuo.energy.project.statisticalData.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dingzhuo.energy.project.statisticalData.domain.GxdhCountIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.statisticalData.mapper.GxdhCountMapper;
import com.dingzhuo.energy.project.statisticalData.domain.GxdhCount;
import com.dingzhuo.energy.project.statisticalData.service.IGxdhCountService;

/**
 * 工序单耗统计功能Service业务层处理
 * 
 * @author zhaow
 * @date 2020-12-26
 */
@Service
public class GxdhCountServiceImpl implements IGxdhCountService 
{
    @Autowired
    private GxdhCountMapper gxdhCountMapper;

    /**
     * 查询工序单耗统计功能
     * 
     * @param id 工序单耗统计功能ID
     * @return 工序单耗统计功能
     */
    @Override
    public GxdhCount selectGxdhCountById(String id)
    {
        return gxdhCountMapper.selectGxdhCountById(id);
    }

    /**
     * 查询工序单耗统计功能列表
     * 
     * @param gxdhCount 工序单耗统计功能
     * @return 工序单耗统计功能
     */
    @Override
    public List<GxdhCount> selectGxdhCountList(GxdhCount gxdhCount)
    {
        return gxdhCountMapper.selectGxdhCountList(gxdhCount);
    }

    /**
     * 新增工序单耗统计功能
     * 
     * @param gxdhCount 工序单耗统计功能
     * @return 结果
     */
    @Override
    public int insertGxdhCount(GxdhCount gxdhCount)
    {
        return gxdhCountMapper.insertGxdhCount(gxdhCount);
    }

    /**
     * 修改工序单耗统计功能
     * 
     * @param gxdhCount 工序单耗统计功能
     * @return 结果
     */
    @Override
    public int updateGxdhCount(GxdhCount gxdhCount)
    {
        return gxdhCountMapper.updateGxdhCount(gxdhCount);
    }

    /**
     * 批量删除工序单耗统计功能
     * 
     * @param ids 需要删除的工序单耗统计功能ID
     * @return 结果
     */
    @Override
    public int deleteGxdhCountByIds(String[] ids)
    {
        return gxdhCountMapper.deleteGxdhCountByIds(ids);
    }

    /**
     * 删除工序单耗统计功能信息
     * 
     * @param id 工序单耗统计功能ID
     * @return 结果
     */
    @Override
    public int deleteGxdhCountById(String id)
    {
        return gxdhCountMapper.deleteGxdhCountById(id);
    }

    /**
     * 查询工序单耗统计功能报表的指标数据列表
     *
     * @param gxdhCountIndex 工序单耗统计功能报表的指标数据
     * @return 工序单耗统计功能集合
     */
    public List<GxdhCountIndex> selectGxdhCountIndexList(GxdhCountIndex gxdhCountIndex){
        List<GxdhCountIndex> resList = new ArrayList<>();
        GxdhCountIndex nyxhl = new GxdhCountIndex();
        nyxhl.setVarietyType("能源消耗量");

        GxdhCountIndex cpcl = new GxdhCountIndex();
        cpcl.setVarietyType("产品产量");

        GxdhCountIndex cpdh = new GxdhCountIndex();
        cpdh.setVarietyType("产品单耗");
        //先查 能源消耗量
        gxdhCountIndex.setVarietyType("NYXHL");
        List<GxdhCountIndex> queryNyxhlRes = gxdhCountMapper.selectGxdhCountIndexList(gxdhCountIndex);
        if(queryNyxhlRes!=null && queryNyxhlRes.size()>0)
        {
            for(GxdhCountIndex gxdhCountIndexT:queryNyxhlRes)
            {
                if(gxdhCountIndexT.getFL().equals("DIAN"))
                {
                    nyxhl.setA1(gxdhCountIndexT.getA1());
                    nyxhl.setA4(gxdhCountIndexT.getA2());
                    nyxhl.setA7(gxdhCountIndexT.getA3());
                    nyxhl.setA10(gxdhCountIndexT.getA4());
                    nyxhl.setA13(gxdhCountIndexT.getA5());
                    nyxhl.setA16(gxdhCountIndexT.getA6());
                    nyxhl.setA19(gxdhCountIndexT.getA7());
                    nyxhl.setA22(gxdhCountIndexT.getA8());
                    nyxhl.setA25(gxdhCountIndexT.getA9());
                    nyxhl.setA28(gxdhCountIndexT.getA10());
                    nyxhl.setA31(gxdhCountIndexT.getA11());
                    nyxhl.setA34(gxdhCountIndexT.getA12());
                }else if(gxdhCountIndexT.getFL().equals("MQ"))
                {
                    nyxhl.setA2(gxdhCountIndexT.getA1());
                    nyxhl.setA5(gxdhCountIndexT.getA2());
                    nyxhl.setA8(gxdhCountIndexT.getA3());
                    nyxhl.setA11(gxdhCountIndexT.getA4());
                    nyxhl.setA14(gxdhCountIndexT.getA5());
                    nyxhl.setA17(gxdhCountIndexT.getA6());
                    nyxhl.setA20(gxdhCountIndexT.getA7());
                    nyxhl.setA23(gxdhCountIndexT.getA8());
                    nyxhl.setA26(gxdhCountIndexT.getA9());
                    nyxhl.setA29(gxdhCountIndexT.getA10());
                    nyxhl.setA32(gxdhCountIndexT.getA11());
                    nyxhl.setA35(gxdhCountIndexT.getA12());
                }else if(gxdhCountIndexT.getFL().equals("ZQ"))
                {
                    nyxhl.setA3(gxdhCountIndexT.getA1());
                    nyxhl.setA6(gxdhCountIndexT.getA2());
                    nyxhl.setA9(gxdhCountIndexT.getA3());
                    nyxhl.setA12(gxdhCountIndexT.getA4());
                    nyxhl.setA15(gxdhCountIndexT.getA5());
                    nyxhl.setA18(gxdhCountIndexT.getA6());
                    nyxhl.setA21(gxdhCountIndexT.getA7());
                    nyxhl.setA24(gxdhCountIndexT.getA8());
                    nyxhl.setA27(gxdhCountIndexT.getA9());
                    nyxhl.setA30(gxdhCountIndexT.getA10());
                    nyxhl.setA33(gxdhCountIndexT.getA11());
                    nyxhl.setA36(gxdhCountIndexT.getA12());
                }
            }
        }

        //再查  产品产量
        gxdhCountIndex.setVarietyType("CPCL");
        List<GxdhCountIndex> queryCpclRes = gxdhCountMapper.selectGxdhCountIndexList(gxdhCountIndex);
        if(queryCpclRes!=null && queryCpclRes.size()>0)
        {
            for(GxdhCountIndex gxdhCountIndexCpcl:queryCpclRes)
            {
                if(gxdhCountIndexCpcl.getFL().equals("DIAN"))
                {
                    cpcl.setA1(gxdhCountIndexCpcl.getA1());
                    cpcl.setA4(gxdhCountIndexCpcl.getA2());
                    cpcl.setA7(gxdhCountIndexCpcl.getA3());
                    cpcl.setA10(gxdhCountIndexCpcl.getA4());
                    cpcl.setA13(gxdhCountIndexCpcl.getA5());
                    cpcl.setA16(gxdhCountIndexCpcl.getA6());
                    cpcl.setA19(gxdhCountIndexCpcl.getA7());
                    cpcl.setA22(gxdhCountIndexCpcl.getA8());
                    cpcl.setA25(gxdhCountIndexCpcl.getA9());
                    cpcl.setA28(gxdhCountIndexCpcl.getA10());
                    cpcl.setA31(gxdhCountIndexCpcl.getA11());
                    cpcl.setA34(gxdhCountIndexCpcl.getA12());
                }else if(gxdhCountIndexCpcl.getFL().equals("MQ"))
                {
                    cpcl.setA2(gxdhCountIndexCpcl.getA1());
                    cpcl.setA5(gxdhCountIndexCpcl.getA2());
                    cpcl.setA8(gxdhCountIndexCpcl.getA3());
                    cpcl.setA11(gxdhCountIndexCpcl.getA4());
                    cpcl.setA14(gxdhCountIndexCpcl.getA5());
                    cpcl.setA17(gxdhCountIndexCpcl.getA6());
                    cpcl.setA20(gxdhCountIndexCpcl.getA7());
                    cpcl.setA23(gxdhCountIndexCpcl.getA8());
                    cpcl.setA26(gxdhCountIndexCpcl.getA9());
                    cpcl.setA29(gxdhCountIndexCpcl.getA10());
                    cpcl.setA32(gxdhCountIndexCpcl.getA11());
                    cpcl.setA35(gxdhCountIndexCpcl.getA12());
                }else if(gxdhCountIndexCpcl.getFL().equals("ZQ"))
                {
                    cpcl.setA3(gxdhCountIndexCpcl.getA1());
                    cpcl.setA6(gxdhCountIndexCpcl.getA2());
                    cpcl.setA9(gxdhCountIndexCpcl.getA3());
                    cpcl.setA12(gxdhCountIndexCpcl.getA4());
                    cpcl.setA15(gxdhCountIndexCpcl.getA5());
                    cpcl.setA18(gxdhCountIndexCpcl.getA6());
                    cpcl.setA21(gxdhCountIndexCpcl.getA7());
                    cpcl.setA24(gxdhCountIndexCpcl.getA8());
                    cpcl.setA27(gxdhCountIndexCpcl.getA9());
                    cpcl.setA30(gxdhCountIndexCpcl.getA10());
                    cpcl.setA33(gxdhCountIndexCpcl.getA11());
                    cpcl.setA36(gxdhCountIndexCpcl.getA12());
                }
            }
        }

        resList.add(nyxhl);
        resList.add(cpcl);
        //计算产品单耗=能源消耗量/产品产量
        cpdh.setA1(calculationCpdh(nyxhl.getA1(),cpcl.getA1()));
        cpdh.setA2(calculationCpdh(nyxhl.getA2(),cpcl.getA2()));
        cpdh.setA3(calculationCpdh(nyxhl.getA3(),cpcl.getA3()));
        cpdh.setA4(calculationCpdh(nyxhl.getA4(),cpcl.getA4()));
        cpdh.setA5(calculationCpdh(nyxhl.getA5(),cpcl.getA5()));
        cpdh.setA6(calculationCpdh(nyxhl.getA6(),cpcl.getA6()));
        cpdh.setA7(calculationCpdh(nyxhl.getA7(),cpcl.getA7()));
        cpdh.setA8(calculationCpdh(nyxhl.getA8(),cpcl.getA8()));
        cpdh.setA9(calculationCpdh(nyxhl.getA9(),cpcl.getA9()));
        cpdh.setA10(calculationCpdh(nyxhl.getA10(),cpcl.getA10()));
        cpdh.setA11(calculationCpdh(nyxhl.getA11(),cpcl.getA11()));
        cpdh.setA12(calculationCpdh(nyxhl.getA12(),cpcl.getA12()));
        cpdh.setA13(calculationCpdh(nyxhl.getA13(),cpcl.getA13()));
        cpdh.setA14(calculationCpdh(nyxhl.getA14(),cpcl.getA14()));
        cpdh.setA15(calculationCpdh(nyxhl.getA15(),cpcl.getA15()));
        cpdh.setA16(calculationCpdh(nyxhl.getA16(),cpcl.getA16()));
        cpdh.setA17(calculationCpdh(nyxhl.getA17(),cpcl.getA17()));
        cpdh.setA18(calculationCpdh(nyxhl.getA18(),cpcl.getA18()));
        cpdh.setA19(calculationCpdh(nyxhl.getA19(),cpcl.getA19()));
        cpdh.setA20(calculationCpdh(nyxhl.getA20(),cpcl.getA20()));
        cpdh.setA21(calculationCpdh(nyxhl.getA21(),cpcl.getA21()));
        cpdh.setA22(calculationCpdh(nyxhl.getA22(),cpcl.getA22()));
        cpdh.setA23(calculationCpdh(nyxhl.getA23(),cpcl.getA23()));
        cpdh.setA24(calculationCpdh(nyxhl.getA24(),cpcl.getA24()));
        cpdh.setA25(calculationCpdh(nyxhl.getA25(),cpcl.getA25()));
        cpdh.setA26(calculationCpdh(nyxhl.getA26(),cpcl.getA26()));
        cpdh.setA27(calculationCpdh(nyxhl.getA27(),cpcl.getA27()));
        cpdh.setA28(calculationCpdh(nyxhl.getA28(),cpcl.getA28()));
        cpdh.setA29(calculationCpdh(nyxhl.getA29(),cpcl.getA29()));
        cpdh.setA30(calculationCpdh(nyxhl.getA30(),cpcl.getA30()));
        cpdh.setA31(calculationCpdh(nyxhl.getA31(),cpcl.getA31()));
        cpdh.setA32(calculationCpdh(nyxhl.getA32(),cpcl.getA32()));
        cpdh.setA33(calculationCpdh(nyxhl.getA33(),cpcl.getA33()));
        cpdh.setA34(calculationCpdh(nyxhl.getA34(),cpcl.getA34()));
        cpdh.setA35(calculationCpdh(nyxhl.getA35(),cpcl.getA35()));
        cpdh.setA36(calculationCpdh(nyxhl.getA36(),cpcl.getA36()));
        resList.add(cpdh);
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
