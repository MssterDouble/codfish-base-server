package org.com.codfish.homeworkhandler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.com.codfish.servlet.ErrReturnObj;

import com.google.gson.Gson;

public class HwHttps {
	static void hwHttpRespone(HttpServletResponse response, String result) {
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().append(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void hwHttpResponeErr(HttpServletResponse response) {
		String errCode = "SERV0002";
		String ErrMsg = "注册失败系统内部错误";
		hwHttpResponeErr(response, errCode, ErrMsg);
	}
	
	static void hwHttpResponeErr(HttpServletResponse response, String errCode, String errMsg) {
		response.setCharacterEncoding("utf-8");
		try {
			ErrReturnObj err = new ErrReturnObj();
			err.setRetcode(errCode);
			err.setRetmsg(errMsg);
			Gson gson = new Gson();
			String jsonStr = gson.toJson(err);
			response.getWriter().append(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
