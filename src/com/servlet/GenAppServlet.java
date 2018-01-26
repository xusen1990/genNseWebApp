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

import com.dao.AppDAO;
import com.dao.GenAppDAO;
import com.po.Acl;
import com.po.Ad;
import com.po.App;
import com.po.Lpm;
import com.po.Mode;
import com.po.Profile;
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
    	
		
		
		Mode mode = new Mode();    	
    	List<Ad> adList = new ArrayList<Ad>();     	
    	List<Acl> aclList = new ArrayList<Acl>();     	
    	List<Lpm> lpmList = new ArrayList<Lpm>();     	
    	List<Profile> profList = new ArrayList<Profile>();
    	String username;
    	String genFileName = null;
    	
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
				
			genFileName = "nseApp_"+username+"_"+localDate.toString()+"_"+localTime.toString().replace(".", "-").replace(":", "-");
			//genFileName = "nseApp_"+username+"_"+localDate.toString();
			
			
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
	    	
	    	
	    	//add end main
	    	GenAppDAO.addEndMain(genFileName,url);
	    	
	    	
	    	
	    	//write to database
			App app = new App();
			app.setUserName(username);
			app.setAppName(genFileName);
			app.setRuleName("rule_test");
			app.setSearchKeyName("searchkey_test");
			AppDAO appDAO = new AppDAO();
			appDAO.addAppHistory(app);
	    	
	    	PrintWriter out = response.getWriter();
			out.println("{\"success\":true,"+"\"filename\":"+"\""+genFileName+"\"}");
			//out.println("{\"success\":true}");
			out.flush();
			out.close();
			
		
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
