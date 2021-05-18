package com.efy.function.dto.order;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 0:12
 * Description :
 **/
@Data
public class QueryDto extends OrderDto{
    @JSONField(name = "account-id")
    @ApiModelProperty(value = "账户 ID")
    private String accountId;
    @ApiModelProperty(value = "订单数量")
    private String amount;
    @JSONField(name = "canceled-at")
    @ApiModelProperty(value = "订单撤销时间")
    private Long canceledAt;
    @JSONField(name = "field-cash-amount")
    @ApiModelProperty(value = "已成交总金额")
    private String fieldCashAmount;
    @JSONField(name = "field-fees")
    @ApiModelProperty(value = "已成交手续费")
    private String fieldFees;
    @JSONField(name = "finished-at")
    @ApiModelProperty(value = "订单变为终结态的时间")
    private Long finishedAt;
    @JSONField(name = "client-order-id")
    @ApiModelProperty(value = "用户自编订单号")
    private String clientOrderId;
    @JSONField(name = "stop-price")
    @ApiModelProperty(value = "止盈止损订单触发价格")
    private String stopPrice;
    @ApiModelProperty(value = "止盈止损订单触发价运算符(gte,lte)")
    private String operator;
}
