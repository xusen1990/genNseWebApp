package com.po;

import net.sf.json.JSONObject;

public class Ad {
	
	private String name;
	private String baseAddr;
	private String size;
	private String width;
	
	public Ad(){}
	
	public Ad(JSONObject ad){
		this.name = ad.getString("name");
		this.baseAddr = ad.getString("baseAddr");
		this.size = ad.getString("size");
		this.width = ad.getString("width");
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseAddr() {
		return baseAddr;
	}

	public void setBaseAddr(String baseAddr) {
		this.baseAddr = baseAddr;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	

}
