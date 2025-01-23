package com.zhitan.common.utils.id;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * longid
 * @author Geoffrey
 * @date 2024/12/31
 */
public class IncrLongIdUtil {

    private static final int INCR_PART_LIMIT_VALUE = 10000;
    private static final AtomicInteger ID_INCR_PART_TEMP = new AtomicInteger(INCR_PART_LIMIT_VALUE);
    private static final StringBuffer ID_BUILDER = new StringBuffer();
    private static transient long baseTime = System.currentTimeMillis() / 1000;
    private static final String DATA_CENTER_ID = "0"; // 0-9

    public static synchronized long nextLongId() {
        ID_BUILDER.setLength(0);
        long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime > baseTime) {
            ID_INCR_PART_TEMP.set(INCR_PART_LIMIT_VALUE);
            baseTime = currentTime;
        }
        int index = ID_INCR_PART_TEMP.incrementAndGet();
        if (index >= (INCR_PART_LIMIT_VALUE * 10 - 1)) {
            if (currentTime == baseTime) {
                try {
                    Thread.sleep(1000);
                    currentTime =  System.currentTimeMillis() / 1000;
                    baseTime = currentTime;
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            ID_INCR_PART_TEMP.set(INCR_PART_LIMIT_VALUE);
        }
        String longStr = ID_BUILDER.append(currentTime).append(DATA_CENTER_ID).append(index).toString();
        return Long.parseLong(longStr);
    }
}
