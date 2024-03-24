package org.com.codfish.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.com.codfish.common.SnowflakeIdGenerator;
import org.com.codfish.common.SystemLog;
import org.com.codfish.homeworkhandler.MPHWADCS;
import org.com.codfish.homeworkhandler.MPHWADCT;
import org.com.codfish.homeworkhandler.MPHWRJCL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * Servlet implementation class api
 */
@WebServlet("/api")
public class api extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public api() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at api: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8"); // 设置请求头U8
		String apiParams = request.getParameter("apiName"); // 获取请求接口
		HttpSession session = request.getSession();
		SystemLog.setSssionId(session.getId());
		// 请求参数转字符串
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String requestBody = buffer.toString();
		String requestLog = "apiParams=" + apiParams + "|" + requestBody + "|";
		SystemLog.printLog(requestLog);
		// 处理接口转发
		switch (apiParams) {
		case "MPHWRJCL": // 注册班级
			MPHWRJCL.run(requestBody, response);
			break;
		case "MPHWADCT": // 老师加入班级
			MPHWADCT.run(requestBody, response);
			break;
		case "MPHWADCS": // 学生加入班级
			MPHWADCS.run(requestBody, response);
			break;
		}
	}
}
