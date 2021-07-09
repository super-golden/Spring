package com.sr.ioc.ioc2.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.datetime.DateFormatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class String2DateConverter implements Converter<String, Date> {
	@Override
	public Date convert(String source) {

		try {
			DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd");

			return dateFormatter.parse(source, Locale.CHINA);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
