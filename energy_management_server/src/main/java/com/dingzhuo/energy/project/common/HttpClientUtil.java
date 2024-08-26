package com.dingzhuo.energy.project.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @Description: http请求工具类
 * @author: yxw
 * @date: 2022年03月18日 17:39
 */
public class HttpClientUtil {
    public static String doGet(String url, String param, String authorization) {
        String charset = "UTF-8";
        String body = "";
        try {
            // 参考资料：https://blog.csdn.net/weixin_44146379/article/details/109809386
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGetWithEntity httpGetWithEntity = new HttpGetWithEntity(url);
            HttpEntity httpEntity = new StringEntity(param, ContentType.APPLICATION_JSON);
            httpGetWithEntity.setEntity(httpEntity);
            httpGetWithEntity.setHeader("Authorization", authorization);
            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpGetWithEntity);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, charset);
            }
            //释放链接
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }
}
