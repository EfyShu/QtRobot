package com.efy.listener.ui;


import javax.swing.*;
import java.lang.reflect.Method;


/**
 * 按钮监听器接口
 * @author Efy
 *
 */
public interface IButtonListener {

    /**
     * 按钮添加监听器
     */
    void addListener(AbstractButton button, String[] strList);

    /**
     * 执行方法前动作
     * @param method
     * @param params
     */
    void preMethod(Method method, Object... params);

    /**
     * 执行方法后动作
     * @param method
     * @param result
     */
    void afterMethod(Method method, Object result);
}
