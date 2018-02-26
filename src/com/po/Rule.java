package com.po;


import net.sf.json.JSONObject;

public class Rule {
	private String name;
	private String type;
	private String config;
	
	
	public Rule(){}
	
	
	public Rule(JSONObject rule)
	{
		this.name = rule.getString("name");
		this.type = rule.getString("type");
		this.config= rule.getString("config");
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getConfig() {
		return config;
	}
	public void setConfig(String Config) {
		this.config = Config;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
