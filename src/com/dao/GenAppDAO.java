package com.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.po.Acl;
import com.po.Ad;
import com.po.Key;
import com.po.Lpm;
import com.po.Mode;
import com.po.Profile;
import com.po.ProfileCfg;
import com.po.Rule;
import com.util.FileUtil;

public class GenAppDAO {
	
	public GenAppDAO(){}
	
		public static void addHead(String genFileName, String url){
	    	String content = FileUtil.readFileContent("include.txt",url);
	    	content +="\r\n   ";
	    	FileUtil.appendContent(genFileName, content, url);  
	    }
	    
	    public static void addDefine(String genFileName, Mode mode, List<Acl> aclList, List<Lpm> lpmList,String url){
	    	
	    	String content = "\r\n";
	    	if(mode.isEnAcl()){    		
	    		for(int i = 0; i < aclList.size(); i++){
	    			content +="#define ";
	    			content += aclList.get(i).getName()+"_ACL_SIZE "+aclList.get(i).getDepth()+"\r\n";
	    			FileUtil.appendContent(genFileName, content, url); 
	    			content="";
	    		}
	    	}
	    	
	    	if(mode.isEnLpm()){    		
	    		for(int i = 0; i < lpmList.size(); i++){
	    			content +="#define ";
	    			content += lpmList.get(i).getName()+"_LPM_SIZE "+lpmList.get(i).getDepth()+"\r\n";
	    			FileUtil.appendContent(genFileName, content, url); 
	    			content="";
	    			
	    		}    		
	    	}
	    	content = "\r\n";
	    	content +="#define BYTE_COUNT(bits) (((bits)+8-1)/8)";
	    	FileUtil.appendContent(genFileName, content, url); 
	    }
	    
	    public static void addDeclareFun(String genFileName,String url){
	    	String content ="\r\n";
	    	content += FileUtil.readFileContent("declareFun.txt",url);
	    	FileUtil.appendContent(genFileName, content,url);  
	    }
	    
	    
	    public static void addMain(String genFileName,String url){
	    	String content ="\r\n";
	    	content += FileUtil.readFileContent("main.txt",url);
	    	FileUtil.appendContent(genFileName, content, url);  
	    }
	    
	    public static void addEndMain(String genFileName,String url){
	    	String content ="\r\n";
	    	content += FileUtil.readFileContent("endMain.txt",url);
	    	FileUtil.appendContent(genFileName, content,url);  
	    }
	    
	    public static void addNseHandler(String genFileName,Mode mode,String url){
	    	String content ="\r\n";
	    	content += "\t// Create NSE Handler\r\n"
	    			+"\tnseStatus = NseManagerDp_Create(&MyStargate, "+mode.getNseMode()+");\r\n"    			
	    			+checkStatus("Error: fail to create manager",0);
	    			
	    	FileUtil.appendContent(genFileName, content,url);       	
	    }
	    
