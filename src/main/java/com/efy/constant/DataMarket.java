package com.efy.constant;

import com.efy.function.dto.account.AccountDto;
import com.efy.function.dto.market.TickersDto;
import com.efy.function.dto.order.OrderDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 1:18
 * Description :
 * 数据仓库,所有接口获得的信息整理后归总在这里,供决策引擎使用
 **/
public class DataMarket {
    /**私钥,密钥**/
    public static String ACCESS_KEY = "0ba54093-2a95f943-2e688786-1qdmpe4rty";
    public static String SECRET_KEY = "e32b73d5-285fed66-d5742866-428ad";

    /**账户信息(含钱包信息,按类型存储)**/
    public static Map<String, AccountDto> ACCOUNTS = new ConcurrentHashMap<>();

    /**涨幅榜(key-symbol,value-日涨幅<24小时滚动价>)**/
    public static List<Map<String,String>> WINGS = new CopyOnWriteArrayList<>();

    /**本轮涨跌幅(key-symbol,value-本轮涨跌幅)**/
    public static Map<String,String> CURRENT_WINGS = new ConcurrentHashMap<>();

    /**现价榜(key-symbol,value-聚合行情)**/
    public static Map<String, TickersDto> TICKERS = new ConcurrentHashMap<>();

    /**订单列表(key-orderId,value-订单信息)**/
    public static Map<Long, OrderDto> ORDERS = new ConcurrentHashMap<>();

    /**订单查询分页缓存(记录分页所需参数,如key->open-prev,value->上一次查询结果中得到的第一条id)**/
    public static Map<String,Long> ORDER_PAGE = new ConcurrentHashMap<>();

    /**当前可用余额**/
    public static Double TRADE_BALANCE;

}
