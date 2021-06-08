package com.efy.ruleEngine.core;

import com.efy.ruleEngine.dto.ResultDTO;
import com.efy.ruleEngine.dto.RuleDTO;

import java.util.List;

/**
 * @Project Sunyard_RLS
 * @Date 2020/1/7 10:49
 * @Created by Efy
 * @Description TODO
 */
public class RuleEngineDemo {
    public static void main(String[] args) throws Exception {
        long start,end;
        RuleEngine engine = RuleEngine.getIns();
        engine.setCardMode(false,true);
        start = System.currentTimeMillis();
        RuleBuilder rb = new RuleBuilder();
        List<RuleDTO> tree = rb
                .root("flag","执行标记",true,"=",true)
                .and("assets","价值",5,">=",5)
                .and("isBuyOrder","是否买单",true,"=",true)
                .or("isBuyOrder","是否买单",true,"=",false)
                .and("timeout","是否超时",5000,">=",5000)
                .or("upWings","涨幅",0.3,">=",0.2F)
                .and("assets","价值",5,">=",5)
                .build();
        ResultDTO score = engine.start(tree);
        System.out.println(score);
        end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");

    }
}
