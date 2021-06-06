package com.efy.main;


import com.efy.frame.Console;
import com.efy.listener.sys.BeanMap;

import javax.swing.*;

/**
 * @author Efy Shu
 * @date 2020/02/18
 */
public class Application {
    //默认菜单
    private static String[][] defaultMenu = new String[][]{
            new String[]{"系统设置","com.efy.function.SystemMenu",
                    "调试模式","setDebug@1",
                    "清空面板","clearPanel",
            }
    };

    //菜单项
    private static String[][] items = new String[][]{
            new String[]{"账户功能","com.efy.function.Account",
                    "载入账户","login",
            },
            new String[]{"量化功能","com.efy.function.Quantitative",
                    "监控钱包状态","listenBalance@1:this",
                    "监控订单状态","listenOrder@1:this",
                    "自动下单","autoPlace@1:this",
            },
//            new String[]{"市场功能","com.efy.function.Market",
//                    "获取K线","kline",
//                    "单交易对聚合行情","merged",
//                    "全交易对聚合行情","tickers",
//            },
            new String[]{"订单功能","com.efy.function.Order",
                    "设置超时时间","caa",
            },
            new String[]{"其它","com.efy.function.SystemMenu",
                    "版权","showCopyright"
            }
    };

    /**
     * 初始化
     */
    private static void init(){
        BeanMap.loadBeans("com.efy");
        Console console = Console.getInstance(1366,768);
        console.initComponents("量化机器人",defaultMenu,items, JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        init();
    }
}
