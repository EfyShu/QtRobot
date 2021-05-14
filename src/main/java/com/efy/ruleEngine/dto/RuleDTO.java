package com.efy.ruleEngine.dto;

import com.efy.ruleEngine.listener.IRuleListener;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project XinDai_POC
 * @Date 2019/1/8 18:44
 * @Author by Efy Shu
 * @Description TODO
 */
@Data
public class RuleDTO {
    /**ID*/
    @ApiModelProperty("ID")
    private Integer id;
    /**父级ID*/
    @ApiModelProperty("父级ID")
    private Integer pid;
    /**规则名(可不设置)*/
    @ApiModelProperty("规则名(可不设置)")
    private String ruleName;
    /**字段名*/
    @ApiModelProperty("字段名")
    private String fieldName;
    /**字段名(中文)*/
    @ApiModelProperty("字段名(中文,只影响显示,不参与计算)")
    private String fieldNameCN;
    /**规则分值 (评分卡模型用)*/
    @ApiModelProperty("规则分值 (评分卡模型用)")
    private Double score = 0.0;
    /**操作符(> , < , = , <= , >= , != , () , !() )*/
    @ApiModelProperty("操作符(> , < , = , <= , >= , != , () , !() )")
    private String operator;
    /**操作符(中文)*/
    @ApiModelProperty("操作符(中文,可不设置)")
    private String operatorCN;
    /**比较值*/
    @ApiModelProperty("比较值")
    private Object sourceValue;
    /**被比较值*/
    @ApiModelProperty("被比较值")
    private Object targetValue;
    /**校验结果*/
    @ApiModelProperty("校验结果")
    private Boolean result = false;
    /**子项 (评分卡模型成功时进入子项)*/
    @ApiModelProperty("子项 (评分卡模型成功时进入子项)")
    private List<RuleDTO> children;

    private IRuleListener listener;


    public String getOperatorCN() {
        this.operatorCN = ">".equals(this.operator) ? "大于" :
                          "<".equals(this.operator) ? "小于" :
                          "=".equals(this.operator) ? "等于" :
                          ">=".equals(this.operator) ? "大于等于" :
                          "<=".equals(this.operator) ? "小于等于" :
                          "!=".equals(this.operator) ? "不等于" :
                          "()".equals(this.operator) ? "包含于" :
                          "!()".equals(this.operator) ? "不包含于" : null;
        return operatorCN;
    }

    public String getRuleName(){
        if(this.ruleName != null && !this.ruleName.trim().isEmpty()){
            return ruleName;
        }
        this.ruleName = this.fieldNameCN.concat(getOperatorCN()).concat(String.valueOf(this.targetValue));
        return ruleName;
    }

    public void addChild(RuleDTO rule){
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(rule);
    }

    public void setScore(Double score) {
        this.score = score == null ? 0.0D : score;
    }

    public void setResult(Boolean result) {
        this.result = result;
        //传递事件
        if(this.listener != null){
            if (result) {
                listener.hit(this);
            } else {
                listener.miss(this);
            }
        }
    }

    @Override
    public String toString() {
        return "RuleDTO{" +
                "id=" + id +
                ", score=" + score +
                ", ruleName='" + this.getRuleName() + '\'' +
                ", sourceValue=" + sourceValue +
                ", children=" + children +
                '}';
    }
}
