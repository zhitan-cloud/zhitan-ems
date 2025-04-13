package com.zhitan.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射相关工具类
 *
 * @author Silence
 * @version 1.0
 */
public class ReflectionUtils {
    /**
     * 获取类（包括父类）的所有属性。
     * 包括public/private/protected/无修饰符
     */
    public static Field[] getAllFields(Object model) {
        Class<?> clazz = model.getClass();
        List<Field> fields = new ArrayList<>();
        while (null != clazz) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        Field[] result = new Field[fields.size()];
        return fields.toArray(result);
    }
}
