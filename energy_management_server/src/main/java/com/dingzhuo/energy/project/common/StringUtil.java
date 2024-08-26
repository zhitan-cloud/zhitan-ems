package com.dingzhuo.energy.project.common;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 字符串工具类
 * @author: yxw
 * @date: 2022年02月02日 12:27
 */
public class StringUtil {
    /**
     * 判断字符串是否为空字符串或者Null
     *
     * @param str 需要判断的字符串
     * @return
     */
    public static boolean isEmptyOrNull(String str) {
        if (str == null || CommonConst.EMPTY.equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 字符串如果为空字符串或者Null返回空字符串，否则返回字符串本身的值
     *
     * @param str
     * @return
     */
    public static String ifEmptyOrNullReturnValue(String str) {
        if (isEmptyOrNull(str)) {
            return CommonConst.EMPTY;
        }
        return str;
    }

    /**
     * 对象转成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * 对象转成JSONObject
     *
     * @param obj
     * @return
     */
    public static JSONObject toJsonObject(Object obj) {
        return JSONObject.parseObject(toJson(obj));
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String captureWord(String str) {
        str = str.toLowerCase();
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
