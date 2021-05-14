package com.efy.function;

import com.efy.annotations.Function;
import com.efy.annotations.Module;
import com.efy.constant.DataMarket;
import com.efy.frame.Console;
import com.efy.function.dto.Result;
import com.efy.function.dto.order.*;
import com.efy.function.enums.OrderEnum;
import com.efy.function.param.UrlParams;
import com.efy.function.param.order.*;
import com.efy.util.RestUtil;

import javax.swing.*;
import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/5/12 12:20
 * Description :
 * 订单交易类
 **/
@Function
public class Order {
    //现货下单接口地址
    private static final String PLACE = "/v1/order/orders/place";
    //设置超时取消时间(针对网络异常,应用崩溃等因素导致的撤销失败等,可以在超时后自动取消)接口地址
    private static final String CAA = "/v2/algo-orders/cancel-all-after";
    //查询未成交订单接口地址
    private static final String OPENORDER = "/v1/order/openOrders";
    //查询已成交订单接口地址
    private static final String MATCHED = "/v1/order/matchresults";
    //取消所有订单(只提申请,是否真正完成还需查询)接口地址
    private static final String CANCELALL = "/v1/order/orders/batchCancelOpenOrders";
    //批量取消指定单接口地址
    private static final String BATCHCANCEL = "/v1/order/orders/batchcancel";
    //取消订单
    private static final String CANCEL = "/v1/order/orders/{order-id}/submitcancel";
    //查询订单详情接口地址
    private static final String QUERY = "/v1/order/orders/{order-id}";

    @Module(value = "设置超时取消时间",tags = {"订单类"})
    public Result<CaaDto> caa(){
        String timeout = JOptionPane.showInputDialog("超时时间");
        CaaParam param = new CaaParam();
        try {
            param.setTimeout(Integer.parseInt(timeout));
        }catch (Exception e){
            JOptionPane.showMessageDialog(Console.getInstance().getConsole(),"请设置正确的数字!!!");
            return Result.fail();
        }
        Result<CaaDto> result = RestUtil.post(CAA, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),CaaDto.class);
        if(!"ok".equals(result)){
            JOptionPane.showMessageDialog(Console.getInstance().getConsole(),result.getMessage());
            System.err.println("设置超时取消时间失败");
        }
        return result;
    }

    @Module(value = "现货下单",tags = {"订单类"})
    public Result<String> place(PlaceParam param){
        Result<String> result = RestUtil.post(PLACE, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),String.class);
        if(result.getData() != null){
            DataMarket.ORDERS.put(result.getData(), OrderEnum.ORDER_STATE_CREATED.code);
        }
        return result;
    }

    @Module(value = "查询未成交订单",tags = {"订单类"})
    public Result<List<OpenOrderDto>> queryOpen(OpenOrderParam param){
        Result<List<OpenOrderDto>> result = RestUtil.post(PLACE, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),OpenOrderDto.class);
        if("ok".equals(result.getStatus()) && result.getData() != null){
            DataMarket.ORDER_PAGE.put("open-prev",result.getData().get(0).getId());
            DataMarket.ORDER_PAGE.put("open-next",result.getData().get(0).getId());
            if(result.getData().size() > 1){
                DataMarket.ORDER_PAGE.put("open-next",result.getData().get(result.getData().size()-1).getId());
            }
        }
        return result;
    }

    @Module(value = "查询已成交订单",tags = {"订单类"})
    public Result<List<MatchedOrderDto>> queryMatched(MatchedOrderParam param){
        Result<List<MatchedOrderDto>> result = RestUtil.post(PLACE, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),MatchedOrderDto.class);
        if("ok".equals(result.getStatus()) && result.getData() != null){
            DataMarket.ORDER_PAGE.put("matched-prev",result.getData().get(0).getId());
            DataMarket.ORDER_PAGE.put("matched-next",result.getData().get(0).getId());
            if(result.getData().size() > 1){
                DataMarket.ORDER_PAGE.put("matched-next",result.getData().get(result.getData().size()-1).getId());
            }
        }
        return result;
    }

    @Module(value = "取消所有订单",tags = {"订单类"})
    public Result<CancelAllDto> cancelAll(CancelAllParam param){
        Result<CancelAllDto> result = RestUtil.post(PLACE, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),CancelAllDto.class);
        if("ok".equals(result.getStatus()) && result.getData() != null){
            DataMarket.ORDER_PAGE.put("cancelAll-next",result.getData().getNextId());
        }
        return result;
    }

    @Module(value = "批量取消指定单",tags = {"订单类"})
    public Result<BatchCancelDto> batchCancel(BatchCancelParam param){
        Result<BatchCancelDto> result = RestUtil.post(PLACE, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),BatchCancelDto.class);
        if("ok".equals(result.getStatus())){
            if(result.getData().getSuccess() != null){
                for(String successId : result.getData().getSuccess()){
                    DataMarket.ORDERS.remove(successId);
                }
            }
            if(result.getData().getFailed() != null){
                //更新订单状态
                for(BatchCancelFailedDto failDto : result.getData().getFailed()){
                    String currState = DataMarket.ORDERS.get(failDto.getOrderId());
                    String resultState = failDto.getOrderState();
                    String orderState =explainState(resultState,currState);
                    DataMarket.ORDERS.put(failDto.getOrderId(),orderState);
                }
            }
        }
        return result;
    }

    @Module(value = "取消订单",tags = {"订单类"})
    public Result<Integer> cancel(CancelParam param){
        String realPath = CANCEL.replace("{order-id}",param.getOrderId());
        Result<Integer> result = RestUtil.post(realPath, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),Integer.class);
        if(!"ok".equals(result.getStatus())){
            String currState = DataMarket.ORDERS.get(param.getOrderId());
            String resultState = result.getData().toString();
            String orderState =explainState(resultState,currState);
            DataMarket.ORDERS.put(param.getOrderId(),orderState);
        }else{
            DataMarket.ORDERS.remove(param.getOrderId());
        }
        return result;
    }

    @Module(value = "查询订单详情",tags = {"订单类"})
    public Result<QueryDto> query(QueryParam param){
        String realPath = QUERY.replace("{order-id}",param.getOrderId());
        Result<QueryDto> result = RestUtil.post(realPath, DataMarket.ACCESS_KEY,DataMarket.SECRET_KEY,new UrlParams(param),QueryDto.class);
        if("ok".equals(result.getStatus())){
            DataMarket.ORDERS.put(param.getOrderId(),result.getData().getState());
        }
        return result;
    }

    private String explainState(String resultState,String currState){
        String orderState =
                    "-1".equals(resultState) ? OrderEnum.ORDER_STATE_CANCELED.code :
                    "5".equals(resultState) ? OrderEnum.ORDER_STATE_PARTIAL_CANCELED.code :
                    "6".equals(resultState) ? OrderEnum.ORDER_STATE_FILLED.code :
                    "7".equals(resultState) ? OrderEnum.ORDER_STATE_CANCELED.code :
                    "10".equals(resultState) ? OrderEnum.ORDER_STATE_CANCELING.code : currState;
        return orderState;
    }

}
