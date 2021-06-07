package com.efy.function.dto.market;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/6/8 0:50
 * Description :
 **/
@Data
public class SymbolsDto {
    @JSONField(name = "base-currency")
    @ApiModelProperty("交易对中的基础币种")
    private String baseCurrency;

    @JSONField(name = "quote-currency")
    @ApiModelProperty("交易对中的报价币种")
    private String quoteCurrency;

    @JSONField(name = "price-precision")
    @ApiModelProperty("交易对报价的精度（小数点后位数），限价买入与限价卖出价格使用")
    private Integer pricePrecision;

    @JSONField(name = "amount-precision")
    @ApiModelProperty("交易对基础币种计数精度（小数点后位数），限价买入、限价卖出、市价卖出数量使用")
    private Integer amountPrecision;

    @JSONField(name = "currency-partition")
    @ApiModelProperty("交易区，可能值: [main，innovation]")
    private String symbolPartition;

    @JSONField(name = "currency")
    @ApiModelProperty("交易对")
    private String symbol;

    @JSONField(name = "state")
    @ApiModelProperty("交易对状态；可能值: [online，offline,suspend] online - 已上线；offline - 交易对已下线，不可交易；suspend -- 交易暂停；pre-online - 即将上线")
    private String state;

    @JSONField(name = "value-precision")
    @ApiModelProperty("交易对交易金额的精度（小数点后位数），市价买入金额使用")
    private Integer valuePrecision;

    @JSONField(name = "min-order-amt")
    @ApiModelProperty("交易对限价单最小下单量 ，以基础币种为单位（即将废弃）")
    private Float minOrderAmt;

    @JSONField(name = "max-order-amt")
    @ApiModelProperty("交易对限价单最大下单量 ，以基础币种为单位（即将废弃）")
    private Float maxOrderAmt;

    @JSONField(name = "limit-order-min-order-amt")
    @ApiModelProperty("交易对限价单最小下单量 ，以基础币种为单位（NEW）")
    private Float limitOrderMinOrderAmt;

    @JSONField(name = "limit-order-max-order-amt")
    @ApiModelProperty("交易对限价单最大下单量 ，以基础币种为单位（NEW）")
    private Float limitOrderMaxOrderAmt;

    @JSONField(name = "sell-market-min-order-amt")
    @ApiModelProperty("交易对市价卖单最小下单量，以基础币种为单位（NEW）")
    private Float sellMarketMinOrderAmt;

    @JSONField(name = "sell-market-max-order-amt")
    @ApiModelProperty("交易对市价卖单最大下单量，以基础币种为单位（NEW）")
    private Float sellMarketMaxOrderAmt;

    @JSONField(name = "buy-market-max-order-value")
    @ApiModelProperty("交易对市价买单最大下单金额，以计价币种为单位（NEW）")
    private Float buyMarketMaxOrderValue;

    @JSONField(name = "min-order-value")
    @ApiModelProperty("交易对限价单和市价买单最小下单金额 ，以计价币种为单位")
    private Float minOrderValue;

    @JSONField(name = "max-order-value")
    @ApiModelProperty("交易对限价单和市价买单最大下单金额 ，以折算后的USDT为单位（NEW）")
    private Float maxOrderValue;

    @JSONField(name = "leverage-ratio")
    @ApiModelProperty("交易对杠杆最大倍数(仅对逐仓杠杆交易对、全仓杠杆交易对、杠杆ETP交易对有效）")
    private Float leverageRatio;

    @JSONField(name = "underlying")
    @ApiModelProperty("标的交易代码 (仅对杠杆ETP交易对有效)")
    private String underlying;

    @JSONField(name = "mgmt-fee-rate")
    @ApiModelProperty("持仓管理费费率 (仅对杠杆ETP交易对有效)")
    private Float mgmtFeeRate;

    @JSONField(name = "charge-time")
    @ApiModelProperty("持仓管理费收取时间 (24小时制，GMT+8，格式：HH:MM:SS，仅对杠杆ETP交易对有效)")
    private String chargeTime;

    @JSONField(name = "rebal-time")
    @ApiModelProperty("每日调仓时间 (24小时制，GMT+8，格式：HH:MM:SS，仅对杠杆ETP交易对有效)")
    private String rebalTime;

    @JSONField(name = "rebal-threshold")
    @ApiModelProperty("临时调仓阈值 (以实际杠杆率计，仅对杠杆ETP交易对有效)")
    private Float rebalThreshold;

    @JSONField(name = "init-nav")
    @ApiModelProperty("初始净值（仅对杠杆ETP交易对有效）")
    private Float initNav;

    @JSONField(name = "api-trading")
    @ApiModelProperty("API交易使能标记（有效值：enabled, disabled）")
    private String apiTrading;
}
