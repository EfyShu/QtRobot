package com.efy.util;

import com.efy.frame.Console;

import javax.swing.text.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class SwingStream extends PrintStream {
	private Color col  = Color.RED;
	private boolean bold = false;
	
	public SwingStream(OutputStream err) {
		super(err);
	}
	
	public SwingStream(OutputStream err,Color col) {
		super(err);
		this.col = col;
	}
	
	public SwingStream(OutputStream err,Color col,boolean bold) {
		super(err);
		this.col = col;
		this.bold = bold;
	}
	
	/**
	 * 重写打印方法,使字节流流向swing组件
	 */
	@Override
	public void write(byte[] buf, int off, int len) {
		String message = new String(buf, off, len);
		Document doc = Console.text.getDocument();
		try {
			doc.insertString(doc.getLength(), message, setDocs(col,bold));
		} catch (BadLocationException e) {
			System.err.println("Cause By:" + e.getCause());
			System.err.println("Messages:" + e.getMessage());
		}
		Console.text.getCaret().setDot(Console.text.getText().length());
	}
	
	/**
	 * 设置字体颜色
	 * @param col
	 * @param bold
	 * @return
	 */
	public AttributeSet setDocs(Color col, boolean bold) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, col);
		StyleConstants.setBold(attrSet, bold);
		return attrSet;
	}
}
