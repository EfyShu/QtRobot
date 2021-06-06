package com.efy.function;

import com.efy.annotations.Function;
import com.efy.frame.Console;
import com.efy.function.proxy.ISystemMenu;

import javax.swing.*;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 1:54
 * Description :
 **/
@Function
public class SystemMenu implements ISystemMenu {
    private Boolean debug = false;

    @Override
    public void setDebug(){
        this.debug = !this.debug;
    }

    private void print(String message, boolean error){
        if(!this.debug){
            return;
        }
        if(error){
            System.err.println(message);
        }else{
            System.out.println(message);
        }
    }

    @Override
    public void printDebug(String message){
        print(message,false);
    }

    @Override
    public void printError(String message){
        print(message,true);
    }

    @Override
    public void clearPanel(){
        Console.text.setText("");
    }

    @Override
    public void showCopyright(){
        JOptionPane.showMessageDialog(Console.getInstance().getConsole(),"版权所有:Efy Shu");
    }
}
