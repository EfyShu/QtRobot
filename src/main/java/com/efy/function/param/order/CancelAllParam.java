package com.efy.function.param.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 23:53
 * Description :
 **/
@Data
public class CancelAllParam implements RequestParam {
    @JSONField(name = "account-id")
    @ApiModelProperty("账户ID")
    private String accountId;
    @ApiModelProperty("交易代码列表（最多10 个symbols，多个交易代码间以逗号分隔）")
    private String symbol;
    @ApiModelProperty("订单类型组合,使用逗号分割")
    private String types;
    @ApiModelProperty("主动交易方向“buy”或“sell”，缺省将返回所有符合条件尚未成交订单")
    private String side;
    @ApiModelProperty(value = "撤销订单的数量",allowableValues = "[0,100]")
    private Integer size;

}
