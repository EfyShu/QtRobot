package com.efy.function.dto.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.function.enums.OrderEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 0:35
 * Description :
 **/
@Data
public class MatchedOrderDto extends OrderDto{
    @JSONField(name = "filled-fees")
    @ApiModelProperty("交易手续费（正值）或交易返佣（负值）")
    private String filledFees;
    @JSONField(name = "fee-currency")
    @ApiModelProperty("交易手续费或交易返佣币种（买单的交易手续费币种为基础币种，卖单的交易手续费币种为计价币种；买单的交易返佣币种为计价币种，卖单的交易返佣币种为基础币种）")
    private String feeCurrency;
    @JSONField(name = "match-id")
    @ApiModelProperty("撮合 ID")
    private Integer matchId;
    @JSONField(name = "trade-id")
    @ApiModelProperty("唯一成交编号")
    private Integer tradeId;
    @ApiModelProperty("成交角色maker,taker")
    private String role;
    @JSONField(name = "filled-points")
    @ApiModelProperty("抵扣数量（可为ht或hbpoint）")
    private String filledPoints;
    @JSONField(name = "fee-deduct-currency")
    @ApiModelProperty("抵扣类型ht,hbpoint")
    private String feeDeductCurrency;
    @JSONField(name = "fee-deduct-state")
    @ApiModelProperty("抵扣状态 抵扣中：ongoing，抵扣完成：done")
    private String feeDeductState;

    public String getState(String state){
        return OrderEnum.ORDER_STATE_FILLED.code;
    }
}
