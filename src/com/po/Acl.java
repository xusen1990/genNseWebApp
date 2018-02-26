package com.po;

import net.sf.json.JSONObject;

public class Acl {
	private String name;
	private String depth;
	private String width;
	private String adBlock;
	
	public Acl(){}
	
	public Acl(JSONObject acl){
		this.name = acl.getString("name");
		this.depth = acl.getString("depth");
		this.width = acl.getString("width");
		this.adBlock = acl.getString("adBlock");
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

	public String getAdBlock() {
		return adBlock;
	}

	public void setAdBlock(String adBlock) {
		this.adBlock = adBlock;
	}
	
	

}
