package com.efy.function.param.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.constant.DataMarket;
import com.efy.function.enums.AccountEnum;
import com.efy.function.enums.MarketEnum;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 18:46
 * Description :
 **/
@Data
public class OpenOrderParam implements RequestParam {
    @JSONField(name = "account-id")
    @ApiModelProperty(value = "账户ID",required = true)
    private String accountId = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getId();
    @ApiModelProperty(value = "交易对",required = true)
    private String symbol = MarketEnum.MARKET_SYMBOL_SHIB_USDT.code;
    @ApiModelProperty(value = "方向,可指定一个方向的订单,默认都返回",allowableValues = "buy,sell,both")
    private String side = "both";
    @ApiModelProperty("订单类型组合,以逗号分割")
    private String types;
    @ApiModelProperty("查询起始ID,向后则赋值为上一次查询结果中得到的最后一条id,向前赋值为上一次查询结果中得到的第一条id")
    private String from;
    @ApiModelProperty(value = "查询方向,from有值,则此值必填,否则可不填",allowableValues = "prev-向前,next-向后")
    private String direct;
    @ApiModelProperty("返回数量,最大500")
    private Integer size = 100;


}
