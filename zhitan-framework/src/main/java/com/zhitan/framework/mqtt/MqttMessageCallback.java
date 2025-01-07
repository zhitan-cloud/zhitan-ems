package com.zhitan.framework.mqtt;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhitan.framework.mqtt.domain.EletricData;
import com.zhitan.realtimedata.data.influxdb.InfluxDBRepository;
import com.zhitan.realtimedata.domain.TagValue;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MqttMessageCallback implements MqttCallback {

    private ExecutorService executorService = Executors.newFixedThreadPool(10); // 创建一个线程池

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

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                final String msg = new String(mqttMessage.getPayload());
                JSONObject jsonObject = JSON.parseObject(msg);
                final String values = jsonObject.getString("values");
                final List<EletricData> eletricData = JSON.parseArray(values, EletricData.class);
                List<TagValue> tagValueList = new ArrayList<>();
                
                //EMS.HB.High.9_42302035834_Voltagec   
                //9 是配电箱，42302035834是电表号
                //结合业务 编写具体信息即可
                eletricData.forEach(ele->{
                    final String key = ele.getKey();
                    TagValue tagValue = new TagValue();
                    tagValue.setDataTime(new DateTime(ele.getTime()));
                    tagValue.setValue(ele.getVaule());
                    final String eNoIndex = key.substring(key.indexOf("_") + 1);
                    String tagValueString = formateTag(eNoIndex);
                    tagValue.setTagCode(tagValueString);
                    tagValueList.add(tagValue);
                });
                repository.store(tagValueList);
            }
        });
        
//      
    }

    //将电表号和指标 42302035834_Voltagec  转换成我们系统的
    private String formateTag(String tag) {
        final String[] tags = tag.split("_");
        String index = tags[1];
        String tagValue = tags[0] + "_";
        switch (index){
            case "PowFactorT":  //功率因数
                tagValue += "PowFactorT";
                break;
            case "Imp":  //正向电能
                tagValue += "ActiveZT";
                break;
//            case "spare":  //
//                tagValue += "";
//                break;
//            case "CurrentD":  //
//                tagValue += "CurrentD";
//                break;
//            case "EXP":  //反向电能
//                tagValue += "";
//                break;
//            case "Total":  //电能总和
//                tagValue += "";
//                break;
            default:
                tagValue = tag;
        }
        return tagValue;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}