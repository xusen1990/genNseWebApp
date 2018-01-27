package com.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileUtil {
	
	//private static String path = "D:\\Java\\Workspaces\\MyEclipse 2017 CI\\genNseWebApp\\NseApplication\\";
	//private static String path2 = "D:\\Java\\Workspaces\\MyEclipse 2017 CI\\genNseWebApp\\templet\\";
	private static String path = "NseApplication"+System.getProperty("file.separator");
	private static String path2 = "templet"+System.getProperty("file.separator");
	private static String fileNameTemp = "";
	
	
	public FileUtil(){};
	
	/**
	 * 创建文件
	 * @param fileName 文件名
	 * @return 是否创建成功，成功返回true
	 * */	
	public static boolean createFile(String fileName, String url){
	
		boolean bool = false;
		fileNameTemp = url + path + fileName + ".c";
		File file=new File(fileNameTemp);   
	    try{
	    	if(!file.exists()){
	    		file.createNewFile();
	    		bool = true;
	    		System.out.println("success create file, file is " + fileNameTemp);
	    	}else{
	    		clearInfoForFile(fileName, url);
	    		System.out.println("success clean file, file is " + fileNameTemp);
	    	}
	    }catch (IOException e){
	    	e.printStackTrace();
	    }
	    return bool;
		
	}

	/**
	 * 向文件末尾写入内容
	 * @param fileName 文件名
	 * @param content 内容
	 * @return void
	 * */	
    public static void appendContent(String fileName, String content, String url) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
        	fileNameTemp = url + path + fileName + ".c";
            FileWriter writer = new FileWriter(fileNameTemp, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	
    
    
    /**
	 * 读取文件的内容
	 * @param fileName 文件名
	 * @return 返回文件的内容
	 * */
    public static String readFileContent(String fileName, String url){
    	 StringBuilder result = new StringBuilder();
    	 fileNameTemp =url + path2 + fileName;
         try{
             BufferedReader br = new BufferedReader(new FileReader(fileNameTemp));//构造一个BufferedReader类来读取文件
             String s = null;
             while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                 result.append(System.lineSeparator()+s);
             }
             br.close();    
         }catch(Exception e){
             e.printStackTrace();
         }
         return result.toString();
    	
    }
    /**
 	 * 清空文件的内容
 	 * @param fileName 文件名
 	 * @return void
 	 * */
    
    public static void clearInfoForFile(String fileName, String url) {
    	fileNameTemp = url + path + fileName + ".c";
        File file =new File(fileNameTemp);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}

