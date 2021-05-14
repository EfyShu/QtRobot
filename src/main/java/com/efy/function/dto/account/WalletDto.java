package com.efy.function.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/12 18:05
 * Description :
 **/
@Data
public class WalletDto {
    @ApiModelProperty("可交易余额")
    private String tradeBalance = "0.0";
    @ApiModelProperty("冻结余额")
    private String frozenBalance = "0.0";
}
