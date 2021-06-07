package com.efy.function.param.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 0:59
 * Description :
 **/
@Data
public class CancelParam implements RequestParam {
    @JSONField(name = "order-id")
    @ApiModelProperty(value = "订单编号",required = true)
    private String orderId;
}
