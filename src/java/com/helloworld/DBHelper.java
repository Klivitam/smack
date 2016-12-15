package com.helloworld;

import java.sql.DriverManager;
import java.sql.SQLException;



public class DBHelper {
	public static final String url = "jdbc:mysql://127.0.0.1/openfire";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "jkb534726781";
	
	public java.sql.Connection conn = null;
	public java.sql.PreparedStatement pst = null;
	public DBHelper(String sql){
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			pst = conn.prepareStatement(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			this.conn.close();
			this.pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
