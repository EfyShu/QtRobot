package com.efy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMD {
    //当前进程
    public static Process pro = null;

    public static void cancel(){
        pro.destroy();
//		SystemMenu.out.println("Control-C \r\n^C");
    }

    public static void printResult() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        String result = "";
        while((result = br.readLine()) != null){
            System.out.println(result);
        }
        br.close();

        br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));

        while((result = br.readLine()) != null){
            System.err.println(result);
        }

        br.close();
    }

    public static void exec(String cmd){
        //先终止上个进程再执行下个进程
        if(pro != null){
            cancel();
        }
        //以管理员身份执行
//			pro = Runtime.getRuntime().exec("net localgroup administrators "+SystemMenu.getProperty("user.name")+" /add");
//			pro = Runtime.getRuntime().exec("runAs /user:liangx8rx@qq.com " + cmd);
        try {
            pro = Runtime.getRuntime().exec(cmd);
            printResult();
        } catch (IOException e) {
            //不报错
        }
    }
}
