package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.po.Mode;

public class ModeServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public ModeServlet() {
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

		Mode mode = new Mode();
		String nseMode, enAcl, enAd, enLpm;
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		
		try{
			
			nseMode = request.getParameter("nseMode");
			enAcl = request.getParameter("enAcl");
			enAd = request.getParameter("enAd");
			enLpm = request.getParameter("enLpm");
			System.out.println(nseMode + " " + enAcl + " " + enAd + " " +enLpm);
			
			mode.setNseMode(nseMode);
			mode.setEnAcl(enAcl.equals("true"));
			mode.setEnAd(enAd.equals("true"));
			mode.setEnLpm(enLpm.equals("true"));
			
			session.setAttribute("mode", mode);
			
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
