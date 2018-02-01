package com.dao;

import java.util.List;

import com.po.Key;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class KeyDAO {
	
	public static void parseJsonKey(String keyJsonStr, List<Key> keyList){
		
		JSONArray jsonKeyArr = JSONArray.fromObject(keyJsonStr);
		
		if(jsonKeyArr != null && jsonKeyArr.size() > 0){
			for(int i = 0; i < jsonKeyArr.size(); i++){
				JSONObject keyObj = jsonKeyArr.getJSONObject(i);
				System.out.println(keyObj);
				keyList.add(new Key(keyObj));				
			}			
		}
	}

}
