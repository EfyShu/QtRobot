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
import com.efy.ruleEngine.dto.ResultDTO;
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
                param.setType(buyTypePolicy());
                String[] result = buyPricePolicy(param);
                param.setPrice(result[0]);
                param.setAmount(result[1]);
                //如果成功,结果在数仓
                order.place(param);
            }
        }
    }

    @Override
    public void sell() {
        IOrder order = BeanMap.getBean(Order.class);
        //所有订单
        Map<String, OrderDto> orders = DataMarket.ORDERS;
        for(Map.Entry<String,OrderDto> entry : orders.entrySet()){
            if(sellPolicy(entry.getValue())){
                PlaceParam param = new PlaceParam();
                param.setSymbol(entry.getValue().getSymbol());
                param.setType(sellTypePolicy(entry.getValue()));
                String[] result = sellPricePolicy(param);
                param.setPrice(result[0]);
                param.setAmount(result[1]);
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
        RuleEngine re = new RuleEngine();
        RuleBuilder rb = new RuleBuilder();
        IQuantitative quantitative = BeanMap.getBean(Quantitative.class);
        try {
            List<RuleDTO> tree = rb
                    .root("flag","执行标记",quantitative.isAutoPlaceFlag(),"=","true")
                    .and("isUsdtSymbol","是否usdt交易对",symbol.endsWith("usdt"),"=","true")
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
        RuleEngine re = new RuleEngine();
        RuleBuilder rb = new RuleBuilder();
        IQuantitative quantitative = BeanMap.getBean(Quantitative.class);
        try {
            //(当前价-买入价)/买入价*100 为本订单涨跌幅
            float currPrice = Float.valueOf(DataMarket.TICKERS.get(order.getSymbol()).getClose());
            float buyPrice = Float.valueOf(DataMarket.BUY_PRICE.get(order.getSymbol()));
            float wings = (currPrice - buyPrice) / currPrice * 100.0F;
            WalletDto wallet = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet().get(order.getSymbol().replace("usdt",""));
            if(wallet == null) return false;  //订单未真正交易完成,钱包不一定有余额
            double assets = Double.parseDouble(wallet.getTradeBalance()) *
                     Double.parseDouble(DataMarket.TICKERS.get(order.getSymbol()).getClose());
            boolean isBuyOrder = order.getType().startsWith(OrderEnum.ORDER_DIRECTION_BUY.code);
            List<RuleDTO> tree = rb
                    .root("flag","执行标记",quantitative.isOrderFlag(),"=",true)
                    .and("orderState","订单状态",order.getState(),"=",OrderEnum.ORDER_STATE_FILLED.code)
                    .and("isBuyOrder","是否买单",isBuyOrder,"=",true)
                    .and("upWings","涨幅",wings,">=",0.2F)
                    .or("downWings","跌幅",wings,"<=",-3F)
                    .and("assets","价值",assets,">=",5)
                    .build();
            return re.start(tree).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("策略引擎构建失败!!!");
        }
        return false;
    }

    /**
     * 买单类型策略
     * @return
     */
    private String buyTypePolicy(){
        RuleEngine re = new RuleEngine();
        RuleBuilder rb = new RuleBuilder();
        String orderType = OrderEnum.ORDER_DIRECTION_BUY.code + "-" + OrderEnum.ORDER_OPERATION_LIMIT.code;
        try {
            List<RuleDTO> tree = rb
                    .root("flag","执行标记",true,"=",true)
                    .build();
            ResultDTO result = re.start(tree);
            if(result.getScore() >= 10 || result.getScore() <= -10){
                orderType = OrderEnum.ORDER_DIRECTION_BUY.code + "-" + OrderEnum.ORDER_OPERATION_MARKET.code;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("策略引擎构建失败!!!");
        }
        return orderType;
    }

    /**
     * 买单价格策略
     * @param param
     * @return
     */
    private String[] buyPricePolicy(PlaceParam param){
        //第一位是price,第二位是amount
        String[] paa = new String[2];
        paa[0] = DataMarket.TICKERS.get(param.getSymbol()).getClose();
        if(param.getType().contains(OrderEnum.ORDER_OPERATION_MARKET.code)){
            paa[1] = "6";
        }else{
            paa[1] = (6D / Double.valueOf(paa[0])) + "";
        }
        //                String amount = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet().get("usdt").getTradeBalance();
        return paa;
    }

    /**
     * 买单价格策略
     * @param param
     * @return
     */
    private String[] sellPricePolicy(PlaceParam param){
        //第一位是price,第二位是amount
        String[] paa = new String[2];
        WalletDto wallet = DataMarket.ACCOUNTS.get(AccountEnum.ACCOUNT_TYPE_SPOT.code).getWallet().get(param.getSymbol().replace("usdt",""));
        paa[0] = DataMarket.TICKERS.get(param.getSymbol()).getClose();
        paa[1] = wallet.getTradeBalance();
        return paa;
    }

    /**
     * 卖单类型策略
     * @param order
     * @return
     */
    private String sellTypePolicy(OrderDto order){
        RuleEngine re = new RuleEngine();
        RuleBuilder rb = new RuleBuilder();
        String orderType = OrderEnum.ORDER_DIRECTION_SELL.code + "-" + OrderEnum.ORDER_OPERATION_LIMIT.code;
        try {
            //(当前价-买入价)/买入价*100 为本订单涨跌幅
            float currPrice = Float.valueOf(DataMarket.TICKERS.get(order.getSymbol()).getClose());
            float buyPrice = Float.valueOf(DataMarket.BUY_PRICE.get(order.getSymbol()));
            float wings = (currPrice - buyPrice) / currPrice * 100.0F;
            List<RuleDTO> tree = rb
                    .root("upWings","涨幅",wings,">",0.5F,10)
                    .or("downWings","跌幅",wings,"<=",-5F,-10)
                    .build();
            ResultDTO result = re.start(tree);
            if(result.getScore() >= 10 || result.getScore() <= -10){
                orderType = OrderEnum.ORDER_DIRECTION_SELL.code + "-" + OrderEnum.ORDER_OPERATION_MARKET.code;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("策略引擎构建失败!!!");
        }
        return orderType;
    }
}
