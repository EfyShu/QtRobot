package com.efy.listener.ui.impl;


import com.efy.annotations.Listener;
import com.efy.listener.sys.BeanMap;
import com.efy.util.InvokeByThread;

import javax.swing.*;


/**
 * 按钮监听器 实现类
 *
 * @author Efy
 *
 */
@Listener
public class MenuListener extends AbstractButtonListener {

    public void excute(final AbstractButton button, final String[] strList, final Object devObj) {
        int i = 0;
        for (String str : strList) {
            i++;
            if (button.getText().equals(str)) {
                if (!strList[i].contains(":")) {
                    String method = strList[i].replaceAll("[@\\d{1}]*[#\\d{1}]*", "");
                    InvokeByThread.invokeMethod(devObj, method, null);
                } else {
                    String method = strList[i].split(":")[0].replaceAll("[@\\d{1}]*[#\\d{1}]*", "");
                    String[] rawParams = strList[i].split(":")[1].split(",");
                    Object[] param = new Object[rawParams.length];
                    for (int p = 0; p < rawParams.length; p++) {
                        if("this".equals(rawParams[p])){
                            param[p] = button;
                        }else{
                            param[p] = rawParams[p];
                        }
                    }
                    InvokeByThread.invokeMethod(devObj, method, param);
                }
                break;
            }
        }
    }

    @Override
    public void addListener(AbstractButton button, final String[] strList) {
        button.addActionListener(e -> {
            AbstractButton tempButton = (AbstractButton) e.getSource();
            try {
                excute(tempButton, strList, BeanMap.getBean(strList[1]));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
