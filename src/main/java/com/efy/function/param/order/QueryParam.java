package com.efy.function.param.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 0:21
 * Description :
 **/
@Data
public class QueryParam implements RequestParam {
    @JSONField(name = "order-id")
    @ApiModelProperty(value = "订单ID，填在path中",required = true)
    private String orderId;
}
