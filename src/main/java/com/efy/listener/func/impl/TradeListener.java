package com.efy.listener.func.impl;

import com.efy.annotations.Listener;
import com.efy.constant.DataMarket;
import com.efy.function.Order;
import com.efy.function.dto.order.OrderDto;
import com.efy.function.enums.OrderEnum;
import com.efy.function.param.order.PlaceParam;
import com.efy.function.proxy.IOrder;
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
            //只看usdt
            if(!wing.getKey().contains("usdt")) continue;
            if(buyPolicy(wing.getKey())){
                PlaceParam param = new PlaceParam();
                param.setSymbol(wing.getKey());
                param.setAmount("5");
                //市价购买
                param.setType(OrderEnum.ORDER_DIRECTION_BUY.code + "-" + OrderEnum.ORDER_OPERATION_MARKET.code);
                //如果成功,结果在数仓
                order.place(param);
            }
        }
    }

    @Override
    public void sell() {
        IOrder order = BeanMap.getBean(Order.class);
        //本次涨幅
        Map<String,String> lastWings = DataMarket.WINGS.get(DataMarket.WINGS.size() - 1);
        for(Map.Entry<String,String> wing : lastWings.entrySet()){
            //只看usdt
            if(!wing.getKey().contains("usdt")) continue;
            //没下单过的不做交易
            if(!hasOrder(wing.getKey())) continue;
            if(sellPolicy(wing.getKey())){
                PlaceParam param = new PlaceParam();
                param.setSymbol(wing.getKey());
                //按最低限价购入
                param.setAmount("5");
                //市价卖出
                param.setType(OrderEnum.ORDER_DIRECTION_SELL.code + "-" + OrderEnum.ORDER_OPERATION_MARKET.code);
                //如果成功,结果在数仓
                order.place(param);
            }
        }
    }

    /**
     * 检查是否有下过单
     * @param symbol
     * @return
     */
    private boolean hasOrder(String symbol){
        for(Map.Entry<Long, OrderDto> entry : DataMarket.ORDERS.entrySet()){
            OrderDto order = entry.getValue();
            if(order.getSymbol().equals(symbol)){
                return true;
            }
        }
        return false;
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
                    .root("balance","余额",DataMarket.TRADE_BALANCE,">=","5")
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
                    .root("upWings","涨幅",DataMarket.CURRENT_WINGS.get(symbol),">=","0.2")
                    .or("downWings","跌幅",DataMarket.CURRENT_WINGS.get(symbol),"<=","-1")
                    .build();
            return re.start(tree).getResult();
        } catch (Exception e) {
            System.err.println("策略引擎构建失败!!!");
        }
        return false;
    }
}
