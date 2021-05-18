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
public class OpenOrderDto extends OrderDto{
    @JSONField(name = "client-order-id")
    @ApiModelProperty("用户自编订单号（所有open订单可返回client-order-id）")
    private String clientOrderId;
    @JSONField(name = "filled-cash-amount")
    @ApiModelProperty("订单中已成交部分的总价格")
    private String filledCashAmount;
    @JSONField(name = "filled-fees")
    @ApiModelProperty("已交交易手续费总额（准确数值请参考matchresults接口）")
    private String filledFees;
    @JSONField(name = "stop-price")
    @ApiModelProperty("止盈止损订单触发价格")
    private String stopPrice;
    @ApiModelProperty("止盈止损订单触发价运算符")
    private String operator;
}
