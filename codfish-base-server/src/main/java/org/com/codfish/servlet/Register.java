package org.com.codfish.servlet;

import java.sql.ResultSet;

import org.com.codfish.common.SnowflakeIdGenerator;
import org.com.codfish.common.SqlBase;

public class Register {
	public static void register(String params) {
		SnowflakeIdGenerator worker = new SnowflakeIdGenerator(1,1,1);
    	String unionid = String.valueOf(worker.nextId());
		try {
			SqlBase conn = new SqlBase();
//			String qub = "SELECT * FROM t_pu_cstinfo";
//			ResultSet resultb = conn.query(qub);
			
//			while (resultb.next()) {
//				System.out.println(resultb.getString(1));
//			}
			String sql = "INSERT INTO t_pu_cstinfo (unionid, mobile, password,channel,subchannel,system,stt,createtm,modifytm) "
					+ "VALUES ("
					+ unionid + ","
					+ "12345678901"+ ","
					+ "1234567890"+ ","
					+ "1"+ ","
					+ "2"+ ","
					+ "1"+ ","
					+ "1"+ ","
					+ "1"+ ","
					+ "1"
					+ ");";
			System.out.println("sql:\n"+sql);
			conn.insert(sql);
			System.out.println("result:\n");
			
			String qua = "SELECT * FROM t_pu_cstinfo";
			System.out.println("sql:\n"+qua);
			ResultSet resulta = conn.query(qua);
			while (resulta.next()) {
				System.out.println(resulta.getString(1));
			}
			conn.closeConn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
