package com.dao;

import java.util.List;

import com.po.Lpm;
import com.po.Profile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ProfileDAO {
	
public static void parseJSON(String pfJsonStr, List<Profile> profList){
		
		JSONArray jsonPfArr = JSONArray.fromObject(pfJsonStr);
		
		if(jsonPfArr != null && jsonPfArr.size() > 0){
			for(int i = 0; i < jsonPfArr.size(); i++){
				JSONObject pfObj = jsonPfArr.getJSONObject(i);
				profList.add(new Profile(pfObj));			
			}			
		}
		
	}

}
