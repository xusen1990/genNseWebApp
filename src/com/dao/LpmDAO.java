package com.dao;

import java.util.List;

import com.po.Lpm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LpmDAO {
	
	public static void parseJsonLpm(String lpmJsonStr, List<Lpm> lpmList){
		
		JSONArray jsonLpmArr = JSONArray.fromObject(lpmJsonStr);
		
		if(jsonLpmArr != null && jsonLpmArr.size() > 0){
			for(int i = 0; i < jsonLpmArr.size(); i++){
				JSONObject lpmObj = jsonLpmArr.getJSONObject(i);
				lpmList.add(new Lpm(lpmObj));				
			}			
		}
	}

}
