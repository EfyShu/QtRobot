package com.efy.function.proxy;

import com.efy.annotations.Module;
import com.efy.function.dto.Result;
import com.efy.function.dto.order.*;
import com.efy.function.param.order.*;

import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/6/7 4:12
 * Description :
 **/
public interface IOrder {
    @Module(value = "设置超时取消时间",tags = {"订单类"})
    Result<CaaDto> caa();

    @Module(value = "现货下单",tags = {"订单类"})
    Result<Long> place(PlaceParam param);

    @Module(value = "查询未成交订单",tags = {"订单类"})
    Result<List<OpenOrderDto>> queryOpen(OpenOrderParam param);

    @Module(value = "查询已成交订单",tags = {"订单类"})
    Result<List<MatchedOrderDto>> queryMatched(MatchedOrderParam param);

    @Module(value = "取消所有订单",tags = {"订单类"})
    Result<CancelAllDto> cancelAll(CancelAllParam param);

    @Module(value = "批量取消指定单",tags = {"订单类"})
    Result<BatchCancelDto> batchCancel(BatchCancelParam param);

    @Module(value = "取消订单",tags = {"订单类"})
    Result<Integer> cancel(CancelParam param);

    @Module(value = "查询订单详情",tags = {"订单类"})
    Result<QueryDto> query(QueryParam param);
}
