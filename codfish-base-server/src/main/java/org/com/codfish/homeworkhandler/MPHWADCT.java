package org.com.codfish.homeworkhandler;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.com.codfish.servlet.ErrReturnObj;
import com.google.gson.Gson;

/**
 * 加入班级老师
 */
public class MPHWADCT {
	public static void run (String requestBody, HttpServletResponse response) {		
		// 输入转json转对象
		Gson gsinput = new Gson();
		ApiInput apiInput = gsinput.fromJson(requestBody, ApiInput.class);
		
		// 加入的班级状态不正常，不允许注册
		List<ObjClass> classSttList = HwSql.getClassListByClassId(apiInput.getClassId());
		if (classSttList == null) {
			HwHttps.hwHttpResponeErr(response);
			return ;
		}
		if (classSttList.size() == 0) { 
			String errCode = "SERV0005";
			String ErrMsg = "加入班级失败，班级不存在";
			HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
			return;
		}
		
		// 查询课程是否已经注册
		String queryTeacherList = "SELECT * FROM t_hw_teach_inf WHERE " 
				+ "classId='" + apiInput.getClassId() + "'"
				+ "program='" + apiInput.getProgram() + "'"
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'>=startTime" 
				+ " AND '" + HwUtils.timeMaker("currentDate") + "'<=endTime" 
				+ " AND stt='1';";
		List<ObjTeacher> progList = HwSql.getTeachList(queryTeacherList);
		if (progList == null) {
			HwHttps.hwHttpResponeErr(response);
			return ;
		}
		if (progList.size() > 0) {
			String errCode = "SERV0006";
			String ErrMsg = "加入班级失败，课程已注册";
			HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
			return;
		}
		
		// 查询老师已经加入的班级数量
		List<ObjClass> teachClassList = HwSql.getClassListByTeacherId(apiInput.getTeacherId());
		if (teachClassList == null) {
			HwHttps.hwHttpResponeErr(response);
			return;
		}
		if (teachClassList.size() >= 5) { // 一个老师不允许加入超过5个班级
			String errCode = "SERV000";
			String ErrMsg = "加入班级失败，课程数量超过5";
			HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
			return;
		}

		int addResult = HwSql.addTeacher(apiInput.getTeacherId(), apiInput.getTeacherName(), apiInput.getTelephone(),apiInput.getProgram(), apiInput.getClassId());
		if (addResult == 0) {
			// TODO注册结果通知，接口返回,带优化 增加结果返回
			ErrReturnObj err = new ErrReturnObj();
			err.setRetcode("00000000");
			err.setRetmsg("加入班级成功");
			Gson gson = new Gson();
			String jsonStr = gson.toJson(err);
			HwHttps.hwHttpRespone(response, jsonStr);
		} else {
			HwHttps.hwHttpResponeErr(response);
		}
	}
	
	class ApiInput {
		public String getTeacherId() {
			return teacherId;
		}
		public void setTeacherId(String teacherId) {
			this.teacherId = teacherId;
		}
		public String getClassId() {
			return classId;
		}
		public void setClassId(String classId) {
			this.classId = classId;
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
		private String teacherId;
		private String classId;
		private String teacherName;
		private String telephone;
		private String program;
	}

	class ApiOutput {
		
	}
}
