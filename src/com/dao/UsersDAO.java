package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.po.Users;
import com.util.DBHelper;

//用户的业务逻辑类
public class UsersDAO {
	
	//判断用户是否合法
	public boolean usersLogin(Users u){
		
		ArrayList<Users> uList = getAllUsers();
		if(uList != null && uList.size() > 0){
			for(Users userSQL : uList){
				if(userSQL.getUsername().equals(u.getUsername())
				   && userSQL.getPassword().equals(u.getPassword())){
					return true;				
				}		
			}		
			return false;		
		}else{
			return false;			
		}
		
	}
	
	
	// 获得所有的用户信息
	public ArrayList<Users> getAllUsers() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Users> uList = new ArrayList<Users>(); // 用户集合
		DBHelper dbHelper = new DBHelper();
		try {
			conn = dbHelper.getConnection();
			String sql = "SELECT * FROM users;"; // sql语句
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Users user = new Users();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				
				uList.add(user); 
			}
			return uList; // 返回集合
		} catch (Exception ex) {
			System.out.println("fail to connect sql!");
			ex.printStackTrace();
			return null;
		} finally {
			// 释放数据集对象
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
	
			}
			// 释放语句对象
			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
}
