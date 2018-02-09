package com.po;

import java.util.ArrayList;
import java.util.List;

import com.dao.ProfileCfgDAO;
import com.po.ProfileCfg;


import net.sf.json.JSONObject;

public class Profile {
	
	private int tbNum;
	private List<ProfileCfg> prfCfgList = new ArrayList<ProfileCfg>();		

	public Profile(){}
	
	public Profile(JSONObject prof){
		this.tbNum = Integer.parseInt(prof.getString("tbnum"));
		ProfileCfgDAO.parseJSON(prof, this.prfCfgList);	
		
	}

	
	public List<ProfileCfg> getPrfCfgList() {
		return prfCfgList;
	}


	public int getTbNum() {
		return tbNum;
	}

	public void setTbNum(int tbNum) {
		this.tbNum = tbNum;
	}

}
