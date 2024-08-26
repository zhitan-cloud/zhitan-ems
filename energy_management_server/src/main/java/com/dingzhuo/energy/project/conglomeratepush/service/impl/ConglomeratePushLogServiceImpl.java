package com.dingzhuo.energy.project.conglomeratepush.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.project.conglomeratepush.HttpUtil;
import com.dingzhuo.energy.project.conglomeratepush.domain.ConglomeratePushLog;
import com.dingzhuo.energy.project.conglomeratepush.mapper.ConglomeratePushLogMapper;
import com.dingzhuo.energy.project.conglomeratepush.service.IConglomeratePushLogService;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhw
 */
@Service
public class ConglomeratePushLogServiceImpl  implements IConglomeratePushLogService {

    @Autowired
    private ConglomeratePushLogMapper conglomeratePushLogMapper;

    @Override
    public void test(){
        ConglomeratePushLog conglomeratePushLog = new ConglomeratePushLog();
        conglomeratePushLog.setId(UUID.fastUUID().toString());
        conglomeratePushLog.setPushContent("abcd");
        conglomeratePushLog.setPushResult("111");
        conglomeratePushLog.setPushStatus("abssscd");
        conglomeratePushLog.setPushCount(1L);
        conglomeratePushLog.setPushTime(new Date());
        //保存发送信息
        conglomeratePushLogMapper.saveLog(conglomeratePushLog);
    }

    @Override
    public void sendConglomerate(String jsonData,long pushCount){
        ConglomeratePushLog conglomeratePushLog = new ConglomeratePushLog();
        conglomeratePushLog.setId(UUID.fastUUID().toString());
        //记录发送信息的对象
        JSONObject sendResJson=null;
        String exeRes="N";
        try{
            //要发送的数据对象
            jsonData = "";
            sendResJson = send(jsonData);
            //不论成功、失败 都记录到推送 信息发送表中
            String success = sendResJson.getString("success");
            //成功
            if(StringUtils.isNotBlank(success) && StringUtils.equals(success, "true")){
                exeRes="Y";
            }
        }catch (Exception e){
            //记录以下Log 捕获异常不要抛出
            conglomeratePushLog.setErrorInfo(e.getMessage());
        }finally {
            //记录要发送的内容
            conglomeratePushLog.setPushContent(jsonData);
            conglomeratePushLog.setPushResult(ObjectUtils.isNotEmpty(sendResJson)?sendResJson.toJSONString():"");
            conglomeratePushLog.setPushStatus(exeRes);
            conglomeratePushLog.setPushCount(pushCount);
            conglomeratePushLog.setPushTime(new Date());
            //保存发送信息
            conglomeratePushLogMapper.saveLog(conglomeratePushLog);

        }
    }

    private JSONObject send(String jsonData){
        try{
            Map<String,String> params = new HashMap<String, String>();
            params.put("appKey", "");
            params.put("call", "");
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("data", jsonData);//
            Response response = HttpUtil.post(params,"url");
            String res = response.body().string();
            return JSONObject.parseObject(res);
        }catch (Exception e)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", false);
            jsonObject.put("result", false);
            jsonObject.put("error", e.getMessage());
            return jsonObject;
        }
    }
}
