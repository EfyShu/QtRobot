package com.efy.function.enums;

import lombok.AllArgsConstructor;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 22:51
 * Description :
 **/
@AllArgsConstructor
public enum AccountEnum {
    /**账户类型**/
    ACCOUNT_TYPE_SPOT("spot","现货账户(币币账户)"),
    ACCOUNT_TYPE_OTC("otc","OTC账户(法币账户)"),
    ACCOUNT_TYPE_MARGIN("margin","逐仓杠杆账户"),
    ACCOUNT_TYPE_SUPER_MARGIN("super-margin","全仓杠杆账户"),
    ACCOUNT_TYPE_POINT("point","点卡账户"),
    ACCOUNT_TYPE_INVESTMENT("investment","C2C杠杆借出账户"),
    ACCOUNT_TYPE_BORROW("borrow","C2C杠杆借入账户"),
    ACCOUNT_TYPE_MINEPOOL("minepool","矿池账户"),
    ACCOUNT_TYPE_ETF("etf","ETF账户"),
    ACCOUNT_TYPE_CRYPTO_LOANS("crypto-loans","抵押借贷账户"),

    /**资产估值币种**/
    ACCOUNT_CURRENCY_BTC("BTC","比特币"),
    ACCOUNT_CURRENCY_CNY("CNY","人民币"),
    ACCOUNT_CURRENCY_HKD("HKD","港币"),
    ACCOUNT_CURRENCY_TWD("TWD","台币"),
    ACCOUNT_CURRENCY_USD("USD","美元"),
    ACCOUNT_CURRENCY_JPY("JPY","日元"),

    /**币种类型**/
    CURRENCY_TYPE_TRADE("trade","可交易"),
    CURRENCY_TYPE_FROZEN("frozen","已冻结"),
    ;

    public String code;
    public String desc;
}
