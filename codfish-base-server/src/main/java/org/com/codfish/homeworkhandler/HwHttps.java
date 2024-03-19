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
}
