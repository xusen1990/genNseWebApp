<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"  %>
<jsp:useBean id="loginUser" class="com.po.Users"></jsp:useBean>
<jsp:useBean id="userDAO" class="com.dao.UsersDAO"></jsp:useBean>
<jsp:setProperty property="*" name="loginUser"/>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	
	
	
	String[] isUseCookies = request.getParameterValues("isUseCookie");
	if(isUseCookies != null && isUseCookies.length > 0){
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Cookie usernameCookie = new Cookie("username",username);
		Cookie passwordCookie = new Cookie("password",password);
		usernameCookie.setMaxAge(864000);
		passwordCookie.setMaxAge(864000);
		response.addCookie(usernameCookie);
		response.addCookie(passwordCookie);
	}else{
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie c : cookies){
				if(c.getName().equals("username") || c.getName().equals("password")){
				c.setMaxAge(0);
				response.addCookie(c);		
				}			
			}		
		}
	}
	if(userDAO.usersLogin(loginUser))
	{
		session.setAttribute("loginUser",loginUser.getUsername());
		request.getRequestDispatcher("main.jsp").forward(request,response);
	}else{
	
		response.sendRedirect("login_failure.jsp");
	}


%>


