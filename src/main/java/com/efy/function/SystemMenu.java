package com.efy.function;

import com.efy.annotations.Function;
import com.efy.frame.Console;

import javax.swing.*;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 1:54
 * Description :
 **/
@Function
public class SystemMenu {
    private static Boolean debug = false;

    public static void setDebug(){
        SystemMenu.debug = !SystemMenu.debug;
    }

    public static void print(String message,boolean error){
        if(!SystemMenu.debug){
            return;
        }
        if(error){
            System.err.println(message);
        }else{
            System.out.println(message);
        }
    }
    public static void printDebug(String message){
        print(message,false);
    }

    public static void printError(String message){
        print(message,true);
    }

    public void clearPanel(){
        Console.text.setText("");
    }

    public void showCopyright(){
        JOptionPane.showMessageDialog(Console.getInstance().getConsole(),"版权所有:Efy Shu");
    }
}
