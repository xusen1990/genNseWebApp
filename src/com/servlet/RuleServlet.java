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

import com.dao.RuleDAO;
import com.po.Rule;

public class RuleServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public RuleServlet() {
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

		List<Rule> ruleList = new ArrayList<Rule>();
		String ruleJsonStr;
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		
		try{
			ruleJsonStr = request.getParameter("ruleJsonStr");
			System.out.println("211");
			//System.out.println(adJsonStr);
			RuleDAO.parseJsonRule(ruleJsonStr, ruleList);
			for(int i = 0; i < ruleList.size(); i++){
				System.out.println(ruleList.get(i).getName());
			}
			
			session.setAttribute("ruleList", ruleList);
		}catch(Exception ex){			
			ex.printStackTrace();			
		}
		
		
		PrintWriter out = response.getWriter();
		out.println("{\"success\":true}");
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
