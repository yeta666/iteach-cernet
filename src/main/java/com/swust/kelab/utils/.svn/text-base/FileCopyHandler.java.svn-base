package com.swust.kelab.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.swust.kelab.domain.Resource;
import com.swust.kelab.service.ResourceService;

/**
 * 文件拷贝线程类
 * @author EasonLian
 *
 */
public class FileCopyHandler extends Thread {
	
	private ResourceService resoService;
	
	private String deletePath;
	private Integer resoId;
	private File tempFile;
	private String localFilePath;
	
	public FileCopyHandler() {}

	@Override
	public void run() {
		try {
			File localFile = new File(localFilePath);
			if(!localFile.exists()) {
				localFile.createNewFile();
			}
System.out.println("开始文件"+ localFile +"拷贝！");
			FileUtils.copyFile(tempFile, localFile);
System.out.println("完成文件" + localFile + "拷贝！");
			Resource reso = new Resource();
			reso.setResoLocation(localFilePath);
			reso.setResoId(resoId);
			reso.setResoStatus(1);
			resoService.modifyResourceByAdmin(reso,null);
			MetaDataGenerator mdg = new MetaDataGenerator(tempFile.getAbsolutePath());
			File metaFile = new File(tempFile.getAbsolutePath()+".meta");
			if(!metaFile.exists()) {
				metaFile.createNewFile();
			}
System.out.println("开始文件"+ metaFile +"拷贝！");
			FileUtils.copyFile(metaFile, metaFile);
System.out.println("完成文件" + metaFile + "拷贝！");	
			if(deletePath != null) {
				File oldFile = new File(deletePath);
				File oldMetaFile = new File(deletePath+".meta");
				if(oldFile.exists())
					oldFile.delete();
				if(oldMetaFile.exists())
					oldMetaFile.delete();
			}
			mdg.generate();
			tempFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ResourceService getResoService() {
		return resoService;
	}
	public void setResoService(ResourceService resoService) {
		this.resoService = resoService;
	}
	public Integer getResoId() {
		return resoId;
	}
	public void setResoId(Integer resoId) {
		this.resoId = resoId;
	}
	public File getTempFile() {
		return tempFile;
	}
	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}
	public String getLocalFilePath() {
		return localFilePath;
	}
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String getDeletePath() {
		return deletePath;
	}

	public void setDeletePath(String deletePath) {
		this.deletePath = deletePath;
	}
}
