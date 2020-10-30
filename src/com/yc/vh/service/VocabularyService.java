package com.yc.vh.service;

import java.util.List;
import java.util.Map;
import com.yc.vh.dao.DBHelper;

public class VocabularyService{
	private DBHelper db = null;
	
	public List<Map<String, String>> findRelativeWordsByName(String value) {
		db = new DBHelper();
		String sql = "select name from vocabulary where name like ? order by name ASC limit 5";
		List<Map<String, String>> result = db.finds(sql, value + "%");
		return result;
	}
	
	public List<Map<String, String>> getSentencesByVoabularyID(int id){
		db = new DBHelper();
		String sql = "select id, name, translation, soundFilePath from sentences where vocabulary_id = ?";
		List<Map<String, String>> result = db.finds(sql, id);
		return result;
	}
	
	public Map<String, String> findVocabularyInfoByName(String value){
		db = new DBHelper();
		String sql = "select * from vocabulary where name = ?";
		Map<String, String> result = db.find(sql, value);
		return result;
	}
	
	public VocabularyService() {
		
	}

	public int addVocabulary(Map<String, String> map) {
		db = new DBHelper();
		String sql = "insert into vocabulary values(0, ?, ?, ?, ?)";
		int result = db.update(sql, map.get("name"), map.get("translation"),
				map.get("soundMark"), map.get("uploadFile"));
		return result;
	}

	public int addSentence(Map<String, String> map) {
		db = new DBHelper();
		String sql = "insert into sentences values(0, ?, ?, ?, ?)";
		int result = db.update(sql, map.get("name"), map.get("translation"),
				map.get("uploadFile"), Integer.parseInt(map.get("vocabulary_id")));
		return result;
	}

	public int modVocabulary(Map<String, String> map, boolean isFileChange) {
		db = new DBHelper();
		
		if(isFileChange) {
			String sql = "update vocabulary set name = ?, translation = ?, soundmark = ?, soundfilepath=? where id = ?";
			int result = db.update(sql, map.get("name"), map.get("translation"), map.get("soundMark"),
					map.get("uploadFile"), Integer.parseInt(map.get("vocabulary_id")));
			return result;
		}else {
			String sql = "update vocabulary set name = ?, translation = ?, soundmark = ? where id = ?";
			int result = db.update(sql, map.get("name"), map.get("translation"), map.get("soundMark"),
					Integer.parseInt(map.get("vocabulary_id")));
			return result;
		}
		
	}

	public int modSentence(Map<String, String> map, boolean isFileChange) {
		db = new DBHelper();
		
		if(isFileChange) {
			String sql = "update sentences set name = ?, translation = ?,soundfilepath= ? where id = ?";
			int result = db.update(sql, map.get("name"), map.get("translation"),
					map.get("uploadFile"), Integer.parseInt(map.get("sentence_id")));
			return result;
		}else {
			String sql = "update sentences set name = ?, translation = ?  where id = ?";
			int result = db.update(sql, map.get("name"), map.get("translation"),
					Integer.parseInt(map.get("sentence_id")));
			return result;
		}
	}

	public int delVocabulary(String id) {
		db = new DBHelper();
		String sql  = "delete from vocabulary where id = ?";
		int result = db.update(sql, Integer.parseInt(id));
		return result;
	}

	public int delSentence(String id) {
		db = new DBHelper();
		String sql  = "delete from sentences where id = ?";
		int result = db.update(sql, Integer.parseInt(id));
		return result;
	}
}
