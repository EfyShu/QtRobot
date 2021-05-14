package com.efy.function.dto.market;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 2:19
 * Description :
 **/
@Data
public class KLineDto {
    @ApiModelProperty("The UNIX timestamp in seconds as response id")
    private Long id;
    @ApiModelProperty("Accumulated trading volume, in base currency")
    private Float amount;
    @ApiModelProperty("The number of completed trades")
    private Integer count;
    @ApiModelProperty("The opening price")
    private Float open;
    @ApiModelProperty("The closing price")
    private Float close;
    @ApiModelProperty("The low price")
    private Float low;
    @ApiModelProperty("The high price")
    private Float high;
    @ApiModelProperty("Accumulated trading value, in quote currency")
    private Float vol;
}
