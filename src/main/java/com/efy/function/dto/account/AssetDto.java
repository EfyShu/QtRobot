package com.efy.function.dto.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 23:32
 * Description :
 **/
@Data
public class AssetDto {
    @ApiModelProperty("按照某一个法币为单位的总资产估值")
    private String balance;
    @ApiModelProperty("数据返回时间，为unix time in millisecond")
    private Long timestamp;
}
