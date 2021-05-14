package com.efy.function;

import com.efy.annotations.Function;
import com.efy.constant.DataMarket;
import com.efy.frame.Console;
import com.efy.function.dto.Result;
import com.efy.function.dto.account.AccountDto;
import com.efy.function.dto.account.WalletDto;
import com.efy.function.enums.AccountEnum;
import com.efy.function.param.account.AssetParam;
import com.efy.function.param.account.BalanceParam;
import com.efy.function.param.market.TickersParam;
import com.efy.listener.sys.BeanMap;

import javax.swing.*;
import java.util.Map;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 1:14
 * Description :
 * 量化功能类
 **/
@Function
public class Quantitative {
    //涨幅榜监控标记
    private boolean billBoardFlag = false;
    //钱包余额监控标记
    private boolean balanceFlag = false;
    //订单监控
    private boolean orderFlag = false;

    /**
     * 监控涨幅榜
     */
    public void listenBillBoard(JMenuItem menu){
        setBillBoardFlag(menu);
        if(!this.billBoardFlag) return;
        TickersParam param = new TickersParam();
        Market market = BeanMap.getBean(Market.class);
        SystemMenu systemMenu = BeanMap.getBean(SystemMenu.class);
        new Thread(() -> {
            try {
                while (this.billBoardFlag){
                    //每秒更新两次
                    Thread.sleep(500);
                    Result result = market.tickers(param);
                    if(!"ok".equals(result.getStatus())) continue;
                    systemMenu.clearPanel();
                    //先取最新记录
//                    List<Map<String,String>> billBoard = DataMarket.WINGS.get(DataMarket.WINGS.size()-1);
                    Map<String,String> billBoard = DataMarket.WINGS.get(DataMarket.WINGS.size()-1);
                    int countForLine = 4;
                    int count = 0;
                    int total = 0;
                    for(Map.Entry<String,String> wing : billBoard.entrySet()){
                        System.out.print(wing.getKey() + ":" + wing.getValue() + "\t");
                        count++;
                        total++;
                        if(count == countForLine) {
                            System.out.println();
                            count = 0;
                        }
                        if(total >= 20) break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.billBoardFlag = false;
                setMenuSelected(menu,false);
                System.out.println("停止监听涨幅榜");
            }
        }).start();
    }

    public void listenBalance(JMenuItem menu){
        if(!checkAccount(menu)) return;
        setBalanceFlag(menu);
        if(!this.balanceFlag) return;
        Account account = BeanMap.getBean(Account.class);
        new Thread(() -> {
            try {
                while (this.balanceFlag){
                    account.balance(new BalanceParam());
                    account.asset(new AssetParam());
                    AccountDto spotAcc = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code);
                    String[] wallets = new String[]{"usdt","shib","doge"};
                    System.out.println("账户估值:" + spotAcc.getValuation());
                    for(String currency : wallets){
                        WalletDto currencyallet = spotAcc.getWallet().get(currency);
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

    public void listenOrder(JMenuItem menu){
        if(!checkAccount(menu)) return;
        setOrderFlag(menu);
        if(!this.orderFlag) return;
        Order order = BeanMap.getBean(Order.class);
        new Thread(() -> {
            try {
                while (this.orderFlag){
                    //每秒更新一次
                    System.out.println("1");
                    Thread.sleep(5000);
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

    public void setBillBoardFlag(JMenuItem menu) {
        this.billBoardFlag = !billBoardFlag;
        setMenuSelected(menu,billBoardFlag);
    }

    public void setBalanceFlag(JMenuItem menu) {
        this.balanceFlag = !balanceFlag;
        setMenuSelected(menu,balanceFlag);
    }

    public void setOrderFlag(JMenuItem menu) {
        this.orderFlag = !orderFlag;
        setMenuSelected(menu,orderFlag);
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
