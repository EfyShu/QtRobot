package com.efy.function.dto.market;

import com.efy.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 13:38
 * Description :
 **/
@Data
public class MergedDto {
    @ApiModelProperty("数据时间ID,聚合接口用symbol代替(无意义)")
    private Long id;
    @ApiModelProperty("以基础币种计量的交易量（以滚动24小时计）")
    private String amount;
    @ApiModelProperty("交易次数（以滚动24小时计）")
    private Integer count;
    @ApiModelProperty("本阶段开盘价（以滚动24小时计,列表接口以新加坡自然日计）")
    private String open;
    @ApiModelProperty("本阶段最新价（以滚动24小时计,列表接口以新加坡自然日计）")
    private String close;
    @ApiModelProperty("本阶段最低价（以滚动24小时计,列表接口以新加坡自然日计）")
    private String low;
    @ApiModelProperty("本阶段最高价（以滚动24小时计,列表接口以新加坡自然日计）")
    private String high;
    @ApiModelProperty("以报价币种计量的交易量（以滚动24小时计）")
    private String vol;
    @ApiModelProperty("当前的最高买价 [price, size]")
    private String[] bid;
    @ApiModelProperty("当前的最低卖价 [price, size]")
    private String[] ask;


    public void setAmount(Float amount) {
        this.amount = NumberUtil.numberToStr(amount);
    }

    public void setOpen(Float open) {
        this.open = NumberUtil.numberToStr(open);
    }

    public void setClose(Float close) {
        this.close = NumberUtil.numberToStr(close);
    }

    public void setLow(Float low) {
        this.low = NumberUtil.numberToStr(low);
    }

    public void setHigh(Float high) {
        this.high = NumberUtil.numberToStr(high);
    }

    public void setVol(Float vol) {
        this.vol = NumberUtil.numberToStr(vol);
    }

    public void setBid(Float[] bid) {
        String[] tempBid = new String[bid.length];
        for(int i=0;i<bid.length;i++){
            tempBid[i] = NumberUtil.numberToStr(bid[i]);
        }
        this.bid = tempBid;
    }

    public void setAsk(Float[] ask) {
        String[] tempBid = new String[ask.length];
        for(int i=0;i<ask.length;i++){
            tempBid[i] = NumberUtil.numberToStr(ask[i]);
        }
        this.ask = tempBid;
    }
}
