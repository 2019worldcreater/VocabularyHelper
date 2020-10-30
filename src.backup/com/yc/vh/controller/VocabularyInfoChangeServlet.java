package com.yc.vh.controller;

import com.google.gson.Gson;
import com.yc.vh.service.FileUploadUtil;
import com.yc.vh.service.StringUtil;
import com.yc.vh.service.VocabularyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/VocabularyInfoChangeServlet")
public class VocabularyInfoChangeServlet extends HttpServlet{

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf8");
		String op = request.getParameter("op");
		
		if(op.equals("addVocabulary")) {
			addVocabulary(request, response);
		}else if(op.equals("addSentence")){
			addSentence(request, response);
		}else if(op.equals("modVocabulary")){
			modVocabulary(request, response);
		}else if(op.equals("modSentence")) {
			modSentence(request, response);
		}else if(op.equals("delVocabulary")) {
			delVocabulary(request,response);
		}else if(op.equals("delSentence")) {
			delSentence(request, response);
		}
	}
	
	private void delSentence(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		VocabularyService service = new VocabularyService();
		int line = service.delSentence(id);
		Map<String, Object> result = new HashMap<String, Object>();
		if(line > 0) {
			result.put("code", 500);
		}else {
			result.put("code", 501);
		}
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		out.write(gson.toJson(result));
	}

	private void delVocabulary(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		VocabularyService service = new VocabularyService();
		int line = service.delVocabulary(id);
		Map<String, Object> result = new HashMap<String, Object>();
		if(line > 0) {
			result.put("code", 500);
		}else {
			result.put("code", 501);
		}
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		out.write(gson.toJson(result));
		
	}

	private void modSentence(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		FileUploadUtil uploadUtil = new FileUploadUtil();
		PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, request, response, null, true, 2048, true);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> map = null;
		try {
			map = uploadUtil.uploads(pageContext);  //上传
			VocabularyService service = new VocabularyService();
			//添加到数据库
			System.out.print(map);
			boolean isFileChange = !(StringUtil.checkNull(map.get("uploadFile")));
			int line = service.modSentence(map, isFileChange);
			if(line < 1) {
				result.put("code", 605);  //数据库添加失败
			}else if(StringUtil.checkNull(map.get("uploadFile"))){
				result.put("code", 606);  //除了文件失败，数据库正常添加
			}else 
				result.put("code", 600); //正常
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Gson gson = new Gson();
		out.write(gson.toJson(result));
	}

	private void modVocabulary(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		FileUploadUtil uploadUtil = new FileUploadUtil();
		PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, request, response, null, true, 2048, true);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> map = null;
		try {
			map = uploadUtil.uploads(pageContext);  //上传
			VocabularyService service = new VocabularyService();
			//添加到数据库
			boolean isFileChange = !(StringUtil.checkNull(map.get("uploadFile")));
			int line = service.modVocabulary(map, isFileChange);
			if(line < 1) {
				result.put("code", 605);  //数据库添加失败
			}else if(StringUtil.checkNull(map.get("uploadFile"))){
				result.put("code", 606);  //除了文件失败，数据库正常添加
			}else 
				result.put("code", 600); //正常
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Gson gson = new Gson();
		out.write(gson.toJson(result));

		
	}

	private void addSentence(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		FileUploadUtil uploadUtil = new FileUploadUtil();
		PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, request, response, null, true, 2048, true);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> map = null;
		try {
			map = uploadUtil.uploads(pageContext);  //上传
			VocabularyService service = new VocabularyService();
			//添加到数据库
			
			int line = service.addSentence(map);
			if(line < 1) {
				result.put("code", 605);  //数据库添加失败
			}else if(StringUtil.checkNull(map.get("uploadFile"))){
				result.put("code", 606);  //除了文件失败，数据库正常添加
			}else 
				result.put("code", 600); //正常
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Gson gson = new Gson();
		out.write(gson.toJson(result));
	}

	private void addVocabulary(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		FileUploadUtil uploadUtil = new FileUploadUtil();
		PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(this, request, response, null, true, 2048, true);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> map = null;
		try {
			map = uploadUtil.uploads(pageContext);  //上传
			VocabularyService service = new VocabularyService();
			//添加到数据库
			int line = service.addVocabulary(map);
			if(line < 1) {
				result.put("code", 605);  //数据库添加失败
			}else if(StringUtil.checkNull(map.get("uploadFile"))){
				result.put("code", 606);  //除了文件失败，数据库正常添加
			}else result.put("code", 600); //正常
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Gson gson = new Gson();
		out.write(gson.toJson(result));

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
