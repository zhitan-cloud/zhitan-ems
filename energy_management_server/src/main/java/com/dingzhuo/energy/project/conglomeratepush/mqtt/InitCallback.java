package com.dingzhuo.energy.project.conglomeratepush.mqtt;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.project.gateway.domain.GatewayHbtLog;
import com.dingzhuo.energy.project.gateway.domain.GatewaySetting;
import com.dingzhuo.energy.project.gateway.service.IGatewayHbtLogService;
import com.dingzhuo.energy.project.gateway.service.IGatewaySettingService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;

/**
 * MQTT回调函数
 *MqttClientCallback 回调函数类中可能不能用 @Autowired 注入的， 解决办法  SpringContextHolder 类引入
 * @author zhw
 * @since 2022/04/05
 */
@Slf4j
@Component
public class InitCallback implements MqttCallback {

  @Autowired
  private IGatewayHbtLogService gatewayHbtLogService;

  @Autowired
  private IGatewaySettingService gatewaySettingService;

  /**
   * MQTT 断开连接会执行此方法
   */
  @Override
  public void connectionLost(Throwable cause)
  {
    log.error(cause.getMessage(), cause);
  }

  /**
   * publish发布成功后会执行到这里
   */
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    //发布完成后 记录日志信息 并断开连接
  }

  /**
   * subscribe订阅后得到的消息会执行到这里
   */
  @Override
  public void messageArrived(String topic, MqttMessage message) {
    String result = new String(message.getPayload(), StandardCharsets.UTF_8);
     try {
//      JSONObject jsonObject = JSON.parseObject(result);
//       Date hbtTime =  jsonObject.getDate("ts");
//       Iterator<String> keys =  jsonObject.getJSONObject("d").keySet().iterator();
//       if(keys.hasNext())
//       {
//         String key = keys.next();
//         //更新实时状态
//         GatewaySetting gatewaySetting = new GatewaySetting();
//         gatewaySetting.setGatewayNum(key);
//         gatewaySetting.setHbtTime(hbtTime);
//         gatewaySettingService.updateGatewaySettingByNum(gatewaySetting);
//
//         //记录日志表
//         GatewayHbtLog gatewayHbtLog = new GatewayHbtLog();
//         gatewayHbtLog.setId(UUID.fastUUID().toString());
//         gatewayHbtLog.setGatewayNo(key);
//         gatewayHbtLog.setHbtTime(hbtTime);
//         gatewayHbtLog.setContent(result);
//         gatewayHbtLogService.insertGatewayHbtLog(gatewayHbtLog);
//       }

    } catch (JSONException e) {
      log.error("JSON Format Parsing Exception : {}", result);
    }
  }
}
