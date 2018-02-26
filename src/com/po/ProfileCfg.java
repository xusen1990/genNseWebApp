package com.po;

import net.sf.json.JSONObject;

public class ProfileCfg {
	
	private String type;
	private String table;
	private String channel;
	private String kgu;
	
	public ProfileCfg(){}
	
	
	public ProfileCfg(JSONObject profCfg){
		this.type = profCfg.getString("type");
		this.table = profCfg.getString("table");
		this.channel = profCfg.getString("channel");
		this.kgu = profCfg.getString("kgu");		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getKgu() {
		return kgu;
	}

	public void setKgu(String kgu) {
		this.kgu = kgu;
	}
	
	

}
