package com.efy.function.param.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.efy.constant.DataMarket;
import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 0:47
 * Description :
 **/
@Data
public class MatchedOrderParam implements RequestParam {
    @ApiModelProperty(value = "交易对",required = true)
    private String symbol;
    @ApiModelProperty(value = "查询的订单类型组合，使用','分割(不填查询所有)")
    private String types;
    @JSONField(name = "start-time")
    @ApiModelProperty(value = "查询开始时间, 时间格式UTC time in millisecond。以订单生成时间进行查询-48h 查询结束时间的前48小时取值范围 [((end-time) – 48h), (end-time)] ，查询窗口最大为48小时，窗口平移范围为最近180天")
    private Long startTime;
    @JSONField(name = "end-time")
    @ApiModelProperty(value = "查询结束时间")
    private Long endTime;
    @JSONField(name = "start-date")
    @ApiModelProperty(value = "查询开始日期（新加坡时区）")
    private String startDate;
    @JSONField(name = "end-date")
    @ApiModelProperty(value = "查询结束日期（新加坡时区）")
    private String endDate;
    @ApiModelProperty(value = "查询起始 ID")
    private Integer from;
    @ApiModelProperty(value = "查询方向prev 向前,next 向后")
    private String direct;
    @ApiModelProperty(value = "查询记录大小",allowableValues = "[1,500]")
    private String size;

    public void setDirect(String direct) {
        this.direct = direct;
        if("prev".equals(direct) && DataMarket.ORDER_PAGE.get("matched-prev") != null){
            this.from = DataMarket.ORDER_PAGE.get("matched-prev");
        }else if("next".equals(direct) && DataMarket.ORDER_PAGE.get("matched-next") != null){
            this.from = DataMarket.ORDER_PAGE.get("matched-next");
        }else{
            this.direct = null;
        }
    }
}
