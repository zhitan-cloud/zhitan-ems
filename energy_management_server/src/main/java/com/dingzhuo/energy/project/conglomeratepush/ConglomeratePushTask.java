package com.dingzhuo.energy.project.conglomeratepush;

import com.dingzhuo.energy.project.conglomeratepush.service.IConglomeratePushLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 集团投递任务
 * @author zhw
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ConglomeratePushTask {

    private IConglomeratePushLogService pushLogService;
    @Autowired
    public ConglomeratePushTask(IConglomeratePushLogService pushLogService) {
        this.pushLogService = pushLogService;
    }

    /**
     * 添加定时任务
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        //读取要投递的数据对象
        List<Object> pushDataList = new ArrayList<>();
        //组织投递报文
        StringBuffer pushMessageBf = new StringBuffer();

//        pushLogService.sendConglomerate(pushMessageBf.toString(),1);
        //测试日志保存
        pushLogService.test();


    }


}
