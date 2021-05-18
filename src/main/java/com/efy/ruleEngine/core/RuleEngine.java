package com.efy.ruleEngine.core;


import com.efy.ruleEngine.dto.ResultDTO;
import com.efy.ruleEngine.dto.RuleDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project XinDai_POC
 * @Date 2019/1/8 14:39
 * @Author by Efy Shu
 * @Description 规则原始方法,包含大于,小于,等于,大于等于,小于等于,不等于,包含,不包含,
 */
public class RuleEngine {
    public static RuleEngine ins;
    /**评分卡模式(false - 规则树<必须一竿子捅到底,否则都算失败>,true - 评分卡<计算所有已经被命中的规则的分数>)**/
    private boolean cardMode = false;
    /**简单模式(配合评分卡使用,true - 非全量遍历, false - 全量遍历)**/
    private boolean simpleMode = true;
    private RuleEngine(){}

    public static RuleEngine getIns() {
        if(ins == null){
            ins = new RuleEngine();
        }
        return ins;
    }

    /**
     * 比对方法
     * @param source 比较值(动态,接口传递)
     * @param target 被比较值(静态,配置中或数据库读取)
     * @param operator 操作符
     * @return
     */
    private boolean compair(Object source, Object target,String operator){
        if(source == null || target == null) {
            return false;
        }
        if("()".equals(operator) || "!()".equals(operator)){
            if(((String)target).contains("|") || ((String)target).contains(",")){
                String[] tList;
                if(((String)target).contains("|")){
                    tList = ((String)target).split("\\|");
                }else{
                    tList = ((String)target).split(",");
                }
                for(String tStr : tList){
                    if(tStr.equals(source)){
                        return "()".equals(operator);
                    }
                }
                return "!()".equals(operator);
            }else{
                return "()".equals(operator) == target.equals(source);
            }
        }else if(source instanceof String){
            return "=".equals(operator) ? source.equals(target) :
                   "!=".equals(operator) && !source.equals(target);
        }else{
            BigDecimal bSource = toNumber(source);
            BigDecimal bTarget = toNumber(target);
            if(bSource == null || bTarget == null) {
                return false;
            }
            int result = bSource.compareTo(bTarget);
            return "=".equals(operator) ? result == 0 :
                   "<".equals(operator) ? result < 0 :
                   ">".equals(operator) ? result > 0 :
                   "<=".equals(operator) ? result < 0 || result == 0 :
                   ">=".equals(operator) ? result > 0 || result == 0 :
                   "!=".equals(operator) && result != 0;
        }
    }

    /**
     * 字符转数字
     * @param source
     * @return
     */
    private BigDecimal toNumber(Object source){
        try {
            BigDecimal sourceNumber = new BigDecimal(source+"");
            return sourceNumber;
        }catch (Exception e){
//            throw e;
            System.err.println("数据内容:"+source+"|字符转换时发生异常!"+e.getMessage());
        }
        return null;
    }

    /**
     * 生成规则树(将废弃,推荐使用RuleBuilder构造)
     * @param source 校验数据源
     * @param ruleList 规则列表(数组形态)
     * @return 规则列表(树形态)
     * @deprecated
     */
    private List<RuleDTO> buildTree(Map<String,Object> source, List<RuleDTO> ruleList){
        List<RuleDTO> ruleTree = new ArrayList<>();
        for (RuleDTO m1 : ruleList) {
            if(null == m1.getSourceValue() || "".equals(m1.getSourceValue())){
                m1.setSourceValue(source.get(m1.getFieldName()));
            }
            if(m1.getPid() == null) {
                ruleTree.add(m1);
            }
            //获取子项
            List<RuleDTO> childs = new ArrayList<>();
            for (RuleDTO m2 : ruleList) {
                //不遍历自身和非子项
                if(m2.getId().equals(m1.getId()) || !m1.getId().equals(m2.getPid())) {
                    continue;
                }
                m2.setSourceValue(source.get(m2.getFieldName()));
                childs.add(m2);
            }
            m1.setChildren(childs);
        }
        return ruleTree;
    }

    /**
     * 批卷算法(正确时打上标记,并进入该分支下一规则)
     * @param ruleTree
     */
    private boolean marking(List<RuleDTO> ruleTree){
        boolean result = false;
        for(RuleDTO dto : ruleTree){
            result = compair(dto.getSourceValue(),dto.getTargetValue(),dto.getOperator());
            //评分卡模式
            if(cardMode && dto.getChildren() != null && !dto.getChildren().isEmpty()){
                dto.setResult(result);
                if(simpleMode){
                    if(result){  //简单模式下,结果正确则中断其他支线
                        marking(dto.getChildren());
                        return dto.getResult();
                    }else{       //否则继续校验其他支线
                        continue;
                    }
                }else{           //非简单模式下,无论结果如何都需要继续遍历
                    marking(dto.getChildren());
                }
            }else if(!cardMode && result && dto.getChildren() != null && !dto.getChildren().isEmpty()){
                //规则树模式父级结果由子级决定
                dto.setResult(marking(dto.getChildren()));
                //其中一条成功则代表其他支线不再有效
                return dto.getResult();
            }else {
                dto.setResult(result);
            }
        }
        return result;
    }

    /**
     * 计算得分
     * @param ruleTree
     * @return
     */
    private ResultDTO sumScore(ResultDTO result, List<RuleDTO> ruleTree){
        if(ruleTree == null || ruleTree.isEmpty()) {
            return result;
        }
        for(RuleDTO temp : ruleTree){
            if(!cardMode && !temp.getResult()) {
                continue;
            }

            if(temp.getResult()){
                double score = result.getScore() + temp.getScore();
                result.setScore(score);
                result.getDetails().add(print(temp));
                result.setHitCount(result.getHitCount()+1);
                result.setResult(temp.getResult());
                result = sumScore(result,temp.getChildren());
                //其中一条成功则代表其他支线不再有效
                break;
            }
            if(cardMode){    //评分卡模式需要继续遍历分数
                result = sumScore(result,temp.getChildren());
                if(temp.getResult()){
                    //其中一条成功则代表其他支线不再有效
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 引擎启动按钮
     * @param data 校验数据源（key-字段名，value-字段值）
     * @param ruleList 规则树
     * @return
     * @deprecated
     */
    public ResultDTO start(Map<String,Object> data, List<RuleDTO> ruleList){
        List<RuleDTO> ruleTree = buildTree(data,ruleList);
        marking(ruleTree);
        ResultDTO result = sumScore(new ResultDTO(),ruleTree);
        return result;
    }

    /**
     * 引擎启动按钮(新方式构造,推荐使用)
     * @param ruleTree 规则树
     * @return
     */
    public ResultDTO start(List<RuleDTO> ruleTree){
        marking(ruleTree);
        ResultDTO result = sumScore(new ResultDTO(),ruleTree);
        return result;
    }

    /**
     * 打印输出格式化
     * @param temp
     * @return
     */
    private String print(RuleDTO temp){
        String format = "字段[%s:%s]命中[%s]规则,得分:%s";
        String detail = String.format(format,
                temp.getFieldNameCN() != null ? temp.getFieldNameCN() : temp.getFieldName(),
                temp.getSourceValue(),
                temp.getRuleName(),
                temp.getScore());
        return detail;
    }

    public void setCardMode(boolean cardMode,boolean simpleMode) {
        this.cardMode = cardMode;
        this.simpleMode = !cardMode || simpleMode;
    }
}
