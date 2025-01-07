package com.zhitan.common.utils.time;

import java.util.HashMap;

/**
 * @description: 判断时间是否覆盖24小时
 * @author hph
 * @date 2022-11-14  10:52
 */
public class Time24HourUtil {
    public static void main(String[] args) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("06:00:00-12:00:00", "尖");
        map.put("12:00:00-16:00:00", "峰");
        map.put("16:00:00-20:30:00", "平");
        map.put("20:30:00-00:00:00", "峰");
        map.put("00:00:00-06:00:00", "谷");

        boolean b = checkTimeSpan(map);
        System.out.print("是否24小时" + b);
    }

    /**
     * 检查 时间段 格式是否正确
     * 1、小时、分钟都是两位 且 小时是0-23，分钟是0-59
     * 2、首尾相连
     * 3、共24小时
     *
     * @param map
     */
    public static boolean checkTimeSpan(HashMap<String, String> map) {
        // 校验格式
        String regex = "([0-1]+[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])-([0-1]+[0-9]|2[0-3]):([0-5][0-9]:([0-5][0-9]))";
        for (String key : map.keySet()) {
            String[] tt = key.split("-");
            if (tt[0].equals(tt[1])) {
                return false;
            }
            if (!key.matches(regex)) {
                return false;
            }
        }
        // 校验首尾相连
        // 以第一个为基准
        int foundSpan = 0;
        String span = "";
        String startSpan = "";
        String endSpan = "";
        int index = 0;
        // 最多循环map.keySet().size()次就能找到所有的
        // 缺点在于，如果一个时间段一直没找到下一个，还是会循环完，才会退出
        // 如何 循环一遍就退出整个循环呢
        // 变量太多，不想再加变量了，太乱
        for (int i = 0; i < map.keySet().size(); i++) {
            if (foundSpan == map.keySet().size()) {
                break;
            }
            // 没办法，还是加个变量吧
            int temp = 0 ;
            for (String value : map.keySet()) {
                String[] tt = value.split("-");
                // 第一次
                if (index == 0) {
                    span = tt[1];
                    startSpan = tt[0];
                    foundSpan++;
                    index++;
                }
                // 找到下一个时间段
                if (span.equals(tt[0])) {
                    foundSpan++;
                    span = tt[1];
                    // 最后一次
                    if (foundSpan == map.keySet().size()) {
                        endSpan = tt[1];
                    }
                    temp++;
                    continue;
                }

            }
            // 如果 一次循环，temp没有增加，那么就退出，不用再循环了
            if(temp == 0){
                break;
            }
        }
        if (foundSpan == map.keySet().size() && startSpan.equals(endSpan)) {
            return true;
        } else {
            return false;
        }

        // 校验 是否是 24小时
        // 按理说 只要首尾相连了，那么肯定是24小时


    }

}


