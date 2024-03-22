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
		// 输入转json转对象
		Gson gsinput = new Gson();
		ApiInput apiInput = gsinput.fromJson(requestBody, ApiInput.class);
		
		// 查询这个班级是否已经注册过
		List<ObjClass> classList = HwSql.getClassListByName(apiInput.getSchoolName(), apiInput.getClassName());
		if (classList == null) {
			HwHttps.hwHttpResponeErr(response);
			return;
		}
		if (classList.size() > 0) {
			String errCode = "SERV0001";
			String ErrMsg = "班级重复注册";
			HwHttps.hwHttpResponeErr(response, errCode, ErrMsg);
			return;
		}
		// 查询这个老师加入班级的数量超标、注册班级的数量超标
//		TODO
		// 创建班级
		SnowflakeIdGenerator worker = new SnowflakeIdGenerator(1, 1, 1);
		String tempClassId = String.valueOf(worker.nextId());
		int addClassResult = HwSql.addClass(tempClassId, apiInput.getSchoolName(), apiInput.getClassName(),
				apiInput.getTeacherId());
		if (addClassResult == 1) {
			HwHttps.hwHttpResponeErr(response);
			return;
		}
		// 创建班级的老师插入老师的列表
		int addTeacherResult = HwSql.addTeacher(apiInput.getTeacherId(), apiInput.getTeacherName(), apiInput.getTelephone(), apiInput.getProgram(), tempClassId);
		if (addTeacherResult == 0) {
			// TODO注册结果通知，接口返回,带优化 增加结果返回
			ErrReturnObj err = new ErrReturnObj();
			err.setRetcode("00000000");
			err.setRetmsg("加入班级成功");
			Gson gson = new Gson();
			String jsonStr = gson.toJson(err);
			HwHttps.hwHttpRespone(response, jsonStr);
		} else {
			HwHttps.hwHttpResponeErr(response);
			//TODO 创建班级成功，但是加入老师失败，需要回滚，存在bug 老师加入班级数量不够但是可以通过创建班级来无限增加
			return;
		}
	}

	class ApiInput {
		private String schoolName;
		private String className;
		private String teacherId;
		private String teacherName;
		private String telephone;
		private String program;

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

		public String getTeacherName() {
			return teacherName;
		}

		public void setTeacherName(String teacherName) {
			this.teacherName = teacherName;
		}
	}

	class ApiOutput {

	}
}
