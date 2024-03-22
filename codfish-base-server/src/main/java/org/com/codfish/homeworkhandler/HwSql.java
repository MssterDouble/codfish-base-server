package org.com.codfish.homeworkhandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.com.codfish.common.SqlCon;
import org.com.codfish.servlet.ErrReturnObj;

import com.google.gson.Gson;

public class HwSql {
	static List<ObjClass> getClassListBySql (String querySql) {
		List<ObjClass> classObjList = new ArrayList<>();
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				ObjClass classObj = new ObjClass();
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
			}
			rs.close();
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return classObjList;
	}
	static List<ObjClass> getClassList(String type, String schoolName, String className, String teacherId, String classId, String classIds) {
		List<ObjClass> classObjList = new ArrayList<>();
		// 查询class 状态正常的班级
		String querySql = null;
		if (type == "schoolName") { // 根据校+班级名称查找状态正常的班级
			querySql = "SELECT * FROM t_hw_class_inf WHERE schoolName='" + schoolName + "'"
					+ " AND className='" + className + "'"
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
					+ " AND stt='1';";
		} else if (type == "classId") { // 根据班级ID查找班级信息
			querySql = "SELECT * FROM t_hw_class_inf WHERE classId='" + classId + "'" 
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime"
					+ " AND stt='1';";
		}  else if (type == "classIds") { // 根据班级ID查找班级信息
			querySql = "SELECT * FROM t_hw_class_inf WHERE classId IN (" + classIds + ")" 
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime"
					+ " AND stt='1';";
		} else if (type == "all") { // 根据teacherId查找班级信息
			querySql = "SELECT * FROM t_hw_class_inf WHERE teacherId='" + teacherId + "';";
		}
		System.out.println("querySql:\t" + querySql);
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				ObjClass classObj = new ObjClass();
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
			}
			rs.close();
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return classObjList;
	}
	public static List<ObjClass> getClassListByTeacherId (String teacherId) {
		List<ObjClass> classList = null;
		List<String> teachIdList = new ArrayList<>(); // teacehr 已经加入的班级的ID集合
		String querySql = "SELECT * FROM t_hw_teach_inf WHERE " 
				+ "teacherId='" + teacherId + "'"
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		System.out.println("querySql:\t" + querySql);
		
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				teachIdList.add(rs.getString(5));
			}
			rs.close();
			sc.closeConn();
			String strStuIdList = String.join(",", teachIdList);
			if (teachIdList.size() > 0) {
				// 根据classId合集查询 全部classId的状态正常的
				String querySqlClass = "SELECT * FROM t_hw_class_inf WHERE classId IN (" + strStuIdList + ")" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime"
						+ " AND stt='1';";
				classList = getClassListBySql(querySqlClass);	
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return classList;
	}
	static List<ObjClass> getClassById (String classId, String type) {
		List<ObjClass> classObjList = null;
		String querySql = null;
		if ("all".equals(type)) {
			querySql = "SELECT * FROM t_hw_class_inf WHERE classId='" + classId + "';";
		} else {
			querySql = "SELECT * FROM t_hw_class_inf WHERE classId='" + classId + "'" 
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
					+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime"
					+ " AND stt='1';";
		}
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				ObjClass classObj = new ObjClass();
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
			}
			rs.close();
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return classObjList;
	}
	
	static List<ObjTeacher> getTeacherList (String type, String teacherId, String program) {
		List<ObjTeacher> teacherList = null;
		String querySql = "SELECT * FROM t_hw_teach_inf WHERE ";
		if (type == "schoolName") { // 根据校+班级名称查找状态正常的班级

		} else if (type == "program") { // 根据班级ID查找班级信息
			querySql += "teacherId='" + teacherId
					+ " AND 'program=" + program 
					+ " AND stt='1';";
		} else if (type == "active") { // 根据teacherId查找状态正常的
			querySql += "teacherId='" + teacherId
					+ " AND stt='1';";
		} else if (type == "all") { // 根据teacherId查找全部记录
			querySql += "teacherId='" + teacherId + "';";
		}
		System.out.println("querySql:\t" + querySql);
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				// TODO  赋值
				ObjTeacher objTeacher = new ObjTeacher();
				teacherList.add(objTeacher);
			}
			rs.close();
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return teacherList;
	}
	
	static List<ObjStudent> getStuendList (String type, String stuendId) {
		List<ObjClass> classList = new ArrayList<>();
		List<ObjStudent> stuList = new ArrayList<>();
		List<String> stuIdList = new ArrayList<>();
		String querySql = "SELECT * FROM t_hw_stu_inf WHERE " 
				+ "stuendId='" + stuendId
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		System.out.println("querySql:\t" + querySql);
		
		// 获取全部
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				stuIdList.add(rs.getString(1));
			}
			rs.close();
			sc.closeConn();
			String result = String.join(",", stuIdList);
			
			List<ObjClass> classLis = getClassList("classIds","","","","","classIds");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return stuList;
	}
	
	/**
	 * 输入SQL语句查询class 列表
	 * @param querySql sql 语句
	 * @return List ObjClass
	 */
	public static List<ObjClass> getClassList (String querySql) {
		 List<ObjClass> classList = null;
			try {
				SqlCon sc = new SqlCon();
				ResultSet rs = sc.query(querySql);
				while (rs.next()) {
					ObjClass classObj = new ObjClass();
					classObj.setClassId(rs.getString(1));
					classObj.setSchoolName(rs.getString(2));
					classObj.setClassName(rs.getString(3));
					classObj.setTeacherId(rs.getString(4));
					classObj.setCreateTime(rs.getString(5));
					classObj.setModifyTime(rs.getString(6));
					classObj.setStartTime(rs.getString(7));
					classObj.setEndTime(rs.getString(8));
					classObj.setStt(rs.getString(9));
					classList.add(classObj);
				}
				rs.close();
				sc.closeConn();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		 return classList;
	}
	/**
	 * 
	 * @param classId 输入classId 查找所有状态正常的班级
	 * @return List ObjClass
	 */
	public static List<ObjClass> getTeachListByClassId(String classId) {
		String queryClassList = "SELECT * FROM t_hw_class_inf WHERE " 
				+ "classId='" + classId + "'"
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		List<ObjClass> classList = getClassList(queryClassList);
		return classList;
	}
	/**
	 *  输入SQL语句查询 teacher 列表
	 * @param querySql sql 语句
	 * @return List ObjTeacher
	 */
	public static List<ObjTeacher> getTeachList(String querySql) {
		List<ObjTeacher> teacherList = null;
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				// TODO  赋值
				ObjTeacher objTeacher = new ObjTeacher();
				teacherList.add(objTeacher);
			}
			rs.close();
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return teacherList;
	}

	/**
	 * 输入SQL语句查询 stuend 列表 
	 * @param querySql sql 语句
	 * @return List ObjStudent
	 */
	public static List<ObjStudent> getStuList (String querySql) {
		List<ObjStudent> stuList = null;
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				// TODO  赋值
				ObjStudent objStu = new ObjStudent();
				
				stuList.add(objStu);
			}
			rs.close();
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return stuList;
	}
	
	
	public static int addClass () {
		int resultStt = 0;
		return resultStt;
	}
	/**
	 * 
	 * @param teacherId 
	 * @param teacherName
	 * @param telephone
	 * @param program
	 * @param classId
	 * @return 0-成功；1-插入失败；
	 */
	public static int addTeacher (String teacherId, String teacherName, String telephone, String program, String classId) {
		int resultStt = 0;
		try {
			SqlCon sc = new SqlCon();
			String insertQuery = "INSERT INTO t_hw_stu_inf "
					+ "(studentId, classId, createTime, modifyTime, startTime, endTime, stt) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = sc.getConnection().prepareStatement(insertQuery);
			preparedStatement.setString(1, teacherId); // 学校名称 输入
			preparedStatement.setString(2, teacherName); // 班级名称 输入
			preparedStatement.setString(3, telephone); // 班级名称 输入
			preparedStatement.setString(4, program); // 班级名称 输入
			preparedStatement.setString(5, classId); // 班级名称 输入
			preparedStatement.setString(6, HwUtils.timeMaker("currentTime")); // 创建时间，当前时间
			preparedStatement.setString(7, HwUtils.timeMaker("currentTime")); // 修改时间 去当前时间
			preparedStatement.setString(8, HwUtils.timeMaker("semeStart")); // 开始时间
			preparedStatement.setString(9, HwUtils.timeMaker("semeEnd")); // 结束时间
			preparedStatement.setString(10, "1");

			// 执行 插入数据
			int rowsInserted = preparedStatement.executeUpdate();
			if (rowsInserted > 0) {
				resultStt = 1;
			}
			sc.closeConn();
			preparedStatement.close();
		} catch (ClassNotFoundException | SQLException e) {
			resultStt = 1;
		}
		return resultStt;
	}
	public static int addStudent () {
		int resultStt = 0;
		return resultStt;
	}
	public static int delClass () {
		int resultStt = 0;
		return resultStt;
	}
	public static int delTeacher () {
		int resultStt = 0;
		return resultStt;
	}
	public static int delStuend () {
		int resultStt = 0;
		return resultStt;
	}
	public static int modifyClass () {
		int resultStt = 0;
		return resultStt;
	}
	public static int modifyTeacher () {
		int resultStt = 0;
		return resultStt;
	}
	public static int modifyStuend () {
		int resultStt = 0;
		return resultStt;
	}
}
