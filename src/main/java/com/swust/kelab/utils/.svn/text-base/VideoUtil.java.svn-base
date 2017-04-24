package com.swust.kelab.utils;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;

/**
 * 视频处理工具类<br>
 * 包括解析flv文件，获取视频长度，甚至可添加视频转换功能<br>
 * 
 * @author EasonLian
 */
public class VideoUtil {
	
	/**
	 * 获取视频实际播放时间<br>
	 * @param vedio
	 * @return 视频总时长秒数
	 */
	public static Long getVideoDuration(File vedio) {
		try {
			Encoder encoder = new Encoder();
			MultimediaInfo mediaInfo = encoder.getInfo(vedio);
			return (mediaInfo.getDuration()/1000);
		} catch (Exception e) {
//			e.printStackTrace();
			return new Long(-1);
		}
	}
	
	/**
	 * 获取格式化视频时间<br>
	 * @param duration
	 * @return
	 */
	public static String getVideoTime(long duration) {
		int minutes = (int) (duration/60);
		int seconds = (int) (duration-minutes*60);
		return minutes+":"+seconds;
	}
}
