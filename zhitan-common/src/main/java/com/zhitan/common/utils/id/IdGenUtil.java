package com.zhitan.common.utils.id;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.util.UUID;

/**
 * 生成ID
 * @author ZhiTan
 * @date 2024/12/31
 */
public class IdGenUtil {
    public static long nextLongId() {
        return IncrLongIdUtil.nextLongId();
    }

    public static String randomId() {
        long id = IdWorker.getId();
        return String.valueOf(id);
    }

    public static Integer nextIntegerId() {
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
