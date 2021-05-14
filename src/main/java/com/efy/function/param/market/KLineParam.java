package com.efy.function.param.market;

import com.efy.function.enums.MarketEnum;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 2:19
 * Description :
 **/
@Data
public class KLineParam implements RequestParam {
    @ApiModelProperty(value = "币种标识",example = "btcusdt")
    private String symbol = MarketEnum.MARKET_SYMBOL_SHIB_USDT.code;
    @ApiModelProperty(value = "K线周期",example = "1min",allowableValues = "1min, 5min, 15min, 30min, 60min, 4hour, 1day, 1mon, 1week, 1year")
    private String period = MarketEnum.MARKET_PERIOD_1_MIN.code;
    @ApiModelProperty(value = "结果集大小",example = "5")
    private Integer size = 5;
}
