
package com.dingzhuo.energy.project.conglomeratepush.mapper;


import com.dingzhuo.energy.project.conglomeratepush.domain.ConglomeratePushLog;

/**
 * 集团信息推送表 Mapper 接口
 *
 * @author zhw
 * @since 2022-03-09
 */
public interface ConglomeratePushLogMapper{

  int  saveLog(ConglomeratePushLog conglomeratePushLog);

}
