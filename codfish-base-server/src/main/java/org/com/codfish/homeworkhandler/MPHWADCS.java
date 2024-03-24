package org.com.codfish.homeworkhandler;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.com.codfish.common.SystemLog;
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
		List<ObjClass> classSttList = HwSql.getClassListByClassId(apiInput.getClassId());
		if (classSttList == null) {
			HwHttps.hwHttpResponeErr(response);
			SystemLog.printLog("MPHWADCS.run| HwSql.getClassListByClassId result null");
			return ;
		}
		if (classSttList.size() <= 0) {
			HwHttps.hwHttpResponeErr(response);
			SystemLog.printLog("MPHWADCS.run| HwSql.getClassListByClassId targetClass not found");
			return ;
		}
		// 查询已经加入班级的数量
		List<ObjClass> classList = HwSql.getClassListByStuId(apiInput.getStudentId());
		if (classList == null) {
			HwHttps.hwHttpResponeErr(response);
			SystemLog.printLog("MPHWADCS.run| HwSql.getClassListByStuId result null");
			return ;
		}
		SystemLog.printLog("MPHWADCS.run| classList.size " + classList.size());
		if (classList.size() >= 10) { // 不能加入超过10个班级 ？ 只能加入一个班级？家长是可以加入多个班级的
			String errCode = "SERV000";
			String ErrMsg = "加入班级失败，已经加入班级数量超过10";
			HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
			SystemLog.printLog("MPHWADCS.run| classList > =10");
			return;
		}
		Boolean ifReg = false;
		for (int i = 0; i < classList.size(); i++) {
			if (classList.get(i).getClassId().equals(apiInput.getClassId())) {
				String errCode = "SERV009";
				String ErrMsg = "加入班级失败，重复加入";
				HwHttps.hwHttpResponeErr(response,errCode,ErrMsg);
				SystemLog.printLog("MPHWADCS.run| readd");
				ifReg = true;
				break;
			}
		}
		
		if (ifReg) {
			return;
		}
		SystemLog.printLog("MPHWADCS.run| ifReg " + ifReg);
		int addResult = HwSql.addStudent(apiInput.getStudentId(), apiInput.getClassId());
		if (addResult == 0) {
			// TODO注册结果通知，接口返回,带优化 增加结果返回
			ErrReturnObj err = new ErrReturnObj();
			err.setRetcode("00000000");
			err.setRetmsg("加入班级成功");
			Gson gson = new Gson();
			String jsonStr = gson.toJson(err);
			HwHttps.hwHttpRespone(response, jsonStr);
			SystemLog.printLog("MPHWADCS.run| HwSql.addStudent success");
		} else {
			HwHttps.hwHttpResponeErr(response);
			SystemLog.printLog("MPHWADCS.run| HwSql.addStudent err");
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
