package com.efy.function.enums;

import lombok.AllArgsConstructor;

/**
 * Author : Efy Shu
 * Date : 2021/5/12 12:39
 * Description :
 **/
@AllArgsConstructor
public enum OrderEnum {
    /**订单来源**/
    ORDER_SOURCE_SPOT_API("spot-api","现货API交易"),
    ORDER_SOURCE_MARGIN_API("margin-api","逐仓杠杆API交易"),
    ORDER_SOURCE_SUPER_MARGIN_API("super-margin-api","全仓杠杆API交易"),
    ORDER_SOURCE_C2C_MARGIN_API("c2c-margin-api","C2C杠杆API交易"),
    ORDER_SOURCE_GRID_TRADING_SYS_API("grid-trading-sys","网格交易（暂不支持API下单）"),


    /**方向**/
    ORDER_DIRECTION_BUY("buy","买"),
    ORDER_DIRECTION_SELL("sell","卖"),

    /**行为**/
    ORDER_OPERATION_MARKET("market","市价"),
    ORDER_OPERATION_LIMIT("limit","限价"),
    ORDER_OPERATION_IOC("ioc","立即交易否则取消（部分成交后，剩余部分也会被取消）"),
    ORDER_OPERATION_LIMIT_MAKER("limit-maker","限价挂单(若订单会作为taker成交，则会被直接取消)"),
    ORDER_OPERATION_LIMIT_FOK("limit-fok","立即完全成交否则完全取消"),
    ORDER_OPERATION_MARKET_GRID("market-grid","网格交易市价单（暂不支持API下单）"),
    ORDER_OPERATION_STOP_LIMIT("stop-limit","止盈止损单（已被委托代替）"),

    /**订单状态(-1表示订单因过期太久已关闭)**/
    ORDER_STATE_CREATED("created","已创建,该状态订单尚未进入撮合队列"),
    ORDER_STATE_SUBMITTED("submitted","已挂单等待成交，该状态订单已进入撮合队列当中"),
    ORDER_STATE_PARTIAL_FILLED("partial-filled","部分成交"),
    ORDER_STATE_FILLED("filled","已成交"),//6
    ORDER_STATE_PARTIAL_CANCELED("partial-canceled","部分成交撤销"),//5
    ORDER_STATE_CANCELING("canceling","撤销中"),//10
    ORDER_STATE_CANCELED("canceled","已撤销"),//7

    ;

    public String code;
    public String desc;
}
