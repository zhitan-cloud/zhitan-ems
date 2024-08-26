package com.dingzhuo.energy.project.conglomeratepush;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 *OkHttpClient 发送
 *
 * @author zhw
 * @since 2022-03-09
 */
public class HttpUtil {


    /**
     * post请求
     * @param params
     * @param url
     * @return
     */
    public static Response post(Map<String,String> params, String url){

        OkHttpClient client = new OkHttpClient();
		FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.add(entry.getKey(),entry.getValue());
        }
        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *get请求
     * @param url
     * @return
     */
	public static Response get(String url){
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
			.url(url)
			.build();
		try {
			Response response = client.newCall(request).execute();
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
