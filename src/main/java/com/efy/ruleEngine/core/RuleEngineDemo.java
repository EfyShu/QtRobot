package com.efy.ruleEngine.core;

import com.efy.ruleEngine.dto.ResultDTO;
import com.efy.ruleEngine.dto.RuleDTO;
import com.efy.ruleEngine.listener.RuleListener;

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
        for(int i=0;i<20;i++){
            start = System.currentTimeMillis();
            RuleBuilder builder = new RuleBuilder();
            builder
                    .root("balance","钱包余额",Math.random() * 20,">",0,30D,new RuleListener())
                    .and("balance","钱包余额",Math.random() * 25,">=",19,15D,new RuleListener())
                    .and("balance","钱包余额",Math.random() * 100,">=",99,33.3D,new RuleListener())
                    ;
            builder.or("name","姓名",new String[]{"Efy","AAA"}[(int)(Math.random() * 2)],"()","Efy,Efy Shu",new RuleListener());
            List<RuleDTO> ruleTree = builder.build();
            ResultDTO score = engine.start(ruleTree);
            System.out.println(score);
            end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
        }

    }
}
