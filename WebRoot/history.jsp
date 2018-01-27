<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">

    <title>My JSP 'history.jsp' starting page</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css">
	<link rel="stylesheet" type="text/css" href="css/bootstrap-table.css">
    <style>
        body {
        padding-top: 50px;
        }
        .del-record{
        	padding: 1px 6px 1px 6px;
        }
    </style>

  </head>

  <body>
    <%
  		if(session.getAttribute("loginUser") == null){
  			response.sendRedirect("index.jsp");
  		}

  	 %>




    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
    <a class="navbar-brand " href="main.jsp" ">Auto-gen System</a>
    </div>

    </div>
    <!-- /.container-fluid -->
    </nav>






    <div class="container body-content" style="padding-top:20px;">
        <div class="panel panel-default">
    <div class="panel-heading"><h4><strong>Query Criteria</strong></h4></div>
             <div class="panel-body">
                 <form class="form-inline">
                    <div class="row">
                        <div class="col-sm-4">
                             <label class="control-label">User Name：</label>
                             <input id="userName" type="text" class="form-control" value="<%=session.getAttribute("loginUser") %>" readonly>
                        </div>
                        <div class="col-sm-4">
                            <label class="control-label">Begin Date：</label>
                            <div class="input-group date" id="begin_datetime">
                                <input id="beginDate" type="text" class="form-control" value="">
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-th"></span>
                                </span>
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <label class="control-label">End Date：</label>
                            <div class="input-group date" id="end_datetime">
                                <input id="endDate" type="text" class="form-control" value="">
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-th"></span>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="row text-right" style="margin-top:20px;">
                        <div class="col-sm-12">
                            <input class="btn btn-primary" type="button" value="Search" id="search-table">
                        </div>
                    </div>
                </form>
             </div>
         </div>

        <table id="table"></table>
	 	<nav class="navbar  navbar-fixed-bottom" role="navigation">
	    <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	       <hr class="divider"> 
	       <footer>        
	        <p class="pull-right">Copyright &copy; 2017-2018 <strong>Corigine</strong> </p>
	       </footer>
	
	    </div>
	    <!-- /.container-fluid -->
	    </nav>
    </div>


    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery-3.2.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>
    <script src="js/bootstrap-table.js"></script>
    <script src="js/moment-with-locales.min.js"></script>
    <script src="js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript">
        $("#begin_datetime").datetimepicker({
            format: "YYYY-MM-DD",
            locale: moment.locale('en')
        });
        $("#end_datetime").datetimepicker({
            format: "YYYY-MM-DD",
            locale: moment.locale('en')
        });
        
        function initTable(){
        	 $('#table').bootstrapTable('destroy');
        	 
        	 $('#table').bootstrapTable({
	            url: 'servlet/HistoryServlet',
	            method: 'get',
	            dataType: "json",//期待返回数据类型
	            striped: true,
	            cache: false,
	            pagination: true,
	            sortable: true,
	            sortOrder: "asc",
	            //queryParamsType: 'undefined', //类型
	            queryParams: queryParams,
	            sidePagination: 'client', //服务端分页
	            pageNumber: 1, //初始化加载第一页，默认第一页
	            pageSize: 6, //每页的记录行数（*）
	            pageList: [5, 10, 15,20, 25],
	            //strictSearch: true,
	            clickToSelect: true,                //是否启用点击选中行
	            height: 398,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	            contentType: "application/x-www-form-urlencoded",
	            search: true,
	            showColumns: true,  //显示隐藏列
	            showRefresh: true,  //显示刷新按钮
	            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
	            cardView: false,                    //是否显示详细视图
	            detailView: false,                   //是否显示父子表
	
	            columns:[
	            {field:'index',title:'Index',align:'center'},
	            {
	            	field:'appName',
	            	title:'App Name',
	            	align:'center',
	            	formatter:function(value,row,index){
	            		var fileName = value;
		                var filePath = "NseApplication/"+fileName+".c";
		                var content = "<a href=\"" + filePath +"\" " + "download=\"" + fileName + ".c\">" + fileName +"</a>";
		                return content;	            	
	            	}
	            },
	            {field:'ruleName',title:'Rule Name',align:'center'},
	            {field:'searchKeyName',title:'Search-key Name',align:'center'},
	            {field:'createDate',title:'Create Date',align:'center'},
	            //{field:'id',title:'ID',align:'center',visible:false}
	            {
	            	field:'id',
	             	title:'Operation',
	             	align:'center',
	             	formatter: function(value,row,index){
	             		var html = "<button type='button' class='btn btn-primary del-record' value='"+value+"' onclick='deleteRec("+value+")'>delete</button>"
	             		//var html = "<button type='button' class='btn btn-primary del-record' value='"+value+"' id='aaa'>delete</button>"
	             		return html;
	             	}
	             }
	            
	            ]
	
	
	        });
        	 
        
        }
        
        
        
        //查询条件
        
        function queryParams(params) {
            //alert('params.limit='+params.limit+' params.offset='+params.offset);
            return {
                //pageSize: params.pageSize,
                //pageIndex: params.pageNumber,
                
                limit: params.limit, //页面大小
   				offset: params.offset, //页码
                userOwner: $.trim($("#userName").val()),
                beginDate: $.trim($("#beginDate").val()),
                endDate: $.trim($("#endDate").val())
            };
        }
        
        
        
        function deleteRec(value){
        	//alert(value);      
        	//$("input[name='newsletter']").attr("checked", true);
        	 
        	$.ajax({
	  				type:"post",
	  				url:"servlet/DelHistoryRecServlet",
	  				data:{
	  					delRecordId: value	  						  					
	  				},
	  				dataType:"json",
	  				success:function(data) {
		                 if(data.success) {
		                    console.log("success");
		                    $("tr[data-uniqueid='"+value+"']").remove(); 
		                 } else {
		                     alert(data.msg);//打印相应的错误信息
		                 }
		             },
		            error:function(msg) {
		                 console.log("error");
		           }
	  			
	  			});  				  
        	
        }
        
        
    $(function(){   
		//调用函数，初始化表格  
          initTable();

        //查询事件
        //function SearchData() {
            //$('#table').bootstrapTable('refresh');
            //$('#table').bind("click",initTable);
        //}
        
        $('#search-table').click(function(){
        	initTable();        
        });
        
 		$('#aaa').click(function(){
 			alert("aaaa");
 		});

    });


    </script>

  </body>
</html>
