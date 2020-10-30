package com.yc.vh.service;

public class StringUtil {
	public static boolean checkNull(String ...strs) {
		if(strs==null||strs.length<0)
			return true;
		for(String str:strs) {
			if(str==null||str.equals(""))
				return true;
		}
		return false;
	}
}
