package com.efy.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 14:28
 * Description :
 **/
public class HuobiOpts {
    //API域名
    public static final String API_HOST = "open.huobigroup.com";

    //访问key参数名
    public static final String ACCESS_KEY_NAME = "AccessKeyId";

    //签名域名(与api域名不一致!!!)
    public static final String SIGN_HOST = "api.huobi.pro";
    //签名方式
    public static final String SIGNATURE_METHOD_NAME = "SignatureMethod";
    public static final String SIGNATURE_METHOD_VALUE = "HmacSHA256";

    //验签版本
    public static final String SIGNATURE_VERSION_NAME = "SignatureVersion";
    public static final String SIGNATURE_VERSION_VALUE = "2";

    //签名字符串参数名
    public static final String SIGNATURE_NAME = "Signature";

    //时间戳参数名
    public static final String TIMESTAMP_NAME = "Timestamp";
    //时间戳格式化模板
    public static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    //时区
    public static final ZoneId ZONE_GMT = ZoneId.of("Z");
}
