package com.efy.function.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 0:00
 * Description :
 **/
@Data
public class BatchCancelDto {
    @ApiModelProperty("撤单成功订单列表")
    private String[] success;
    @ApiModelProperty("撤单失败订单列表")
    private List<BatchCancelFailedDto> failed;
}