	    public static void addTable(String genFileName, Mode mode, List<Ad> adList, List<Acl> aclList, List<Lpm> lpmList,String url ){
	    	String content ="\r\n";
	    	if(mode.isEnAd()){
	    		content+="\t// ------------------------       Add AD ----------------------------------------------------------\r\n"
	    				+"\tnseStatus = NseAdBlock_Create(&MyADBuff);\r\n"    				
	    				+checkStatus("Error: fail to create AD table",0);
	    			
	    		
	    		for(int i = 0; i < adList.size(); i++){
	    			content +="\r\n"
	    					+"\t//AD"+(i+1)+"\r\n";
	    			int portId = 0;   //Here need to modify next step
	    			content +="\tMyADBuff->baseAddr = "+adList.get(i).getBaseAddr()+" ;\r\n"
	    					+"\tMyADBuff->size = "+adList.get(i).getSize()+" ;\r\n"
	    					+"\tMyADBuff->width = "+adList.get(i).getWidth()+" ;\r\n"
	    					+"\tnseStatus = NseManagerDp_AdBlock_Add(MyStargate, MyADBuff, "+portId+", "+i+");\r\n"
	    					+checkStatus("Error: fail to add AD table",0);
	    			
	    		}   		
	    		
	    		
	    		FileUtil.appendContent(genFileName, content,url); 
	    	}
	    	
	    	
	    	if(mode.isEnAcl()){
	    		content ="\r\n"
	    				+"\t// ------------------------       Add ACL TABLE ----------------------------------------------------------\r\n"
	    				+"\tnseStatus = NseTable_Create(&MyTableBuff);  // allocate memory for table handler\r\n"
	    				+checkStatus("Error: fail to create ACL table",0);
	    		
	    		for(int i = 0; i < aclList.size(); i++){
	    			content +="\r\n"
	    					+"\t//ACL"+(i+1)+"\r\n";
	    			int portId = 0;   //Here need to modify next step
	    			content += "\tMyTableBuff->name = " + "\""+aclList.get(i).getName()+"\"" + " ;\r\n"
	    					+"\tMyTableBuff->depth = "+ aclList.get(i).getName() + "_ACL_SIZE;\r\n"
	    					+"\tMyTableBuff->width = " + aclList.get(i).getWidth() + " ;\r\n";
	    			int j = 0;
	    			boolean findAdBlock = false;
	    			if(adList != null)
	    			{	
	    				for(; j < adList.size(); j++){
	    					if(adList.get(j).getName().equals(aclList.get(i).getAdBlock()))
	    					{
	    						findAdBlock = true;
	    						break;
	    					} 
	    				}
	    			}
	    			if(findAdBlock){
	    				content +="\tMyTableBuff->adBlock = NseManagerDp_AdBlock_Get(MyStargate, " + portId +", "+ j +");\r\n";
	    			}
	    			else
	    			{
	    				content +="\tMyTableBuff->adBlock = NULL;"+"\r\n";
	    			}
	    				content	+="\tnseStatus = NseManagerDp_Table_Add(MyStargate, MyTableBuff, " + portId +", "+ i +");\r\n"
	    						+checkStatus("Error: fail to add  ACL table",0);   				
	    				
	    		}
	    			
	    		FileUtil.appendContent(genFileName, content,url); 
	    		
	    	}
	    	
	    	
	    	
	    	if(mode.isEnLpm()){
	    		content ="\r\n"
	    				+"\t// ------------------------       Add LPM TABLE ----------------------------------------------------------\r\n"
	    				+"\tnseStatus = NseLpmTable_Create(&MyLpmTableBuff);\r\n"
	    				+checkStatus("Error: fail to create LPM table",0);
	    		
	    		for(int i = 0; i < lpmList.size(); i++){
	    			content +="\r\n"
	    					+"\t//LPM"+(i+1)+"\r\n";
	    			int portId = 0;   //Here need to modify next step
	    			content += "\tMyLpmTableBuff->name = " + "\""+lpmList.get(i).getName()+"\"" + " ;\r\n"
	    					+"\tMyLpmTableBuff->depth = "+ lpmList.get(i).getName() + "_LPM_SIZE;\r\n"
	    					+"\tMyLpmTableBuff->width = " + lpmList.get(i).getWidth() + " ;\r\n"
	    					+"\tMyLpmTableBuff->type = " + lpmList.get(i).getType()+";\r\n"
	    					+"\tnseStatus = NseManagerDp_LpmTable_Add(MyStargate, MyLpmTableBuff, "+ portId + ");\r\n"
	    					+checkStatus("Error: fail to add LPM table",0);    			
	    		} 		
	    		FileUtil.appendContent(genFileName, content,url); 
	    		
	    	}
	    	
	    }
	    
	    
	    public static void addProfile(String genFileName, Mode mode, List<Acl> aclList, List<Lpm> lpmList, List<Profile> profList,String url){
	    	
	    	String content = "\r\n";
	    	content += FileUtil.readFileContent("profile.txt",url);
	    	FileUtil.appendContent(genFileName, content,url); 
	    	content = "\r\n";
	    	
	    	for(int i = 0; i < profList.size(); i++){
	    		int offset = 0;
	    		int portId = 0; //Here need to modify next step
	    		content += "\r\n"
	    				+"\t//profile"+i+"\r\n"
	    				+"\tnseStatus = NseManagerDp_Profile_Add(MyStargate, MyProfileBuff, " + portId +", " + i +");\r\n"
	    				+checkStatus("Error: fail to add profile",0)
	    				+"\tMyProfile = NseManagerDp_Profile_Get(MyStargate, " +portId +", " + i +");\r\n";    		
	    		
	    		for(int j = 0; j < profList.get(i).getTbNum(); j++)
	    		{
	    			
	    			
	    			ProfileCfg pfCfg = profList.get(i).getPrfCfgList().get(j);
	    			String type = pfCfg.getType();    		
	    			
	    			if(type.equals("LPM"))
	    			{
	    				int lpmTableId = 0;    				
	    				boolean findLPMTable = false;
	    				for(; lpmTableId < lpmList.size(); lpmTableId++){
	    					if(pfCfg.getTable().equals(lpmList.get(lpmTableId).getName())){
	    						findLPMTable = true;
	    						break;
	    					}			    					
	    				}	
	    				if(findLPMTable){
	    					content += "\r\n"
	    							+"\tMyLpmTableInfo->table = NseManagerDp_LpmTable_Get(MyStargate, " + portId +", "+lpmList.get(lpmTableId).getType() +");\r\n"
	    							+"\tMyLpmTableInfo->chanMask = " + getSelChannel(Integer.parseInt(pfCfg.getChannel())) +";\r\n"
	    							+"\tMyTableInfo->keySelect = " + pfCfg.getKgu() + ";\r\n"
	    							+"\tnseStatus = NseProfile_LpmTableInfo_Add(MyProfile, MyLpmTableInfo);\r\n"
	    							+checkStatus("Error: fail to add search lpm table",0);
	    					//setting kgu
	    					content += addKguSetting(Integer.parseInt(pfCfg.getKgu()),144,offset,url);   
	    					offset += 144;
	    				}   				
	    				else{
	    					System.out.println("Error: can not find LPM Table to configure profile");
	    				}
	    			 }
	    		}
	    		for(int j = 0; j < profList.get(i).getTbNum(); j++)
	    		{
	    			ProfileCfg pfCfg = profList.get(i).getPrfCfgList().get(j);
	    			String type = pfCfg.getType();    
	    		
	    			if(type.equals("ACL")){
	    				
	    				int tableId = 0;    				
	    				boolean findTable = false;
	    				for(; tableId < aclList.size(); tableId++){
	    					if(pfCfg.getTable().equals(aclList.get(tableId).getName())){
	    						findTable = true;
	    						break;
	    					}			    					
	    				}			
	    				if(findTable){
	    					content += "\r\n"
	    							+"\tMyTableInfo->table = NseManagerDp_Table_Get(MyStargate, "+ portId +", " + tableId + ");\r\n"
	    							+"\tMyTableInfo->chanMask = " + getSelChannel(Integer.parseInt(pfCfg.getChannel())) +";\r\n"
	    							+"\tMyTableInfo->keySelect = " + pfCfg.getKgu() + ";\r\n";
	    					String bitmask ="";
	    					int width = Integer.parseInt(aclList.get(tableId).getWidth());
	    					for(int k = 0; k < width/2; k++){
	    						bitmask = bitmask.concat("1");
	    					}
	    					content +="\tstrcpy(bitmask, " + "\"" + bitmask + "\");\r\n"
	    							+"\tstr2binary(bitmask, " + width/2 +");\r\n"
	    							+"\tMyTableInfo->bitmask = bitmask;\r\n"
	    							+"\tnseStatus = NseProfile_TableInfo_Add(MyProfile, MyTableInfo, " + j +");\r\n"
	    							+checkStatus("Error: fail to add search table",0);
	    					//setting kgu
	    					content += addKguSetting(Integer.parseInt(pfCfg.getKgu()),width,offset,url);
	    					offset += width;
	    					
	    				}else{    					
	    					System.out.println("Error: can not find ACL Table to configure profile");
	    				}
	    				
	    			}
	    			
	    			
	    			
	    			
	    		} 
	    		
	    	}    	
	    	FileUtil.appendContent(genFileName, content,url); 
	    	
	    	
	    }   

	    
	    
	    
	    public static void addLockConfig(String genFileName,String url){
	    	String content ="\r\n";
	    	content += FileUtil.readFileContent("lockConfig.txt",url);
	    	FileUtil.appendContent(genFileName, content,url);    	
	    }
	    
	    
	    
