package com.sr.ioc.ioc2.bean;

import java.util.Date;

public class UserManger {

	private Date dataValue;

	private String message;

	public UserManger() {
		System.out.println("UserManger construct");
	}

	public Date getDataValue() {
		return dataValue;
	}

	public void setDataValue(Date dataValue) {
		this.dataValue = dataValue;
	}

	@Override
	public String toString() {
		return "UserManger{" +
				"dataValue=" + dataValue +
				'}';
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
