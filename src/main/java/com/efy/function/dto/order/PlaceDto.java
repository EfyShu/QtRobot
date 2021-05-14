package com.efy.function.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/12 12:25
 * Description :
 **/
@Data
public class PlaceDto {
    @ApiModelProperty("订单编号")
    private String orderId;
}
