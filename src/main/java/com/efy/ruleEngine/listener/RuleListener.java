package com.efy.ruleEngine.listener;

import com.efy.ruleEngine.dto.RuleDTO;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 11:34
 * Description :
 **/
public class RuleListener implements IRuleListener{
    @Override
    public void hit(RuleDTO rule) {
        System.out.println(rule.getRuleName() + "规则被命中");
    }

    @Override
    public void miss(RuleDTO rule) {
        System.out.println(rule.getRuleName() + "规则未命中");
    }
}
