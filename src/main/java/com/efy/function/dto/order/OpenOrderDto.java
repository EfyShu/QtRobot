package com.efy.function.dto.order;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 18:55
 * Description :
 **/
@Data
public class OpenOrderDto {
    @ApiModelProperty("订单id，无大小顺序，可作为下一次翻页查询请求的from字段")
    private Integer id;
    @JSONField(name = "client-order-id")
    @ApiModelProperty("用户自编订单号（所有open订单可返回client-order-id）")
    private String clientOrderId;
    @ApiModelProperty("交易对, 例如btcusdt,ethbtc")
    private String symbol;
    @ApiModelProperty("limit order的交易价格")
    private String price;
    @JSONField(name = "created-at")
    @ApiModelProperty("订单创建的调整为新加坡时间的时间戳，单位毫秒")
    private Integer createdAt;
    @ApiModelProperty("订单类型")
    private String type;
    @JSONField(name = "illed-amount")
    @ApiModelProperty("订单中已成交部分的数量")
    private String illedAmount;
    @JSONField(name = "filled-amount")
    @ApiModelProperty("订单中已成交部分的数量")
    private String filledAmount;
    @JSONField(name = "filled-cash-amount")
    @ApiModelProperty("订单中已成交部分的总价格")
    private String filledCashAmount;
    @JSONField(name = "filled-fees")
    @ApiModelProperty("已交交易手续费总额（准确数值请参考matchresults接口）")
    private String filledFees;
    @ApiModelProperty("订单来源")
    private String source;
    @ApiModelProperty("订单状态，包括created, submitted, partial_filled")
    private String state;
    @JSONField(name = "stop-price")
    @ApiModelProperty("止盈止损订单触发价格")
    private String stopPrice;
    @ApiModelProperty("止盈止损订单触发价运算符")
    private String operator;
}
