package com.efy.listener.ui.impl;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * 窗口同步显示
 * @author Efy
 *
 */
public class WindowShowSyn implements WindowStateListener {
    private JFrame frame;

    /**
     * 构造
     * @param willChangeFrame 需要同步的窗口
     */
    public WindowShowSyn(JFrame willChangeFrame) {
        frame = willChangeFrame;
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        //如果原窗口为最大化状态,则附属窗口不做变化,否则跟随原窗口状态
        if(e.getNewState() == JFrame.MAXIMIZED_BOTH){
            return;
        }
        frame.setExtendedState(e.getNewState());
    }
}
