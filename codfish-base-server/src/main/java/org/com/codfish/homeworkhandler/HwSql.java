package org.com.codfish.homeworkhandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.com.codfish.common.SqlCon;

public class HwSql {
	static List getClassList(String type, String schoolName, String className, String teacherId, String classId) {
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
}
