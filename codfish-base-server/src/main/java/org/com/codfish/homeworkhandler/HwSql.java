package org.com.codfish.homeworkhandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.com.codfish.common.SnowflakeIdGenerator;
import org.com.codfish.common.SqlCon;
import org.com.codfish.servlet.ErrReturnObj;

import com.google.gson.Gson;

public class HwSql {
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
	 * 根据 classId 查询全部正常的班级
	 * @param classId 输入classId 查找所有状态正常的班级
	 * @return List ObjClass
	 */
	public static List<ObjClass> getClassListByClassId(String classId) {
		String queryClassList = "SELECT * FROM t_hw_class_inf WHERE " 
				+ "classId='" + classId + "'"
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		List<ObjClass> classList = getClassList(queryClassList);
		return classList;
	}
	/**
	 * 根据 studentId 查询全部正常的班级
	 * @param studentId
	 * @return 
	 */
	public static List<ObjClass> getClassListByStuId(String stuendId) {
		List<ObjClass> classList = null;
		List<String> stuClassIdList = new ArrayList<>(); // stu 已经加入的班级的ID集合
		String querySql = "SELECT * FROM t_hw_stu_inf WHERE " 
				+ "stuendId='" + stuendId + "'"
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		System.out.println("querySql:\t" + querySql);
		try {
			SqlCon sc = new SqlCon();
			ResultSet rs = sc.query(querySql);
			while (rs.next()) {
				stuClassIdList.add(rs.getString(2));
			}
			rs.close();
			sc.closeConn();
			String strStuClassIdList = String.join(",", stuClassIdList);
			if (stuClassIdList.size() > 0) {
				// 根据classId合集查询 全部classId的状态正常的
				String querySqlClass = "SELECT * FROM t_hw_class_inf WHERE classId IN (" + strStuClassIdList + ")" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime"
						+ " AND stt='1';";
				classList = getClassList(querySqlClass);	
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return classList;
	}
	/**
	 * 根据 teacherId 查询全部正常的班级
	 * @param teacherId
	 * @return List ObjClass 
	 */
	public static List<ObjClass> getClassListByTeacherId (String teacherId) {
		List<ObjClass> classList = null;
		List<String> teachClassIdList = new ArrayList<>(); // teacehr 已经加入的班级的ID集合
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
				teachClassIdList.add(rs.getString(5));
			}
			rs.close();
			sc.closeConn();
			String strTeachClassIdList = String.join(",", teachClassIdList);
			if (teachClassIdList.size() > 0) {
				// 根据classId合集查询 全部classId的状态正常的
				String querySqlClass = "SELECT * FROM t_hw_class_inf WHERE classId IN (" + strTeachClassIdList + ")" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
						+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime"
						+ " AND stt='1';";
				classList = getClassList(querySqlClass);	
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return classList;
	}
	/**
	 * 根据学校+班级名称查询学校列表
	 * @param schoolName
	 * @param className
	 * @return
	 */
	static List<ObjClass> getClassListByName(String schoolName, String className) {
		List<ObjClass> classObjList = null;
		// 查询class 状态正常的班级
		String querySql = "SELECT * FROM t_hw_class_inf WHERE schoolName='" + schoolName + "'"
				+ " AND className='" + className + "'"
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		System.out.println("querySql:\t" + querySql);
		classObjList = getClassList(querySql);
		return classObjList;
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
				objTeacher.setTeacherId(rs.getString(1));
				objTeacher.setTeacherName(rs.getString(2));
				objTeacher.setTelephone(rs.getString(3));
				objTeacher.setProgram(rs.getString(4));
				objTeacher.setClassId(rs.getString(5));
				objTeacher.setCreateTime(rs.getString(6));
				objTeacher.setModifyTime(rs.getString(7));
				objTeacher.setStartTime(rs.getString(8));
				objTeacher.setEndTime(rs.getString(9));
				objTeacher.setStt(rs.getString(10));
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
				objStu.setStudentId(rs.getString(1));
				objStu.setClassId(rs.getString(2));
				objStu.setCreateTime(rs.getString(3));
				objStu.setModifyTime(rs.getString(4));
				objStu.setStartTime(rs.getString(5));
				objStu.setEndTime(rs.getString(6));
				objStu.setStt(rs.getString(7));
				stuList.add(objStu);
			}
			rs.close();
			sc.closeConn();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return stuList;
	}
	
	/**
	 * 创建新班级
	 * @param schoolName
	 * @param className
	 * @param teacherId
	 * @return 0-成功；1-插入失败；
	 */
	public static int addClass (String classId, String schoolName, String className, String teacherId) {
		int resultStt = 0;
		try {
			SqlCon sc = new SqlCon();
			String insertQuery = "INSERT INTO t_hw_class_inf "
					+ "(classId, schoolName, className, teacherId, createTime, modifyTime, startTime, endTime, stt) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = sc.getConnection().prepareStatement(insertQuery);
			// 设置参数值

			preparedStatement.setString(1, classId); // 班级唯一ID 随机创建
			preparedStatement.setString(2, schoolName); // 学校名称 输入
			preparedStatement.setString(3, className); // 班级名称 输入
			preparedStatement.setString(4, teacherId); // 班主任openid 输入
			preparedStatement.setString(5, HwUtils.timeMaker("currentTime")); // 创建时间，当前时间
			preparedStatement.setString(6, HwUtils.timeMaker("currentTime")); // 修改时间 去当前时间
			preparedStatement.setString(7, HwUtils.timeMaker("semeStart")); // 开始时间
			preparedStatement.setString(8, HwUtils.timeMaker("semeEnd")); // 结束时间
			preparedStatement.setString(9, "1");
			// 执行 插入数据1成功0失败
			int rowsInserted = preparedStatement.executeUpdate();
			resultStt = rowsInserted > 0 ? 0 : 1;
			sc.closeConn();
			preparedStatement.close();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return resultStt;
	}
	/**
	 * 老师加入班级
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

			// 执行 插入数据1成功0失败
			int rowsInserted = preparedStatement.executeUpdate();
			resultStt = rowsInserted > 0 ? 0 : 1;
			sc.closeConn();
			preparedStatement.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			resultStt = 1;
		}
		return resultStt;
	}
	/**
	 * 学生加入班级
	 * @param studentId 
	 * @param classId
	 * @return 0-成功；1-插入失败；
	 */
	public static int addStudent (String studentId, String classId) {
		int resultStt = 0;
		try {
			SqlCon sc = new SqlCon();
			String insertQuery = "INSERT INTO t_hw_stu_inf "
					+ "(studentId, classId, createTime, modifyTime, startTime, endTime, stt) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = sc.getConnection().prepareStatement(insertQuery);
			preparedStatement.setString(1, studentId); // 学校名称 输入
			preparedStatement.setString(2, classId); // 班级名称 输入
			preparedStatement.setString(3, HwUtils.timeMaker("currentTime")); // 创建时间，当前时间
			preparedStatement.setString(4, HwUtils.timeMaker("currentTime")); // 修改时间 去当前时间
			preparedStatement.setString(5, HwUtils.timeMaker("semeStart")); // 开始时间
			preparedStatement.setString(6, HwUtils.timeMaker("semeEnd")); // 结束时间
			preparedStatement.setString(7, "1");
			// 执行 插入数据1成功0失败
			int rowsInserted = preparedStatement.executeUpdate();
			resultStt = rowsInserted > 0 ? 0 : 1;
			sc.closeConn();
			preparedStatement.close();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return resultStt;
	}
	
	/**
	 *  跟俊classId 删除classID
	 * @param classId
	 * @return 0-成功；1-失败；
	 */
	public static int delClass (String classId) {
		int resultStt = 0;
		try {
			SqlCon sc = new SqlCon();
			String delSql = "DELETE FROM t_hw_class_inf WHERE classId ='" + classId + "'";;
			PreparedStatement preparedStatement = sc.getConnection().prepareStatement(delSql);
			// 执行 插入数据1成功0失败
			int rowsInserted = preparedStatement.executeUpdate();
			resultStt = rowsInserted > 0 ? 0 : 1;
			sc.closeConn();
			preparedStatement.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			resultStt = 1;
		}
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
