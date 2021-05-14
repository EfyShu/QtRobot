package com.efy.function.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 13:06
 * Description :
 **/
@Data
public class BalanceDto {
    @ApiModelProperty("账户ID")
    private String id;
    @ApiModelProperty(value = "账户状态",allowableValues = "working,lock")
    private String state;
    @ApiModelProperty(value = "账户类型",allowableValues = "spot, margin, otc, point, super-margin, investment, borrow")
    private String type;
    @ApiModelProperty("详细列表")
    private List<CurrencyDto> list;
}
