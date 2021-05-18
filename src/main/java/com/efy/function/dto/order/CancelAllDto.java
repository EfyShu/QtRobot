package com.efy.function.dto.order;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 23:49
 * Description :
 **/
@Data
public class CancelAllDto {
    @JSONField(name = "success-count")
    @ApiModelProperty("成功取消的订单数")
    private Integer successCount;
    @JSONField(name = "failed-count")
    @ApiModelProperty("取消失败的订单数")
    private Integer failedCount;
    @JSONField(name = "next-id")
    @ApiModelProperty("下一个可以撤销的订单号，返回-1表示没有可以撤销的订单")
    private Long nextId;

}
