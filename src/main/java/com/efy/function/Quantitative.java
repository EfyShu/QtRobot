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
import com.efy.function.enums.OrderEnum;
import com.efy.function.param.account.AssetParam;
import com.efy.function.param.account.BalanceParam;
import com.efy.function.param.market.TickersParam;
import com.efy.function.param.order.CancelAllParam;
import com.efy.function.param.order.PlaceParam;
import com.efy.function.param.order.QueryParam;
import com.efy.function.proxy.*;
import com.efy.listener.func.IQuantitativeListener;
import com.efy.listener.func.impl.TradeListener;
import com.efy.listener.sys.BeanMap;
import com.efy.util.NumberUtil;

import javax.swing.*;
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
    public void listenBalance(){
        if(!checkAccount()) return;
        setBalanceFlag();
        if(!this.balanceFlag) return;
        new Thread(() -> {
            try {
                System.out.println("开始监听钱包");
                IAccount account = BeanMap.getBean(Account.class);
                ISystemMenu systemMenu = BeanMap.getBean(SystemMenu.class);
                int i=0;
                while (this.balanceFlag){
                    account.balance(new BalanceParam());
                    account.asset(new AssetParam());
                    AccountDto spotAcc = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code);
                    double todayWings = (Double.valueOf(spotAcc.getValuation()) - DataMarket.BASE_ASSETS) / Double.valueOf(spotAcc.getValuation()) * 100D;
                    if(i>=20){
                        i=0;
                        systemMenu.clearPanel();
                        System.out.println("账户估值:" + spotAcc.getValuation() + ",今日涨跌:" + NumberUtil.format(todayWings,2));
                    }
                    if(todayWings <= -5D){
                        this.orderFlag = false;
                        this.autoPlaceFlag = false;
                        this.balanceFlag = false;
                        System.err.println("今日跌幅已达最大,停止交易!!!");
                    }
                    //每秒更新一次
                    Thread.sleep(1000);
                    i++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.balanceFlag = false;
                System.out.println("停止监听钱包");
            }
        }).start();
    }

    @Override
    @Module(value = "监控订单",tags = {"量化类"})
    public void listenOrder(){
        if(!checkAccount()) return;
        setOrderFlag();
        if(!this.orderFlag) return;
        new Thread(() -> {
            try {
                System.out.println("开始监听订单");
                IOrder order = BeanMap.getBean(Order.class);
                int i = 0;
                while (this.orderFlag){
                    if(i>=5){
                        order.cancelAll(new CancelAllParam());
                        i=0;
                    }
                    //每秒更新一次
                    for(Map.Entry<String, OrderDto> entry : DataMarket.ORDERS.entrySet()){
                        //已成交/取消的订单不再查询
                        if(OrderEnum.ORDER_STATE_FILLED.code.equals(DataMarket.ORDERS.get(entry.getKey()).getState())
                        || OrderEnum.ORDER_STATE_CANCELED.code.equals(DataMarket.ORDERS.get(entry.getKey()).getState())){
                            continue;
                        }
                        QueryParam param = new QueryParam();
                        param.setOrderId(entry.getKey());
                        order.query(param);
                    }
                    Thread.sleep(1000);
                    i++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.orderFlag = false;
                System.out.println("停止监听订单");
            }
        }).start();
    }
    
    @Override
    @Module(value = "自动下单",tags = {"量化类"})
    public void autoPlace(){
        if(!checkAccount()) return;
        setAutoPlaceFlag();
        if(!this.autoPlaceFlag) return;
        new Thread(() -> {
            try {
                TickersParam param = new TickersParam();
                IMarket market = BeanMap.getBean(Market.class);
                while (this.autoPlaceFlag){
                    //每秒更新两次
//                    Thread.sleep(500);
                    Result result = market.tickers(param);
                    if(!"ok".equals(result.getStatus())) continue;
//                    printWings();
                    doBuy();
                    doSell();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.autoPlaceFlag = false;
                doSellAll();
                System.out.println("停止自动下单");
            }
        }).start();
    }

    /**
     * 买入操作
     */
    private void doBuy(){
//        new Thread(() -> {
            listener.buy();
//        }).start();
    }

    /**
     * 卖出操作
     */
    private void doSell(){
//        new Thread(() -> {
            listener.sell();
//        }).start();
    }

    /**
     * 卖出全部货币
     */
    private void doSellAll(){
        IOrder order = BeanMap.getBean(Order.class);
        IAccount account = BeanMap.getBean(Account.class);
        //取消所有订单
        order.cancelAll(new CancelAllParam());
        //卖出现有货币转为USDT
        account.balance(new BalanceParam());
        Map<String,WalletDto> wallets = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet();
        for(Map.Entry<String,WalletDto> entry : wallets.entrySet()){
            if(entry.getKey().equals("usdt")) continue;
            String symbol = entry.getKey()+"usdt";
            double price = Double.parseDouble(entry.getValue().getTradeBalance()) *
                    Double.parseDouble(DataMarket.TICKERS.get(symbol).getClose());
            if(price < 5) continue;   //持有价值少于5美元,无法卖出
            PlaceParam placeParam = new PlaceParam();
            placeParam.setSymbol(symbol);
            placeParam.setType(OrderEnum.ORDER_DIRECTION_SELL.code+"-"+OrderEnum.ORDER_OPERATION_MARKET.code);
            placeParam.setPrice(DataMarket.TICKERS.get(symbol).getAsk());
            placeParam.setAmount(entry.getValue().getTradeBalance());
            order.place(placeParam);
        }
        account.asset(new AssetParam());
        AccountDto spotAcc = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code);
        double todayWings = (Double.valueOf(spotAcc.getValuation()) - DataMarket.BASE_ASSETS) / Double.valueOf(spotAcc.getValuation()) * 100D;
        System.out.println("转换完成,估值:" + spotAcc.getValuation() + ",今日收益:" + NumberUtil.format(todayWings,2));
    }

    private void printWings(){
//        ISystemMenu systemMenu = BeanMap.getBean(System·1Menu.class);
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

    private void setBalanceFlag() {
        this.balanceFlag = !balanceFlag;
    }

    private void setOrderFlag() {
        this.orderFlag = !orderFlag;
    }

    private void setAutoPlaceFlag() {
        this.autoPlaceFlag = !autoPlaceFlag;
    }

    private boolean checkAccount(){
        if(DataMarket.ACCESS_KEY == null || DataMarket.SECRET_KEY == null
            || DataMarket.ACCOUNTS == null || DataMarket.ACCOUNTS.isEmpty()){
            JOptionPane.showMessageDialog(Console.getInstance().getConsole(),
                "请先载入密钥对","错误提示", JOptionPane.ERROR_MESSAGE);
            return false;
        }
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
