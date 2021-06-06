package com.efy.ruleEngine.core;

import com.efy.ruleEngine.dto.RuleDTO;
import com.efy.ruleEngine.listener.IRuleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 1:46
 * Description :
 * 规则构造器
 **/
public class RuleBuilder {
    private List<RuleDTO> rules = new ArrayList<>();
    private RuleDTO father;
    private int index = 0;
    private boolean rootFlag = true;

    /**
     * 创建根节点(支持多根)
     * @param field      字段名
     * @param desc       字段描述
     * @param operator   操作符
     * @param source     数据来源
     * @param target     规则比较值
     * @return
     */
    public RuleBuilder root(String field, String desc,Object source, String operator, Object target, double score, IRuleListener listener) throws Exception {
        rootFlag = true;
        RuleDTO firstRule = createDto(field,desc,source,operator,target,score,listener);
        if(firstRule.getOperatorCN() == null){
            throw new Exception("不支持的操作符");
        }
        father = firstRule;
        father.setChildren(new ArrayList<>());
        for(RuleDTO bro : this.rules){
            bro.setChildren(father.getChildren());
        }
        return this;
    }

    /**
     * 并且条件节点
     * @param field      字段名
     * @param desc       字段描述
     * @param source     数据来源
     * @param operator   操作符
     * @param target     规则比较值
     * @param score      规则分值
     * @return
     */
    public RuleBuilder and(String field,String desc,Object source,String operator,Object target,double score,IRuleListener listener) throws Exception {
        if(father == null){
            root(field,desc,source,operator,target,score,listener);
        }else{
            if(father.getChildren() != null && !father.getChildren().isEmpty()){
                father = father.getChildren().get(0);
            }
            rootFlag = false;
            createDto(field,desc,source,operator,target,score,listener);
        }
        return this;
    }

    /**
     * 或者条件节点
     * @param field      字段名
     * @param desc       字段描述
     * @param operator   操作符
     * @param source     数据来源
     * @param target     规则比较值
     * @return
     */
    public RuleBuilder or(String field,String desc,Object source,String operator,Object target,double score,IRuleListener listener) throws Exception {
        if(rootFlag || father == null){
            root(field,desc,source,operator,target,score,listener);
        }else{
            RuleDTO rule = createDto(field,desc,source,operator,target,score,listener);
            rule.setChildren(new ArrayList<>());
            for(RuleDTO bro : father.getChildren()){
                bro.setChildren(rule.getChildren());
            }
        }
        return this;
    }

    public RuleBuilder root(RuleDTO rule) throws Exception {
        return root(rule.getFieldName(),rule.getFieldNameCN(),rule.getSourceValue(),rule.getOperator()
                ,rule.getTargetValue(),rule.getScore(),rule.getListener());
    }

    public RuleBuilder and(RuleDTO rule) throws Exception {
        return and(rule.getFieldName(),rule.getFieldNameCN(),rule.getSourceValue(),rule.getOperator()
                ,rule.getTargetValue(),rule.getScore(),rule.getListener());
    }

    public RuleBuilder or(RuleDTO rule) throws Exception {
        return or(rule.getFieldName(),rule.getFieldNameCN(),rule.getSourceValue(),rule.getOperator()
                ,rule.getTargetValue(),rule.getScore(),rule.getListener());
    }

    public RuleBuilder root(String field,String desc,Object source,String operator,Object target) throws Exception {
        return root(field,desc,source,operator,target,0.0D);
    }

    public RuleBuilder and(String field,String desc,Object source,String operator,Object target) throws Exception {
        return and(field,desc,source,operator,target,0.0D);
    }

    public RuleBuilder or(String field,String desc,Object source,String operator,Object target) throws Exception {
        return or(field,desc,source,operator,target,0.0D);
    }


    public RuleBuilder root(String field,String desc,Object source,String operator,Object target,double score) throws Exception {
        return root(field,desc,source,operator,target,score,null);
    }

    public RuleBuilder and(String field,String desc,Object source,String operator,Object target,double score) throws Exception {
        return and(field,desc,source,operator,target,score,null);
    }

    public RuleBuilder or(String field,String desc,Object source,String operator,Object target,double score) throws Exception {
        return or(field,desc,source,operator,target,score,null);
    }

    public RuleBuilder root(String field,String desc,Object source,String operator,Object target,IRuleListener listener) throws Exception {
        return root(field,desc,source,operator,target,0.0D,listener);
    }

    public RuleBuilder and(String field,String desc,Object source,String operator,Object target,IRuleListener listener) throws Exception {
        return and(field,desc,source,operator,target,0.0D,listener);
    }

    public RuleBuilder or(String field,String desc,Object source,String operator,Object target,IRuleListener listener) throws Exception {
        return or(field,desc,source,operator,target,0.0D,listener);
    }

    private RuleDTO createDto(String field,String desc,Object source,String operator,Object target,double score,IRuleListener listener){
        RuleDTO rule = new RuleDTO();
        rule.setId(index++);
        rule.setFieldName(field);
        rule.setFieldNameCN(desc);
        rule.setOperator(operator);
        rule.setSourceValue(source);
        rule.setTargetValue(target);
        rule.setScore(score);
        rule.setListener(listener);
        if(rootFlag){
            this.rules.add(rule);
        }else{
            rule.setPid(father.getId());
            father.addChild(rule);
        }
        return rule;
    }

    public List<RuleDTO> build(){
        return this.rules;
    }
}
