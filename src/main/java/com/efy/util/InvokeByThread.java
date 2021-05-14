package com.efy.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 用线程处理按钮的响应事件
 * @author Efy
 *
 */
public class InvokeByThread {
	public static void invokeMethod(final Object obj,final String method,final Object[] param){
		Thread t = new Thread(() -> {
            try {
                Method methodObj = getMethod(obj,method,param);
                if(methodObj == null) return;
                if(null != param){
                    List<Class> typeList = new ArrayList<>();
                    for(Object paramObj : param){
                        typeList.add(paramObj.getClass());
                    }
                    methodObj.invoke(obj,param);
                }else{
                    methodObj.invoke(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
		t.start();
	}

	private static Method getMethod(Object obj,String methodName,Object... param) throws NoSuchMethodException {
        try {
            Method methodObj;
            if(null != param){
                List<Class> typeList = new ArrayList<>();
                for(Object paramObj : param){
                    typeList.add(paramObj.getClass());
                }
                methodObj = obj.getClass().getMethod(methodName, typeList.toArray(new Class[]{}));
            }else{
                methodObj = obj.getClass().getMethod(methodName);
            }
            methodObj.setAccessible(true);
            return methodObj;
        } catch (NoSuchMethodException e){
            //单独检查一次因类型不匹配导致的方法实例获取不到
            if(param != null){
                for(Method method : obj.getClass().getMethods()){
                    if(!methodName.equals(method.getName()) || method.getParameterCount() != param.length){
                        continue;
                    }
                    Class[] paramTypes = method.getParameterTypes();
                    for(int i=0;i< param.length;i++){
                        Class clazz = paramTypes[i];
                        if(!clazz.isInstance(param[i])){
                            throw e;
                        }
                    }
                    return method;
                }

            }
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
