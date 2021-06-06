package com.efy.function;

import com.efy.annotations.Function;
import com.efy.annotations.Module;
import com.efy.constant.DataMarket;
import com.efy.frame.Console;
import com.efy.function.dto.Result;
import com.efy.function.dto.account.AccountDto;
import com.efy.function.dto.account.WalletDto;
import com.efy.function.dto.order.OpenOrderDto;
import com.efy.function.dto.order.OrderDto;
import com.efy.function.enums.AccountEnum;
import com.efy.function.enums.OrderEnum;
import com.efy.function.param.account.AssetParam;
import com.efy.function.param.account.BalanceParam;
import com.efy.function.param.market.TickersParam;
import com.efy.function.param.order.*;
import com.efy.function.proxy.IAccount;
import com.efy.function.proxy.IMarket;
import com.efy.function.proxy.IOrder;
import com.efy.function.proxy.IQuantitative;
import com.efy.listener.func.IQuantitativeListener;
import com.efy.listener.func.impl.TradeListener;
import com.efy.listener.sys.BeanMap;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 1:14
 * Description :
 * 量化功能类
 **/
@Function
public class Quantitative implements IQuantitative {
    //钱包余额监控标记
    private boolean balanceFlag = false;
    //订单监控标记
    private boolean orderFlag = false;
    //自动下单标记
    private boolean autoPlaceFlag = false;
    //交易监听器
    private IQuantitativeListener listener = new TradeListener();

    @Override
    @Module(value = "监控钱包",tags = {"量化类"})
    public void listenBalance(JMenuItem menu){
        if(!checkAccount(menu)) return;
        setBalanceFlag(menu);
       if(!this.balanceFlag) return;
        new Thread(() -> {
            try {
                IAccount account = BeanMap.getBean(Account.class);
                while (this.balanceFlag){
                    account.balance(new BalanceParam());
                    account.asset(new AssetParam());
                    AccountDto spotAcc = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code);
                    System.out.println("账户估值:" + spotAcc.getValuation());
                    for(Map.Entry<String,WalletDto> currency : spotAcc.getWallet().entrySet()){
                        Double totalBalance = Double.valueOf(currency.getValue().getTradeBalance())
                                + Double.valueOf(currency.getValue().getFrozenBalance());
                        if(totalBalance < 0.0001) continue;
                        WalletDto currencyallet = currency.getValue();
                        System.out.println(currency + ":" +
                                "tradeBalance=" + currencyallet.getTradeBalance() + " " +
                                "frozenBalance=" + currencyallet.getFrozenBalance());
                    }
                    //每秒更新一次
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.balanceFlag = false;
                setMenuSelected(menu,false);
                System.out.println("停止监听钱包");
            }
        }).start();
    }

