package com.zhitan.framework.mqtt;

import com.zhitan.realtimedata.data.influxdb.InfluxDBRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttMessageCallback implements MqttCallback {

    @Autowired
    private InfluxDBRepository repository;
    /**
     * 链接丢失时处理
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        //可以做重连 或者 其他业务处理
    }
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("接收到消息具体信息---->{}",new String(mqttMessage.getPayload()));
//        final String msg = new String(mqttMessage.getPayload());
//        JSONObject jsonObject = JSON.parseObject(msg);
//        final String values = jsonObject.getString("values");
//        final List<EletricData> eletricData = JSON.parseArray(values, EletricData.class);
//        List<TagValue> tagValueList = new ArrayList<>();
//        //结合业务 编写具体信息即可
//        eletricData.forEach(ele->{
//            final String key = ele.getKey();
//            TagValue tagValue = new TagValue();
//            tagValue.setDataTime(new DateTime(ele.getTime()));
//            tagValue.setValue(ele.getVaule());
//            tagValue.setTagCode(key.substring(key.lastIndexOf(".") + 1));
//            tagValueList.add(tagValue);
//        });
//        repository.store(tagValueList);
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
