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

    private RuleEngine(){}

    public static RuleEngine getIns() {
        if(ins == null){
            ins = new RuleEngine();
        }
        return ins;
    }

    /**是否简单模式(true-命中任意规则返回,false-命中继续遍历)*/
    private boolean simpleMode = false;

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
            throw e;
//            System.err.println("数据内容:"+source+"|字符转换时发生异常!"+e.getMessage());
//            e.printStackTrace();
        }
//        return null;
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
     * (递归算法实现,超过50层时建议优化)
     * @param ruleTree
     */
    private void marking(List<RuleDTO> ruleTree){
        for(RuleDTO dto : ruleTree){
            boolean result;
            result = compair(dto.getSourceValue(),dto.getTargetValue(),dto.getOperator());
            dto.setResult(result);
            if(result && simpleMode){
                break;
            }
            //成功则进入子项比较
            if(result && (dto.getChildren() != null && !dto.getChildren().isEmpty())) {
                marking(dto.getChildren());
                //其中一条成功则代表其他支线不再有效
                break;
            }
        }
    }

    /**
     * 计算得分
     * (递归算法实现,超过50层时建议优化)
     * @param ruleTree
     * @return
     */
    private ResultDTO sumScore(ResultDTO result, List<RuleDTO> ruleTree){
        if(ruleTree == null) {
            return result;
        }
        for(RuleDTO temp : ruleTree){
            if(!temp.getResult()) {
                continue;
            }
            double score = result.getScore() + temp.getScore();
            result.setScore(score);
            result.getDetails().add(print(temp));
            //任意一条原子规则命中,则总体结果为命中
            if(temp.getResult()){
                result.setHitCount(result.getHitCount()+1);
                result.setResult(true);
            }
            //如果没有子规则,则直接中断计算
            if(temp.getChildren() == null || temp.getChildren().isEmpty()) {
                break;
            }
            result = sumScore(result,temp.getChildren());
            //其中一条成功则代表其他支线不再有效
            break;
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
}
