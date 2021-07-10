package com.sr.ioc.ioc2.bean;

public class SimpleBean {

	private String connectiongString;
	private String password;
	private String username;

	public SimpleBean() {
		System.out.println("invoke SimpleBean() !!");
	}

	public String getConnectiongString() {
		return connectiongString;
	}

	public void setConnectiongString(String connectiongString) {
		this.connectiongString = connectiongString;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "SimpleBean{" +
				"connectiongString='" + connectiongString + '\'' +
				", password='" + password + '\'' +
				", username='" + username + '\'' +
				'}';
	}
}
