package com.swust.kelab.utils;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 格式化 工具类
 * 
 * @author Wu
 *
 */
public class FormatUtil {
    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    
    /**
     * 默认时间格式化对象
     */
    private static FastDateFormat formatter= FastDateFormat
            .getInstance(DEFAULT_TIME_FORMAT, 
            TimeZone.getDefault(),Locale.getDefault());;
    
    /**
     * 格式化给定时间,采用默认的时间格式
     * 
     * @param sourceDate 需要格式化的时间
     * @return           格式化后的时间字符串
     */
    public static String formatDate(Date sourceDate){
    	if(sourceDate != null){
    		return formatter.format(sourceDate);
    	}
    	return null;
    }
    
    /**
     * 格式化给定时间,采用指定的时间格式
     * 
     * @param sourceDate 需要格式化的时间
     * @return           格式化后的时间字符串
     */
    public static String formatDate(Date sourceDate,String timeFormat){
        FastDateFormat newformatter=null;
        if(DEFAULT_TIME_FORMAT.equals(timeFormat)){
            newformatter=formatter;
        }else{
            newformatter= FastDateFormat.getInstance(timeFormat,
                TimeZone.getDefault(),Locale.getDefault());
        }
        return newformatter.format(sourceDate);
    }
    
    public static void main(String[] args) {
        String test1=FormatUtil.formatDate(new Date());
        System.out.println(test1);
        String test2=FormatUtil.formatDate(new Date(),"yyyy-MM-dd");
        System.out.println(test2);
    }
}
