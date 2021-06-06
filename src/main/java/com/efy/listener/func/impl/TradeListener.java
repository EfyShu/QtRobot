package com.efy.listener.func.impl;

import com.efy.annotations.Listener;
import com.efy.constant.DataMarket;
import com.efy.function.Order;
import com.efy.function.Quantitative;
import com.efy.function.enums.AccountEnum;
import com.efy.function.enums.OrderEnum;
import com.efy.function.param.order.PlaceParam;
import com.efy.function.proxy.IOrder;
import com.efy.function.proxy.IQuantitative;
import com.efy.listener.func.IQuantitativeListener;
import com.efy.listener.sys.BeanMap;
import com.efy.ruleEngine.core.RuleBuilder;
import com.efy.ruleEngine.core.RuleEngine;
import com.efy.ruleEngine.dto.RuleDTO;
import com.efy.util.NumberUtil;

import java.util.List;
import java.util.Map;

/**
 * Author : Efy Shu
 * Date : 2021/6/7 1:01
 * Description :
 **/
@Listener
public class TradeListener implements IQuantitativeListener {
    @Override
    public void buy() {
        IOrder order = BeanMap.getBean(Order.class);
        //本次涨幅
        Map<String,String> lastWings = DataMarket.WINGS.get(DataMarket.WINGS.size() - 1);
        int total = 0;
        for(Map.Entry<String,String> wing : lastWings.entrySet()){
            if(!checkPlaceFlag()) return;
            //只看usdt
            if(!wing.getKey().endsWith("usdt")) continue;
            if(buyPolicy(wing.getKey())){
                PlaceParam param = new PlaceParam();
                param.setSymbol(wing.getKey());
                String amount = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet().get("usdt").getTradeBalance();
                amount = NumberUtil.numberToStr(Double.valueOf(amount),true);
//                String amount = "5";
                param.setAmount(amount);
                //市价购买
                param.setType(OrderEnum.ORDER_DIRECTION_BUY.code + "-" + OrderEnum.ORDER_OPERATION_MARKET.code);
                //如果成功,结果在数仓
                order.place(param);
                total++;
            }
            if(total >= 10) break;
        }
    }

    @Override
    public void sell() {
        IOrder order = BeanMap.getBean(Order.class);
        //本次涨幅
        Map<String,String> lastWings = DataMarket.WINGS.get(DataMarket.WINGS.size() - 1);
        int total = 0;
        for(Map.Entry<String,String> wing : lastWings.entrySet()){
            if(!checkPlaceFlag()) return;
            //只看usdt
            if(!wing.getKey().endsWith("usdt")) continue;
            //没持有的不做交易
            if(!hasWallet(wing.getKey())) continue;
            if(sellPolicy(wing.getKey())){
                PlaceParam param = new PlaceParam();
                param.setSymbol(wing.getKey());
                String amount = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet()
                        .get(wing.getKey().replace("usdt","")).getTradeBalance();
                //全部卖出
                param.setAmount(amount);
                //市价卖出
                param.setType(OrderEnum.ORDER_DIRECTION_SELL.code + "-" + OrderEnum.ORDER_OPERATION_MARKET.code);
                //如果成功,结果在数仓
                order.place(param);
                total++;
            }
            if(total >= 10) break;
        }
    }

    /**
     * 检查是否已经停止交易
     * @return
     */
    private boolean checkPlaceFlag(){
        IQuantitative quantitative = BeanMap.getBean(Quantitative.class);
        return quantitative.isAutoPlaceFlag();
    }

    /**
     * 检查是否持有
     * @param symbol
     * @return
     */
    private boolean hasWallet(String symbol){
        return DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet()
                .get(symbol.replace("usdt","")) == null;
    }

    /**
     * 买入策略
     * @return
     */
    private boolean buyPolicy(String symbol){
        RuleEngine re = RuleEngine.getIns();
        RuleBuilder rb = new RuleBuilder();
        try {
            List<RuleDTO> tree = rb
                    .root("balance","余额",DataMarket.TRADE_BALANCE,">=","10")
                    .build();
            return re.start(tree).getResult();
        } catch (Exception e) {
            System.err.println("策略引擎构建失败!!!");
        }
        return false;
    }

    /**
     * 卖出策略
     * @return
     */
    private boolean sellPolicy(String symbol){
        RuleEngine re = RuleEngine.getIns();
        RuleBuilder rb = new RuleBuilder();
        try {
            List<RuleDTO> tree = rb
                    .root("upWings","涨幅",DataMarket.CURRENT_WINGS.get(symbol),">=","0.0")
                    .or("downWings","跌幅",DataMarket.CURRENT_WINGS.get(symbol),"<=","-0.1")
                    .build();
            return re.start(tree).getResult();
        } catch (Exception e) {
            System.err.println("策略引擎构建失败!!!");
        }
        return false;
    }
}
