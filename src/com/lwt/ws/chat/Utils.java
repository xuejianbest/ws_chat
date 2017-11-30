package com.lwt.ws.chat;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

public class Utils {
	/**
	 * @param len 生成的字符创长度
	 * @param radix 生成的字符串进制
	 * @return 
	 */
	public static String randomString(int len, int radix){
		Double d = Math.random() * Math.pow(radix, len);
		String s = Long.toString(d.longValue(), radix);
		return s;
	}
	
	/**
	 * @param bg 开始字符串
	 * @param ed 结束字符串
	 * @return 获取当前时间，格式为bg + "05-10 11:02:02" + ed
	 */
	public static String getTime(String bg, String ed){
		Calendar rightNow = Calendar.getInstance();
		int M = rightNow.get(Calendar.MONTH) + 1;
		int d = rightNow.get(Calendar.DATE);
		int h = rightNow.get(Calendar.HOUR_OF_DAY);
		int m = rightNow.get(Calendar.MINUTE);
		int s = rightNow.get(Calendar.SECOND);
		String time = "";
		time += M<10 ? "0"+M : M;
		time += "-";
		time += d<10 ? "0"+d : d;
		time += " ";
		time += h<10 ? "0"+h : h;
		time += ":";
		time += m<10 ? "0"+m : m;
		time += ":";
		time += s<10 ? "0"+s : s;
		return bg + time + ed;
	}
	
	public static void main(String[] args) {
		String str = "    a<";
		String a = StringEscapeUtils.escapeHtml4(str);//unescapeHtml4(str);
		a = StringEscapeUtils.escapeXml11(str);  
		System.out.println(a);
	}
//	public static void main(){
//		String s = new Utils().getClass().getClassLoader().getResource("record").getPath();
//		
//		File file = new File(s);
//		System.out.println(file.getAbsolutePath());
//	}
}