	    public static String checkStatus(String printInfo , int i){
	    	String content = "";
	    	if(i == 0)
	    	{	
	    	  content ="\tif (nseStatus != Nse_OK)\r\n"
	    		           +"\t{\r\n"
			    			+"\t\tprintf(\""+printInfo+"\\n\");\r\n"
			    			+"\t\tgoto done;\r\n"
			    			+"\t}\r\n";
	    	}
	    	else
	    	{
	    	  content ="\tif (nseStatus != Nse_OK)\r\n"
	    		           +"\t\t{\r\n"
			    			+"\t\t\tprintf(\""+printInfo+"\\n\");\r\n"
			    			+"\t\t\tgoto done;\r\n"
			    			+"\t\t}\r\n";
	    	}
	    	
	    	return content;    	
	    }
	    
	    public static int getSelChannel(int ch){
	    	
	    	return (int)Math.pow(2.0, ch);    	
	    }
	    
	    
	    public static String addKguSetting(int kguId, int width,int offset ,String url)
	    {
	    	//width :80 or 160 or 320 or 640 
	    	String content ="";
	    	int index = 0;
	    	if (width == 144){
	    		content += "\tMyKguSegment->offset = "+offset/8+";\r\n"
	    				+"\tMyKguSegment->byteCount = 15;\r\n"
	    				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	    				+checkStatus("Error: fail to add kgu segment",0);
	    		index++;
	    		content += "\tMyKguSegment->offset = "+(offset/8+16)+";\r\n"
	    				+"\tMyKguSegment->byteCount = 1;\r\n"
	    				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	    				+checkStatus("Error: fail to add kgu segment",0);
	    		index++;
	    		content += "\tMyKguSegment->offset = 0;\r\n"
	    				+"\tMyKguSegment->byteCount = 1;\r\n"
	    				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	    				+checkStatus("Error: fail to add kgu segment",0);
	    		index++;
	    	}else if (width == 80){
	    		content += "\tMyKguSegment->offset = "+offset/8+";\r\n"
	    				+"\tMyKguSegment->byteCount = 9;\r\n"
	    				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	    				+checkStatus("Error: fail to add kgu segment",0);
	    		index++;
	    	}else{
	    		content += "\tMyKguSegment->offset = "+offset/8+";\r\n"
	    				+"\tMyKguSegment->byteCount = 15;\r\n"
	    				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	    				+checkStatus("Error: fail to add kgu segment",0);
	    		index++;
	    		
	    		if(width == 160){
	    			content += "\tMyKguSegment->offset = "+(offset/8+16)+";\r\n"
	        				+"\tMyKguSegment->byteCount = 3;\r\n"
	        				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	        				+checkStatus("Error: fail to add kgu segment",0);
	        		index++;   	    			
	    		}else{
	    			content += "\tMyKguSegment->offset = "+(offset/8+16)+";\r\n"
	        				+"\tMyKguSegment->byteCount = 15;\r\n"
	        				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	        				+checkStatus("Error: fail to add kgu segment",0);
	        		index++;   	      			
	    			if(width == 320){
	    				content += "\tMyKguSegment->offset = "+(offset/8+32)+";\r\n"
	            				+"\tMyKguSegment->byteCount = 7;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment",0);
	            		index++; 
	    			}else if(width == 640){
	    				content += "\tMyKguSegment->offset = "+(offset/8+32)+";\r\n"
	            				+"\tMyKguSegment->byteCount = 15;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment",0);
	            		index++; 
	            		content += "\tMyKguSegment->offset = "+(offset/8+48)+";\r\n"
	            				+"\tMyKguSegment->byteCount = 15;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment",0);
	            		index++; 
	            		content += "\tMyKguSegment->offset = "+(offset/8+64)+";\r\n"
	            				+"\tMyKguSegment->byteCount = 15;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment",0);
	            		index++;  								
	    			}	
	    		}	
	    	}
	    	
	    	
	    	return content;
	    }
	    public static int gettbnum(String Tname , List<Profile> profList)
	    {
	    	int tbnum = -1;
	    	for(int i = 0; i < profList.size(); i++)
	    	{
	    		for(int j = 0; j < profList.get(i).getTbNum(); j++)
	    		{
	    			if(((((profList.get(i)).getPrfCfgList()).get(j)).getTable()).equals(Tname))
	    			{
	    				tbnum = i ;
	    			}
	    				
	    		}
	    	}
	    	
	    	return tbnum;
	    }
	    public static void parseAcl(String name , int width , int depth , String config, String type,String url,String genFileName)
        {
        	Calendar cal=Calendar.getInstance(); 
			int m=cal.get(Calendar.MONTH);      
		    int d=cal.get(Calendar.DATE);      
		    int h=cal.get(Calendar.HOUR_OF_DAY);
		    int mi=cal.get(Calendar.MINUTE)/20*20;
		    int seed = mi+h*100+d*1000+m*10000;
		    final Process process;
		    name = genFileName+"_"+name;
		   
        	//System.out.println(config);
        	if(type.equals("Random"))
    		{	
        		
    		    
        		System.out.println(type);
    			try {  	  
    				process = Runtime.getRuntime().exec("perl "+url+"templet"+System.getProperty("file.separator")+"rule-gen.pl -m 3 -t "+name+" -w "+width+" -d "+depth/1024+" -f "+config+" -s " +seed+" -p "+url+"RuleCollections"+System.getProperty("file.separator"));
    				
    				
    				if( process.waitFor() == 0)
    				{
    					System.out.println("success perl "+url+"templet"+System.getProperty("file.separator")+"rule-gen.pl -m 3 -t "+name+" -w "+width+" -d "+depth/1024+" -f "+config+" -s " +seed+" -p "+url+"RuleCollections"+System.getProperty("file.separator"));
		  				return ;
    				}
    			} catch (Exception ex) {  
                  System.out.print("error");
    			}
    		}
    		else if(type.equals("Fixed"))
    		{
    			
    			return ;
    		}
    		else if(type.equals("INC"))
    		{
    			System.out.println(type);
    			try {  	  
    				process = Runtime.getRuntime().exec("perl "+url+"templet"+System.getProperty("file.separator")+"rule-gen.pl -m 3 -t "+name+" -w "+width+" -d "+depth/1024+" -i 1"+" -p "+url+"RuleCollections"+System.getProperty("file.separator")); 
    				
    				
    				if( process.waitFor() == 0)
    				{
    					System.out.println("success perl "+url+"templet"+System.getProperty("file.separator")+"perl rule-gen.pl -m 3 -t "+name+" -w "+width+" -d "+depth/1024+" -i 1"+" -p "+url+"RuleCollections"+System.getProperty("file.separator")+"\n");
		  				return ;
    				}
    			} catch (Exception ex) {  
                  System.out.print("error");
    			}
    			
    			
    		}
    		else
    		{
    			System.out.println("Please confirm that the rulemode is filled correctly");
    		}
        }	    
	    public static void parseLpm(String name , String type, int depth , String config, String i_type,String url,String genFileName)
        {
        	String cmd ="";
        	name = genFileName+"_"+name;
        	Calendar cal=Calendar.getInstance(); 
			int m=cal.get(Calendar.MONTH);      
		    int d=cal.get(Calendar.DATE);      
		    int h=cal.get(Calendar.HOUR_OF_DAY);
		    int mi=cal.get(Calendar.MINUTE)/20*20;
		    int seed = mi+h*100+d*1000+m*10000;
		    m=cal.get(Calendar.MONTH);      
		    d=cal.get(Calendar.DATE);      
		    h=cal.get(Calendar.HOUR_OF_DAY);
		    final Process process;
    		if(i_type.equals("Random"))
    		{	   
    			  try 
    			 {  		
    				if(type.equals("eNseLpmTableType_IP4"))
    				{
    					cmd ="perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 0 -n "+name+" -s "+depth/1024+" -seed " +seed+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
    					
    					 process = Runtime.getRuntime().exec(cmd); 
    					 if( process.waitFor() == 0)
    					 {
    						 System.out.println(cmd);
    						 return ;
    					 }
    		  				  
    				}else if(type.equals("eNseLpmTableType_IP6")){
    					cmd = "perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 1 -n "+name+" -s "+depth/1024+" -seed " +seed+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
    					process = Runtime.getRuntime().exec(cmd);
    					if( process.waitFor() == 0)
    					{
    						 System.out.println(cmd);
    						 return ;
    					}
    		  				 
    				}else if(type.equals("eNseLpmTableType_VRF_IP4")){
    					cmd = "perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 0 -vpn 1 -n "+name+" -s "+depth/1024+" -seed " +seed+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
    					System.out.println(cmd);
    					process = Runtime.getRuntime().exec(cmd); 
    					if( process.waitFor() == 0)
    					{
    						 System.out.println(cmd);
    						 return ;
    					}
    				}else if(type.equals("eNseLpmTableType_VRF_IP6")){
    					cmd = "perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 1 -vpn 1 -n "+name+" -s "+depth/1024+" -seed " +seed+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
    					
    					process = Runtime.getRuntime().exec(cmd); 
    					 if( process.waitFor() == 0)
    					 {
    						 System.out.println(cmd);
    						 return ;
    					 }
    		  				  
    				}else{
    					System.out.println("Please confirm that the type is filled correctly");
    				}
    				System.out.println(cmd);
    			 }catch (Exception ex) {  
    	              System.out.print("error");}
    		}			  
    		else if(i_type.equals("Fixed"))
    		{
    			return ;
    		}
    		else if(i_type.equals("INC"))
    		{
    				
    			System.out.println("parse lpm inc");
  			  try 
  			 {  
  				if(type.equals("eNseLpmTableType_IP4"))
  				{
  					cmd ="perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 0  -n "+name+" -s "+depth/1024+" -inc 1"+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
  					
  					process = Runtime.getRuntime().exec(cmd); 
  					 if( process.waitFor() == 0)
					 {
						 System.out.println(cmd);
						 return ;
					 }
  				}else if(type.equals("eNseLpmTableType_IP6")){
  					cmd = "perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 1  -n "+name+" -s "+depth/1024+" -inc 1"+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
  					
  					process = Runtime.getRuntime().exec(cmd); 
  					 if( process.waitFor() == 0)
					 {
						 System.out.println(cmd);
						 return ;
					 }
  				}else if(type.equals("eNseLpmTableType_VRF_IP4")){
  						cmd = "perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 0 -vpn 1 -n "+name+" -s "+depth/1024+" -inc 1"+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
  						
  						process = Runtime.getRuntime().exec(cmd); 
  						 if( process.waitFor() == 0)
    					 {
    						 System.out.println(cmd);
    						 return ;
    					 }
  				}else if(type.equals("eNseLpmTableType_VRF_IP6")){
  						cmd = "perl "+url+"templet"+System.getProperty("file.separator")+"lpm_gen_ide.pl -ip 1 -vpn 1 -n "+name+" -s "+depth/1024+" -inc 1"+" -p "+url+"RuleCollections"+System.getProperty("file.separator");
  						
  						process = Runtime.getRuntime().exec(cmd); 
  						 if( process.waitFor() == 0)
    					 {
    						 System.out.println(cmd);
    						 return ;
    					 }
  				}else{
  					System.out.println("Please confirm that the type is filled correctly");
  				}
  					System.out.println(cmd);
  			 	}
  			  catch (Exception ex) {  
  	              System.out.print("error");
  	            }
  			 
    		}
    		else
    		{
    				System.out.println("Please confirm that the rulemode is filled correctly");
    		}            
        }
	    public static void parserule(List<Acl> aclList,List<Lpm> lpmList, List<Rule> ruleList,String url,String genFileName)
        {
        	
        	int width;
        	int depth;
        	String Tname= "";
        	String type = "";
        	int flag = 0 ;
 
       		for(int i = 0 ; ruleList != null && i< ruleList.size() ; i++)
        	{
        			System.out.println(ruleList);
        			Rule rule = ruleList.get(i);
        			Tname = rule.getName();
        			
        			System.out.println("rule_type:"+rule.getType());
        			System.out.println("rule_config:"+rule.getConfig());
        			System.out.println("rule_tname:"+Tname);

        			
        				
        			for(int j = 0 ;  aclList != null && j < aclList.size() ; j++ )
        			{
        				
        				if(Tname.equals((aclList.get(j)).getName()))
        				{
        					
        					flag = 1 ;
        					width = Integer.parseInt(aclList.get(j).getWidth());
        					depth = Integer.parseInt(aclList.get(j).getDepth());
        					parseAcl(Tname , width , depth , rule.getConfig(),rule.getType(),url,genFileName);
        					break;
        				}
        				
        			}
        		  
        			
        			if(flag == 0)
        			{
        				for(int j = 0 ; lpmList != null && j < lpmList.size() ; j++ )
            			{
            				if(Tname.equals((lpmList.get(j)).getName()))
            				{
            					
            					flag = 1 ;
            					type = lpmList.get(j).getType();
            					depth = Integer.parseInt(lpmList.get(j).getDepth());
            					System.out.println("type:"+type+"\ndepeth:"+depth);
            					parseLpm(Tname , type , depth , rule.getConfig(),rule.getType(),url,genFileName);
            					break;
            				}
            				
            			}
        			}
        			flag = 0;	
        		}
        }	 
	    public static String gettype(String Tname , List<Rule> ruleList)
	    {
	    	
	    	if(ruleList == null)
	    		return "no";
	    	for(int i = 0; i < ruleList.size(); i++)
	    	{
	    		Rule a = ruleList.get(i);
	    		if(a.getName().equals(Tname))
	    		{
	    		   if(a.getType().equals("Fixed"))
	    			   return a.getConfig() ;
	    		   else
	    			   return "no";
	    		}
	    		
	    	}
	    	return "no";
	    }
	    public static void genrule(String genFileName, List<Acl> aclList, List<Lpm> lpmList ,List<Rule> ruleList, String url)
	       {
	    	    String rule = genFileName+"_rule";
	     	    String path1 = url + "RuleCollections"+System.getProperty("file.separator");
				String source = "";
				final Process process;
				int flag = 0;
				if(lpmList != null)
				{	
					for(int t = 0 ; t < lpmList.size(); t++)
					{
						if( gettype(lpmList.get(t).getName(),ruleList).equals("no"))
						{	   
							String fileNameTemp1 = genFileName +"_"+lpmList.get(t).getName();
							if(flag == 0)	
								source += fileNameTemp1;
							else
								source += "," + fileNameTemp1;
							flag++;
						}
					}
				}
				if(aclList != null)
				{
					for(int t = 0 ; t < aclList.size(); t++)
					{
						if( gettype(aclList.get(t).getName(),ruleList).equals("no"))
						{
							String fileNameTemp1 = genFileName +"_"+aclList.get(t).getName();
							if(flag == 0)	
								source += fileNameTemp1;
							else
								source += "," + fileNameTemp1;
							flag++;
						}
					}
				}
				
				try {  	  
    				process = Runtime.getRuntime().exec("perl "+url+"templet"+System.getProperty("file.separator")+"gen.pl -type 0 -source "+source+" -path "+path1+" -dest "+rule);
    				
    				
    				if( process.waitFor() == 0)
    				{
    					System.out.println("perl "+url+"templet"+System.getProperty("file.separator")+"gen.pl -type 0 -source "+source+" -path "+path1+" -dest "+rule);
    					return ;
    				}
    			} catch (Exception ex) {  
                  System.out.print("error");
    			}
	        
	       }	    
	    public static void addrule(String genFileName, Mode mode, List<Ad> adList, List<Acl> aclList, List<Lpm> lpmList, List<Profile> profList,List<Rule> ruleList,String url,List<Integer> ADflag)
	       {
	    	String content ="\r\n"
	    				   + "\tfile = \""+genFileName+"_"+"rule.txt\";"+"\r\n"
	    				   + "\tfiletofile(file);"+"\r\n";
	    	FileUtil.appendContent(genFileName, content,url);
	    	String name;
	    	String ADname;
	    	int portId;
	    	int width;
	    	int profileID;
	    	int AdId = -1;
	    	if(mode.isEnAcl())
	    	{
	    		
	    		content ="\r\n"
	    				+ "\tnum = 0;"+ "\r\n"
	    				+"\t//------------------------Add ACL Record ----------------------------------------------------------\r\n";
	    			
	    		for(int i = 0; i < aclList.size(); i++)
	    		{
	    			
	    			name = aclList.get(i).getName();
	    			profileID = gettbnum(name,profList);
	    			if(profileID == -1)
	    				continue;
	    			portId = 0;   //Here need to modify next step    					
	    			width = Integer.parseInt(aclList.get(i).getWidth());
	    			String path3 = gettype(name,ruleList);
	    			if(path3.equals("no"))
	    			{	
	    				path3 = genFileName+"_"+name+".txt";
	    			}

	    			
	    			ADname = aclList.get(i).getAdBlock();
	    			
	    
	    				if(adList != null)
	    				{	
	    					for( int j = 0 ; j < adList.size();j++)
	    					{
	    						if(adList.get(j).getName().equals(ADname))
	    						{
	    							AdId = j ;
	    							break;
	    						}
	    					}	
	    				}
	    			
	    			
	    			content +="\r\n"
	    					+ "\tfp = fopen(\""+path3+"\", \"r\");"+ "\r\n"
	    					+ "\tMyTable = NseManagerDp_Table_Get(MyStargate, " + portId + ", "+i+");" + "\r\n"
	   					    + "\tnseStatus = NseTableRecord_Create(&MyRecordBuff);  // allocate memory for record handler\r\n"+ "\r\n"
	     				    + checkStatus("Error: fail to create acl record\\n",0)
	    					+ "\twhile(fgets(data_buff, 3000, fp) != NULL)"+ "\r\n"
	    					+ "\t{"+ "\r\n"
						    + "\t\tMyRecordBuff->format = NSE_TABLERECORD_FORMAT_DATA_MASK;" +"\r\n"
						    + "\t\tMyRecordBuff->width =" + width+";" +"\r\n"  					
	    					+ "\t\tdata_temp = strtok(data_buff,\",\");"+ "\r\n"
	    					+ "\t\tmask_temp = strtok(NULL,\",\");"+ "\r\n"
	    					+ "\t\tstrncpy( data, data_temp, strlen(data_temp) );"+ "\r\n"
	    					+ "\t\tstrncpy( mask, mask_temp, strlen(mask_temp) );"+ "\r\n"
	    					+ "\t\tpri = strtok(NULL,\",\");"+ "\r\n"
	    					+ "\t\tstr2binary(data, "+width+");"+ "\r\n"
	    					+ "\t\tstr2binary(mask, "+width+");"+ "\r\n"
	    					+ "\t\tprio=str2int(pri);"+ "\r\n"
	    					+ "\t\tMyRecordBuff->data = data;  // point data pointer in record handler to data buffer"+ "\r\n"
	    					+ "\t\tMyRecordBuff->mask = mask;  // point mask pointer in record handler to mask buffer"+ "\r\n"
	    					+ "\t\tMyRecordBuff->priority = prio;  // set priroity of this record to be 100"+ "\r\n"
	    					+ "\t\tMyRecordBuff->range1Lower = NSE_TABLERECORD_RANGE_LOWER_NONE;  // no range"+ "\r\n"
	    					+ "\t\tMyRecordBuff->range1Upper = NSE_TABLERECORD_RANGE_UPPER_NONE;  // no range"+ "\r\n"
	    					+ "\t\tMyRecordBuff->range2Lower = NSE_TABLERECORD_RANGE_LOWER_NONE;  // no range"+ "\r\n"
	    					+ "\t\tMyRecordBuff->range2Upper = NSE_TABLERECORD_RANGE_UPPER_NONE;  // no range"+ "\r\n"
	    					+ "\t\tnseStatus = NseTable_Record_Add(MyTable, MyRecordBuff, prio);  // Add this record to T1, id is for customer to manage record, like read or delete"+ "\r\n"
	    					+ "\t"+checkStatus("Error: fail to add acl record\\n",1)+ "\r\n"
	    					+ "\t\tmemset(data, 0, sizeof(data));"+"\r\n"
	    					+ "\t\tmemset(mask, 0, sizeof(mask));"+"\r\n"
	    					+ "\t\tMyRecord = NseTable_Record_Get(MyTable, prio);  // read record from database"+ "\r\n"
	    					+ "\t\tbinary2str(MyRecord->data, data, "+width/8+");  // convert binary memory to data string"+"\r\n"
	    					+ "\t\tbinary2str(MyRecord->mask, mask, "+width/8+");  // convert binary memory to data string"+"\r\n"
	    					+ "\t\tprintf(\"Read record: data: %s\\n\", data);"+"\r\n"
	    					+ "\t\tprintf(\"             mask: %s\\n\", mask);"+"\r\n";
	    		
	    			if(AdId >= 0 && ADflag.get(AdId) == 0)
	    			{
	    				System.out.println("sss7"+ADflag.get(AdId));
	    				ADflag.set(AdId,1);
	    				content += "//------------------------Add Ad----------------------------------------------------------\r\n"		
	    						+ "\tMyAD = NseManagerDp_AdBlock_Get(MyStargate, "+portId+", "+ AdId +");  // get ad handler"+ "\r\n"
	    						+ "\tmemset(data, 0, sizeof(data));"+ "\r\n"
	    						+ "\tint2Bin(num, data, 16);"+"\r\n"
	    						+ "\tfor (int i = 16; i < "+adList.get(AdId).getWidth()+"; i++)"+ "\r\n"
	    						+ "\t{"+"\r\n"
	    						+ "\t\tstrcat(data, \"0\");"+"\r\n"
	    						+ "\t}"+"\r\n"
	    						+ "\tprintf(\"Add AD entry: %s\\n\", data);"+ "\r\n"
	    						+ "\tstr2binary(data, "+adList.get(AdId).getWidth()+");  // convert data string to binary memory"+ "\r\n"
	    						+ "\tnseStatus = NseAdBlock_AD_Add(MyAD, prio, data);  // add AD entry to address 100, pair with the record just added"+"\r\n"
	    						+ "\t"+checkStatus("Error: fail to add ad entry\\n",1)+ "\r\n";
	    
	    			}
	    					
	    			content += "\t\tnum++;"+"\r\n"
	    					+ "\t}"+"\r\n"
	    					+ "\tfclose(fp);" ;
	    			
	    			AdId = -1 ;
	    	   }
	    		FileUtil.appendContent(genFileName, content,url); 
	    	
	       }
	    	if(mode.isEnLpm())
	    	{
	    		
	    		content ="\r\n"
	    				+"\t//------------------------Add LPM Record ----------------------------------------------------------\r\n";
	    			
	    		for(int i = 0; i < lpmList.size(); i++)
	    		{
	    			name =lpmList.get(i).getName();
	    			profileID = gettbnum(name,profList);
	    			if(profileID == -1)
	    				continue;
	    			portId = 0;   //Here need to modify next step
	    			width = Integer.parseInt(lpmList.get(i).getWidth());
	    			
	    			String path3 = gettype(name,ruleList);
	    			if(path3.equals("no"))
	    			{	
	    				path3 = genFileName+"_"+name+".txt";
	    			}
	    			
	    			content +="\r\n"
	   					    + "\tfp = fopen(\""+path3+"\", \"r\");"+ "\r\n"
	   					    + "\tMyLpmTable = NseManagerDp_LpmTable_Get(MyStargate, " + portId + ", "+lpmList.get(i).getType()+");" + "\r\n"
						    + "\tnseStatus = NseLpmTableRecord_Create(&MyLpmRecordBuff);  // allocate memory for record handler\r\n"
						    + checkStatus("Error: fail to create MyLpmRecordBuff",0)
	    					+ "\twhile(fgets(data_buff, 3000, fp) != NULL)"+ "\r\n"
	    					+ "\t{"+ "\r\n"
	   					    + "\t\tMyLpmRecordBuff->format = NSE_TABLERECORD_FORMAT_DATA_MASK;" +"\r\n"
	   					    + "\t\tMyLpmRecordBuff->width = " + width+";" +"\r\n"
	   					    + "\t\tdata_temp = strtok(data_buff,\",\");"+ "\r\n"
	   					    + "\t\tmask_temp = strtok(NULL,\",\");"+ "\r\n"
	   					    + "\t\tstrncpy( data, data_temp, strlen(data_temp) );"+ "\r\n"
	   					    + "\t\tswapl(data);"+"\r\n"
	   					    + "\t\tstrncpy( mask, mask_temp, strlen(mask_temp) );"+ "\r\n"
	    					+ "\t\tpri = strtok(NULL,\",\");"+ "\r\n"
	    					+ "\t\tstr2binary(data, "+width+");"+ "\r\n"
	    					+ "\t\tstr2binary(mask, "+width+");"+ "\r\n"
	    					+ "\t\tprio=str2int(pri);"+ "\r\n"
	    					+ "\t\tMyLpmRecordBuff->data = data;  // point data pointer in record handler to data buffer"+ "\r\n"
	    					+ "\t\tMyLpmRecordBuff->mask = mask; // point mask pointer in record handler to mask buffer"+ "\r\n"
	    					+ "\t\tMyLpmRecordBuff->priority = prio;  // set priroity of this record to be 100"+ "\r\n"
	    					+ "\t\tnseStatus = NseLpmTable_Record_Add(MyLpmTable, MyLpmRecordBuff, prio);  // Add this record to T1, id is for customer to manage record, like read or delete"+ "\r\n"
	    					+ "\t"+checkStatus("Error: fail to Add LPM record",1)+ "\r\n"
	    					+ "\t\tmemset(data, 0, sizeof(data));"+"\r\n"
	    					+ "\t\tmemset(mask, 0, sizeof(mask));"+"\r\n"
	    					+ "\t\tMyLpmRecord = NseLpmTable_Record_Get(MyLpmTable, prio);  // read record from database"+ "\r\n"
	    					+ "\t\tbinary2str(MyLpmRecord->data, data, "+width/8+");  // convert binary memory to data string"+"\r\n"
	    					+ "\t\tbinary2str(MyLpmRecord->mask, mask, "+width/8+");  // convert binary memory to data string"+"\r\n"
	    					+ "\t\tprintf(\"Read record: data: %s\\n\", data);"+"\r\n"
	    					+ "\t\tprintf(\"             mask: %s\\n\", mask);"+"\r\n"
	    					+ "\t}"+"\r\n"
	    					+ "\tfclose(fp);" ;
	    	   }
	    		FileUtil.appendContent(genFileName, content,url); 
	    	
	       }
	    	
	    	
	    
	    
	   }
	    
	    public static void gensearch(String genFileName,Mode mode,List<Ad> adList, List<Acl> aclList, List<Lpm> lpmList, List<Profile> profList,List<Key> keyList, String url)
        {
        	
        	String path1 = url + "KeyCollections"+System.getProperty("file.separator");
        	String path2 = url + "RuleCollections"+System.getProperty("file.separator");
        	String search ="searchkey";
        	String fileNameTemp = genFileName+"_"+search ;  	
        	String source1 ="";
        	String fileNameTemp1 ="";
        	Process process;
    		int flag = 0;

    		System.out.println(keyList.size());
    		
        	for(int i = 0; i < keyList.size(); i++)
        	{

        		
    			int profileid = Integer.parseInt( keyList.get(i).getProfileId());
        		String[] table = new String[4];
        		fileNameTemp1 ="";
        		String source0 ="";
        		
        		if(keyList.get(i).getType().equals("Auto"))
        	  {		
        			String head =genFileName+"_profile"+profileid+"searchkey";
        			if(flag == 0){
        				source1 += head;
        				flag++;
        			}
        			else{
        				source1 += ","+ head ;
        			}
        			for(int j = 0; j < profList.get(i).getTbNum(); j++)
        			{
        				ProfileCfg pfCfg = profList.get(profileid-1).getPrfCfgList().get(j);
        				String name = pfCfg.getTable();
        				int channel = Integer.parseInt(pfCfg.getChannel());
        				table[channel] = name;
        			}
      		
        		
        			for(int t = 0 ; t < 4; t++)
        			{
        				if(table[t] != null)
        				{	
        					fileNameTemp1 =  genFileName +"_"+table[t];
        					if(t == 0)
        					{ 	
        						source0 += fileNameTemp1 ;     			
        					}
        					else
        					{
        						source0 +=","+fileNameTemp1;
        					}
        				}

    				}
    				
    				try {  	
        				process = Runtime.getRuntime().exec("perl "+url+"templet"+System.getProperty("file.separator")+"gen.pl -type 1 -source "+source0+" -path "+path1+" -dest "+head+" -spath "+ path2);
        				if( process.waitFor() == 0)
        				{	
        					System.out.println("success perl "+url+"templet"+System.getProperty("file.separator")+"gen.pl -type 1 -source "+source0+" -path "+path1+" -dest "+head+" -spath "+ path2);
    		  				continue ;
        				}
        			} catch (Exception ex) {  
                      System.out.print("error");
        			}
        	  }
        		
        	}
        	
        	for(int t = 0 ; lpmList != null && t < lpmList.size(); t++)
			{
        		fileNameTemp1 = path2+genFileName +"_"+lpmList.get(t).getName()+".txt" ;
        		File file=new File(fileNameTemp1);
        		if(file.exists())
				{
					file.delete();
				}
        		
			}
			for(int t= 0 ; aclList!=null && t < aclList.size();t++)
			{
				fileNameTemp1 = path2+genFileName +"_"+aclList.get(t).getName()+".txt" ;
        		File file=new File(fileNameTemp1);
        		if(file.exists())
				{
					file.delete();
				} 
				
			}
        	try {  
    
				process = Runtime.getRuntime().exec("perl "+url+"templet"+System.getProperty("file.separator")+"gen.pl -type 0 -source "+source1+" -path "+path1+" -delete 1 -dest "+fileNameTemp);
				
				
				if( process.waitFor() == 0)
				{	
					System.out.println("success perl "+url+"templet"+System.getProperty("file.separator")+"gen.pl -type 0 -source "+source1+" -path "+path1+" -delete 1 -dest "+fileNameTemp);
	  				return ;
				}
			} catch (Exception ex) {  
              System.out.print("error");
			}
        	
        	
        }
	    public static void addsearch(String genFileName,Mode mode,List<Ad> adList, List<Acl> aclList, List<Lpm> lpmList, List<Profile> profList,List<Key> keyList, String url)
        {
        	String content ="\r\n"
    				+ "\tfile = \""+genFileName+"_"+"searchkey.txt\";"+"\r\n"
    				+ "\tfiletofile(file);"+"\r\n";
        	FileUtil.appendContent(genFileName, content,url); 
        	
        	for(int i = 0; i < keyList.size(); i++)
        	{
        		
    			int profileid = Integer.parseInt( keyList.get(i).getProfileId());
        		int portId = 0; //Here need to modify next step	
        		int width = 0;
        		
        		content ="\r\n"
            			+"\t//------------------------Add profile"+profileid+" Search ----------------------------------------------------------\r\n";
        		String path2="";
        		if(keyList.get(i).getType().equals("Auto"))
        	  {		
        		for(int j = 0; j < profList.get(i).getTbNum(); j++)
        		{
            		ProfileCfg pfCfg = profList.get(profileid-1).getPrfCfgList().get(j);
            		String type = pfCfg.getType();
            		String name = pfCfg.getTable();
            		if(type.equals("ACL"))
            		{
            			for(int t = 0 ; t < aclList.size(); t++ )
            			{
            				if(aclList.get(t).getName().equals(name))
            				{
            					width += Integer.parseInt(aclList.get(t).getWidth());
            				}
            			}
            		}
            		else
            		{
            			width += 160 ;
            		}
        		}
        		String search = "profile"+profileid+"searchkey";
        		path2 = genFileName+"_"+search + ".txt";
    		   }
        	   else if(keyList.get(i).getType().equals("Fixed"))
        	   {
        		   path2 = keyList.get(i).getConfig();
        	   }
        		content +="\r\n"
       					+ "\tfp = fopen(\""+path2+"\", \"r\");"+ "\r\n"
       					+ "\tnseStatus = NseLookupResponse_Create(&MyResponse);" + "\r\n"
         				+ checkStatus("Error: fail to create response buffer",0)+ "\r\n"
         				+ "\twhile(fgets(data_buff, 3000, fp) != NULL)"+ "\r\n"
        				+ "\t{"+ "\r\n"
            			+ "\t\tdata_temp = data_buff;"+ "\r\n"
            			+ "\t\tstrncpy( data, data_temp, strlen(data_temp) );"+ "\r\n"
            			+ "\t\tstrcpy(search, data);  // set one search key"+"\r\n"
            			+ "\t\tstr2binary(search, "+width+");  // convert string to binary"+"\r\n"
            			+ "\t\tnseStatus = NseManagerDp_Lookup(MyStargate, "+portId+", "+(profileid-1)+", 0, "+(width/80-1)+", search, " + width/8 + ", MyResponse);  "
            			+ "// send search request to TCAM, the response will be ready(searched), match(hit), priority"+"\r\n"
            			+ "\t"+checkStatus("Error: fail to perform search",1)+ "\r\n"
            			+ "\t\tdisplayresponse(MyResponse->readies, MyResponse->matches, MyResponse->priorities);"+"\r\n"
        				+ "\t}"+"\r\n"
        				+ "\tfclose(fp);";
        	   
        		FileUtil.appendContent(genFileName, content,url); 
        	
           }
        	
		}
        
}
