package org.com.codfish.homeworkhandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.com.codfish.common.SqlCon;
import org.com.codfish.servlet.ErrReturnObj;

import com.google.gson.Gson;

/**
 * 学生加入班级
 */
public class MPHWADCS {
	public static void run (String requestBody, HttpServletResponse response) {
		// 输入转json转对象
		Gson gsinput = new Gson();
		ApiInput apiInput = gsinput.fromJson(requestBody, ApiInput.class);
		
		// 加入的班级状态不正常，不允许注册
		List<ObjClass> classSttList = HwSql.getTeachListByClassId(apiInput.getClassId());
		if (classSttList == null) {
			HwHttps.hwHttpResponeErr(response);
			return ;
		}
		
		// 根据stuid 查询全部classId
		List<ObjClass> classList = null;
		List<String> stuIdList = new ArrayList<>();
		String querySql = "SELECT * FROM t_hw_stu_inf WHERE " 
				+ "stuendId='" + apiInput.getStudentId() + "'"
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		System.out.println("querySql:\t" + querySql);
		
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				stuIdList.add(rs.getString(1));
			}
			rs.close();
			sc.closeConn();
			String strStuIdList = String.join(",", stuIdList);
			if (stuIdList.size() > 0) {
				// 根据classId合集查询 全部classId的状态正常的
				String querySqlClass = "SELECT * FROM t_hw_class_inf WHERE classId IN (" + strStuIdList + ")" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime"
						+ " AND stt='1';";
				classList = HwSql.getClassListBySql(querySqlClass);	
			} else {
				
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		if (classList.size() > 10) { // 如果这个stuid 对应的classId 已经超过10个了，就不要在加入了
			String errCode = "SERV0003";
			String ErrMsg = "加入班级失败，已经加入过班级了";
			HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
		} else {
			try {
				SqlCon sc = new SqlCon();
				String insertQuery = "INSERT INTO t_hw_stu_inf "
						+ "(studentId, classId, createTime, modifyTime, startTime, endTime, stt) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStatement = sc.getConnection().prepareStatement(insertQuery);
				preparedStatement.setString(1, apiInput.getStudentId()); // 学校名称 输入
				preparedStatement.setString(2, apiInput.getClassId()); // 班级名称 输入
				preparedStatement.setString(3, HwUtils.timeMaker("currentTime")); // 创建时间，当前时间
				preparedStatement.setString(4, HwUtils.timeMaker("currentTime")); // 修改时间 去当前时间
				preparedStatement.setString(5, HwUtils.timeMaker("semeStart")); // 开始时间
				preparedStatement.setString(6, HwUtils.timeMaker("semeEnd")); // 结束时间
				preparedStatement.setString(7, "1");

				// 执行 插入数据
				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					// TODO注册结果通知，接口返回
					ErrReturnObj err = new ErrReturnObj();
					err.setRetcode("00000000");
					err.setRetmsg("加入班级成功");
					Gson gson = new Gson();
					String jsonStr = gson.toJson(err);
					HwHttps.hwHttpRespone(response, jsonStr);
				}
				sc.closeConn();
				preparedStatement.close();

			} catch (ClassNotFoundException | SQLException e) {
				String errCode = "SERV0002";
				String ErrMsg = "注册失败系统内部错误";
				HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
			}
		}
	}
	class ApiInput {
		private String studentId;
		public String getStudentId() {
			return studentId;
		}
		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}
		public String getClassId() {
			return classId;
		}
		public void setClassId(String classId) {
			this.classId = classId;
		}
		private String classId;	
	}

	class ApiOutput {
		
	}
}
