package com.efy.function.proxy;

import com.efy.annotations.Module;

/**
 * Author : Efy Shu
 * Date : 2021/6/7 4:29
 * Description :
 **/
public interface IQuantitative {
    @Module(value = "监控钱包",tags = {"量化类"})
    void listenBalance();

    @Module(value = "监控订单",tags = {"量化类"})
    void listenOrder();

    @Module(value = "自动下单",tags = {"量化类"})
    void autoPlace();

    boolean isBalanceFlag();

    boolean isOrderFlag();

    boolean isAutoPlaceFlag();
}
