package com.efy.function.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 13:08
 * Description :
 * BalanceDto的子类
 **/
@Data
public class CurrencyDto {
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty(value = "币种类型",allowableValues = "trade, frozen")
    private String type;
    @ApiModelProperty("币种余额")
    private String balance;
}
