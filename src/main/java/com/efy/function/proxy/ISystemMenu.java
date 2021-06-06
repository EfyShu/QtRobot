package com.efy.function.proxy;


/**
 * Author : Efy Shu
 * Date : 2021/6/7 4:26
 * Description :
 **/
public interface ISystemMenu {
    void setDebug();

    void setInfo();

    void printDebug(String message);

    void printInfo(String message);

    void printError(String message);

    void clearPanel();

    void showCopyright();
}
