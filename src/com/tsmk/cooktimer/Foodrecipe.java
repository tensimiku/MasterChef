package com.tsmk.cooktimer;


public class Foodrecipe {
	private int totalstep;
	private String foodname;
	private String foodsection;
	
	Foodrecipe(){
		this(0, "TEST","그 외");
	}
	Foodrecipe(int step,String name){
		this(step,name,"그 외");
	}
	Foodrecipe(int step,String name, String section){
		totalstep = step;
		foodname = name;
		foodsection = section;
	}
	public String getsection(){
		return foodsection;
	}
	public int getstep(){
		return totalstep;
	}
	public String getname(){
		return foodname;
	}
}
