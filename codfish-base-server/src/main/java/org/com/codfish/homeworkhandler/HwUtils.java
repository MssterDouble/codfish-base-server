package org.com.codfish.homeworkhandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class HwUtils {
	// 公共 - 时间方法 - 格式8/16位数字
	static String timeMaker(String type) {
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
	static String semeTime(String type, Date now) {
		// 获取当前年字符串
		SimpleDateFormat sdfNowYear = new SimpleDateFormat("yyyy");
		String nowYear = sdfNowYear.format(now);
		// 将字符串转换为整数
		int numNowYear = Integer.parseInt(nowYear);
		String currYear91 = nowYear + "0901";
		String start;
		String end;

		if (numNowYear >= Integer.parseInt(currYear91)) { // 当前日期 >= 9.1
			start = currYear91; // 开始时间取 今年的 91
			end = Integer.toString(numNowYear + 1) + "0831"; // 结束时间取明年的8.31
		} else {
			start = Integer.toString(numNowYear - 1) + "0901"; // 结束时间取前1年的9.1
			end = nowYear + "0831"; // 结束时间取前1年的9.1
		}

		if (type == "start") {
			return start;
		} else {
			return end;
		}
	}

}
