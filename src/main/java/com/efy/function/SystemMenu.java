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
    private boolean debug = false;
    private boolean info = false;

    @Override
    public void setDebug(){
        this.debug = !this.debug;
    }

    @Override
    public void setInfo() {
        this.info = !info;
    }

    private void print(String message, boolean error){
        if(error){
            System.err.println(message);
        }else{
            System.out.println(message);
        }
    }

    @Override
    public void printDebug(String message){
        if(!this.debug){
            return;
        }
        print(message,false);
    }

    @Override
    public void printInfo(String message){
        if(!this.info){
            return;
        }
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
