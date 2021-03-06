<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"%>
<%@ page import="com.po.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'main.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/bootstrap-3.3.4.css">
	<link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css">
	<link rel="stylesheet" type="text/css" href="css/build.css">
	<link rel="stylesheet" type="text/css" href="css/fileinput.css">
	
	<style>
        body {
            padding-top: 50px;
        }

        .gourpbtn {
            padding-left: 15px;
            padding-right: 15px;
            padding-bottom: 15px;
        }

        .progress {
            margin-top: 10px;
        }

        .sidebar {
            background-color: #f5f5f5;
            position: fixed;
            left: 0px;
            bottom: 0px;
            top: 51px;
            padding: 20px 0 0 0;
        }

        .nav-sidebar > .active > a,
        .nav-sidebar > .active > a:hover,
        .nav-sidebar > .active > a:focus {
            color: #fff;
            background-color: #428bca;
        }        
        
        .logo{  
		    width: 200px;  
		    height: 50px;  
		    max-width: 100%;  
		    max-height: 100%;     
		} 
		
		.brand{
			float:none;
			margin-left: -15px;
			height: 50px;
		    padding: 15px 15px;
		    font-size: 18px;
		    line-height: 20px;			
		} 
		.user-name{
			color:#FFFFFF;
			float:none;
			height: 50px;			
			font-size: 16px;
			line-height: 20px;				
		}
		.btn-drop{
			padding: 14px 14px;
		}
		
		.title-text{
			padding: 10px 15px 10px 15px;		
		}
		.title-text2{
			padding: 20px 15px 10px 15px;		
		}
		
		.ad-table-conf-body,.acl-table-conf-body,.lpm-table-conf-body,.add-rules-conf-body,.search-key-conf-body{
			width:100%;
			height:280px;
			overflow:auto;
		}	
		
		.profile-conf-body{
			width:100%;
			height:330px;
			overflow:auto;		
		}
		
		
		.profile-config-head{
			width:auto;
			height:30px;
			padding: 0 15px 0 15px;
		}	
    </style>
	
  </head>
  
  <body>
  	 <%
  		if(session.getAttribute("loginUser") == null){
  			response.sendRedirect("index.jsp");  		
  		}
  		
  		ArrayList<Ad> AdList = new ArrayList<Ad>(); 		
  		 	  	
  	 %>
  
  
  
  
     <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <a class="navbar-brand " href="main.jsp" ">Auto-gen System</a>          
            </div>
            
            
            <form class="navbar-form navbar-right" role="search"  action="index.jsp" method="post">
            	<span class="glyphicon glyphicon-user" style="color:#FFFFFF;"></span>
            	<span class="user-name" id="user-name"><%=session.getAttribute("loginUser") %></span>&nbsp;&nbsp;&nbsp;&nbsp;            	
            	<button type="submit" class="btn btn-info" id="logout"><span class="glyphicon glyphicon-off" style="color:#FFFFFF;"></span></button>            	
            </form>          
        </div>
        <!-- /.container-fluid -->
    </nav>

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-2 sidebar">
                <ul class="nav nav-sidebar">
                    <li><a data-toggle="modal" href="#mode-config">Table Configure&nbsp;&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-wrench" style="color:#337ab7;"></span></a></li>
                    <li><a data-toggle="modal" href="#profile-config">Profile Configure&nbsp;&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-cog" style="color:#337ab7;"></span></a></li>
                    <li><a data-toggle="modal" href="#add-rules">Add Rules&nbsp;&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-edit" style="color:#337ab7;"></span></a></li>
                    <li><a data-toggle="modal" href="#search-key">Search Key&nbsp;&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-search" style="color:#337ab7;"></span></a></li>
                    <li><a data-toggle="modal" href="#gen-app">Gen-Application&nbsp;&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-send" style="color:#337ab7;"></span></a></li>
                    <li><a data-toggle="modal" href="#history">History&nbsp;&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon glyphicon-list-alt" style="color:#337ab7;"></span></a></li>
                    <li><a data-toggle="modal" href="#help">Help&nbsp;&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-exclamation-sign" style="color:#337ab7;"></span></a></li>
                    
                </ul>
            </div>
            <div class="col-md-10 col-md-offset-2">
                <div class="page-header">
                    <h1>Dashboard</h1>
                </div>
         
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-primary">
                            <div class="panel-heading">Profile Configure</div>
                            <div class="panel-body">
                                <div class="alert alert-success" role="alert">
                                    <strong>Warning!</strong> Better check yourself, you're not looking too good.
                                </div>
                                <div class="alert alert-info" role="alert">
                                    <strong>Warning!</strong> Better check yourself, you're not looking too good.
                                </div>
                                
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="panel panel-primary">
                            <div class="panel-heading">AD Table Configure</div>
                            <div class="panel-body">
                                <ul class="list-group">
                                    <li class="list-group-item list-group-item-info">Table1<span class="badge">42</span></li>
                                </ul>
                                <ul class="list-group">
                                    <li class="list-group-item list-group-item-info">Table2<span class="badge">20</span></li>
                                </ul>
                                <ul class="list-group">
                                    <li class="list-group-item list-group-item-info">Table3<span class="badge">10</span></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-primary">
                            <div class="panel-heading">ACL Table Configure</div>
                            <div class="panel-body">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>test</th>
                                            <th>test</th>
                                            <th>test</th>
                                            <th>test</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>1</td>
                                            <td>T1</td>
                                            <td>10</td>
                                            <td>80000</td>
                                            <td>test</td>
                                        </tr>
                                        <tr>
                                            <td>2</td>
                                            <td>T2</td>
                                            <td>20</td>
                                            <td>65000</td>
                                            <td>test</td>
                                        </tr>
                                        <tr>
                                            <td>3</td>
                                            <td>T3</td>
                                            <td>5</td>
                                            <td>50000</td>
                                            <td>test</td>
                                        </tr>
                                    </tbody>
                                </table>
                                <button type="button" class="btn btn-primary">查看详情 >></button>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="panel panel-primary">
                            <div class="panel-heading">LPM Table Configure</div>
                            <div class="panel-body">
                                <label class="label label-primary">test</label>
                                <div class="progress">
                                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
                                        <span class="sr-only">60% Complete</span>
                                    </div>
                                </div>
                                <label class="label label-danger">test</label>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
                                        <span class="sr-only">60% Complete</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
       <hr class="divider"> 
       <footer>        
        <p class="pull-right">Copyright &copy; 2017 <strong>Corigine</strong> </p>
       </footer>
       
       
       
      
       <!-- 模态框（Modal） -->       
       
       <!-- Tb configure -->
	    <div class="modal fade " id="mode-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title" >Configure Mode</h4>
	                </div>
	                <div class="modal-body">
	                	<form role="form">		     
	                		<div class="row">
	                			<div class="col-lg-1 col-md-1">	          
	                			</div>
	                			<div class="col-lg-2 col-md-2 title-text">
	                				<strong>Mode:</strong>
	                			</div>  
	                			<div class="col-lg-8 col-md-8">         	
									<div class="input-group" id="mode-select">								
										<input type="text" class="form-control" id="nseMode" name="nseMode">
										<div class="input-group-btn">					
											<button type="button" class="btn btn-default dropdown-toggle btn-drop" data-toggle="dropdown" tabindex="-1">
												<span class="caret"></span>
												<span class="sr-only">切换下拉菜单</span>
											</button>
											<ul class="dropdown-menu pull-right" >
												<li><a href="#">NSE_MODE_SINGLE_PORT</a></li>
												<li><a href="#">NSE_MODE_DUAL_PORT_SINGLE_BANK</a></li>
												<li><a href="#">NSE_MODE_DUAL_PORT_DUAL_BANK</a></li>												
											</ul>
										</div><!-- /btn-group -->
									</div><!-- /input-group -->	
								</div><!-- /col -->	
							</div><!-- /input-row -->	
							<div class="row">
								<div class="col-lg-1 col-md-1"></div>
								<div class="col-lg-6 col-md-6 title-text">
									<div class="checkbox checkbox-success">
			                        	<input id="checkbox-ad" class="styled" type="checkbox" name="enAd">
			                        	<label for="checkbox-ad">
			                            	enable AD
			                        	</label>
			                    	</div>	
			                    </div>						
							</div>
							<div class="row">
								<div class="col-lg-1 col-md-1"></div>
								<div class="col-lg-6 col-md-6 title-text">
									<div class="checkbox checkbox-success">
			                        	<input id="checkbox-acl" class="styled" type="checkbox" name="enAcl">
			                        	<label for="checkbox-acl">
			                            	enable ACL
			                        	</label>
			                    	</div>	
			                    </div>						
							</div>
							<div class="row">
								<div class="col-lg-1 col-md-1"></div>
								<div class="col-lg-6 col-md-6 title-text">
									<div class="checkbox checkbox-success">
			                        	<input id="checkbox-lpm" class="styled" type="checkbox" name="enLpm">
			                        	<label for="checkbox-lpm">
			                            	enable LPM
			                        	</label>
			                    	</div>	
			                    </div>						
							</div>
												
						</form>
	                
	                </div>
	                <div class="modal-footer">	                    
	                    <button type="button" class="btn btn-primary" id="mode-next" data-toggle="modal">next step</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    <!-- help -->
	     <div class="modal fade " id="help" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Help</strong></h4>
	                </div>
	                <div class="modal-body">
	                	If you have any question, please contact with <a href="mailto:xusen.li@corigine.com?subject=QA: Auto-gen System">Xusen</a>.	                
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal">close</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    
	    <!-- gen-app -->
	     <div class="modal fade " id="gen-app" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Generate Application</strong></h4>
	                </div>
	                <div class="modal-body">
	                	<div style="text-align:center" class="loading-pg">
	                	   <img  src="pg/loading.gif"  > 
	                	</div>	                	
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-primary" data-dismiss="modal">close</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    
	     <!-- AD -->
	     <div class="modal fade " id="ad-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Configure AD</strong></h4>
	                </div>
	                <div class="modal-body ad-table-conf-body" >
	                	
						<div class="panel panel-default ad-table-panel">
						  <div class="panel-heading">
						    <h4 class="panel-title"><span>AD Table1</span><span type="button" class="glyphicon glyphicon-plus ad-table-add" style="float:right"></span></h4>
						  </div>
						  <div class="panel-body" >
						  	<form role="form">
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>name:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control ad-name" >
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>baseAddr:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control ad-baseAddr">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>size:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control ad-size">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>width:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control ad-width">
							      </div>							      
							    </div>
						    </form>
						  </div>
						</div>             	
	                	            
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-primary" id="ad-next" data-toggle="modal">next step</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    <!-- ACL -->
	     <div class="modal fade " id="acl-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Configure ACL</strong></h4>
	                </div>
	                <div class="modal-body acl-table-conf-body">
	                	
	                	<div class="panel panel-default acl-table-panel">
						  <div class="panel-heading">
						    <h4 class="panel-title"><span>ACL Table1</span><span type="button" class="glyphicon glyphicon-plus acl-table-add" style="float:right"></span></h4>
						  </div>
						  <div class="panel-body" >
						  	<form role="form">
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>name:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control acl-name">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>depth:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control acl-depth">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>width:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control acl-width">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>adBlock:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control acl-adBlock">
							      </div>							      
							    </div>
						    </form>
						  </div>
						</div>
	                	
	                	
	                	
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-primary" id="acl-next" data-toggle="modal">next step</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    
	    
	    <!-- LPM -->
	     <div class="modal fade " id="lpm-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Configure LPM</strong></h4>
	                </div>
	                <div class="modal-body lpm-table-conf-body">
	                
	                	<div class="panel panel-default lpm-table-panel">
						  <div class="panel-heading">
						    <h4 class="panel-title"><span>LPM Table1</span><span type="button" class="glyphicon glyphicon-plus lpm-table-add" style="float:right"></span></h4>
						  </div>
						  <div class="panel-body" >
						  	<form role="form">
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>name:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control lpm-name">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>depth:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control lpm-depth">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>width:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control lpm-width">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>type:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control lpm-type">
							      </div>							      
							    </div>
						    </form>
						  </div>
						</div>
	                		                
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-primary" id="lpm-next" data-toggle="modal">Submit</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    
	    
	    <!-- profile-config -->
	     <div class="modal fade " id="profile-config" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Configure Profile</strong></h4>
	                </div>
	                <div class="modal-body profile-conf-body">
	                
	                	
	                	<div class="panel panel-default profile-panel">
						  <div class="panel-heading">
						    <h4 class="panel-title"><span>Profile1</span><span type="button" class="glyphicon glyphicon-plus profile-add" style="float:right"></span></h4>
						  </div>
						  <div class="panel-body profile-config-body" >
						  	<form role="form">
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-1 col-md-2 col-md-offset-1 title-text">
							      	<strong>tb-num:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control profile-tb-num">
							      </div>	
							      <div class="col-lg-2 col-lg-offset-1 col-md-2 col-md-offset-1">							      
							      	<span type="button" class="glyphicon glyphicon-chevron-down detail-profile-add" style="float:center; padding:10px 0 0 0"></span>
							      </div>
							     </div>
							</form>							
						  </div>						  
						</div>     
	                		                
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-primary" id="profile-submit" data-dismiss="modal">submit</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    <div class="add-config-profile" style="display:none">
	    	<div class="panel panel-default profile-panel">
			  <div class="panel-heading">
			    <h4 class="panel-title"><span>Profile1</span><span type="button" class="glyphicon glyphicon-plus profile-add" style="float:right"></span></h4>
			  </div>
			  <div class="panel-body profile-config-body" >
			  	<form role="form">
				    <div class="row">
				      <div class="col-lg-2 col-lg-offset-1 col-md-2 col-md-offset-1 title-text">
				      	<strong>tb-num:</strong>
				      </div>
				      <div class="col-lg-6 col-md-6">
				      	<input type="text" class="form-control profile-tb-num">
				      </div>	
				      <div class="col-lg-2 col-lg-offset-1 col-md-2 col-md-offset-1">							      
				      	<span type="button" class="glyphicon glyphicon-chevron-down detail-profile-add" style="float:center; padding:10px 0 0 0;"></span>
				      </div>
				     </div>
				</form>							
			  </div>						  
			</div>   
	    </div>
	    
	    
	    <div class="add-config-profile-table" style="display:none">
	    	<br>
	    	<div class="panel-heading profile-config-head">
			  <h6 class="panel-title" style="font-style:italic"><span>Configure1</span></h6>
			  <hr>
			</div>
			<div class="panel-body" >
				<form role="form">
					<div class="row">
					  <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
						<strong>type:</strong>
					  </div>
					  <div class="col-lg-6 col-md-6">
						<input type="text" class="form-control pf-type">
					  </div>							      
					</div>
					<div class="row">
					  <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
						<strong>table:</strong>
					  </div>
					  <div class="col-lg-6 col-md-6">
						<input type="text" class="form-control pf-table">
					  </div>							      
					</div>
					<div class="row">
					  <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
						<strong>channel:</strong>
					  </div>
					  <div class="col-lg-6 col-md-6">
						<input type="text" class="form-control pf-channel">
					  </div>							      
					</div>
					<div class="row">
					  <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
						<strong>kgu:</strong>
					  </div>
					  <div class="col-lg-6 col-md-6">
						<input type="text" class="form-control pf-kgu">
					  </div>							      
					</div>
				</form>
			</div>	    
	    </div>
	    
	    
	    
	    
	    
	    
	    <!-- add-rules -->
	     <div class="modal fade " id="add-rules" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Add Rules</strong></h4>
	                </div>
	                <div class="modal-body add-rules-conf-body">
	                	<div class="panel panel-default rule-panel">
						  <div class="panel-heading">
						    <h4 class="panel-title"><span>Rule1</span><span type="button" class="glyphicon glyphicon-plus rule-add" style="float:right"></span></h4>
						  </div>
						  <div class="panel-body" >
						  	<form role="form">
						  		<div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text2">
							      	<strong>Table:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6" style="padding:10px 0 0 0">
							      	<input type="text" class="form-control">							      	
							      </div>							      
							    </div>	
						  	
						  	
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>Type:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6" style="padding:10px 0 0 0">
							      	
							      	<div class="radio-inline">
									    <label>
									        <input type="radio" class="add-inc-rule" name="type" value="" checked/>INC&nbsp;&nbsp;&nbsp;&nbsp;
									    </label>
									</div>
									
									<div class="radio-inline">
									    <label>
									        <input type="radio" class="add-random-rule" name="type" value="" />RND&nbsp;&nbsp;&nbsp;&nbsp;
									    </label>
									</div>
									
									<div class="radio-inline">
									    <label>
									        <input type="radio" class="add-rule-from-file" name="type" value="" />From File
									    </label>
									</div>	
							      </div>							      
							    </div>	
							    
							    <div class="row file-upload" style="display:none">
							    	<div class="col-lg-10 col-lg-offset-2 col-md-10 col-md-offset-2">
							    		<input type="file" class="file"  data-show-preview="false" />
							    	</div>
							    </div>						   
							    <div class="row file-form" style="display:none">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text2">
							      	<strong>Form:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6" style="padding:10px 0 0 0">
							      	<input type="text" class="form-control" placeholder="s1,s1,s3,s3">							      	
							      </div>	
							      <div class="col-lg-2 col-md-2">
							      	<a class="glyphicon glyphicon-flag"  data-toggle="modal" href="#hint"
							      		style="color:#337ab7; padding:20px 0 0 0;"></a>							      		
							      </div>						      
							    </div>						    							    						    
						    </form>
						  </div>
						</div>
	                
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-primary" data-dismiss="modal">submit</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    
	    
	    
	    
	     <!-- hint -->
	     <div class="modal fade " id="hint" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog modal-sm">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Hint</strong></h4>
	                </div>
	                <div class="modal-body">
	                	<h5><strong>avaiable segments splited by comma: s1,s2,s3,s4,s5</strong></h5>
						<h6>s1 delegates IP adress V4, 4 bytes taken</h6>
						<h6>s2 delegates IP adress V6, 16 bytes taken</h6>
						<h6>s3 delegates MAC adress, 6 bytes taken</h6>
						<h6>s4 delegates port number, 2 bytes taken</h6>
						<h6>s5 delegates protocol number, 1 byte taken</h6>
	                </div>
	               
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
	    
	    
	    
	     <!-- search key -->
	     <div class="modal fade " id="search-key" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	        <div class="modal-dialog ">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title"><strong>Search Key</strong></h4>
	                </div>
	                <div class="modal-body search-key-conf-body">
	                	
	                	<div class="panel panel-default search-key-panel">
						  <div class="panel-heading">
						    <h4 class="panel-title"><span>Key1</span><span type="button" class="glyphicon glyphicon-plus search-key-add" style="float:right"></span></h4>
						  </div>
						  <div class="panel-body" >
						  	<form role="form">
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>Profile ID:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control">
							      </div>							      
							    </div>
							    <div class="row">
							      <div class="col-lg-2 col-lg-offset-2 col-md-2 col-md-offset-2 title-text">
							      	<strong>Width:</strong>
							      </div>
							      <div class="col-lg-6 col-md-6">
							      	<input type="text" class="form-control">
							      </div>							      
							    </div>							   
						    </form>
						  </div>
						</div>                   	
	                	               
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-primary" data-dismiss="modal">submit</button>	                    
	                </div>
	            </div><!-- /.modal-content -->
	        </div><!-- /.modal -->
	    </div> 
	    
	    
        
    </div>
    

    
    
    
    
   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
   <script src="js/jquery-3.2.1.min.js"></script>
   <!-- Include all compiled plugins (below), or include individual files as needed -->
   <script src="js/bootstrap.min.js"></script>
   <script src="js/fileinput.min.js"></script>
   <script type="text/javascript">  	
   		//used for checkbox
		function changeState(el) {
	        if (el.readOnly) el.checked=el.readOnly=false;
	        else if (!el.checked) el.readOnly=el.indeterminate=true;
	    }
	    
	    
	    $(function(){
	    	$("#mode-select ul>li").click(function(event){
	  			//alert($(this).find('a').html());
	  			$(this).parent().parent().prevAll().filter('input').val($(this).find('a').html());
	  			event.preventDefault();
	  		}); 
	  		
	  		
	  		
	  		var en_ad = false;
	  		var en_acl = false;
	  		var en_lpm = false;
	  		$("#mode-next").click(function(){
	  			en_ad = $("#checkbox-ad").get(0).checked;
	  			en_acl = $("#checkbox-acl").get(0).checked;
	  			en_lpm = $("#checkbox-lpm").get(0).checked;
	  			//console.log(en_ad,en_acl,en_lpm);  			
	  			
	  			if($("#mode-select>input").val()==""){
	  				alert("please select one mode!");
	  			}else if(en_ad){
	  				if(!en_acl && !en_lpm){
	  					alert("please at least select one of ACL and LPM!");
	  				}else{
	  					$("#mode-config").modal('hide');
	  					$("#ad-config").modal();
	  				}
	  			}else if(en_acl){
	  				$("#mode-config").modal('hide');
	  				$("#acl-config").modal();
	  			
	  			}else if(en_lpm){
	  				$("#mode-config").modal('hide');
	  				$("#lpm-config").modal();	  			
	  			}else{
	  				alert("please at least select one of ACL and LPM!");	  			
	  			}
	  			
	  			$.ajax({
	  				type:"post",
	  				url:"servlet/ModeServlet",
	  				data:{
	  					nseMode: $("#nseMode").val(),
	  					enAd:  $("#checkbox-ad").prop("checked"),
	  					enAcl: $("#checkbox-acl").prop("checked"),
	  					enLpm: $("#checkbox-lpm").prop("checked")	  					
	  				},
	  				dataType:"json",
	  				success:function(data) {
		                 if(data.success) {
		                    console.log("success");
		                 } else {
		                     alert(data.msg);//打印相应的错误信息
		                 }
		             },
		            error:function(msg) {
		                 console.log("error");
		           }
	  			
	  			});  				  		
	  		});
	  		
	  		
	  		
	  		var ad_table_num = 1;
	  		$(".ad-table-add").click(function(){
	  			$(".ad-table-panel").first().clone(true).appendTo(".ad-table-conf-body");
	  			$(".ad-table-panel").last().find('h4').find('span').first().html("AD Table"+(++ad_table_num));	  		
	  		});
	  		
	  		
	  		$("#ad-next").click(function(event){
	  			if(en_acl){
	  				$("#acl-next").html('Finish');
	  				$("#ad-config").modal('hide');
	  				if(en_lpm){
	  					$("#acl-next").html('next step');
	  					$("#acl-config").modal();
	  				}else{
	  					$("#acl-next").html('Submit');
	  					$("#acl-config").modal();
	  				}		
	  				
	  			}else if(en_lpm){
	  				$("#ad-config").modal('hide');
	  				$("#lpm-config").modal();	  			
	  			}	
	  			
	  			var adJsonArr = new Array();
	  			var adIndex = $('.ad-name').length;
	  			for(var i = 0; i < adIndex; i++){
	  				var temp = {};
	  				temp.name = $('.ad-name').eq(i).val();
	  				temp.baseAddr = $('.ad-baseAddr').eq(i).val();
	  				temp.size = $('.ad-size').eq(i).val();
	  				temp.width = $('.ad-width').eq(i).val();
	  				adJsonArr.push(temp);
	  			}  			
	  			var adJsonStr = JSON.stringify(adJsonArr);
	  			
	  			$.ajax({
	  				type:"post",
	  				url:"servlet/AdServlet",
	  				data:{
	  					adJsonStr : adJsonStr	  					
	  				},
	  				dataType:"json",
	  				success:function(data) {
		                 if(data.success) {
		                    console.log("success");
		                 } else {
		                     alert(data.msg);//打印相应的错误信息
		                 }
		             },
		            error:function(msg) {
		                 console.log("error");
		           }
	  			
	  			});  			
	  			
	  					
	  				
	  		});
	  		
	  		
	  		
	  		var acl_table_num = 1;
	  		$(".acl-table-add").click(function(){
	  			$(".acl-table-panel").first().clone(true).appendTo(".acl-table-conf-body");
	  			$(".acl-table-panel").last().find('h4').find('span').first().html("ACL Table"+(++acl_table_num));		  		
	  		});
	  		
	  		$("#acl-next").click(function(){
	  			if(en_lpm){
	  				$("#acl-config").modal('hide');
	  				$("#lpm-config").modal();	  
	  			}else{
	  				$("#acl-config").modal('hide');
	  				//alert("Finish config Table");
	  			}
	  				
	  			
	  			var aclJsonArr = new Array();
	  			var aclIndex = $('.acl-name').length;
	  			for(var i = 0; i < aclIndex; i++){
	  				var temp = {};
	  				temp.name = $('.acl-name').eq(i).val();
	  				temp.depth = $('.acl-depth').eq(i).val();
	  				temp.width = $('.acl-width').eq(i).val();
	  				temp.adBlock = $('.acl-adBlock').eq(i).val();
	  				aclJsonArr.push(temp);
	  			}  			
	  			var aclJsonStr = JSON.stringify(aclJsonArr);
	  			
	  			$.ajax({
	  				type:"post",
	  				url:"servlet/AclServlet",
	  				data:{
	  					aclJsonStr : aclJsonStr	  					
	  				},
	  				dataType:"json",
	  				success:function(data) {
		                 if(data.success) {
		                    console.log("success");
		                 } else {
		                     alert(data.msg);//打印相应的错误信息
		                 }
		             },
		            error:function(msg) {
		                 console.log("error");
		           }
	  			
	  			});  			
	  			
	  			
	  			
	  			  		
	  		});
	  		
	  		
	  		
	  		var lpm_table_num = 1;
	  		$(".lpm-table-add").click(function(){
	  			$(".lpm-table-panel").first().clone(true).appendTo(".lpm-table-conf-body");
	  			$(".lpm-table-panel").last().find('h4').find('span').first().html("LPM Table"+(++lpm_table_num));		  		
	  		});
	  		
	  		
	  		$("#lpm-next").click(function(){
  				$("#lpm-config").modal('hide');
  				//alert("Finish config Table");
  				var lpmJsonArr = new Array();
	  			var lpmIndex = $('.lpm-name').length;
	  			for(var i = 0; i < lpmIndex; i++){
	  				var temp = {};
	  				temp.name = $('.lpm-name').eq(i).val();
	  				temp.depth = $('.lpm-depth').eq(i).val();
	  				temp.width = $('.lpm-width').eq(i).val();
	  				temp.type = $('.lpm-type').eq(i).val();
	  				lpmJsonArr.push(temp);
	  			}  			
	  			var lpmJsonStr = JSON.stringify(lpmJsonArr);
	  			
	  			$.ajax({
	  				type:"post",
	  				url:"servlet/LpmServlet",
	  				data:{
	  					lpmJsonStr : lpmJsonStr	  					
	  				},
	  				dataType:"json",
	  				success:function(data) {
		                 if(data.success) {
		                    console.log("success");
		                 } else {
		                     alert(data.msg);//打印相应的错误信息
		                 }
		             },
		            error:function(msg) {
		                 console.log("error");
		           }
	  			
	  			});  				  				  		
	  		});	 
	  		
	  		
	  		var profile_num = 1;
	  		$(".profile-add").click(function(){
	  			$(".add-config-profile").last().clone(true).appendTo(".profile-conf-body");	
	  			$(".profile-conf-body").find(".add-config-profile").last().attr("style","display:block");  
	  			$(".profile-conf-body").find(".add-config-profile").last().find('h4').find('span').first().html("Profile"+(++profile_num));			
	  		});
	  		
	  		
	  		$(".detail-profile-add").click(function(event){
	  			if($(this).hasClass("glyphicon-chevron-down")){
	  				$(this).removeClass("glyphicon-chevron-down");
		  			$(this).addClass("glyphicon-chevron-up");
		  			var profile_table_num = $(this).parent().parent().find('input').val();			  			
		  			if($(this).parents(".profile-config-body").find(".add-config-profile-table").length != profile_table_num){
		  				$(this).parents(".profile-config-body").find(".add-config-profile-table").remove();
		  				for(var i = 0; i < profile_table_num; i++){
			  				$(".add-config-profile-table").last().clone(true).appendTo($(this).parents(".profile-config-body"));
			  				$(this).parents(".profile-config-body").find(".add-config-profile-table").last().attr("style","display:block");  
			  				$(this).parents(".profile-config-body").find(".add-config-profile-table").last().find('h6').find('span').first().html("Configure table"+(i+1));	
		  				}		  			
		  			}else{
		  				$(this).parents(".profile-config-body").find(".add-config-profile-table").attr("style","display:block");	  		  			
		  			}
		  			
	  			
	  			}else if($(this).hasClass("glyphicon-chevron-up")){
	  				$(this).removeClass("glyphicon-chevron-up");
		  			$(this).addClass("glyphicon-chevron-down");
	  				$(this).parents(".profile-config-body").find(".add-config-profile-table").attr("style","display:none");	  			
	  			}
	  			
	  		});
	  		
	  		
	  		$("#profile-submit").click(function(){
	  			var profileJsonArr = new Array();
	  			var profileIndex = $('.profile-tb-num').length;
	  			for(var i = 0; i < profileIndex-1; i++){
	  				var tmpProfile = {};
	  				var profileCfgJsonArr = new Array();
	  				var profileCfgs = $('.profile-tb-num').eq(i).parents('.profile-config-body').find('.add-config-profile-table');
	  				var profileCfgIndex = profileCfgs.length;
	  				//console.log("pfc"+profileCfgIndex);
	  				for(var j = 0; j < profileCfgIndex; j++){
	  					var temPfofileCfg = {};
	  					temPfofileCfg.type =  profileCfgs.eq(j).find('.pf-type').eq(0).val();
	  					temPfofileCfg.table =  profileCfgs.eq(j).find('.pf-table').eq(0).val();
	  					temPfofileCfg.channel =  profileCfgs.eq(j).find('.pf-channel').eq(0).val();
	  					temPfofileCfg.kgu =  profileCfgs.eq(j).find('.pf-kgu').eq(0).val();
	  					profileCfgJsonArr.push(temPfofileCfg);
	  				}
	  				tmpProfile.tbnum = $('.profile-tb-num').eq(i).val();
	  				tmpProfile.config = profileCfgJsonArr;
	  				profileJsonArr.push(tmpProfile);  				
	  				
	  			}  			
	  			var pfJsonStr = JSON.stringify(profileJsonArr);
	  			//console.log(pfJsonStr);
	  			$.ajax({
	  				type:"post",
	  				url:"servlet/ProfileServlet",
	  				data:{
	  					pfJsonStr : pfJsonStr	  					
	  				},
	  				dataType:"json",
	  				success:function(data) {
		                 if(data.success) {
		                    console.log("success");
		                 } else {
		                     alert(data.msg);//打印相应的错误信息
		                 }
		             },
		            error:function(msg) {
		                 console.log("error");
		           }
	  			
	  			});  				  	
	  		
	  		
	  		});
	  		
	  		
	  		
	  		$(".add-inc-rule").click(function(){
	  			$(this).parents('.row').nextAll().filter('.file-upload').attr("style","display:none"); 
	  			$(this).parents('.row').nextAll().filter('.file-form').attr("style","display:none"); 
	  		});
	  		
	  		
	  		$(".add-random-rule").click(function(){
	  			$(this).parents('.row').nextAll().filter('.file-upload').attr("style","display:none"); 
	  			$(this).parents('.row').nextAll().filter('.file-form').attr("style","display:block");  		
	  		});
	  		
			$(".add-rule-from-file").click(function(){
				$(this).parents('.row').nextAll().filter('.file-upload').attr("style","display:block"); 
	  			$(this).parents('.row').nextAll().filter('.file-form').attr("style","display:none");  			
			});
	  		
	  		
	  		var rule_num = 1;
	  		$(".rule-add").click(function(){
	  			$(".rule-panel").first().clone(true).appendTo(".add-rules-conf-body");
	  			$(".rule-panel").last().find('h4').find('span').first().html("Rule"+(++rule_num));	
	  			$(".add-inc-rule").last().trigger('click');
	  		});
	  		
	  		var search_key_num = 1;
	  		$(".search-key-add").click(function(){
	  			$(".search-key-panel").first().clone(true).appendTo(".search-key-conf-body");
	  			$(".search-key-panel").last().find('h4').find('span').first().html("Key"+(++search_key_num));	  		
	  		});	
	  		
	  		
	  		
	  		
	  		$('#gen-app').on('shown.bs.modal', function (e) {
	  			
			    $.ajax({
	  				type:"post",
	  				url:"servlet/GenAppServlet",
	  				data:{
	  					 	  					
	  				},
	  				dataType:"json",
	  				success:function(data) {
		                 if(data.success) {
		                    console.log("success");
		                    var $node = $('.loading-pg').parent('.modal-body');
		                    $node.empty();
		                    var fileName = data.filename;
		                    var filePath = "NseApplication/"+fileName+".c";
		                    var content = "<a href=\"" + filePath +"\" " + "download=\"" + fileName + ".c\">" + fileName +"</a>";
		                    $('<span>Please click '+content +' to download.<span>').appendTo($node);		                    
		                    
		                 } else {
		                     alert(data.msg);//打印相应的错误信息
		                 }
		             },
		            error:function(msg) {
		                 console.log("error");
		           }
	  			
	  			});  				
			});  		 		
	  		
	  		    
	    });
	    
	    
	    
	    
	    
	  	
   
   </script>
  </body>
</html>
