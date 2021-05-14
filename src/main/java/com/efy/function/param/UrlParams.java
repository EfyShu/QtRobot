package com.efy.function.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.efy.constant.HuobiOpts;
import lombok.Data;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 23:20
 * Description :
 **/
@Data
public class UrlParams {
    private Map<String,Object> params = new TreeMap<>();

    public UrlParams() {
    }

    public <T extends RequestParam> UrlParams(T param){
        this.setParams(param);
    }

    public String buildParam(){
        StringBuffer paramsBuffer = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!("").equals(paramsBuffer.toString())) {
                paramsBuffer.append("&");
            }
            paramsBuffer.append(entry.getKey());
            paramsBuffer.append("=");
            paramsBuffer.append(urlEncode(entry.getValue().toString()));
        }
        return paramsBuffer.toString();
    }

    public RequestBody buildBody(){
        if(params.isEmpty()){
            return RequestBody.create("", MediaType.get("application/json"));
        }
        return RequestBody.create(JSON.toJSONString(params), MediaType.get("application/json"));
    }

    private String urlEncode(String param){
        try {
            return URLEncoder.encode(param, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private long epochNow() {
        return Instant.now().getEpochSecond();
    }

    public String gmtNow() {
        return Instant.ofEpochSecond(epochNow()).atZone(HuobiOpts.ZONE_GMT).format(HuobiOpts.DT_FORMAT);
    }

    /**
     * 参数类
     * @param params
     */
    public <T extends RequestParam> void setParams(T params){
//        System.out.println("UrlParams:" + params);
        Map<String,Object> map = JSON.parseObject(JSON.toJSONString(params),new TypeReference<Map<String,Object>>(){});
        this.params = new TreeMap<>(map);
    }
}
