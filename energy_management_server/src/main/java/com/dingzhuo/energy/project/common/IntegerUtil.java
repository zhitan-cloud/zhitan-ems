package com.dingzhuo.energy.project.common;

/**
 * @Description: 整数相关工具类
 * @author: yxw
 * @date: 2022年03月10日 17:31
 */
public class IntegerUtil {
    /**
     * 字符串转成int类型
     *
     * @param str
     * @return
     */
    public static int toInt(String str) {
        int d = 0;
        try {
            d = Integer.parseInt(str);
        } catch (Exception e) {
            d = 0;
        }
        return d;
    }
}
