package com.efy.function.enums;

import lombok.AllArgsConstructor;

/**
 * Author : Efy Shu
 * Date : 2021/5/12 0:03
 * Description :
 **/
@AllArgsConstructor
public enum MarketEnum {
    /**交易对**/
    MARKET_SYMBOL_BTC_USDT("btcusdt","比特币/USDT"),
    MARKET_SYMBOL_DOGE_USDT("dogeusdt","狗狗币/USDT"),
    MARKET_SYMBOL_SHIB_USDT("shibusdt","柴犬币/USDT"),

    /**行情粒度**/
    MARKET_PERIOD_1_MIN("1min","1分钟"),
    MARKET_PERIOD_5_MIN("5min","5分钟"),
    MARKET_PERIOD_15_MIN("15min","15分钟"),
    MARKET_PERIOD_30_MIN("30min","30分钟"),
    MARKET_PERIOD_60_MIN("60min","60分钟"),
    MARKET_PERIOD_4_HOUR("4hour","4小时"),
    MARKET_PERIOD_1_DAY("1day","按天"),
    MARKET_PERIOD_1_MON("1mon","按月"),
    MARKET_PERIOD_1_WEEK("1week","按周"),
    MARKET_PERIOD_1_YEAR("1year","按年"),


    ;

    public String code;
    public String desc;
}
