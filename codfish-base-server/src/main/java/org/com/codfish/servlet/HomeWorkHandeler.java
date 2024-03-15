package org.com.codfish.servlet;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.com.codfish.common.SqlCon;

import com.google.gson.Gson;

/**
 * 鳕鱼的小工具，注册班级接口
 * 
 */
public class HomeWorkHandeler {
	// 注册班级
	public static void registerClass (String requestBody, HttpServletResponse response) {
		Gson gsinput = new Gson();
		ClassObj jsonElementInput = gsinput.fromJson(requestBody, ClassObj.class);
		int totalCount = 0; // 已经注册并且状态正常的班级数量
		try {
			SqlCon sc = new SqlCon();
			// 查询class
//			String querySql = 
//					"SELECT * FROM t_hw_class_inf WHERE telephone = " + telephone
//					+ " AND majorTeacherOpenid = " + majorTeacherOpenid
//					+ " AND " + time +" >= startTime"
//					+ " AND " + time +" <= endTime"
//					+ " AND stt=" + stt + ";";
			String querySql = 
			"SELECT COUNT(*) FROM t_hw_class_inf WHERE telephone = " + jsonElementInput.getTelephone()
			+ " AND majorTeacherOpenid = " + jsonElementInput.getMajorTeacherOpenid()
			+ " AND " + jsonElementInput.getCreateTime() +" >= startTime"
			+ " AND " + jsonElementInput.getCreateTime() +" <= endTime"
			+ " AND stt=" + jsonElementInput.getStt() + ";";
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				totalCount = rs.getInt(1);
			}
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		if (totalCount > 0) { // 超过5个就不让注册了
			ErrReturnObj err = new ErrReturnObj();
			err.setRetcode("SERV0001");
			err.setRetmsg("大哥您已经注册超过5个班级了，您忙的过来吗？您忙的过来鳕鱼也忙不过来了，咱少注册一个哈");
			Gson gson = new Gson();
			String jsonStr = gson.toJson(err);
			try {
				publicReturn(response, jsonStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			SqlCon sc;
			try {
				sc = new SqlCon();

				String insertQuery = "INSERT INTO t_hw_class_inf " +
	                    "(classId, schoolName, className, majorTeacher, majorTeacherOpenid, telephone, createTime, modifyTime, startTime, endTime, stt) " +
	                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStatement = sc.getConnection().prepareStatement(insertQuery);
	
		         // 设置参数值
		         preparedStatement.setString(1, jsonElementInput.getClassId()); // 班级唯一ID 随机创建
		         preparedStatement.setString(2, jsonElementInput.getSchoolName()); // 学校名称 输入
		         preparedStatement.setString(3, jsonElementInput.getClassName()); // 班级名称 输入
		         preparedStatement.setString(4, jsonElementInput.getMajorTeacher()); // 班主任名称 输入
		         preparedStatement.setString(5, jsonElementInput.getMajorTeacherOpenid()); // 班主任openid 输入
		         preparedStatement.setString(6, jsonElementInput.getTelephone()); // 班主任手机号 - 输入
		         preparedStatement.setString(7, jsonElementInput.getCreateTime()); // 创建时间，当前时间
		         preparedStatement.setString(8, jsonElementInput.getCreateTime()); // 修改时间 去当前时间
		         preparedStatement.setString(9, jsonElementInput.getCreateTime()); // 开始时间，取当前时间
		         preparedStatement.setString(10, jsonElementInput.getCreateTime()); // 结束时间，取当年或明年8月31
		         preparedStatement.setString(11, "1");
	
		         // 执行插入操作
		         int rowsInserted = preparedStatement.executeUpdate();
		         if (rowsInserted > 0) {
		             System.out.println("A new record was inserted successfully.");
		         }
		         sc.closeConn();
		         preparedStatement.close();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//修改班级信息
	static void modifyClass () {
		
	}
	// 班级添加老师
	static void addTeacher () {
		
	}
	// 班级添加学生openid
	static void addStudent () {
		
	}
	// 发布作业
	static void publicHomeWork () {
		
	}
	// 修改作业
	static void modifyHomeWork () {
		
	}
	// 查看作业
	static void queryHomwWork () {
		
	}
	
	static void run (String requestBody) {
		System.out.println("CFMPRGCL run");
		checkUserDB("123");
	}
	// 校验用户输入数据有效性
	Boolean checkInput () {
		return true;
	}
	
	// 处理return 结果
	
	static void publicReturn (HttpServletResponse response, String result) throws IOException {
		response.getWriter().append(result);
	}

	// 校验是否已经注册
	static Boolean checkUserDB (String id) {
		Boolean hasReg = false;
		try {
			SqlCon sc = new SqlCon();
			String querySql = "SELECT * FROM t_class_uerinfo";
			ResultSet result = sc.query(querySql);
			while (result.next()) {
				if (id.equals(result.getString(1))) {
					hasReg = true;
				}
			}
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		System.out.println("checkUserDB\t" + hasReg);
		return hasReg;
	}

	// 发起注册
	void register () {
		
	}

	// 公共返回结果
	void returnResult () {
		
	}
}

class ClassObj {
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
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
	public String getMajorTeacher() {
		return majorTeacher;
	}
	public void setMajorTeacher(String majorTeacher) {
		this.majorTeacher = majorTeacher;
	}
	public String getMajorTeacherOpenid() {
		return majorTeacherOpenid;
	}
	public void setMajorTeacherOpenid(String majorTeacherOpenid) {
		this.majorTeacherOpenid = majorTeacherOpenid;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStt() {
		return stt;
	}
	public void setStt(String stt) {
		this.stt = stt;
	}
	String classId;
	String schoolName;
	String className;
	String majorTeacher;
	String majorTeacherOpenid;
	String telephone;
	String createTime;
	String modifyTime;
	String startTime;
	String endTime;
	String stt;
}

class TeacherObj {
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	String classId;
	String teacher;
	String teacherName;
	String telephone;
	String program;
	String openId;
}

class studentObj {
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	String classId;
	String openId;
}

class HomeWorkObj {
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getHomeWork() {
		return homeWork;
	}
	public void setHomeWork(String homeWork) {
		this.homeWork = homeWork;
	}
	public String getHomeworkId() {
		return homeworkId;
	}
	public void setHomeworkId(String homeworkId) {
		this.homeworkId = homeworkId;
	}
	String classId;
	String program;
	String createTime;
	String startTime;
	String endTime;
	String homeWork;
	String homeworkId;
	
}
