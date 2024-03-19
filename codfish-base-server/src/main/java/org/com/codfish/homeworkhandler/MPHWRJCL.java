package org.com.codfish.homeworkhandler;

import org.com.codfish.common.SnowflakeIdGenerator;

public class MPHWRJCL {
	 private String classId;
     private String schoolName;
     private String className;
     private String teacherId;
     private String createTime;
     private String modifyTime;
     private String startTime;
     private String endTime;
     private String stt;
	 public String getClassId() {
			return classId;
		}
		public void setClassId() {
			SnowflakeIdGenerator worker = new SnowflakeIdGenerator(1,1,1);
			this.classId =  String.valueOf(worker.nextId());
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

}
class ApiInput {
	
}
class ApiOutput {
	
}
