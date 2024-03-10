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

import org.com.codfish.common.SnowflakeIdGenerator;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		Register.register("123");
//		System.out.println("doGet request:\n"+request.toString());
//		System.out.println("doGet request:\n"+request.getRequestURI());
//		System.out.println("doGet request:\n"+request.getContextPath());
//		System.out.println("doGet request:\n"+request.getQueryString());
//		System.out.println("doGet request getParameter:"+request.getParameter("params"));
//		System.out.println("doGet request:\n"+request.getParameterValues("data"));
//		 StringBuilder buffer = new StringBuilder();
//		    BufferedReader reader = request.getReader();
//		    String line;
//		    while ((line = reader.readLine()) != null) {
//		        buffer.append(line);
//		    }
//		    String requestBody = buffer.toString();
//		    System.out.println("--------> doget request json is :" + requestBody);
		
		
		request.setCharacterEncoding("UTF-8");
		StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = request.getReader();
//	    String line;
	    while (reader.readLine()!= null) {
	    	System.out.println("readLine" + reader.readLine());
	    	System.out.println("readLine" + URLDecoder.decode(reader.readLine(), "utf-8"));
	    	buffer.append(reader.readLine());
	    }
//	    while ((line = URLDecoder.decode(reader.readLine(), "utf-8")) != null) {
//	        buffer.append(line);
//	    }
	    String requestBody = buffer.toString();
	    System.out.println("--------> doGet request json is :" + requestBody);
	    response.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at api: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("post 请求格式" + request.getCharacterEncoding());
		request.setCharacterEncoding("UTF-8");
		StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
//	    while (reader.readLine() != null) {
//	    	System.out.println("readLine" + reader.readLine());
//	    	System.out.println("readLine" + URLDecoder.decode(reader.readLine(), "utf-8"));	
//	    	buffer.append(reader.readLine());
//	    }
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	    }
	    String requestBody = buffer.toString();
	    Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate + "\tdoPost request apiName:\t"+request.getParameter("apiName"));
	    System.out.println(formattedDate + "\tdoPost request apiParams:\t " + requestBody);
//		doGet(request, response);
	    Gson gson = new Gson();
//	    JsonElement jsonElement = gson.fromJson(requestBody, JsonElement.class);
//	    System.out.println(formattedDate + "\tdoPost request jsonElement:\t " + jsonElement);
//	    DemoId ddd = new DemoId();
//	    ddd.setId("1212");
//	    String jsonStr = gson.toJson(ddd);
//	    System.out.println(formattedDate + "\tjsonStr:\t " + jsonStr);
	    DemoId demoid = gson.fromJson(requestBody, DemoId.class);
	    System.out.println(formattedDate + "\tdemoid.getID:\t " + demoid.getId());
	    
	    DemoId ddd = new DemoId();
	    ddd.setId("respone");
	    ddd.setRetcode("00000000");
	    ddd.setRetmsg("交易成功");
	    String jsonStr = gson.toJson(ddd);
	    System.out.println(formattedDate + "\tresponse.getWriter:\t " + jsonStr);
	    response.setCharacterEncoding("utf-8");
	    response.getWriter().append(jsonStr);
	}
	class DemoId {
		private String ID = null;
		private String RETCODE = null;
		private String RETMSG = null;
	    public void setId(String id) {
	        this.ID = id;
	    }
	    public String getId() {
	        return ID;
	    }
	    public void setRetcode(String retcode) {
	        this.RETCODE = retcode;
	    }
	    public String getRetcode() {
	        return RETCODE;
	    }
	    public void setRetmsg(String retmsg) {
	        this.RETMSG = retmsg;
	    }
	    public String getRetmsg() {
	        return RETMSG;
	    }
	}
}
