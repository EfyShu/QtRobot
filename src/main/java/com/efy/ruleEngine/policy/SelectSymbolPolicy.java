package com.efy.ruleEngine.policy;

import com.efy.ruleEngine.core.RuleBuilder;
import lombok.Data;

/**
 * @Project QtRobot
 * @Date 2021/5/18 20:51
 * @Created by Efy
 * @Description
 * 择币策略
 */
@Data
public class SelectSymbolPolicy {
    private String symbol;

    public SelectSymbolPolicy(String symbol) {
        this.symbol = symbol;
        init();
    }

    private void init(){
        RuleBuilder builder = new RuleBuilder();
//        builder.root("relativeWings","相对涨幅",)
    }
}
