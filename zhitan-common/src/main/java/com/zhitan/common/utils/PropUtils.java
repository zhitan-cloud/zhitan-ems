package com.zhitan.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhitan.common.constant.CommonConst;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 属性值操作工具类
 * @author ZhiTan
 * @date: 2022年03月07日 9:57
 */
public class PropUtils {
    /**
     * 为实体赋值
     *
     * @param obj
     * @param propName
     * @param value
     * @return
     */
    public static Object setValue(Object obj, String propName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(propName);
            field.setAccessible(true);
            String nameTypeName = field.getType().getTypeName();
            nameTypeName = StringUtil.ifEmptyOrNullReturnValue(nameTypeName).toLowerCase();
            if (nameTypeName.contains("float")) {
                float num = (float) DoubleUtil.toDouble(value);
                field.set(obj, num);
            } else if (nameTypeName.contains("double")) {
                field.set(obj, DoubleUtil.toDouble(value));
            } else if (nameTypeName.contains("bigdecimal")) {
                BigDecimal valueOf = BigDecimal.valueOf(Double.parseDouble(String.valueOf(value)))
                        .setScale(CommonConst.DIGIT_2, BigDecimal.ROUND_HALF_UP);

                field.set(obj, valueOf);
            } else {
                field.set(obj, value);
            }
        } catch (Exception e) {
            return null;
        }
        return obj;
    }

    /**
     * 获取对象的指定字段的值
     *
     * @param obj
     * @param propName
     * @return
     */
    public static String getPropValue(Object obj, String propName) {
        String propValue = "";
        try {
            if (null != obj) {
                JSONObject jsonObject = StringUtil.toJsonObject(obj);
                if (!StringUtil.isEmptyOrNull(propName)) {
                    propValue = jsonObject.getString(propName);
                }
            }
        } catch (Exception e) {
        }
        return propValue;
    }

    /**
     * 获取对象的指定字段的类型
     * 返回属性的类型字符串（小写，例如:string/integer/double/date）
     *
     * @param obj      实体
     * @param propName 属性名
     * @return （小写，例如:string/integer/double/date）
     */
    public static String getPropType(Object obj, String propName) {
        String propType = "";
        try {
            if (null != obj && !StringUtil.isEmptyOrNull(propName)) {
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName().toLowerCase();
                    if (name.equals(propName.toLowerCase())) {
                        propType = field.getType().getSimpleName().toLowerCase();
                    }
                }
            }

        } catch (Exception e) {

        }
        return propType;
    }

    /**
     * 行转列
     * @param clazz 结果类，如 UserInfo.class
     * @param list 数据集
     * @return
     * @param <T>
     */
    public static  <T> List rowToColumn(Class<T> clazz, List<T> list){
        List<List> result;
        Field[] fields = clazz.getDeclaredFields();
        result = new ArrayList<>(fields.length);
        for (int i = 0; i < fields.length; i++) {
            result.add(new ArrayList());
        }
        for (T t : list) {
            for (int i = 0; i < fields.length; i++) {
                List l = result.get(i);
                Field field = fields[i];
                field.setAccessible(true);
                try {
                    l.add(field.get(t));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
