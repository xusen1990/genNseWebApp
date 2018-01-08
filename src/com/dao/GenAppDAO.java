package com.dao;

import java.util.List;

import com.po.Acl;
import com.po.Ad;
import com.po.Lpm;
import com.po.Mode;
import com.po.Profile;
import com.po.ProfileCfg;
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
	    			+checkStatus("Error: fail to create manager");
	    			
	    	FileUtil.appendContent(genFileName, content,url);       	
	    }
	    
	    public static void addTable(String genFileName, Mode mode, List<Ad> adList, List<Acl> aclList, List<Lpm> lpmList,String url ){
	    	String content ="\r\n";
	    	if(mode.isEnAd()){
	    		content+="\t// ------------------------       Add AD ----------------------------------------------------------\r\n"
	    				+"\tnseStatus = NseAdBlock_Create(&MyADBuff);\r\n"    				
	    				+checkStatus("Error: fail to create AD table");
	    			
	    		
	    		for(int i = 0; i < adList.size(); i++){
	    			content +="\r\n"
	    					+"\t//AD"+(i+1)+"\r\n";
	    			int portId = 0;   //Here need to modify next step
	    			content +="\tMyADBuff->baseAddr = "+adList.get(i).getBaseAddr()+" ;\r\n"
	    					+"\tMyADBuff->size = "+adList.get(i).getSize()+" ;\r\n"
	    					+"\tMyADBuff->width = "+adList.get(i).getWidth()+" ;\r\n"
	    					+"\tnseStatus = NseManagerDp_AdBlock_Add(MyStargate, MyADBuff, "+portId+", "+i+");\r\n"
	    					+checkStatus("Error: fail to add AD table");
	    			
	    		}   		
	    		
	    		
	    		FileUtil.appendContent(genFileName, content,url); 
	    	}
	    	
	    	
	    	if(mode.isEnAcl()){
	    		content ="\r\n"
	    				+"\t// ------------------------       Add ACL TABLE ----------------------------------------------------------\r\n"
	    				+"\tnseStatus = NseTable_Create(&MyTableBuff);  // allocate memory for table handler\r\n"
	    				+checkStatus("Error: fail to create ACL table");
	    		
	    		for(int i = 0; i < aclList.size(); i++){
	    			content +="\r\n"
	    					+"\t//ACL"+(i+1)+"\r\n";
	    			int portId = 0;   //Here need to modify next step
	    			content += "\tMyTableBuff->name = " + "\""+aclList.get(i).getName()+"\"" + " ;\r\n"
	    					+"\tMyTableBuff->depth = "+ aclList.get(i).getName() + "_ACL_SIZE;\r\n"
	    					+"\tMyTableBuff->width = " + aclList.get(i).getWidth() + " ;\r\n";
	    			int j = 0;
	    			boolean findAdBlock = false;
	    			for(; j < adList.size(); j++){
	    				if(adList.get(j).getName().equals(aclList.get(i).getAdBlock())){
	    					findAdBlock = true;
	    					break;
	    				}    				
	    			}
	    			if(findAdBlock){
	    				content +="\tMyTableBuff->adBlock = NseManagerDp_AdBlock_Get(MyStargate, " + portId +", "+ j +");\r\n"
	    						+"\tnseStatus = NseManagerDp_Table_Add(MyStargate, MyTableBuff, " + portId +", "+ i +");\r\n"
	    						+checkStatus("Error: fail to add  ACL table");   				
	    				
	    			}
	    			else{
	    				System.out.println("Error: cannot find AD block!!");
	    			}
	    		}
	    		
	    		FileUtil.appendContent(genFileName, content,url); 
	    		
	    	}
	    	
	    	
	    	
	    	if(mode.isEnLpm()){
	    		content ="\r\n"
	    				+"\t// ------------------------       Add LPM TABLE ----------------------------------------------------------\r\n"
	    				+"\tnseStatus = NseLpmTable_Create(&MyLpmTableBuff);\r\n"
	    				+checkStatus("Error: fail to create LPM table");
	    		
	    		for(int i = 0; i < lpmList.size(); i++){
	    			content +="\r\n"
	    					+"\t//LPM"+(i+1)+"\r\n";
	    			int portId = 0;   //Here need to modify next step
	    			content += "\tMyLpmTableBuff->name = " + "\""+lpmList.get(i).getName()+"\"" + " ;\r\n"
	    					+"\tMyLpmTableBuff->depth = "+ lpmList.get(i).getName() + "_LPM_SIZE;\r\n"
	    					+"\tMyLpmTableBuff->width = " + lpmList.get(i).getWidth() + " ;\r\n"
	    					+"\tMyLpmTableBuff->type = " + lpmList.get(i).getType()+";\r\n"
	    					+"\tnseStatus = NseManagerDp_LpmTable_Add(MyStargate, MyLpmTableBuff, "+ portId + ");\r\n"
	    					+checkStatus("Error: fail to add LPM table");    			
	    		} 		
	    		FileUtil.appendContent(genFileName, content,url); 
	    		
	    	}
	    	
	    }
	    
	    
	    public static void addProfile(String genFileName, Mode mode, List<Acl> aclList, List<Lpm> lpmList, List<Profile> profList,String url){
	    	
	    	String content = "\r\n";
	    	content += FileUtil.readFileContent("profile.txt",url);
	    	FileUtil.appendContent(genFileName, content,url);  
	    	
	    	for(int i = 0; i < profList.size(); i++){
	    		
	    		int portId = 0; //Here need to modify next step
	    		content += "\r\n"
	    				+"\t//profile"+i+"\r\n"
	    				+"\tnseStatus = NseManagerDp_Profile_Add(MyStargate, MyProfileBuff, " + portId +", " + i +");\r\n"
	    				+checkStatus("Error: fail to add profile")
	    				+"\tMyProfile = NseManagerDp_Profile_Get(MyStargate, " +portId +", " + i +");\r\n";    		
	    		
	    		for(int j = 0; j < profList.get(i).getTbNum(); j++){
	    			
	    			
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
	    							+checkStatus("Error: fail to add search table");
	    					//setting kgu
	    					content += addKguSetting(Integer.parseInt(pfCfg.getKgu()),width);
	    					
	    					
	    				}else{    					
	    					System.out.println("Error: can not find ACL Table to configure profile");
	    				}
	    				
	    			}
	    			
	    			if(type.equals("LPM")){
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
	    							+checkStatus("Error: fail to add search lpm table");
	    					//setting kgu
	    					content += addKguSetting(Integer.parseInt(pfCfg.getKgu()),160);    					
	    				}   				
	    				else{
	    					System.out.println("Error: can not find LPM Table to configure profile");
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
	    
	    
	    
	    public static String checkStatus(String printInfo){
	    	String content ="\tif (nseStatus != Nse_OK)\r\n"
	    		           +"\t{\r\n"
			    			+"\t\tprintf(\""+printInfo+"\\n\");\r\n"
			    			+"\t\tgoto done;\r\n"
			    			+"\t}\r\n";
	    	
	    	return content;    	
	    }
	    
	    public static int getSelChannel(int ch){
	    	
	    	return (int)Math.pow(2.0, ch);    	
	    }
	    
	    
	    public static String addKguSetting(int kguId, int width){
	    	//width :80 or 160 or 320 or 640 
	    	String content ="";
	    	int index = 0;
	    	if (width == 80){
	    		content += "\tMyKguSegment->offset = 0;\r\n"
	    				+"\tMyKguSegment->byteCount = 9;\r\n"
	    				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	    				+checkStatus("Error: fail to add kgu segment");
	    		index++;
	    	}else{
	    		content += "\tMyKguSegment->offset = 0;\r\n"
	    				+"\tMyKguSegment->byteCount = 15;\r\n"
	    				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	    				+checkStatus("Error: fail to add kgu segment");
	    		index++;
	    		
	    		if(width == 160){
	    			content += "\tMyKguSegment->offset = 16;\r\n"
	        				+"\tMyKguSegment->byteCount = 3;\r\n"
	        				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	        				+checkStatus("Error: fail to add kgu segment");
	        		index++;   	    			
	    		}else{
	    			content += "\tMyKguSegment->offset = 16;\r\n"
	        				+"\tMyKguSegment->byteCount = 15;\r\n"
	        				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	        				+checkStatus("Error: fail to add kgu segment");
	        		index++;   	      			
	    			if(width == 320){
	    				content += "\tMyKguSegment->offset = 32;\r\n"
	            				+"\tMyKguSegment->byteCount = 7;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment");
	            		index++; 
	    			}else if(width == 640){
	    				content += "\tMyKguSegment->offset = 32;\r\n"
	            				+"\tMyKguSegment->byteCount = 15;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment");
	            		index++; 
	            		content += "\tMyKguSegment->offset = 48;\r\n"
	            				+"\tMyKguSegment->byteCount = 15;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment");
	            		index++; 
	            		content += "\tMyKguSegment->offset = 64;\r\n"
	            				+"\tMyKguSegment->byteCount = 15;\r\n"
	            				+"\tnseStatus = NseProfile_KguSegment_Add(MyProfile, " +kguId +", MyKguSegment, "+ index +");\r\n"
	            				+checkStatus("Error: fail to add kgu segment");
	            		index++;  				
	    				
	    			}
	    			
	    		}
	    		
	    		
	    	}
	    	
	    	
	    	return content;
	    }

}
