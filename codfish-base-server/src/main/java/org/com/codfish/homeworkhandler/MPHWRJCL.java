package org.com.codfish.homeworkhandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.com.codfish.common.SnowflakeIdGenerator;
import org.com.codfish.common.SqlCon;
import org.com.codfish.servlet.ErrReturnObj;

import com.google.gson.Gson;

/**
 * 注册班级
 */
public class MPHWRJCL {
	public static void run(String requestBody, HttpServletResponse response) {
		Gson gsinput = new Gson();
		ApiInput apiInput = gsinput.fromJson(requestBody, ApiInput.class);
		List<ObjClass> classList =  HwSql.getClassList("schoolName", apiInput.getSchoolName(), apiInput.getClassName(), apiInput.getTeacherId(), "", "");
		System.out.println("classList . size" + classList.size());
		if (classList.size() > 0) { // 如果已经注册过班级就不让注册了
			String errCode = "SERV0001";
			String ErrMsg = "班级重复注册";
			HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
		} else {
			SqlCon sc; // 注册班级
			try {
				sc = new SqlCon();
				String insertQuery = "INSERT INTO t_hw_class_inf "
						+ "(classId, schoolName, className, teacherId, createTime, modifyTime, startTime, endTime, stt) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStatement = sc.getConnection().prepareStatement(insertQuery);
				// 设置参数值
				SnowflakeIdGenerator worker = new SnowflakeIdGenerator(1, 1, 1);
				String tempClassId = String.valueOf(worker.nextId());
				preparedStatement.setString(1, tempClassId); // 班级唯一ID 随机创建
				preparedStatement.setString(2, apiInput.getSchoolName()); // 学校名称 输入
				preparedStatement.setString(3, apiInput.getClassName()); // 班级名称 输入
				preparedStatement.setString(4, apiInput.getTeacherId()); // 班主任openid 输入
				preparedStatement.setString(5, HwUtils.timeMaker("currentTime")); // 创建时间，当前时间
				preparedStatement.setString(6, HwUtils.timeMaker("currentTime")); // 修改时间 去当前时间
				preparedStatement.setString(7, HwUtils.timeMaker("semeStart")); // 开始时间
				preparedStatement.setString(8, HwUtils.timeMaker("semeEnd")); // 结束时间
				preparedStatement.setString(9, "1");

				// 执行 插入数据
				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					// TODO注册结果通知，接口返回
					ErrReturnObj err = new ErrReturnObj();
					err.setRetcode("00000000");
					err.setRetmsg("班级注册成功");
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
		private String schoolName;
		private String className;
		private String teacherId;
		public String getSchoolName() {
			return schoolName;
		}
		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public String getTeacherId() {
			return teacherId;
		}
		public void setTeacherId(String teacherId) {
			this.teacherId = teacherId;
		}
	}

	class ApiOutput {

	}
}
