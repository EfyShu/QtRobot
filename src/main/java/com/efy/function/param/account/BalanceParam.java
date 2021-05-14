package com.efy.function.param.account;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.constant.DataMarket;
import com.efy.function.enums.AccountEnum;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 22:47
 * Description :
 **/
@Data
public class BalanceParam implements RequestParam {
    @JSONField(name = "account-id")
    @ApiModelProperty("账户唯一ID")
    private String accountId = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getId();

}
