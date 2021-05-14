package com.efy.function.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 22:31
 * Description :
 **/
@Data
public class AccountDto {
    @ApiModelProperty("ID")
    private String id;
    @ApiModelProperty("账户类型(spot：现货账户, margin：逐仓杠杆账户, otc：OTC 账户, point：点卡账户, super-margin：全仓杠杆账户, investment: C2C杠杆借出账户, borrow: C2C杠杆借入账户，矿池账户: minepool, ETF账户: etf, 抵押借贷账户: crypto-loans)")
    private String type;
    @ApiModelProperty("子账户类型(逐仓杠杆交易标的，例如btcusdt)")
    private String subtype;
    @ApiModelProperty("账户状态(working：正常, lock：账户被锁定)")
    private String state;


    /**通过其他接口获得**/
    @ApiModelProperty("账号钱包")
    private Map<String,WalletDto> wallet = new HashMap<>();
    @ApiModelProperty("资产估值(按请求币种估值)")
    private String valuation = "0.0";

    public void setWallet(Map<String,WalletDto> wallet) {
        this.wallet = (wallet == null) ? new HashMap<>() : wallet;
    }

    public void setValuation(String valuation) {
        this.valuation = (valuation == null || valuation.isEmpty()) ? "0.0" : valuation;
    }
}
