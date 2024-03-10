package org.com.codfish.common;

import java.sql.*;

public class SqlBase {
	private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final static String CODFISH_DATABASE_URL = "jdbc:mysql://60.205.125.66:3306/codfish_base_db";
	private final static String CODFISH_DATABASE_NAME = "root";
	private final static String CODFISH_DATABASE_PWD = "O4cV0c-S5h%e";
	private static ResultSet res = null;
	private static Connection conn = null;
	private static Statement stmt = null;
	public static ResultSet query (String sql) {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(CODFISH_DATABASE_URL, CODFISH_DATABASE_NAME, CODFISH_DATABASE_PWD);
			stmt = conn.createStatement();
			res = stmt.executeQuery(sql);
		} catch (Exception e) {
			System.out.println("conn database error:\n" + e);
		}
		return res;
	}
	public static void insert (String sql) {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(CODFISH_DATABASE_URL, CODFISH_DATABASE_NAME, CODFISH_DATABASE_PWD);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("conn database error:\n" + e);
		}
		
	}
	public static void closeConn () {
        try {
            if (res != null) {
            	res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
        	System.out.println("close database error");
        }
	}
}
