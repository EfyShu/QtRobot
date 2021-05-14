package com.efy.function;

import com.efy.annotations.Function;
import com.efy.annotations.Module;
import com.efy.constant.DataMarket;
import com.efy.frame.Console;
import com.efy.function.dto.Result;
import com.efy.function.dto.account.*;
import com.efy.function.enums.AccountEnum;
import com.efy.function.param.UrlParams;
import com.efy.function.param.account.AssetParam;
import com.efy.function.param.account.BalanceParam;
import com.efy.util.RestUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/5/10 13:54
 * Description :
 * 账户接口
 **/
@Function
public class Account {
    //获取账号信息接口地址
    public static final String ACCOUNT = "/v1/account/accounts";
    //获取钱包信息接口地址
    public static final String BALANCE = "/v1/account/accounts/{account-id}/balance";
    //账户资产估值
    public static final String ASSET = "/v2/account/asset-valuation";


    public void login(){
//        String accessKey = "";
//        String secretKey = "";
//        DataMarket.ACCESS_KEY = accessKey;
//        DataMarket.SECRET_KEY = secretKey;
        System.out.println("载入中...");
        Result<List<AccountDto>> result = info();
        if(!"ok".equals(result.getStatus())){
            System.out.println("载入账户信息失败,请重试!");
            JOptionPane.showMessageDialog(Console.getInstance().getConsole(),
                    "载入账户信息失败,请重试!","错误提示",JOptionPane.ERROR_MESSAGE);
        }else{
            balance(new BalanceParam());
            asset(new AssetParam());
            System.out.println("载入完成");
            JOptionPane.showMessageDialog(Console.getInstance().getConsole(),"载入完成");
        }
    }

    @Module(name = "获取账户信息",tags = {"账户类"})
    public Result<List<AccountDto>> info(){
        UrlParams params = new UrlParams();
        Result<List<AccountDto>> result = RestUtil.get(ACCOUNT,DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,params, AccountDto.class);
        if("ok".equals(result.getStatus())){
            for(AccountDto dto : result.getData()){
                DataMarket.ACCOUNTS.put(dto.getType(),dto);
            }
        }
        return result;
    }

    @Module(name = "获取钱包信息",tags = {"账户类"})
    public Result<BalanceDto> balance(BalanceParam param){
        AccountDto account = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code);
        String realPath = BALANCE.replace("{account-id}",param.getAccountId());
        Result<BalanceDto> result = RestUtil.get(realPath,DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),BalanceDto.class);
        List<CurrencyDto> hasBalanceList = new ArrayList<>();
        for(CurrencyDto currencyDto : result.getData().getList()){
            //过滤小额币种
            if(!"0".equals(currencyDto.getBalance())){
                //添加到账户钱包信息
                if(account.getWallet().get(currencyDto.getCurrency()) != null){
                    if(AccountEnum.CURRENCY_TYPE_TRADE.code.equals(currencyDto.getType())){
                        account.getWallet().get(currencyDto.getCurrency()).setTradeBalance(currencyDto.getBalance());
                    }else if(AccountEnum.CURRENCY_TYPE_FROZEN.code.equals(currencyDto.getType())){
                        account.getWallet().get(currencyDto.getCurrency()).setFrozenBalance(currencyDto.getBalance());
                    }
                }else{
                    WalletDto walletDto = new WalletDto();
                    if(AccountEnum.CURRENCY_TYPE_TRADE.code.equals(currencyDto.getType())){
                        walletDto.setTradeBalance(currencyDto.getBalance());
                    }else if(AccountEnum.CURRENCY_TYPE_FROZEN.code.equals(currencyDto.getType())){
                        walletDto.setFrozenBalance(currencyDto.getBalance());
                    }
                    account.getWallet().put(currencyDto.getCurrency(),walletDto);
                }
                hasBalanceList.add(currencyDto);
            }
        }
        result.getData().setList(hasBalanceList);
        return result;
    }

    @Module(value = "资产估值",tags = {"账户类"})
    public Result<AssetDto> asset(AssetParam param){
        AccountDto account = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code);
        Result<AssetDto> result = RestUtil.get(ASSET,DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),AssetDto.class);
        account.setValuation(result.getData().getBalance());
        return result;
    }
}
