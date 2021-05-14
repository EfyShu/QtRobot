package com.efy.listener.ui.impl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * 窗口同步移动监听器
 * @author Efy
 *
 */
public class WindowMoveSyn implements ComponentListener{
    private JFrame frame;
    private int firstWidth;
    private int firstHeight;
    private int currentPosition;

    //窗口放置位置
    public static final int POSITION_UP      =         0x00000000;
    public static final int POSITION_DOWN    =         0x00000001;
    public static final int POSITION_LEFT    =         0x00000002;
    public static final int POSITION_RIGHT   =         0x00000003;

    /**
     *
     * 构造
     * @param willChangeFrame 需要同步的窗口
     * @param willPutPosition 需要放置的位置
     * @see #POSITION_UP
     * @see #POSITION_DOWN
     * @see #POSITION_LEFT
     * @see #POSITION_RIGHT
     */
    public WindowMoveSyn(JFrame willChangeFrame,int willPutPosition) {
        this.frame = willChangeFrame;
        this.firstWidth = willChangeFrame.getWidth();
        this.firstHeight = willChangeFrame.getHeight();
        this.currentPosition = willPutPosition;
    }

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void componentMoved(ComponentEvent e) {
        //如果要同步的窗口是最大化状态则不移动位置
        if(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH){
            return;

            //如果原窗口是最大化状态则把要同步的窗口设置为原始大小并居中
        } else if(((JFrame)e.getComponent()).getExtendedState() == JFrame.MAXIMIZED_BOTH){
            frame.setSize(firstWidth, firstHeight);
            frame.setLocationRelativeTo(null);

            //跟随移动
        } else{
            switch (currentPosition) {
                case POSITION_UP:
                    if(e.getComponent().getY() - frame.getHeight() < 0){
                        currentPosition = POSITION_DOWN;
                        return;
                    }
                    frame.setBounds(e.getComponent().getX(), e.getComponent().getY()-frame.getHeight(), e.getComponent().getWidth(), frame.getHeight());
                    break;
                case POSITION_DOWN:
                    if(e.getComponent().getY() + e.getComponent().getHeight() + frame.getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getHeight()){
                        currentPosition = POSITION_UP;
                        return;
                    }
                    frame.setBounds(e.getComponent().getX(), e.getComponent().getHeight(), e.getComponent().getWidth(), frame.getHeight());
                    break;
                case POSITION_LEFT:
                    if(e.getComponent().getX()-frame.getWidth() < 0){
                        currentPosition = POSITION_RIGHT;
                        return;
                    }
                    frame.setBounds(e.getComponent().getX()-frame.getWidth(), e.getComponent().getY(), frame.getWidth(), frame.getHeight());
                    break;
                case POSITION_RIGHT:
                    if(e.getComponent().getX()+e.getComponent().getWidth()+frame.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()){
                        currentPosition = POSITION_LEFT;
                        return;
                    }
                    frame.setBounds(e.getComponent().getX()+e.getComponent().getWidth(), e.getComponent().getY(), frame.getWidth(), frame.getHeight());
                    break;
            }
            e.getComponent();
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        //如果要同步的窗口是最大化状态则不重置大小
        if(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH){
            return;

            //如果原窗口是最大化状态则把要同步的窗口设置为原始大小并居中
        } else if(((JFrame)e.getComponent()).getExtendedState() == JFrame.MAXIMIZED_BOTH){
            frame.setSize(firstWidth, firstHeight);
            frame.setLocationRelativeTo(null);

            //其他情况则与原窗口同步(上下则宽度同步,高度不同步,左右则高度同步,宽度不同步)
        } else{
            switch (currentPosition) {
                case POSITION_UP:
                    frame.setBounds(e.getComponent().getX(), e.getComponent().getY()-frame.getHeight(), frame.getWidth(), e.getComponent().getHeight());
                    break;
                case POSITION_DOWN:
                    frame.setBounds(e.getComponent().getX(), e.getComponent().getHeight(), frame.getWidth(), e.getComponent().getHeight());
                    break;
                case POSITION_LEFT:
                    frame.setBounds(e.getComponent().getX()-frame.getWidth(), e.getComponent().getY(), frame.getWidth(), e.getComponent().getHeight());
                    break;
                case POSITION_RIGHT:
                    frame.setBounds(e.getComponent().getX()+e.getComponent().getWidth(), e.getComponent().getY(), frame.getWidth(), e.getComponent().getHeight());
                    break;
            }
            e.getComponent().requestFocus();
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {}
}
