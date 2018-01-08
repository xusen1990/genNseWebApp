package com.dao;

import java.util.List;

import com.po.Ad;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AdDAO {
	
	public static void parseJsonAd(String adJsonStr, List<Ad> adList){
		
		JSONArray jsonAdArr = JSONArray.fromObject(adJsonStr);
		
		if(jsonAdArr != null && jsonAdArr.size() > 0){
			for(int i = 0; i < jsonAdArr.size(); i++){
				JSONObject adObj = jsonAdArr.getJSONObject(i);
				adList.add(new Ad(adObj));				
			}			
		}
	}

}
