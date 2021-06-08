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
        RuleEngine engine = new RuleEngine();
        engine.setCardMode(false,true);
        start = System.currentTimeMillis();
        RuleBuilder rb = new RuleBuilder();
        List<RuleDTO> tree = rb
                .root("flag","执行标记",true,"=",true)
                .and("assets","价值",5,">=",5)
                .and("isBuyOrder","是否买单",true,"=",true)
                .or("isBuyOrder","是否买单",true,"=",false)
                .and("upWings","涨幅",0.1,">=",0.2F)
                .or("downWings","跌幅",1,"<=",-1F)
                .and("assets","价值",5,">=",5)
                .build();
        ResultDTO score = engine.start(tree);
        System.out.println(score);
        end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");

    }
}
