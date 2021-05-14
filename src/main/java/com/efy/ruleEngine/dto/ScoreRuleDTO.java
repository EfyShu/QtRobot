package com.efy.ruleEngine.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Project XinDai_POC
 * @Date 2020年3月25日 15:46:24
 * @Author by wlp
 * @Description TODO
 */
@Data
public class ScoreRuleDTO {
    /**ID*/
    @ApiModelProperty("规则编号")
    private String ruleNbr;
    /**规则名*/
    @ApiModelProperty("规则名")
    private String ruleName;
    /**字段名*/
    @ApiModelProperty("字段名")
    private String fieldName;
    /**字段名(中文)*/
    @ApiModelProperty("字段名(中文)")
    private String fieldNameCN;
    /**字段类型(long,double,String)*/
    @ApiModelProperty("字段类型(long,double,String)")
    private String fieldType;
    /**规则分值 (评分卡模型用)*/
    @ApiModelProperty("规则分值 (评分卡模型用)")
    private Double score = 0.0;
    /**操作符(> , < , = , <= , >=,!=)*/
    @ApiModelProperty("操作符(> , < , = , <= , >=,!=)")
    private String operator;
    /**比较值*/
    @ApiModelProperty("比较值")
    private String sourceValue;
    /**比较值取值字段名*/
    @ApiModelProperty("比较值的决策字段名称")
    private String sourceField;
    /**比较值取值字段名*/
    @ApiModelProperty("比较值取值字段类型")
    private String sourceFieldType;
    @ApiModelProperty("比较值的决策字段的值(变量名称或固定值或表达式)")
    private String sourceContent;
    /**被比较值*/
    @ApiModelProperty("被比较值")
    private String targetValue;
    /**被比较值取值字段名*/
    @ApiModelProperty("被比较值的决策字段名称")
    private String targetField;
    /**被比较值取值字段名*/
    @ApiModelProperty("被比较值取值字段类型")
    private String targetFieldType;
    @ApiModelProperty("被比较值的决策字段的值(变量名称或固定值或表达式)")
    private String targetContent;
    /**被比较值取值字段名*/
    @ApiModelProperty("比较类型 0-变量 1-固定值")
    private String compareType;


}
