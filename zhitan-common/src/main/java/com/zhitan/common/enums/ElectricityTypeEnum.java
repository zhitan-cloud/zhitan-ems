package com.zhitan.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ElectricityTypeEnum {
    SHARP("尖"),
    PEAK("峰"),
    FLAT("平"),
    VALLEY("谷"),
    DEEP("深谷");


    private final String desc;

    public static String getNameByType(String type) {
        for (ElectricityTypeEnum electricityType : ElectricityTypeEnum.values()) {
            if (electricityType.name().equalsIgnoreCase(type)) {
                return electricityType.name();
            }
        }
        throw new IllegalArgumentException("Invalid electricity type: " + type);
    }
}