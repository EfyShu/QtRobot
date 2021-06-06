package com.efy.listener.sys;

import com.efy.annotations.Aspect;
import com.efy.annotations.Function;
import com.efy.annotations.Listener;
import com.efy.listener.ui.impl.FunctionListener;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author : Efy
 * Date : 2021年5月10日 13点30分
 * Description :
 *
 **/
public class BeanMap {
    private static Map<String,Object> beans;


    public static <T> T getBean(Class<T> clazz){
        if(BeanMap.beans == null){
            BeanMap.beans = new ConcurrentHashMap<>();
        }
        return (T)BeanMap.beans.get(clazz.getCanonicalName());
    }

    public static <T> T getBean(String name) {
        if(BeanMap.beans == null){
            BeanMap.beans = new ConcurrentHashMap<>();
        }
        return (T)BeanMap.beans.get(name);
    }

    public static Map<String,Object> getBeans(){
        return BeanMap.beans;
    }

    public static void addBean(String name ,Object bean) {
        if(BeanMap.beans == null){
            BeanMap.beans = new ConcurrentHashMap<>();
        }
        BeanMap.beans.put(name,bean);
    }

    public static void loadBeans(String packagePath){
        List<Class> beanClasses = BeanMap.scanClass(packagePath, Aspect.class, Listener.class,Function.class);
        FunctionListener functionListener = new FunctionListener();
        for(Class clazz : beanClasses){
            try {
                if(clazz.isAnnotationPresent(Aspect.class)){
                    doAspect(clazz);
                }else if(clazz.isAnnotationPresent(Listener.class)){
                    doListener(clazz);
                }else if(clazz.isAnnotationPresent(Function.class)){
                    doFunction(clazz,functionListener);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注解标记为空时,不扫描任何类
     * @param packagePath
     * @param annotations
     * @return
     */
    public static List<Class> scanClass(String packagePath, Class<? extends Annotation>... annotations) {
        List<Class> classList = new ArrayList<>();
        if (packagePath ==  null || packagePath.isEmpty() || annotations == null || annotations.length == 0) {
            return classList;
        }
        try {
            String tempPath =packagePath.replace(".","/");
            Enumeration<URL> dir = Thread.currentThread().getContextClassLoader().getResources(tempPath);
            if(dir.hasMoreElements()){
                URL url = dir.nextElement();
                if("file".equals(url.getProtocol())){
                    File file = new File(url.getPath().substring(1));
                    List<String> classNames = getClassNames(file,new ArrayList<>(),packagePath);
                    for (String className : classNames){
                        Class clazz = Class.forName(className,false,Thread.currentThread().getContextClassLoader());
                        for(Class anno : annotations){
                            //过滤不包含注解的类
                            if(clazz.isAnnotationPresent(anno)){
                                classList.add(clazz);
                            }
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return classList;
    }

    private static void doAspect(Class clazz) throws IllegalAccessException, InstantiationException {
        BeanMap.addBean(clazz.getCanonicalName(),clazz.newInstance());
    }

    private static void doListener(Class clazz) throws IllegalAccessException, InstantiationException {
        BeanMap.addBean(clazz.getCanonicalName(),clazz.newInstance());
    }

    private static void doFunction(Class clazz, FunctionListener listener) throws IllegalAccessException, InstantiationException {
        InvocationHandler handler = new BeanInvocation(clazz.newInstance(),listener);
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
        BeanMap.addBean(clazz.getCanonicalName(),proxy);
//        BeanMap.addBean(clazz.getCanonicalName(),clazz.newInstance());
    }

    private static List<String> getClassNames(File file,List<String> list,String packagePath){
        if(list == null) list = new ArrayList<>();
        if(file.isDirectory()){
            File[] fs = file.listFiles((dir1, name) -> {
                // 排除内部内
                return !name.contains("$");
            });
            for(File tempFile : fs){
                getClassNames(tempFile,list,packagePath);
            }
        }else{
            String tempPath = file.getAbsolutePath().replace("\\",".").replace("/",".");
            String className = tempPath.substring(tempPath.indexOf(packagePath)).replace(".class","");
            list.add(className);
        }

        return list;
    }
}
