package org.com.codfish.homeworkhandler;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.com.codfish.common.SnowflakeIdGenerator;
import org.com.codfish.common.SqlCon;
import org.com.codfish.servlet.ErrReturnObj;

import com.google.gson.Gson;

/**
 * 鳕鱼的小工具，注册班级接口
 * 
 */
public class HandelerHomework {
	static ObjClass classObj;
	static ObjTeacher teachObj;
	static ObjStudent stuObj;
	static ObjHomeWork hwObj;
//	static CFMPRGCL CFMPRGCL; // 注册班级接口对象
	static void initObj (String requestBody) {
		Gson gsinput = new Gson();
		MPHWRJCL CFMPRGCL = gsinput.fromJson(requestBody, MPHWRJCL.class);
		System.out.println("CFMPRGCL" + CFMPRGCL.toString());
//		classObj = gsinput.fromJson(requestBody, ClassObj.class);
//		teachObj = gsinput.fromJson(requestBody, TeacherObj.class);
//		stuObj = gsinput.fromJson(requestBody, StudentObj.class);
//		hwObj = gsinput.fromJson(requestBody, HomeWorkObj.class);

//		 System.out.println("classObj" + classObj);
//		 System.out.println("teachObj" + teachObj);
//		 System.out.println("stuObj" + stuObj);
	}
	// 注册班级
	public static void registerClass (String requestBody, HttpServletResponse response) {
		// api 输入参数json转class
		Gson gsinput = new Gson();
		MPHWRJCL CFMPRGCL = gsinput.fromJson(requestBody, MPHWRJCL.class);
		CFMPRGCL.setClassId(); // 初始化班级ID
		System.out.println("CFMPRGCL" + CFMPRGCL.toString());
		
//		ClassObj classObj = gsinput.fromJson(requestBody, ClassObj.class);
		// 获取班级是否重复注册
		List classList = getClassList("schoolName",CFMPRGCL.getSchoolName(), CFMPRGCL.getClassName(), CFMPRGCL.getTeacherId(), "");
		
		System.out.println("query classList res\n" + classList + classList.size());
		if (classList.size() > 0) { // 超过5个就不让注册了
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
		} else { // 注册班级
			SqlCon sc;
			try {
				sc = new SqlCon();
				String insertQuery = "INSERT INTO t_hw_class_inf " +
	                    "(classId, schoolName, className, teacherId, createTime, modifyTime, startTime, endTime, stt) " +
	                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStatement = sc.getConnection().prepareStatement(insertQuery);
		         // 设置参数值
		         preparedStatement.setString(1, CFMPRGCL.getClassId()); // 班级唯一ID 随机创建
		         preparedStatement.setString(2, CFMPRGCL.getSchoolName()); // 学校名称 输入
		         preparedStatement.setString(3, CFMPRGCL.getClassName()); // 班级名称 输入
		         preparedStatement.setString(4, CFMPRGCL.getTeacherId()); // 班主任openid 输入
		         preparedStatement.setString(5, timeMaker("currentTime")); // 创建时间，当前时间
		         preparedStatement.setString(6, timeMaker("currentTime")); // 修改时间 去当前时间
		         preparedStatement.setString(7, timeMaker("semeStart")); // 开始时间
		         preparedStatement.setString(8, timeMaker("semeEnd")); // 结束时间
		         preparedStatement.setString(9, "1");
	
		         // 执行 插入数据
		         int rowsInserted = preparedStatement.executeUpdate();
		         if (rowsInserted > 0) {
		        	 // TODO注册结果通知，接口返回
		         }
		         sc.closeConn();
		         preparedStatement.close();
		         
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	 			ErrReturnObj err = new ErrReturnObj();
				err.setRetcode("SERV0002");
				err.setRetmsg("注册失败系统内部错误");
				Gson gson = new Gson();
				String jsonStr = gson.toJson(err);
				try {
					publicReturn(response, jsonStr);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	static List getClassList (String type, String schoolName, String className, String teacherId, String classId) {
		List<ObjClass> classObjList = new ArrayList<>();
		// 查询class 状态正常的班级
		String querySql = null;
		if (type == "schoolName") { // 根据校+班级名称查找状态正常的班级
			querySql = 
					"SELECT * FROM t_hw_class_inf WHERE schoolName='" + classObj.getSchoolName() + "'"
					+ " AND className='" + classObj.getClassName() + "'"
					+ " AND '" + timeMaker("currentDate") +"' >= startTime"
					+ " AND '" + timeMaker("currentDate") +"' <= endTime"
					+ " AND stt='1';";	
		} else if (type == "classId") { // 根据班级ID查找班级信息
			querySql = 
					"SELECT * FROM t_hw_class_inf WHERE classId='" + classId + "'"
					+ " AND '" + timeMaker("currentDate") +"' >= startTime"
					+ " AND '" + timeMaker("currentDate") +"' <= endTime"
					+ " AND stt='1';";
		} else if (type == "all" ) { // 根据teacherId查找班级信息
			querySql = "SELECT * FROM t_hw_class_inf WHERE teacherId='" + teacherId + "';";
		}
		System.out.println("querySql\n" + querySql);
		try {
			SqlCon sc = new SqlCon();	
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				ObjClass classObj= new ObjClass();
				classObj.setClassId(rs.getString(1));
				classObj.setSchoolName(rs.getString(2));
				classObj.setClassName(rs.getString(3));
				classObj.setTeacherId(rs.getString(4));
				classObj.setCreateTime(rs.getString(5));
				classObj.setModifyTime(rs.getString(6));
				classObj.setStartTime(rs.getString(7));
				classObj.setEndTime(rs.getString(8));
				classObj.setStt(rs.getString(9));
				classObjList.add(classObj);
				System.out.println("querySql res\t" + rs.getString(1));
			}
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return classObjList;
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
	// 公共 - 时间方法 - 格式8/16位数字
	static String timeMaker (String type) {
		String retTime = null;
		SimpleDateFormat sdf;
		Date date = new Date();
		if (type == "currentDate") { // 获取当前日期
			sdf = new SimpleDateFormat("yyyyMMdd");
			retTime = sdf.format(date);
		} else if (type == "currentTime") { // 获取当前时间
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			retTime = sdf.format(date);	
		} else if (type == "semeStart") { // 学期开始
			retTime = semeTime("start", date);
		} else if (type == "semeEnd") { // 学期结束
			retTime = semeTime("end", date);
		}
		return retTime;
	}
	
	// 计算当前日期的学期开始结束时间 格式 8 数字
	static String semeTime (String type, Date now) {
		// 获取当前年字符串
	    SimpleDateFormat sdfNowYear = new SimpleDateFormat("yyyy");
	    String nowYear = sdfNowYear.format(now);
	    // 将字符串转换为整数
	    int numNowYear = Integer.parseInt(nowYear);
	    String currYear91 = nowYear + "0901";
	    String start;
	    String end;
	    
	    if (numNowYear >=  Integer.parseInt(currYear91)) { // 当前日期 >= 9.1
	    	start = currYear91; // 开始时间取 今年的 91
	    	end = Integer.toString(numNowYear + 1) +  "0831"; // 结束时间取明年的8.31
	    } else {
	    	start = Integer.toString(numNowYear - 1) +  "0901"; // 结束时间取前1年的9.1
	    	end = nowYear +  "0831"; // 结束时间取前1年的9.1
	    }
	    
	    if (type == "start") {
	    	return start;
	    } else {
	    	return end;
	    }
	}
}

