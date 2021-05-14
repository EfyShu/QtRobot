package com.efy.function.param.order;

import com.efy.function.param.RequestParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 23:47
 * Description :
 **/
@Data
public class CaaParam implements RequestParam {
    @ApiModelProperty(value = "超时时间（单位：秒）",required = true,allowableValues = "0或者大于等于5秒")
    private Integer timeout = 5;
}
