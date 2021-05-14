package com.efy.function.dto.order;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 0:04
 * Description :
 **/
@Data
public class BatchCancelFailedDto {
    @JSONField(name = "order-id")
    @ApiModelProperty("订单编号（如用户创建订单时包含order-id，返回中也须包含此字段）")
    private String orderId;
    @JSONField(name = "client-err-code")
    @ApiModelProperty("用户自编订单号（如用户创建订单时包含client-order-id，返回中也须包含此字段）")
    private String clientErrCode;
    @JSONField(name = "err-code")
    @ApiModelProperty("订单被拒错误码（仅对被拒订单有效）")
    private String errCode;
    @JSONField(name = "err-msg")
    @ApiModelProperty("订单被拒错误信息（仅对被拒订单有效）")
    private String errMsg;
    @JSONField(name = "order-state")
    @ApiModelProperty("当前订单状态（若有）,参考OrderEnum")
    private String orderState;
}
