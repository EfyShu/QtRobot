package com.efy.function.param.market;

import com.efy.function.enums.MarketEnum;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 23:59
 * Description :
 **/
@Data
public class MergedParam implements RequestParam {
    @ApiModelProperty(value = "交易对",required = true,allowableValues = "btcusdt, ethbtc...")
    private String symbol = MarketEnum.MARKET_SYMBOL_SHIB_USDT.code;
}
