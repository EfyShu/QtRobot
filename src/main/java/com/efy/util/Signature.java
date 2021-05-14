package com.efy.util;

import com.efy.constant.HuobiOpts;
import com.efy.function.param.UrlParams;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 22:22
 * Description :
 **/
public class Signature {
    /**
     * 获取签名
     * @param apiUrl api地址
     * @param method 访问方式(GET,POST)
     * @param accessKey
     * @param secretKey
     * @param params 参数列表
     * @return
     */
    public static String create(String apiUrl, String method,String accessKey,String secretKey, UrlParams params){
        StringBuffer rawSign = new StringBuffer();
        //基础参数
        rawSign.append(method.toUpperCase()).append("\n");
        rawSign.append(HuobiOpts.SIGN_HOST).append("\n");
        rawSign.append(apiUrl).append("\n");

        //API参数
        params.getParams().put(HuobiOpts.ACCESS_KEY_NAME,accessKey);
        params.getParams().put(HuobiOpts.SIGNATURE_VERSION_NAME,HuobiOpts.SIGNATURE_VERSION_VALUE);
        params.getParams().put(HuobiOpts.SIGNATURE_METHOD_NAME,HuobiOpts.SIGNATURE_METHOD_VALUE);
        params.getParams().put(HuobiOpts.TIMESTAMP_NAME,params.gmtNow());
        rawSign.append(params.buildParam());

        //生成Hash
        try {
            Mac hmacSha256 = Mac.getInstance(HuobiOpts.SIGNATURE_METHOD_VALUE);
            SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
                    HuobiOpts.SIGNATURE_METHOD_VALUE);
            hmacSha256.init(secKey);
            String payload = rawSign.toString();
            byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getEncoder().encodeToString(hash);
            return signature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
