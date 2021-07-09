package com.sr.ioc.ioc2.editor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {

	private  String format = "yyyy-MM-dd";

	public void setFormat(String format) {
		this.format = format;
	}



	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		System.out.println("text:"+text);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date parse = dateFormat.parse(text);
			this.setValue(parse);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		super.setAsText(text);
	}
}
