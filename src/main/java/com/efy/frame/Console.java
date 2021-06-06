package com.efy.frame;


import com.efy.listener.ui.impl.MenuListener;
import com.efy.util.SwingStream;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.JTextComponent;
import java.awt.*;


/**
 * UI控制台,监听系统输出流,写入到JTextComponent中
 * @author Efy
 *
 */
public class Console{
	private JFrame console = new JFrame();       //主窗体
	private int width;
	private int height;
	public Font font;

	public static JTextComponent text;

	private static Console ins;

	private Console(int initWidth,int initHeight,Font font) {
		//切换windows皮肤
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { 
//				SystemMenu.out.println(info.getName());
		        if ("Windows".equals(info.getName())) {  
		            UIManager.setLookAndFeel(info.getClassName());  
		            break;  
		        }  
		    }
            SwingStream out = new SwingStream(System.out,Color.GRAY);
            SwingStream err = new SwingStream(System.err,Color.RED);
            this.width = initWidth;
            this.height = initHeight;
            if(font != null){
                this.font = font;
            }else {
                this.font = new Font("宋体",Font.PLAIN,12);
            }
            System.setOut(out);
            System.setErr(err);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

    /**
     * 生成一个控制台面板
     * @return 面板对象
     */
    public static Console getInstance(int initWidth,int initHeight,Font font){
        //将系统输出流转入JTextComponent
        if(ins == null){
            ins = new Console(initWidth,initHeight,font);
        }
        return ins;
    }

	/**
	 * 生成一个控制台面板
	 * @return 面板对象
	 */
	public static Console getInstance(int initWidth,int initHeight){
		return getInstance(initWidth,initHeight,null);
	}

    /**
     * 生成一个控制台面板
     * @return 面板对象
     */
    public static Console getInstance(){
        return getInstance(640,480,null);
    }
	
	/**
	 * 生成一个控制台面板
	 * @param title           面板标题
	 * @param defaultItems    默认菜单项
	 * @param customItems     自定义菜单
	 * @param closeOperation  面板关闭属性
	 */
	public void initComponents(String title,String[][] defaultItems,String[][] customItems,int closeOperation){
		JTextPane jText = new JTextPane();            //文本域
		JScrollPane jsp = new JScrollPane(jText);     //滚动条
		
		console.setTitle(title);
		console.setSize(width, height);
		console.setLocationRelativeTo(null);        //窗体居中显示
		console.setDefaultCloseOperation(closeOperation);
		
		//当需要时显示滚动条
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.getVerticalScrollBar().setUnitIncrement(50);  //设置鼠标滚动速度

        jText.setFont(font);
		jText.setMargin(new Insets(0,10,0,0));
		jText.setEditable(false);
		jText.setAutoscrolls(true);
		jText.setBackground(Color.BLACK);
		jText.setForeground(Color.RED);
		jText.setCaretColor(Color.LIGHT_GRAY);
		console.setContentPane(jsp);
		
		text =  jText;
		
		//设置菜单栏
		console.setJMenuBar(initMenu(defaultItems,customItems));
		
		console.setVisible(true);
	}

	/**
	 * 初始化菜单栏(@1表示是checkbox,#1表示checkbox默认选中)
	 * @param defaultItems 默认菜单项
	 * @param customItems  自定义菜单
	 * @return
	 */
	private JMenuBar initMenu(String[][] defaultItems,String[][] customItems){
		JMenuBar menuBar = new JMenuBar();
		MenuListener buttonListener = new MenuListener();
		//默认菜单项
		bindListener(menuBar,defaultItems,buttonListener);
		//自定义菜单项
		bindListener(menuBar,customItems,buttonListener);
		return menuBar;
	}

	/**
	 * 为菜单绑定监听器
	 * @param menuBar
	 * @param menus
	 * @param buttonListener
	 */
	private void bindListener(JMenuBar menuBar,String[][] menus,MenuListener buttonListener){
		boolean checkBoxFlag = false;
		for(String[] item : menus){
			JMenu menu = new JMenu(item[0]);
			for(int i=2;i<item.length;i+=2){
                checkBoxFlag = item[i + 1].contains("@1");
				if(checkBoxFlag){
					JCheckBoxMenuItem tempItem = new JCheckBoxMenuItem(item[i]);
					menu.add(tempItem);
					if(item[i+1].contains("#1")) tempItem.setSelected(true);
					buttonListener.addListener(tempItem, item);
				}else{
					JMenuItem tempItem = new JMenuItem(item[i]);
					menu.add(tempItem);
					buttonListener.addListener(tempItem, item);
				}
			}
			menuBar.add(menu);
		}
	}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Font getFont() {
        return font;
    }

    public JFrame getConsole() {
		return console;
	}
}
