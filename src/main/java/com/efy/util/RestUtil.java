package com.efy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efy.constant.HuobiOpts;
import com.efy.function.SystemMenu;
import com.efy.function.dto.Result;
import com.efy.function.param.UrlParams;
import com.efy.function.proxy.ISystemMenu;
import com.efy.listener.sys.BeanMap;
import io.swagger.models.HttpMethod;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 22:41
 * Description :
 **/
public class RestUtil {
    private static ConnectionPool connectionPool =
            new ConnectionPool(20, 300, TimeUnit.SECONDS);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .followSslRedirects(false)
            .followRedirects(false)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .connectionPool(connectionPool)
            .addNetworkInterceptor(new Interceptor() {
                @NotNull
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request();

                    Long startNano = System.nanoTime();

                    Response response = chain.proceed(request);

                    Long endNano = System.nanoTime();

                    return response;
                }
            })
            .build();


    public static String execute(Request request) throws IOException {
        Response response = null;
        try {
            String responseBody;
            ISystemMenu systemMenu = BeanMap.getBean(SystemMenu.class);
            systemMenu.printDebug("Rest调用:"+request.url());
            response = client.newCall(request).execute();
            if(!response.isSuccessful() || response == null || response.body() == null){
                systemMenu.printError("Rest调用失败");
                systemMenu.printError(response.toString());
            }
            responseBody = response.body().string();
            systemMenu.printDebug("调用结果:"+responseBody);
            return responseBody;
        } catch (IOException e) {
            throw e;
        } finally {
            response.close();
        }
    }

    public static <T> Result<T> get(String apiUrl, String accessKey,String secretKey,UrlParams params,Class responseType){
        String sign = Signature.create(apiUrl, HttpMethod.GET.name(),accessKey,secretKey,params);
        params.getParams().put(HuobiOpts.SIGNATURE_NAME,sign);
        String url = "https://" + HuobiOpts.API_HOST + apiUrl + "?" + params.buildParam();
        Request executeRequest = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36")
                .build();

        String responseBody;
        try {
            responseBody = execute(executeRequest);
        } catch (Exception e) {
            return Result.fail("连接失败.请检查网络" + e.getMessage());
        }
        return assemblyResponse(responseBody,responseType);
    }

    public static <T> Result<T> post(String apiUrl, String accessKey,String secretKey,UrlParams params,Class responseType){
        String sign = Signature.create(apiUrl, HttpMethod.POST.name(),accessKey,secretKey,params);
        params.getParams().put(HuobiOpts.SIGNATURE_NAME,sign);
        String url = "https://" + HuobiOpts.API_HOST + apiUrl + "?" + params.buildParam();
        Request executeRequest = new Request.Builder()
                .url(url)
                .post(params.buildBody())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36")
                .build();
        String responseBody;
        try {
            responseBody = execute(executeRequest);
        } catch (Exception e) {
            return Result.fail("连接失败.请检查网络" + e.getMessage());
        }
        return assemblyResponse(responseBody,responseType);
    }

    private static <T> Result<T> assemblyResponse(String responseBody,Class responseType){
        if(responseBody == null || responseBody.trim().isEmpty()){
            return Result.fail();
        }
        JSONObject result = JSON.parseObject(responseBody);
        String status = result.getString("status");
        String code = result.getString("code");
        if("error".equals(status)){
            return Result.fail(result.getString("err-code"),result.getString("err-msg"),(T)result.get("order-state"));
        }else if(code != null && !"200".equals(code)){
            return Result.fail(code,result.getString("message"),null);
        }
        Object dataObj = null;
        if(result.containsKey("data") && result.get("data") != null){
            dataObj = result.get("data");
        //补充tick返回值
        }else if(result.containsKey("tick") || result.get("tick") != null){
            dataObj =result.get("tick");
        }else{
            dataObj = null;
        }
        if(dataObj != null){
            T data = decodeData(dataObj,responseType);
            return Result.ok(data);
        }
        return Result.ok();
    }

    private static <T> T decodeData(Object rawData,Class responseType){
        T data = null;
        if(rawData instanceof JSONArray){
            JSONArray arrayData = (JSONArray) rawData;
            List<T> listData = new ArrayList<>();
            for(int i=0;i<arrayData.size();i++){
                listData.add((T) arrayData.getObject(i,responseType));
            }
            data = (T) listData;
        }else if(rawData instanceof JSONObject){
            data = (T) JSON.parseObject(((JSONObject)rawData).toJSONString(),responseType);
        }else{
            data = (T) rawData;
        }
        return data;
    }
}
