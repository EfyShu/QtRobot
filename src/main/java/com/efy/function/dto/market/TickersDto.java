package com.efy.function.dto.market;

import com.efy.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 13:45
 * Description :
 * 交易对行情列表dto(与聚合行情字段稍微有不同)
 **/
@Data
public class TickersDto {
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
    @ApiModelProperty("交易对，例如btcusdt, ethbtc")
    private String symbol;
    @ApiModelProperty("买一价")
    private String bid;
    @ApiModelProperty("买一量")
    private String bidSize;
    @ApiModelProperty("卖一价")
    private String ask;
    @ApiModelProperty("卖一量")
    private String askSize;

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

    public void setBid(Float bid) {
        this.bid = NumberUtil.numberToStr(bid);
    }

    public void setAsk(Float ask) {
        this.ask = NumberUtil.numberToStr(ask);
    }

    public void setBidSize(String bidSize) {
        this.bidSize = NumberUtil.numberToStr(bidSize);
    }

    public void setAskSize(String askSize) {
        this.askSize = NumberUtil.numberToStr(askSize);
    }
}
