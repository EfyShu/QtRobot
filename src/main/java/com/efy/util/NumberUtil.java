package com.efy.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Author : Efy Shu
 * Date : 2021/5/12 11:39
 * Description :
 **/
public class NumberUtil {

    public static String numberToStr(Object number){
        return numberToStr(number,false);
    }

    public static String numberToStr(Object number,boolean format){
        if(number instanceof Float || number instanceof Double){
            try {
                BigDecimal bigDecimal;
                if(format){
                    bigDecimal = new BigDecimal(number.toString())
                            .setScale(2, RoundingMode.DOWN);
                }else{
                    bigDecimal = new BigDecimal(number.toString());
                }
                return bigDecimal.toPlainString();
            }catch (Exception e){

            }
        }
        return "0";
    }

    public static String format(Object number,int partition){
        if(number instanceof Float || number instanceof Double){
            try {
                BigDecimal bigDecimal = new BigDecimal(number.toString())
                        .setScale(partition, RoundingMode.DOWN);
                return bigDecimal.toPlainString();
            }catch (Exception e){

            }
        }
        return "0";
    }
}
