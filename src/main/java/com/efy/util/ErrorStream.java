package com.efy.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorStream extends PrintStream{
		public ErrorStream(OutputStream out) {
			super(out);
		}
		
		@Override
		public void write(byte[] buf, int off, int len) {
			String message = new String(buf, off, len);
			StringBuffer sb = new StringBuffer();
			//保存之前的错误信息
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(new Date()).split(" ")[0];
			String time = sdf.format(new Date()).split(" ")[1];
			File file = new File("errorLog_" + date + ".log");
			try {
				if(!file.exists()){
					file.mkdirs();
					file.delete();
					file.createNewFile();
				}
				FileInputStream fis = new FileInputStream(file);
				byte[] b = new byte[512];
				int length = 0;
				while((length=fis.read(b)) > 0){
					sb.append(new String(b,0,length));
				}
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//继续写错误信息
			if(!"\r\n".equals(message)){
				sb.append("[").append(time).append("]");
				sb.append(message);
			}else{
				sb.append(message);
			}
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes(), off, sb.toString().getBytes().length);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}