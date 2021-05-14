package com.efy.ruleEngine.listener;

import com.efy.ruleEngine.dto.RuleDTO;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 11:14
 * Description :
 * 规则被命中时的监听器
 **/
public interface IRuleListener {
    /**命中**/
    void hit(RuleDTO rule);

    /**未命中**/
    void miss(RuleDTO rule);
}
