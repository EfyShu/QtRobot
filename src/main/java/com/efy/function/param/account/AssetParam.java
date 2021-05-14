package com.efy.function.param.account;

import com.efy.function.enums.AccountEnum;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 23:34
 * Description :
 **/
@Data
public class AssetParam implements RequestParam {
    @ApiModelProperty(value = "账户类型",required = true,allowableValues = "spot：现货账户， margin：逐仓杠杆账户，otc：OTC 账户，super-margin：全仓杠杆账户")
    private String accountType = AccountEnum.ACCOUNT_TYPE_SPOT.code;
    @ApiModelProperty(value = "资产估值法币，即资产按哪个法币为单位进行估值。",allowableValues = "可选法币有：BTC、CNY、USD、JPY、KRW、GBP、TRY、EUR、RUB、VND、HKD、TWD、MYR、SGD、AED、SAR （大小写敏感）")
    private String valuationCurrency = AccountEnum.ACCOUNT_CURRENCY_CNY.code;
    @ApiModelProperty(value = "子用户的 UID,若不填，则返回API key所属用户的账户资产估值")
    private String subUid;
}
