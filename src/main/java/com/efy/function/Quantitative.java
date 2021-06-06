package com.efy.function;

import com.efy.annotations.Function;
import com.efy.annotations.Module;
import com.efy.constant.DataMarket;
import com.efy.frame.Console;
import com.efy.function.dto.Result;
import com.efy.function.dto.account.AccountDto;
import com.efy.function.dto.account.WalletDto;
import com.efy.function.dto.order.OrderDto;
import com.efy.function.enums.AccountEnum;
import com.efy.function.param.account.AssetParam;
import com.efy.function.param.account.BalanceParam;
import com.efy.function.param.market.TickersParam;
import com.efy.function.param.order.BatchCancelParam;
import com.efy.function.param.order.CancelAllParam;
import com.efy.function.param.order.MatchedOrderParam;
import com.efy.function.proxy.*;
import com.efy.listener.func.IQuantitativeListener;
import com.efy.listener.func.impl.TradeListener;
import com.efy.listener.sys.BeanMap;

import javax.swing.*;
import java.util.HashMap;
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
//                    doBuy();
//                    doSell();
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
        //取消所有订单
        CancelAllParam cancelAllParam = new CancelAllParam();
        //现货账户ID
        cancelAllParam.setAccountId(DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getId());
        order.cancelAll(cancelAllParam);
        //卖出现有货币转为USDT
        BatchCancelParam batchCancelParam = new BatchCancelParam();
        batchCancelParam.setOrderIds(DataMarket.ORDERS.keySet().toArray(new String[]{}));
        order.batchCancel(batchCancelParam);
    }

    private void printWings(){
        ISystemMenu systemMenu = BeanMap.getBean(SystemMenu.class);
        systemMenu.clearPanel();
        //先取最新记录
        Map<String,String> billBoard = DataMarket.WINGS.get(DataMarket.WINGS.size()-1);
        int countForLine = 4;
        int count = 0;
        int total = 0;
        for(Map.Entry<String,String> wing : billBoard.entrySet()){
            //只看usdt
            if(!wing.getKey().contains("usdt")) continue;
            if(DataMarket.CURRENT_WINGS.get(wing.getKey()) != null){
                System.out.print(wing.getKey() + ":" + wing.getValue() + "%("+ DataMarket.CURRENT_WINGS.get(wing.getKey()) +"%)" + "\t");
            }else{
                System.out.print(wing.getKey() + ":" + wing.getValue() + "%\t");
            }
            count++;
            total++;
            if(count == countForLine) {
                System.out.println();
                count = 0;
            }
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
}
