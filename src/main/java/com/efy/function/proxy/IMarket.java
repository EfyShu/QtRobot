package com.efy.function.proxy;

import com.efy.annotations.Module;
import com.efy.function.dto.Result;
import com.efy.function.dto.market.KLineDto;
import com.efy.function.dto.market.MergedDto;
import com.efy.function.dto.market.SymbolsDto;
import com.efy.function.dto.market.TickersDto;
import com.efy.function.param.market.KLineParam;
import com.efy.function.param.market.MergedParam;
import com.efy.function.param.market.TickersParam;

import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/6/7 4:08
 * Description :
 **/
public interface IMarket {
    @Module(value = "获取K线",tags = {"行情类"})
    Result<KLineDto> kline(KLineParam param);

    @Module(value = "单交易对聚合行情",tags = {"行情类"})
    Result<MergedDto> merged(MergedParam param);

    @Module(value = "所有交易对基础信息",tags = {"基础类"})
    Result<List<SymbolsDto>> symbols();

    @Module(value = "全交易对聚合行情",tags = {"行情类"})
    Result<List<TickersDto>> tickers(TickersParam param);
}
