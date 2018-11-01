package com.github.bioinfo.crawler.util;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 常用工具类
 * @author fujian
 *
 */
public class CommonUtil {

	/**
	 * 默认将日期字符串转换成"yyyy-MM-dd"格式日期
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date convertStrDate2Date(String dateStr, String pattern) {
		
		if("".equals(dateStr) || null== dateStr) {
			return null;
		}
		
		Date date = null;
		if(null == pattern) {
			pattern = "yyyy-MM-dd";
		}
		try {
			date = DateUtils.parseDate(dateStr, pattern);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
}
