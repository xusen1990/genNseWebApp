package com.po;


import net.sf.json.JSONObject;

public class Key {
	private String ProfileId;
	private String type;
	private String config;
	
	
	public Key(){}
	
	
	public Key(JSONObject key)
	{
		this.ProfileId = key.getString("profileId");
		this.type = key.getString("type");
		this.config= key.getString("config");
		
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
	public String getProfileId() {
		return ProfileId;
	}
	public void setName(String ProfileId) {
		this.ProfileId = ProfileId;
	}
}
