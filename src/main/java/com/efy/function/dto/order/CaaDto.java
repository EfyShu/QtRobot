package com.efy.function.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 23:46
 * Description :
 **/
@Data
public class CaaDto {
    @ApiModelProperty("当前时间")
    private Long currentTime;
    @ApiModelProperty("出发时间")
    private Long triggerTime ;
}
