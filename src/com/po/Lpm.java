package com.po;

import net.sf.json.JSONObject;

public class Lpm {
	private String name;
	private String depth;
	private String width;
	private String type;

	public Lpm(){}
	
	public Lpm(JSONObject lpm){
		this.name = lpm.getString("name");
		this.depth = lpm.getString("depth");
		this.width = lpm.getString("width");
		this.type = lpm.getString("type");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
