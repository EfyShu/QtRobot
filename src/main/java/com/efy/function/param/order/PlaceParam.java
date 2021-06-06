package com.efy.function.param.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.constant.DataMarket;
import com.efy.function.enums.AccountEnum;
import com.efy.function.enums.MarketEnum;
import com.efy.function.enums.OrderEnum;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/12 12:27
 * Description :
 **/
@Data
public class PlaceParam implements RequestParam {
    @JSONField(name = "account-id")
    @ApiModelProperty(value = "账号ID",required = true)
    private String accountId = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getId();
    @ApiModelProperty(value = "交易对",required = true)
    private String symbol = MarketEnum.MARKET_SYMBOL_SHIB_USDT.code;
    @ApiModelProperty(value = "订单类型,通过方向(buy,sell)-行为类型(market,limit...)组合",required = true)
    private String type = OrderEnum.ORDER_DIRECTION_BUY.code + "-" + OrderEnum.ORDER_OPERATION_MARKET.code;
    @ApiModelProperty(value = "订单交易量（市价买单为订单交易额,尽量使用整数）",required = true)
    private String amount;
    @ApiModelProperty("订单价格（对市价单无效）")
    private String price;
    @ApiModelProperty(value = "交易来源",allowableValues = "spot-api,margin-api,super-margin-api,c2c-margin-api")
    private String source = OrderEnum.ORDER_SOURCE_SPOT_API.code;
    @JSONField(name = "client-order-id")
    @ApiModelProperty("用户自编订单号（最大长度64个字符，须在24小时内保持唯一性）")
    private String clientOrderId;
    @JSONField(name = "stop-price")
    @ApiModelProperty("止盈止损订单触发价")
    private String stopPrice;
    @ApiModelProperty(value = "止盈止损订单运算符",allowableValues = ">=,<=")
    private String operator;
}
