package com.efy.function.proxy;

import com.efy.annotations.Module;
import com.efy.function.dto.Result;
import com.efy.function.dto.account.AccountDto;
import com.efy.function.dto.account.AssetDto;
import com.efy.function.dto.account.BalanceDto;
import com.efy.function.param.account.AssetParam;
import com.efy.function.param.account.BalanceParam;

import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/6/7 3:02
 * Description :
 **/
public interface IAccount {
    void login();

    @Module(name = "获取账户信息",tags = {"账户类"})
    Result<List<AccountDto>> info();

    @Module(name = "获取钱包信息",tags = {"账户类"})
    Result<BalanceDto> balance(BalanceParam param);

    @Module(value = "资产估值",tags = {"账户类"})
    Result<AssetDto> asset(AssetParam param);
}
