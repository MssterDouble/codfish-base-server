package org.com.codfish.common;
import java.sql.*;


public class SqlCon {
	private String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private String CODFISH_DATABASE_URL = "jdbc:mysql://60.205.125.66:3306/codfish_base_db";
	private String CODFISH_DATABASE_NAME = "root";
	private String CODFISH_DATABASE_PWD = "O4cV0c-S5h%e";
	
	private ResultSet res = null;
	private Connection conn = null;
	private Statement stmt = null;
	
	public SqlCon () throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(CODFISH_DATABASE_URL, CODFISH_DATABASE_NAME, CODFISH_DATABASE_PWD);
		System.out.println("SqlCon init:\n");
	}
	
	public ResultSet query (String querySql) throws SQLException {
		stmt = conn.createStatement();
		res = stmt.executeQuery(querySql);
		return res;
	}
	public Connection getConnection () {
		return conn;
	}
	public void closeConn () throws SQLException {
        if (res != null) {
        	res.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (conn != null) {
            conn.close();
        }
	}
}
