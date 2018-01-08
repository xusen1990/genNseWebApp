package com.dao;

import java.util.List;

import com.po.Acl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AclDAO {
	
	public static void parseJsonAcl(String aclJsonStr, List<Acl> aclList){
		
		JSONArray jsonAclArr = JSONArray.fromObject(aclJsonStr);
		
		if(jsonAclArr != null && jsonAclArr.size() > 0){
			for(int i = 0; i < jsonAclArr.size(); i++){
				JSONObject aclObj = jsonAclArr.getJSONObject(i);
				aclList.add(new Acl(aclObj));				
			}			
		}
	}

}
