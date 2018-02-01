package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.AppDAO;
import com.po.App;

import net.sf.json.JSONArray;



public class HistoryServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public HistoryServlet() {
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

		doPost(request,response);
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

		
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		//int pageSize, pageNumber,total;
		String userOwner,beginDate,endDate,resJson=null;
		AppDAO appDAO = new AppDAO();
		List<App> appList = new ArrayList<App>();
		JSONArray jsonArray = null;
		
		try{
			//pageSize = Integer.parseInt(request.getParameter("pageSize"));
			//pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
			userOwner = request.getParameter("userOwner");
			beginDate = request.getParameter("beginDate");
			endDate = request.getParameter("endDate");
			
			if(!(beginDate == null || beginDate.equals(""))){
				beginDate += " 00:00:00";				
			}
			if(!(endDate ==null || endDate.equals(""))){
				endDate += " 23:59:59";
			}
			System.out.println(userOwner+","+beginDate+","+endDate);
			
			appList = appDAO.query(userOwner, beginDate, endDate);
			
			jsonArray = JSONArray.fromObject(appList);
			resJson = jsonArray.toString();
			System.out.println("result: "+resJson);
			
		}catch(Exception ex){			
			ex.printStackTrace();			
		}
				 
		
		out.println(resJson);
		out.flush();
		out.close();
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
