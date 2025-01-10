package com.zhitan.knowledgeBase.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author DYL
 **/
@Getter
@AllArgsConstructor
public enum EnergyTypeEnum {

    ELECTRICITY(0,"Electricity", "电"),
    WATER(1,"Water", "水"),
    GAS(2,"Gas", "天然气"),
    STEAM(3,"Steam", "蒸汽");

    private final Integer code;
    private final String name;
    private final String desc;

    public static EnergyTypeEnum getEnumByCode(Integer code) {
        for (EnergyTypeEnum e : EnergyTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (EnergyTypeEnum e : EnergyTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.name;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (EnergyTypeEnum e : EnergyTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}
