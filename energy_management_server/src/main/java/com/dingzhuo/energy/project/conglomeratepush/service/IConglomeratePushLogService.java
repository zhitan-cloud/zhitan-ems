package com.dingzhuo.energy.project.conglomeratepush.service;

/**
 * 接口
 * @author zhw
 */
public interface IConglomeratePushLogService {
    /**
     * 发送集团对接信息
     * @param jsonData
     * @param pushCount
     */
    void sendConglomerate(String jsonData,long pushCount);

    public void test();
}
