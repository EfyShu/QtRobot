package com.efy.function;

import com.efy.annotations.Function;
import com.efy.annotations.Module;
import com.efy.constant.DataMarket;
import com.efy.function.dto.Result;
import com.efy.function.dto.market.KLineDto;
import com.efy.function.dto.market.MergedDto;
import com.efy.function.dto.market.SymbolsDto;
import com.efy.function.dto.market.TickersDto;
import com.efy.function.param.UrlParams;
import com.efy.function.param.market.KLineParam;
import com.efy.function.param.market.MergedParam;
import com.efy.function.param.market.TickersParam;
import com.efy.function.proxy.IMarket;
import com.efy.util.NumberUtil;
import com.efy.util.RestUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 14:56
 * Description :
 **/
@Function
public class Market implements IMarket {
    //获取K线接口地址
    public static final String KLINE = "/market/history/kline";
    //单交易对聚合行情接口地址
    public static final String MERGED = "/market/detail/merged";
    //全交易对聚合行情接口地址
    public static final String SYMBOLS = "/v1/common/symbols";
    //全交易对聚合行情接口地址
    public static final String TICKERS = "/market/tickers";
    //涨幅榜保留数据个数(每次保存全量数据,需定时滚动,否则内存无限消耗)
    public static final int billBoardCount = 5;

    @Override
    @Module(value = "获取K线",tags = {"行情类"})
    public Result<KLineDto> kline(KLineParam param){
        Result<KLineDto> result = RestUtil.get(KLINE,DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),KLineDto.class);
        return result;
    }

    @Override
    @Module(value = "单交易对聚合行情",tags = {"行情类"})
    public Result<MergedDto> merged(MergedParam param){
        Result<MergedDto> result = RestUtil.get(MERGED,DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),MergedDto.class);
        return result;
    }

    @Override
    @Module(value = "所有交易对基础信息",tags = {"基础类"})
    public Result<List<SymbolsDto>> symbols() {
        Result<List<SymbolsDto>> result = RestUtil.get(SYMBOLS,DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(),SymbolsDto.class);
        if("ok".equals(result.getStatus())) {
            for(SymbolsDto dto : result.getData()){
                DataMarket.SYMBOLS.put(dto.getBaseCurrency()+dto.getQuoteCurrency(),dto);
            }
        }
        return result;
    }

    @Override
    @Module(value = "全交易对聚合行情",tags = {"行情类"})
    public Result<List<TickersDto>> tickers(TickersParam param){
        Result<List<TickersDto>> result = RestUtil.get(TICKERS,DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),TickersDto.class);
        if(!"ok".equals(result.getStatus())) return result;
        //按涨幅排序
        Map<Float,String> percent = new TreeMap<>((o1, o2) -> o1 == o2 ? 0 : o1 > o2 ? -1 : 1);
        for(TickersDto dto : result.getData()){
            Float wings = (Float.valueOf(dto.getClose()) - Float.valueOf(dto.getOpen())) /Float.valueOf(dto.getOpen()) * 100.0F;
            percent.put(wings,dto.getSymbol());
            //缓存当前价数据
            DataMarket.TICKERS.put(dto.getSymbol(),dto);
        }
        Map<String,String> sortMap = new LinkedHashMap<>();
        for(Map.Entry<Float,String> entry : percent.entrySet()){
            sortMap.put(entry.getValue(), NumberUtil.numberToStr(entry.getKey(),true));
        }
        //滚动缓存
        if(DataMarket.WINGS.size() > billBoardCount){
            DataMarket.WINGS.remove(0);
        }
        DataMarket.WINGS.add(sortMap);
        //计算本轮涨跌幅(缓存数据小于2次时不执行)
        if(DataMarket.WINGS.size() < 2){
            return result;
        }
        //上次涨幅
        Map<String,String> beforeWings = DataMarket.WINGS.get(DataMarket.WINGS.size() - 2);
        //本次涨幅
        Map<String,String> lastWings = DataMarket.WINGS.get(DataMarket.WINGS.size() - 1);
        for(Map.Entry<String,String> wing : lastWings.entrySet()){
            if(beforeWings.get(wing.getKey()) == null) continue;
            //本轮涨跌幅
            double currWings = Double.valueOf(wing.getValue()) - Double.valueOf(beforeWings.get(wing.getKey()));
            DataMarket.CURRENT_WINGS.put(wing.getKey(),NumberUtil.numberToStr(currWings,true));
        }
        return result;
    }
}
