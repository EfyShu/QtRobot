package com.efy.listener.ui.impl;

import com.efy.annotations.Module;
import com.efy.listener.sys.BeanMap;
import com.efy.listener.ui.IButtonListener;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 14:46
 * Description :
 **/
public abstract class AbstractButtonListener implements IButtonListener {

    public AbstractButtonListener() {
        BeanMap.addBean(this.getClass().getCanonicalName(),this);
    }

    @Override
    public void addListener(AbstractButton button, String[] strList) {

    }

    @Override
    public void preMethod(Method method, Object... params) {
        if(!method.isAnnotationPresent(Module.class)) return;
        StringBuffer paramStr = new StringBuffer();
        if(params != null && params.length > 0){
            for(Object param : params){
                if(!paramStr.toString().isEmpty()){
                    paramStr.append(",");
                }
                paramStr.append(param.toString());
            }
        }
        System.out.println(getLogPre(method) + "调用:" + paramStr.toString());
    }

    @Override
    public void afterMethod(Method method, Object result) {
        if(!method.isAnnotationPresent(Module.class)) return;
        System.out.println(getLogPre(method) + "返回:" + result);
    }

    /**
     * 拼接模块日志前缀
     * @param currM
     * @return
     */
    private String getLogPre(Method currM){
        if(!currM.isAnnotationPresent(Module.class)) return "";
        Module module = currM.getAnnotation(Module.class);
        String name = (module.value() == null || module.value().isEmpty()) ? module.name() : module.value();
        StringBuffer pre = new StringBuffer();
        for(String tag : module.tags()){
            pre.append("【").append(tag).append("】");
        }
        pre.append(name);
        return pre.toString();
    }
}
