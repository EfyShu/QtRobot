package com.efy.listener.sys;

import com.efy.listener.ui.impl.FunctionListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 2:40
 * Description :
 **/
public class BeanInvocation implements InvocationHandler {
    private Object target;
    private FunctionListener listener;

    public BeanInvocation(Object target, FunctionListener listener) {
        this.target = target;
        this.listener = listener;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.setAccessible(true);
        listener.preMethod(method,args);
        Object result = method.invoke(target,args);
        listener.afterMethod(method,result);
        return result;
    }

    public Object getTarget() {
        return target;
    }
}
