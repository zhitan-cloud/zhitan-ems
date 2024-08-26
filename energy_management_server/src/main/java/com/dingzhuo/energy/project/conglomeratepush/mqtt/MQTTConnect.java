package com.dingzhuo.energy.project.conglomeratepush.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * MQTT工具类操作
 *
 * @author zhw
 * @since 2022/04/05
 */
@Slf4j
@Component
public class MQTTConnect {

  @Value("${mqtt.host}")
  private String HOST;
  private final String clientId = "ClientHBT" + (int) (Math.random() * 100000000);
  private MqttClient mqttClient;

  /**
   * 客户端connect连接mqtt服务器
   *
   * @param username 用户名
   * @param password 密码
   * @param mqttCallback 回调函数
   **/
  public void setMqttClient(String username, String password, MqttCallback mqttCallback)
      throws MqttException {
    System.out.println("MQTT服务器连接开始...........");
    MqttConnectOptions options = mqttConnectOptions(username, password);
    mqttClient.setCallback(mqttCallback);
    mqttClient.connect(options);
  }

  /**
   * MQTT连接参数设置
   */
  private MqttConnectOptions mqttConnectOptions(String userName, String passWord)
      throws MqttException {
    mqttClient = new MqttClient(HOST, clientId, new MemoryPersistence());
    MqttConnectOptions options = new MqttConnectOptions();
    options.setUserName(userName);
    options.setPassword(passWord.toCharArray());
    options.setConnectionTimeout(10);///默认：30
    options.setAutomaticReconnect(true);//默认：false
    options.setCleanSession(false);//默认：true
    //options.setKeepAliveInterval(20);//默认：60
    return options;
  }

  /**
   * 关闭MQTT连接
   */
  public void close() throws MqttException {
    try {
      if(mqttClient!=null){
        if(mqttClient.isConnected()){
          mqttClient.unsubscribe(clientId);
        }
        mqttClient.disconnect();
        mqttClient.close();
      }

    }catch (Exception e){
      e.printStackTrace();
    }

  }

  /**
   * 向某个主题发布消息 默认qos：1
   */
  public void pub(String topic, String msg) throws MqttException {
    MqttMessage mqttMessage = new MqttMessage();
    //mqttMessage.setQos(2);
    mqttMessage.setPayload(msg.getBytes());
    MqttTopic mqttTopic = mqttClient.getTopic(topic);
    MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
    token.waitForCompletion();
  }

  /**
   * 向某个主题发布消息
   *
   * @param topic: 发布的主题
   * @param msg: 发布的消息
   * @param qos: 消息质量    Qos：0、1、2
   */
  public void pub(String topic, String msg, int qos) throws MqttException {
    MqttMessage mqttMessage = new MqttMessage();
    mqttMessage.setQos(qos);
    mqttMessage.setPayload(msg.getBytes());
    MqttTopic mqttTopic = mqttClient.getTopic(topic);
    MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
    token.waitForCompletion();
  }

  /**
   * 订阅某一个主题 ，此方法默认的的Qos等级为：1
   *
   * @param topic 主题
   */
  public void sub(String topic) throws MqttException {
    mqttClient.subscribe(topic);
  }

  /**
   * 订阅某一个主题，可携带Qos
   *
   * @param topic 所要订阅的主题
   * @param qos 消息质量：0、1、2
   */
  public void sub(String topic, int qos) throws MqttException {
    mqttClient.subscribe(topic, qos);
  }

  public static void main(String[] args) throws MqttException {
    //测试连接及主动订阅信息 输出 通过
    //要做推送 直接在计划任务中连接推送即可，做全局conn对象，然后 每次计划任务轮询发送前判断
    //对象是否存在且 没有关闭 要进行处理
//    MQTTConnect mqttConnect = new MQTTConnect();
//    String msg = "Mr.Qu" + (int) (Math.random() * 100000000);
//    mqttConnect.setMqttClient("dingzhuo", "dingzhuo@2022", new InitCallback());
//    mqttConnect.sub("iot-2/evt/waconn/#",0);
//    try {
//      Thread.sleep(5000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//    mqttConnect.close();
  }
}
