package com.dao;

import java.util.List;

import com.po.Rule;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RuleDAO {
	
	public static void parseJsonRule(String ruleJsonStr, List<Rule> ruleList){
		
		JSONArray jsonRuleArr = JSONArray.fromObject(ruleJsonStr);
		
		if(jsonRuleArr != null && jsonRuleArr.size() > 0){
			for(int i = 0; i < jsonRuleArr.size(); i++){
				JSONObject ruleObj = jsonRuleArr.getJSONObject(i);
				System.out.println(ruleObj);
				ruleList.add(new Rule(ruleObj));				
			}			
		}
	}

}
