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
	 * �����ļ�
	 * @param fileName �ļ���
	 * @return �Ƿ񴴽��ɹ����ɹ�����true
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
	 * ���ļ�ĩβд������
	 * @param fileName �ļ���
	 * @param content ����
	 * @return void
	 * */	
    public static void appendContent(String fileName, String content, String url) {
        try {
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
        	fileNameTemp = url + path + fileName + ".c";
            FileWriter writer = new FileWriter(fileNameTemp, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	
    
    
    /**
	 * ��ȡ�ļ�������
	 * @param fileName �ļ���
	 * @return �����ļ�������
	 * */
    public static String readFileContent(String fileName, String url){
    	 StringBuilder result = new StringBuilder();
    	 fileNameTemp =url + path2 + fileName;
         try{
             BufferedReader br = new BufferedReader(new FileReader(fileNameTemp));//����һ��BufferedReader������ȡ�ļ�
             String s = null;
             while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
                 result.append(System.lineSeparator()+s);
             }
             br.close();    
         }catch(Exception e){
             e.printStackTrace();
         }
         return result.toString();
    	
    }
    /**
 	 * ����ļ�������
 	 * @param fileName �ļ���
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

