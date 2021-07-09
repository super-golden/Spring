package com.sr.ioc.ioc2.il8n;

import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main {

	/*
	java.util.Locale 类表示一个本地化对象，它允许通过语言参数和国家/地区参数创建一个确定的本地化对象
	 */

	public static void main(String[] args) {
//		//1、带有语言和国家/地区信息的本地化对象
//		Locale locale1 = new Locale("zh","CN");
//		System.out.println(locale1.getDisplayName());
//
//		//2、只有语言信息的本地化对象
//		Locale locale2 = new Locale("zh");
//		System.out.println(locale2.getDisplayName());
//
//		//3、等同于Locale("zh","CN");
//		Locale locale3 = Locale.CHINA;
//		System.out.println(locale3);
//
//		//4、等同于Locale("zh")
//		Locale locale4 =Locale.CHINESE;
//		System.out.println(locale4);
//
//		//5、获取本地系统默认的本地化对象
//		Locale locale5 = Locale.getDefault();
//		System.out.println(locale5);


		//信息格式化串
		String pattern1 = "{0},你好！你与{1}在工商银行存入{2}元。";
		String pattern2 = "At {1,time,short} On {1,date,long},{0} paid {2,number,currency}.";
		//用于动态替换占位符的参数
		Object[] params = {"John", new GregorianCalendar().getTime(), 1.0E3};

		//使用默认本地化对象格式化信息
		String msg1 = MessageFormat.format(pattern1,params);

		//使用指定的本地化对象格式化信息
		MessageFormat mf = new MessageFormat(pattern2, Locale.US);

		String msg2 = mf.format(params);

		System.out.println(msg1);
		System.out.println(msg2);

	}
}
