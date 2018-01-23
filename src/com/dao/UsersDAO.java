package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.po.Users;
import com.util.DBHelper;

//�û���ҵ���߼���
public class UsersDAO {
	
	//�ж��û��Ƿ�Ϸ�
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
	
	
	// ������е��û���Ϣ
	public ArrayList<Users> getAllUsers() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Users> uList = new ArrayList<Users>(); // �û�����
		DBHelper dbHelper = new DBHelper();
		try {
			conn = dbHelper.getConnection();
			String sql = "SELECT * FROM users;"; // sql���
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Users user = new Users();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				
				uList.add(user); 
			}
			return uList; // ���ؼ���
		} catch (Exception ex) {
			System.out.println("fail to connect sql!");
			ex.printStackTrace();
			return null;
		} finally {
			// �ͷ����ݼ�����
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
	
			}
			// �ͷ�������
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
