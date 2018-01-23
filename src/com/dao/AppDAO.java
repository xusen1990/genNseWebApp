package com.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.po.App;
import com.util.DBHelper;



public class AppDAO {
	
	
	public AppDAO(){}
	
	public List<App> query(String userName, String beginDate, String endDate) throws Exception{
		DBHelper dbHelper = new DBHelper();
		Connection conn = dbHelper.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, appName,ruleName,searchKeyName FROM genApp_history"
											+" WHERE userName=" +"\'"+userName+"\'"
											+"AND (createdate between "
											+"\'"+beginDate+"\'"
											+"and"
											+"\'"+endDate+"\')"
				
										);
		ArrayList<App> appList = new ArrayList<App>(); // app list
		App app = null;
		while(rs.next()){
			app = new App();
			app.setId(rs.getInt("id"));
			app.setAppName(rs.getString("appName"));
			app.setRuleName(rs.getString("ruleName"));
			app.setSearchKeyName(rs.getString("searchKeyName"));
			appList.add(app);
		}
		
		if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();   
        }
        
        if (conn != null) {
            conn.close();   
        }		
		return appList;
	}
	
	
	
	
	public void addAppHistory(App app) throws SQLException{
		DBHelper dbHelper = new DBHelper();
		Connection conn = dbHelper.getConnection();
		String sql = ""+
				"insert into genApp_history"+
				"(userName,appName,ruleName,searchKeyName,createdate)"+
				"values("+
				"?,?,?,?,now())";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		
		ptmt.setString(1, app.getUserName());
		ptmt.setString(2, app.getAppName());
		ptmt.setString(3, app.getRuleName());
		ptmt.setString(4, app.getSearchKeyName());
		
		ptmt.execute();	
		
	
        if (ptmt != null) {
        	ptmt.close();   
        }
        
        if (conn != null) {
            conn.close();   
        }		
		
		
	}
	
	
	public void delApp(int id) throws SQLException{
		DBHelper dbHelper = new DBHelper();
		Connection conn = dbHelper.getConnection();
		String sql = "delete from genApp_history " +
					 "where id=?";
		PreparedStatement ptmt = conn.prepareStatement(sql);	
		ptmt.setInt(1, id);	
		ptmt.execute();
		if (ptmt != null) {
        	ptmt.close();   
        }
        
        if (conn != null) {
            conn.close();   
        }		
	} 
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		AppDAO appDAO = new AppDAO();
		List<App> appList = appDAO.query("test4","2017-01-01 00:00:00","2018-12-01 00:00:00");
		for(App app: appList){
					
			System.out.println(app.getAppName()+","+app.getRuleName());
		}
		
		
		App app1 = new App();
		app1.setUserName("test5");
		app1.setAppName("nseApp_test");
		app1.setRuleName("rule_test");
		app1.setSearchKeyName("searchkey_test");
		AppDAO appDAO2 = new AppDAO();
		appDAO2.addAppHistory(app1);
		
		
		AppDAO appDao = new AppDAO();
		appDao.delApp(13);
		
	}
	
	
	
	
	

}
