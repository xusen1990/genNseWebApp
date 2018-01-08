package com.dao;

import java.util.List;

import com.po.ProfileCfg;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ProfileCfgDAO {
	
	public ProfileCfgDAO(){}
	
	
	public static void parseJSON(JSONObject prof,List<ProfileCfg> prfCfgList){
		JSONArray jsonProfCfgArr = JSONArray.fromObject(prof.getString("config"));
		//System.out.println("e:"+jsonProfCfgArr.size());
		for(int i = 0; i < jsonProfCfgArr.size(); i++){
			JSONObject profCfgObj = jsonProfCfgArr.getJSONObject(i);
			System.out.println(profCfgObj);
			prfCfgList.add(new ProfileCfg(profCfgObj));					
		}
		
		
	}

}
