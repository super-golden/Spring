package com.sr.ioc.ioc4.lifecycle.beans;

public class Car {

	public Car(){
		System.out.println("car constructor.....");
	}
	public void init(){
		System.out.println("car ......init....");
	}
	public void destroy(){
		System.out.println("car .......destroy.....");
	}
}
