package com.efy.function.dto.order;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Project QtRobot
 * @Date 2021/5/18 14:32
 * @Created by Efy
 * @Description TODO
 */
@Data
public class OrderDto {
    @ApiModelProperty("订单成交记录 ID，无大小顺序，可作为下一次翻页查询请求的from字段")
    private Long id;
    @JSONField(name = "created-at")
    @ApiModelProperty("该成交记录创建的时间戳（略晚于成交时间）")
    private Long createdAt;
    @ApiModelProperty("订单来源")
    private String source;
    @JSONField(name = "order-id")
    @ApiModelProperty("订单 ID")
    private Long orderId;
    @ApiModelProperty("订单类型")
    private String type;
    @ApiModelProperty("交易对, 例如btcusdt,ethbtc")
    private String symbol;
    @ApiModelProperty("limit order的交易价格")
    private String price;
    @JSONField(name = "filled-amount")
    @ApiModelProperty("成交数量")
    private String filledAmount;
    @ApiModelProperty("订单状态，包括created, submitted, partial_filled")
    private String state;
}
