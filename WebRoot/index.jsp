<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>login</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<style type="text/css">
		.brand{
			text-align:center;
			font-size:90px;
			font-family:'Arial Black';
			font-weight：bold;
			color:#0E3058
		}
		.jumbotron{
			margin:50px
		}
		.sys-name{
			text-align:center;
			font-size:50px;
			font-family:'Arial Black';			
			color:#0E3058
		}
		.logo{  
		    width: 200px;  
		    height: 50px;  
		    max-width: 100%;  
		    max-height: 100%;     
		}  
		.login{
			margin:50px		
		}
	
	</style>
  </head>
  
  <body>
  	<%
  		String username = "";
  		String password = "";
  		Cookie[] cookies = request.getCookies();
  		if(cookies != null && cookies.length > 0){
  			for(Cookie c : cookies){
  				if(c.getName().equals("username")){
  					username = c.getValue();  				
  				}
  				if(c.getName().equals("password")){
  					password = c.getValue();
  				}  			
  			} 		
  		}		
  		
  	 %>
  
  
    <div class="container">
       
	   <div class="row login">
	   		<div class="col-lg-12 col-md-12">
	   			<div class="jumbotron">			        
			        <img  src="pg/brand.png"  class="img-rounded logo">
			        <div class="sys-name">Auto-gen System</div>
			        
			        
			         <form class="form-horizontal login" action="dologin.jsp" method="post">
			            <div class="form-group has-primary">
			                <label for="name" class="col-lg-4 control-label"></label>
			                <div class="col-lg-4">  			                               
			                   <div class="input-group" id="name">
			                       <span class="input-group-addon">
			                           <span class="glyphicon glyphicon-user"></span>
			                       </span>                      
			                       <input type="text" class="form-control" placeholder="username" name="username" value="<%=username %>">                      
			                   </div>		                    
			                </div>
			                <label for="name" class="col-lg-4 control-label"></label>
			            </div> 
			            <div class="form-group has-primary">
			                <label for="passd" class="col-lg-4 control-label"></label>
			                <div class="col-lg-4">  			                               
			                   <div class="input-group" id="password">
			                       <span class="input-group-addon">
			                           <span class="glyphicon glyphicon-lock"></span>
			                       </span>                      
			                       <input type="password" class="form-control" placeholder="password" name="password" value="<%=password %>">                      
			                   </div>		                    
			                </div>
			                <label for="passwd" class="col-lg-4 control-label"></label>
			            </div> 
			           	
			           	<div class="form-group has-primary">		        		                                           
				            <div class="checkbox col-lg-4 col-lg-offset-4">
								<label>
									<input type="checkbox" name="isUseCookie" checked="checked"> Login within 10 days
								</label>
							</div>       
			            </div>    
			      
			            
			            
			            <div class="form-group has-primary">
			                <label for="submit" class="col-lg-4 control-label"></label>
			                <div class="col-lg-4">  			                               
			                    <button type="submit" class="btn btn-primary">Submit</button>   	                    
			                </div>
			                <label for="submit" class="col-lg-4 control-label"></label>
			            </div> 			                        
			        </form>  
			        
			      
			   </div>	  
			</div>   		
	   		
	   </div>
	</div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery-1.11.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript">
    
    	
    
    </script>
    
  </body>
</html>
