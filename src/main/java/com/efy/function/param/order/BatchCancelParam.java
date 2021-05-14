package com.efy.function.param.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 23:56
 * Description :
 **/
@Data
public class BatchCancelParam implements RequestParam {
    @JSONField(name = "order-ids")
    @ApiModelProperty("订单编号组(与order-ids互斥),单次不超过50个")
    private String[] orderIds;
    @JSONField(name = "client-order-ids")
    @ApiModelProperty("自定义订单编号组(与order-ids互斥),单次不超过50个")
    private String[] clientOrderIds;
}
