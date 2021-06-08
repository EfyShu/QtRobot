package com.efy.listener.func.impl;

import com.efy.annotations.Listener;
import com.efy.constant.DataMarket;
import com.efy.function.Order;
import com.efy.function.Quantitative;
import com.efy.function.dto.account.WalletDto;
import com.efy.function.dto.order.OrderDto;
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
        for(Map.Entry<String,String> wing : lastWings.entrySet()){
            if(buyPolicy(wing.getKey())){
                PlaceParam param = new PlaceParam();
                param.setSymbol(wing.getKey());
                //市价购买
                param.setType(OrderEnum.ORDER_DIRECTION_BUY.code + "-" + OrderEnum.ORDER_OPERATION_LIMIT.code);
                param.setPrice(DataMarket.TICKERS.get(wing.getKey()).getBid());
//                String amount = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet().get("usdt").getTradeBalance();
                String amount = (6D / Double.valueOf(param.getPrice())) + "";
//                String amount = "6";
                param.setAmount(amount);
                //如果成功,结果在数仓
                order.place(param);
            }
        }
    }

    @Override
    public void sell() {
        IOrder order = BeanMap.getBean(Order.class);
        //已持有货币
        Map<String, OrderDto> orders = DataMarket.ORDERS;
        for(Map.Entry<String,OrderDto> entry : orders.entrySet()){
            if(sellPolicy(entry.getValue())){
                WalletDto wallet = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet().get(entry.getValue().getSymbol().replace("usdt",""));
                PlaceParam param = new PlaceParam();
                param.setSymbol(entry.getValue().getSymbol());
                //市价卖出
                param.setType(OrderEnum.ORDER_DIRECTION_SELL.code + "-" + OrderEnum.ORDER_OPERATION_LIMIT.code);
                param.setPrice(DataMarket.TICKERS.get(entry.getValue().getSymbol()).getAsk());
                //全部卖出
                param.setAmount(wallet.getTradeBalance());
                //如果成功,结果在数仓
                order.place(param);
            }
        }
    }

    /**
     * 买入策略
     * @return
     */
    private boolean buyPolicy(String symbol){
        RuleEngine re = RuleEngine.getIns();
        RuleBuilder rb = new RuleBuilder();
        IQuantitative quantitative = BeanMap.getBean(Quantitative.class);
        try {
            List<RuleDTO> tree = rb
                    .root("flag","执行标记",quantitative.isAutoPlaceFlag(),"=","true")
                    .and("isUsdt","是否usdt交易对",symbol.endsWith("usdt"),"=","true")
                    .and("balance","余额",DataMarket.TRADE_BALANCE,">=","6")
                    .build();
            return re.start(tree).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("策略引擎构建失败!!!");
        }
        return false;
    }

    /**
     * 卖出策略
     * @return
     */
    private boolean sellPolicy(OrderDto order){
        RuleEngine re = RuleEngine.getIns();
        RuleBuilder rb = new RuleBuilder();
        IQuantitative quantitative = BeanMap.getBean(Quantitative.class);
        try {
            //(买入价-当前价)/买入价*100 为本订单涨跌幅
            float currPrice = Float.valueOf(DataMarket.TICKERS.get(order.getSymbol()).getClose());
            float buyPrice = Float.valueOf(order.getPrice());
            float wings = (buyPrice - currPrice) / buyPrice * 100.0F;
            WalletDto wallet = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet().get(order.getSymbol().replace("usdt",""));
            double assets = Double.parseDouble(wallet.getTradeBalance()) *
                     Double.parseDouble(DataMarket.TICKERS.get(order.getSymbol()).getClose());
            boolean isBuyOrder = order.getType().startsWith(OrderEnum.ORDER_DIRECTION_BUY.code);
            List<RuleDTO> tree = rb
                    .root("flag","执行标记",quantitative.isOrderFlag(),"=",true)
                    .and("orderState","订单状态",order.getState(),"=",OrderEnum.ORDER_STATE_FILLED.code)
                    .and("isBuyOrder","是否买单",isBuyOrder,"=",true)
                    .or("isBuyOrder","是否买单",isBuyOrder,"=",false)
                    .and("upWings","涨幅",wings,">=",0.5F)
                    .and("assets","价值",assets,">=",5)
//                    .or("downWings","跌幅",wings,"<=",-3F)
                    .build();
            return re.start(tree).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("策略引擎构建失败!!!");
        }
        return false;
    }
}
