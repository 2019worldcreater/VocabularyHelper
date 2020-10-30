package com.yc.vh.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yc.vh.service.VocabularyService;

@WebServlet("/VocabularyServlet")
public class VocabularyFindServlet extends HttpServlet{
	@Override
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

		if(op.equals("findRelativeWords")) {
			FindRelativeWords(request, response);
		}else if(op.equals("findVocabularyInfo")) {
			findVocabularyInfo(request, response);
		}else if(op.equals("findVocabularySentences")) {
			findVocabularySentences(request, response);
		}
	}
	
	private void findVocabularySentences(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().serializeNulls().create();
		VocabularyService service = new VocabularyService(); 		
		int id = Integer.parseInt(request.getParameter("id"));
		List<Map<String, String>> result = service.getSentencesByVoabularyID(id);
		out.write(gson.toJson(result));
	}

	private void findVocabularyInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().serializeNulls().create();
		VocabularyService service = new VocabularyService(); 
		String value = request.getParameter("value"); 
		Map<String, String> result = service.findVocabularyInfoByName(value);
		out.write(gson.toJson(result));
	}

	private void FindRelativeWords(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().serializeNulls().create();
		VocabularyService service = new VocabularyService(); //服务层
		String value = request.getParameter("value");  //单词值
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(value.equals("")) {
			map.put("code", 100);
			out.write(gson.toJson(map));
		}
		else {
			List<Map<String, String>> list = service.findRelativeWordsByName(value);
			if(list.isEmpty()) {
				map.put("code", 404);
				out.write(gson.toJson(map));
			}
			else {
				out.write(gson.toJson(list));
			}
		}
	}		

	private static final long serialVersionUID = 2449081362218260998L;
}
