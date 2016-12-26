package com.swust.kelab.utils;

import java.io.File;
import java.util.List;

/**
 * 文件操作工具类
 * @author pery
 *
 */
public class FileUtil {
 /**
  * 删除一个文件
  * @param absolutePath 文件的绝对路径
  */
	public static void deleteOneFile(String absolutePath){
	 File file = new File(absolutePath);
	 if(file.isDirectory()){
		 return;
	 }else{
		 file.delete();
	 }
  }
	/**
	 * 批量删除多个文件
	 * @param filesPathList  文件绝路路径列表
	 */
	public static void deleteMutFiles(List<String> filesPathList){
		for (int i = 0; i < filesPathList.size(); i++) {
			deleteOneFile(filesPathList.get(i));
		}
	}
}
