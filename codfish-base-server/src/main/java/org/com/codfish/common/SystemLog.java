package org.com.codfish.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemLog {
	static String sessionId = null;
	public static void printLog (String log) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSSS");
		String formattedDate = sdf.format(date);
		String out = formattedDate + "|" + sessionId + "|" + log;
		System.out.println(out);
	}
	public static void setSssionId (String sessionId) {
		SystemLog.sessionId = sessionId;
	}
}
