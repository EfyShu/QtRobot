package com.efy.function.proxy;

import com.efy.annotations.Module;

import javax.swing.*;

/**
 * Author : Efy Shu
 * Date : 2021/6/7 4:29
 * Description :
 **/
public interface IQuantitative {
    @Module(value = "监控钱包",tags = {"量化类"})
    void listenBalance(JMenuItem menu);

    @Module(value = "监控订单",tags = {"量化类"})
    void listenOrder(JMenuItem menu);

    @Module(value = "自动下单",tags = {"量化类"})
    void autoPlace(JMenuItem menu);

    boolean isBalanceFlag();

    boolean isOrderFlag();

    boolean isAutoPlaceFlag();
}
