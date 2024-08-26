package com.dingzhuo.energy.project.energyBalance.domain;

import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 能流分析  EnergyPic
 * 
 * @author zhaow
 * @date 2020-12-26
 */
public class EnergyPic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置主键 */
    private String index_id;

    /** 指标名字*/
    private String name;

    /** 指标的code*/
    private String code;

    /** 指标分类排序地址 */
    private String address;
    /**查询日期**/
    private String dataTime;
    /**选择小时**/
    private String selectHour;
    /**模型的名字**/
    private String modename;
    /**模型的id**/
    private String nodeid;
    /**查询的sql专用特殊处理的 日期条件 只针对  能流分析使用**/
    private String sqlWhereDataTime;

    private String 	a1;
    private String 	a2;
    private String 	a3;
    private String 	a4;
    private String 	a5;
    private String 	a6;
    private String 	a7;
    private String 	a8;
    private String 	a9;
    private String 	a10;
    private String 	a11;
    private String 	a12;
    private String 	a13;
    private String 	a14;
    private String 	a15;
    private String 	a16;
    private String 	a17;
    private String 	a18;
    private String 	a19;
    private String 	a20;
    private String 	a21;
    private String 	a22;
    private String 	a23;
    private String 	a24;
    private String 	a25;
    private String 	a26;
    private String 	a27;
    private String 	a28;
    private String 	a29;
    private String 	a30;
    private String 	a31;
    private String 	a32;
    private String 	a33;
    private String 	a34;
    private String 	a35;
    private String 	a36;

    public String getIndex_id() {
        return index_id;
    }

    public void setIndex_id(String index_id) {
        this.index_id = index_id;
    }

    public String getA1() {
        return getNlfxVal(getNlfxVal(this.a1));
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return getNlfxVal(this.a2);
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA3() {
        return getNlfxVal(this.a3);
    }

    public void setA3(String a3) {
        this.a3 = a3;
    }

    public String getA4() {
        return getNlfxVal(this.a4);
    }

    public void setA4(String a4) {
        this.a4 = a4;
    }

    public String getA5() {
        return getNlfxVal(this.a5);
    }

    public void setA5(String a5) {
        this.a5 = a5;
    }

    public String getA6() {
        return getNlfxVal(this.a6);
    }

    public void setA6(String a6) {
        this.a6 = a6;
    }

    public String getA7() {
        return getNlfxVal(this.a7);
    }

    public void setA7(String a7) {
        this.a7 = a7;
    }

    public String getA8() {
        return getNlfxVal(this.a8);
    }

    public void setA8(String a8) {
        this.a8 = a8;
    }

    public String getA9() {
        return getNlfxVal(this.a9);
    }

    public void setA9(String a9) {
        this.a9 = a9;
    }

    public String getA10() {
        return getNlfxVal(this.a10);
    }

    public void setA10(String a10) {
        this.a10 = a10;
    }

    public String getA11() {
        return getNlfxVal(this.a11);
    }

    public void setA11(String a11) {
        this.a11 = a11;
    }

    public String getA12() {
        return getNlfxVal(this.a12);
    }

    public void setA12(String a12) {
        this.a12 = a12;
    }

    public String getA13() {
        return getNlfxVal(this.a13);
    }

    public void setA13(String a13) {
        this.a13 = a13;
    }

    public String getA14() {
        return getNlfxVal(this.a14);
    }

    public void setA14(String a14) {
        this.a14 = a14;
    }

    public String getA15() {
        return getNlfxVal(this.a15);
    }

    public void setA15(String a15) {
        this.a15 = a15;
    }

    public String getA16() {
        return getNlfxVal(this.a16);
    }

    public void setA16(String a16) {
        this.a16 = a16;
    }

    public String getA17() {
        return getNlfxVal(this.a17);
    }

    public void setA17(String a17) {
        this.a17 = a17;
    }

    public String getA18() {
        return getNlfxVal(this.a18);
    }

    public void setA18(String a18) {
        this.a18 = a18;
    }

    public String getA19() {
        return getNlfxVal(this.a19);
    }

    public void setA19(String a19) {
        this.a19 = a19;
    }

    public String getA20() {
        return getNlfxVal(this.a20);
    }

    public void setA20(String a20) {
        this.a20 = a20;
    }

    public String getA21() {
        return getNlfxVal(this.a21);
    }

    public void setA21(String a21) {
        this.a21 = a21;
    }

    public String getA22() {
        return getNlfxVal(this.a22);
    }

    public void setA22(String a22) {
        this.a22 = a22;
    }

    public String getA23() {
        return getNlfxVal(this.a23);
    }

    public void setA23(String a23) {
        this.a23 = a23;
    }

    public String getA24() {
        return getNlfxVal(this.a24);
    }

    public void setA24(String a24) {
        this.a24 = a24;
    }

    public String getA25() {
        return getNlfxVal(this.a25);
    }

    public void setA25(String a25) {
        this.a25 = a25;
    }

    public String getA26() {
        return getNlfxVal(this.a26);
    }

    public void setA26(String a26) {
        this.a26 = a26;
    }

    public String getA27() {
        return getNlfxVal(this.a27);
    }

    public void setA27(String a27) {
        this.a27 = a27;
    }

    public String getA28() {
        return getNlfxVal(this.a28);
    }

    public void setA28(String a28) {
        this.a28 = a28;
    }

    public String getA29() {
        return getNlfxVal(this.a29);
    }

    public void setA29(String a29) {
        this.a29 = a29;
    }

    public String getA30() {
        return getNlfxVal(this.a30);
    }

    public void setA30(String a30) {
        this.a30 = a30;
    }

    public String getA31() {
        return getNlfxVal(this.a31);
    }

    public void setA31(String a31) {
        this.a31 = a31;
    }

    public String getA32() {
        return getNlfxVal(this.a32);
    }

    public void setA32(String a32) {
        this.a32 = a32;
    }

    public String getA33() {
        return getNlfxVal(this.a33);
    }

    public void setA33(String a33) {
        this.a33 = a33;
    }

    public String getA34() {
        return getNlfxVal(this.a34);
    }

    public void setA34(String a34) {
        this.a34 = a34;
    }

    public String getA35() {
        return getNlfxVal(this.a35);
    }

    public void setA35(String a35) {
        this.a35 = a35;
    }

    public String getA36() {
        return getNlfxVal(this.a36);
    }

    public void setA36(String a36) {
        this.a36 = a36;
    }


    public String getSelectHour() {
        return selectHour;
    }

    public void setSelectHour(String selectHour) {
        this.selectHour = selectHour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getModename() {
        return modename;
    }

    public void setModename(String modename) {
        this.modename = modename;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getSqlWhereDataTime() {
        return this.dataTime+" "+this.selectHour;
    }

    public void setSqlWhereDataTime(String sqlWhereDataTime) {
        this.sqlWhereDataTime = sqlWhereDataTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("index_id", getIndex_id())
                .toString();
    }

    private String getNlfxVal(String val)
    {
        String tVal = (val==null || val.length()==0)?"--":val;
        if(tVal.indexOf(".")>-1)
        {
            tVal = String.format("%.2f", Double.parseDouble(tVal));
        }
        return tVal;
    }
}