    @Override
    @Module(value = "监控订单",tags = {"量化类"})
    public void listenOrder(JMenuItem menu){
        if(!checkAccount(menu)) return;
        setOrderFlag(menu);
        if(!this.orderFlag) return;
        new Thread(() -> {
            try {
                IOrder order = BeanMap.getBean(Order.class);
                while (this.orderFlag){
                    Map<String,String> map = new HashMap<>();
                    //每秒更新一次
                    for(Map.Entry<Long, OrderDto> entry : DataMarket.ORDERS.entrySet()){
                        map.put(entry.getValue().getSymbol(),"");
                    }
                    for(Map.Entry<String,String> entry : map.entrySet()){
                        MatchedOrderParam param = new MatchedOrderParam();
                        param.setSymbol(entry.getKey());
                        order.queryMatched(param);
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.orderFlag = false;
                setMenuSelected(menu,false);
                System.out.println("停止监听订单");
            }
        }).start();
    }
    
    @Override
    @Module(value = "自动下单",tags = {"量化类"})
    public void autoPlace(JMenuItem menu){
        if(!checkAccount(menu)) return;
        setAutoPlaceFlag(menu);
        if(!this.autoPlaceFlag) return;
        new Thread(() -> {
            try {
                TickersParam param = new TickersParam();
                IMarket market = BeanMap.getBean(Market.class);
                while (this.autoPlaceFlag){
                    //每秒更新两次
                    Thread.sleep(500);
                    Result result = market.tickers(param);
                    if(!"ok".equals(result.getStatus())) continue;
                    printWings();
                    doBuy();
                    doSell();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.autoPlaceFlag = false;
                setMenuSelected(menu,false);
                doSellAll();
                System.out.println("停止自动下单");
            }
        }).start();
    }

    /**
     * 买入操作
     */
    private void doBuy(){
        //两次以上才开始交易
        if(DataMarket.WINGS.size() < 2) return;
        new Thread(() -> {
            listener.buy();
        }).start();
    }

    /**
     * 卖出操作
     */
    private void doSell(){
        //两次以上才开始交易
        if(DataMarket.WINGS.size() < 2) return;
        new Thread(() -> {
            listener.sell();
        }).start();
    }

    /**
     * 卖出全部货币
     */
    private void doSellAll(){
        IOrder order = BeanMap.getBean(Order.class);
        IAccount account = BeanMap.getBean(Account.class);
        if(!DataMarket.ORDERS.isEmpty()){
            //取消所有订单
            List<OpenOrderDto> orders = order.queryOpen(new OpenOrderParam()).getData();
            BatchCancelParam batchCancelParam = new BatchCancelParam();
            batchCancelParam.setOrderIds(DataMarket.ORDERS.keySet().toArray(new String[]{}));
            order.batchCancel(batchCancelParam);
            for(Map.Entry<String,String> entry : DataMarket.CURRENT_WINGS.entrySet()){
                CancelAllParam cancelAllParam = new CancelAllParam();
                cancelAllParam.setSymbol(entry.getKey());
                order.cancelAll(cancelAllParam);
            }
        }
        //卖出现有货币转为USDT
        account.balance(new BalanceParam());
        Map<String,WalletDto> wallets = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet();
        for(Map.Entry<String,WalletDto> entry : wallets.entrySet()){
            if(entry.getKey().equals("usdt")) continue;
            String symbol = entry.getKey()+"usdt";
            PlaceParam placeParam = new PlaceParam();
            placeParam.setSymbol(symbol);
            placeParam.setType(OrderEnum.ORDER_DIRECTION_SELL.code+"-"+OrderEnum.ORDER_OPERATION_MARKET.code);
            placeParam.setAmount(entry.getValue().getTradeBalance());
            order.place(placeParam);
        }
        account.asset(new AssetParam());
        System.out.println("转换完成,估值:" + DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getValuation());
    }

    private void printWings(){
//        ISystemMenu systemMenu = BeanMap.getBean(SystemMenu.class);
//        systemMenu.clearPanel();
        //先取最新记录
        Map<String,String> billBoard = DataMarket.WINGS.get(DataMarket.WINGS.size()-1);
        int total = 0;
        for(Map.Entry<String,String> wing : billBoard.entrySet()){
            //只看usdt
            if(!wing.getKey().contains("usdt")) continue;
            if(DataMarket.CURRENT_WINGS.get(wing.getKey()) != null){
                System.out.println(wing.getKey() + ":" + wing.getValue() + "%("+ DataMarket.CURRENT_WINGS.get(wing.getKey()) +"%)" + "\t");
            }else{
                System.out.println(wing.getKey() + ":" + wing.getValue() + "%\t");
            }
            total++;
            if(total >= 10) break;
        }
    }

    private void setBalanceFlag(JMenuItem menu) {
        this.balanceFlag = !balanceFlag;
        setMenuSelected(menu,balanceFlag);
    }

    private void setOrderFlag(JMenuItem menu) {
        this.orderFlag = !orderFlag;
        setMenuSelected(menu,orderFlag);
    }

    private void setAutoPlaceFlag(JMenuItem menu) {
        this.autoPlaceFlag = !autoPlaceFlag;
        setMenuSelected(menu,autoPlaceFlag);
    }

    private void setMenuSelected(JMenuItem menu,boolean flag){
        menu.setSelected(flag);
    }

    private boolean checkAccount(JMenuItem menu){
        if(DataMarket.ACCESS_KEY == null || DataMarket.SECRET_KEY == null
            || DataMarket.ACCOUNTS == null || DataMarket.ACCOUNTS.isEmpty()){
            JOptionPane.showMessageDialog(Console.getInstance().getConsole(),
                "请先载入密钥对","错误提示", JOptionPane.ERROR_MESSAGE);
            setMenuSelected(menu,false);
            return false;
        }
        setMenuSelected(menu,true);
        return true;
    }

    @Override
    public boolean isBalanceFlag() {
        return balanceFlag;
    }

    @Override
    public boolean isOrderFlag() {
        return orderFlag;
    }

    @Override
    public boolean isAutoPlaceFlag() {
        return autoPlaceFlag;
    }
}
