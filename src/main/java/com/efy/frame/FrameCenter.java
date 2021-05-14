package com.efy.frame;


import com.efy.listener.ui.IButtonListener;
import com.efy.listener.ui.impl.MenuListener;
import com.efy.listener.ui.impl.WindowMoveSyn;
import com.efy.listener.ui.impl.WindowShowSyn;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * 驱动测试,GUI工具
 * @author Efy
 *
 */
public class FrameCenter implements Serializable{

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 3655931195013312137L;
	

	public static JFrame mainFrame = new JFrame();
	public static JPanel mainPanel = new JPanel();
	public static Console console;
	private static String[][] buttons;
	private static IButtonListener buttonListener;
	
	public FrameCenter() {
		buttons = new String[][]{
			new String[]{"网络适配器","com.efy.device.NetWork","启用","enable","禁用","disable","ping","ping","获取状态","getStatus"},
			new String[]{"网站服务器","com.efy.device.WebServer","开启","start","关闭","shutdown","检测","check","版本","version"},
		};
		
		//如果没有默认按钮监听器则默认注册
		buttonListener = new MenuListener();
		
		//切换windows皮肤
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { 
//				SystemMenu.out.println(info.getName());
		        if ("Windows".equals(info.getName())) {  
		            UIManager.setLookAndFeel(info.getClassName());  
		            break;  
		        }  
		    }
		} catch (Exception e) {
			System.err.println("Cause By:" + e.getCause());
			System.err.println("Messages:" + e.getMessage());
		}
	}
		
	/**
	 * 初始化窗口
	 * @param title
	 */
	public void initFrame(String title,int width,int height){
		//控制台面板
		console = Console.getInstance(700,0);
//		console.initComponents(title.concat("信息"), JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setTitle(title);
		mainFrame.setBounds(console.getConsole().getX() + console.getWidth(), console.getConsole().getY(), width, height);  //先设置大小
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置图标
//		mainFrame.setIconImage(new ImageIcon("E:/图片/LiPlane.png").getImage());
//		console.getConsole().setIconImage(new ImageIcon("E:/图片/LiPlane.png").getImage());
		//窗口同步移动和显示
		WindowMoveSyn wms = new WindowMoveSyn(console.getConsole(),WindowMoveSyn.POSITION_LEFT);
		WindowShowSyn wss = new WindowShowSyn(console.getConsole());
		mainFrame.addComponentListener(wms);
		mainFrame.addWindowStateListener(wss);

		//初始化主面板
		initPanel();

		//初始化菜单
		mainFrame.setJMenuBar(initMenu());
		
		mainFrame.setVisible(true);
	}
	
	/**
	 * 初始化菜单栏
	 * @return
	 */
	private JMenuBar initMenu(){
		String[][] items = new String[][]{
			new String[]{"系统设置","com.efy.test.AutoReply","显示源码","showSourceFunc","显示参数","showParamsFunc"},
		};
		JMenuBar menuBar = new JMenuBar();
		for(String[] item : items){
			JMenu menu = new JMenu(item[0]);
			for(int i=2;i<item.length;i+=2){
				JCheckBoxMenuItem tempItem = new JCheckBoxMenuItem(item[i]);
				menu.add(tempItem);
				buttonListener.addListener(tempItem, item);
			}
			menuBar.add(menu);
		}
		return menuBar;
	}
	
	/**
	 * 初始化主面板
	 */
	private void initPanel(){
		mainPanel.setSize(mainFrame.getWidth(), mainFrame.getHeight());
		mainPanel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(50);  //设置鼠标滚动速度
		//初始化按钮
		initButton();
//		mainPanel.revalidate();    //重新验证panel大小,显示滚动条
		mainFrame.setContentPane(scrollPane);
	}

	/**
	 * 初始化按钮
	 */
	@SuppressWarnings("serial")
	private void initButton(){
		int padding=10,x=padding,y=padding,width=100,height=30;
		//生成label标签
		for(String[] button : buttons){
			JLabel tempLabel = new JLabel(button[0]+":");
			tempLabel.setBounds(x, y, width, height);
			mainPanel.add(tempLabel);

			x+=tempLabel.getWidth();
			//生成控制按钮
			for(int i=2;i<button.length;i+=2){
				if((x+width+padding)>=mainPanel.getWidth() - padding){
					x = width+padding;
					y +=height+padding;
				}
				JButton tempButton = new JButton(button[i]);
				tempButton.setBounds(x, y, width, height);
				tempButton.setToolTipText(button[i]);
				try {
					//为按钮注册监听
					buttonListener.addListener(tempButton, button);
				} catch (Exception e) {
					System.err.println("\"" + button[1] + "\"不存在!" + e.getMessage());
				}
				mainPanel.add(tempButton);
				x+=width+padding;
			}
			x=padding;
			y+=height+padding;
			//添加分割线
			JLabel hr = new JLabel(){
				@Override
				protected void paintComponent(Graphics g) {
					g.setColor(Color.LIGHT_GRAY);
					g.drawLine(0, 0, mainPanel.getWidth(), 0);
					g.setColor(Color.WHITE);
					g.drawLine(0, 1, mainPanel.getWidth(), 1);
				}
			};
			hr.setBounds(0, y, mainPanel.getWidth(), 2);
			mainPanel.add(hr);
			y+=hr.getHeight() + padding;
		}
		mainPanel.setPreferredSize(new Dimension(x,y));
	}
	
	/**
	 * 弹出窗口显示图片
	 * @param imgPath
	 */
	public static void initImageFrame(String imgPath,String title){
		ImageIcon icon = new ImageIcon(imgPath);
		//解决bmp格式图片显示问题
		if(imgPath.endsWith(".bmp")){
			try {
				BufferedImage bi = ImageIO.read(new File(imgPath));
				icon = new ImageIcon(bi.getScaledInstance(-1, -1, -1));
			} catch (IOException e) {
				System.err.println("图片读取IO错误");
				return;
			}
		}
		//超出屏幕显示范围时,窗口弹出会失败
		int width = icon.getIconWidth(),height = icon.getIconHeight();
		if(width < 150 || height < 150){
			width += 120;
			height += 45;
		}else if (width > 1000 || height > 600) {
			width = 1000;
			height = 600;
		}
		JFrame imageFrame = new JFrame();
		JLabel imageLabel = new JLabel(icon);
		JScrollPane srcollPane = new JScrollPane(imageLabel);
		srcollPane.getVerticalScrollBar().setUnitIncrement(30);  //设置鼠标滚动速度
		
		imageFrame.setTitle(title);
		imageFrame.add(srcollPane);
		imageFrame.setSize(width,height);
		imageFrame.setLocationRelativeTo(null);
		imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		imageFrame.setVisible(true);
	} 
	
	public String[][] getButtons() {
		return buttons;
	}

	/**
	 * 按钮组件,非默认按钮需自行实现监听接口
	 * <p>格式:
	 * <p>1.第一个为当前模块组的标签名
	 * <p>2.第二个为监听器的方法名
	 * <p>3.后面的都是按钮名称及响应方法名
	 * <p><b>例:new String[]{"密码键盘","com.efy.device.Pin","开启","open","接收输入","startEPP"}</b>
	 * <p><b>该数组会在面板生成标签为"密码键盘",监听类为com.efy.device.Pin,含有"开启"和"接收输入"按钮,
	 * 		 "开启"和"接收输入"响应该类中的open和startEPP方法段</b>
	 * @see IButtonListener
	 * @see MenuListener
	 */
	public void setButtons(String[][] buttons) {
		FrameCenter.buttons = buttons;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public IButtonListener getButtonListener() {
		return buttonListener;
	}

	public void setButtonListener(IButtonListener buttonListener) {
		FrameCenter.buttonListener = buttonListener;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FrameCenter fc = new FrameCenter();
		fc.initFrame("设备控制面板",700,500);
//		fc.initImageFrame("E:\\图片\\地精理论.png","显示图片");
	}
}
