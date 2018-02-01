package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.GenAppDAO;
import com.dao.AppDAO;
import com.po.Acl;
import com.po.App;
import com.po.Ad;
import com.po.Key;
import com.po.Lpm;
import com.po.Mode;
import com.po.Profile;
import com.po.Rule;
import com.util.FileUtil;



public class GenAppServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public GenAppServlet() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		LocalDate localDate = LocalDate.now();   
		LocalTime localTime = LocalTime.now();
		long start = System.currentTimeMillis();
    	
		Mode mode = new Mode();    	
    	List<Ad> adList = new ArrayList<Ad>();     	
    	List<Acl> aclList = new ArrayList<Acl>();     	
    	List<Lpm> lpmList = new ArrayList<Lpm>();     	
    	List<Profile> profList = new ArrayList<Profile>();
    	String username;
    	String genFileName = null;
    	List<Rule> ruleList = new ArrayList<Rule>();
    	List<Key>  keyList = new ArrayList<Key>();
    	List<Integer>  ADflag = new ArrayList<>();
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		
		
		
		try{
			mode = (Mode)session.getAttribute("mode");
			adList = (List<Ad>) session.getAttribute("adList");
			aclList = (List<Acl>) session.getAttribute("aclList");
			lpmList = (List<Lpm>) session.getAttribute("lpmList");
			profList = (List<Profile>) session.getAttribute("profList");
			username = (String) session.getAttribute("loginUser");
			ruleList= (List<Rule>) session.getAttribute("ruleList");
			keyList = (List<Key>)session.getAttribute("keyList");
				
			//genFileName = "nseApp_"+username+"_"+localDate.toString();
			String FileName = localDate.toString()+"_"+localTime.toString().replace(".", "-").replace(":", "-");
			genFileName = "nseApp_"+username+"_"+FileName;
			//genFileName = "nseApp_"+username+"_"+localDate.toString();
			
			
			if(adList != null)
	    	{
	    		for(int pair = 0 ; pair < adList.size() ; pair++)
	    		{
	    			ADflag.add(0);
	    		}
	    	}
			//测试读写文件
			
			String url = request.getSession().getServletContext().getRealPath("");
			System.out.println("url:"+url);
	    	FileUtil.createFile(genFileName,url);
	    	
	    	//加头文件
	    	GenAppDAO.addHead(genFileName,url);
	    	
	    	//加define
	    	GenAppDAO.addDefine(genFileName,mode,aclList,lpmList,url);
	    	
	    	//加函数声明
	    	GenAppDAO.addDeclareFun(genFileName,url);
	    	
	    	//加main、变量声明
	    	GenAppDAO.addMain(genFileName,url);
	    	
	    	//create nse handler
	    	GenAppDAO.addNseHandler(genFileName,mode,url);
	    	    	
	    	//add table
	    	GenAppDAO.addTable(genFileName,mode,adList,aclList,lpmList,url);
	    	
	    	//add profile
	    	GenAppDAO.addProfile(genFileName,mode,aclList,lpmList,profList,url);
	    	
	    	//add lock config
	    	GenAppDAO.addLockConfig(genFileName,url);
	    	
	    	
	    	
	    	
	    	
	    	//generate rule
	    	GenAppDAO.parserule(aclList,lpmList,ruleList,url,genFileName);
	    	long start0 = System.currentTimeMillis();
	    	System.out.println("parse rule pathtime1:"+(start0-start));
	    	//add rule
	    	GenAppDAO.genrule(genFileName, aclList, lpmList, url);
	    	long start1 = System.currentTimeMillis();
	    	System.out.println("gen rule pathtime1:"+(start1-start0));
	    	
	    	GenAppDAO.addrule(genFileName,mode,adList,aclList,lpmList,profList,url,ADflag);
	    	long start2 = System.currentTimeMillis();
	    	System.out.println("add rule pathtime2:"+(start2-start1));
	    	
	    	//add search which will hit the previous record
	    	GenAppDAO.gensearch(genFileName, mode, adList, aclList, lpmList, profList, keyList, url);
	    	long start3 = System.currentTimeMillis();
	    	System.out.println("gensearch pathtime3:"+(start3-start2));
	    	
	    	GenAppDAO.addsearch(genFileName,mode,adList,aclList,lpmList,profList,keyList,url);
	    	long start4 = System.currentTimeMillis();
	    	System.out.println("addsearch pathtime4:"+(start4-start3));
	    	System.out.println("total pathtime5:"+(start4-start));
	    	//add end main
	    	GenAppDAO.addEndMain(genFileName,url);
	    	
	    	PrintWriter out = response.getWriter();
	    	String keyname = genFileName+"_searchkey";
	    	//System.out.println("{\"success\":true,"+"\"filename\":"+"\""+genFileName+"\","+"\"keyname\":"+"\""+keyname+"\""+"}");
	    	//out.println("{\"success\":true,"+"\"filename\":"+"\""+genFileName+"\","+"\"keyname\":"+"\""+keyname+"\""+"}");
	    	String rulename = genFileName+"_rule";;
	    	System.out.println("{\"success\":true,"+"\"filename\":"+"\""+genFileName+"\","+"\"rulename\":"+"\""+rulename+"\","+"\"keyname\":"+"\""+keyname+"\""+"}");
	    	
			
			App app = new App();
			app.setUserName(username);
			app.setAppName(genFileName);
			app.setRuleName(rulename);
			app.setSearchKeyName(keyname);
			AppDAO appDAO = new AppDAO();
			appDAO.addAppHistory(app);
	    	
			
			out.println("{\"success\":true,"+"\"filename\":"+"\""+genFileName+"\","+"\"rulename\":"+"\""+rulename+"\","+"\"keyname\":"+"\""+keyname+"\""+"}");
			//out.println("{\"success\":true}");
			out.flush();
			out.close();
	    	
			long end = System.currentTimeMillis();
			System.out.println("pathtime:"+(end-start));
		
		}catch(Exception ex){			
			ex.printStackTrace();			
		}
		
		
		
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}
