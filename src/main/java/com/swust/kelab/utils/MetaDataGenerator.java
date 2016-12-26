package com.swust.kelab.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.red5.io.FileKeyFrameMetaCache;
import org.red5.io.flv.impl.FLVReader;
/**
 * @author Shaw Joe
 * @Title: 
 * @ClassName£ºMetaDataGenerator.java
 * @Package: swust.edu.cn
 * @Description: TODO
 * @author Shao Zhou
 * @createDate:2013  ÏÂÎç7:02:28    
 * @email:shaozhou@swust.edu.cn
 * @phone:15881615397
 * @Department:Knowledge engineering and Data Mining Lab of Computer Science and Technology Academy of SWUST
 * @Address:Southwest University of  Science and Technology 59 Qinglong Road, Mianyang, 621010, P.R.China
 * @reviseNote:
 * @version:V1.0
 */
public class MetaDataGenerator {

	private static String path="files/ms-qjjy.flv";
	private File flvFile;

	
	public static void main(String[] args) {
		System.out.println(args.length);
		
		if (args.length > 0 && !args[0].equals("")){
			MetaDataGenerator meta = new MetaDataGenerator(args[0]);
			meta.generate();
		}else{
			MetaDataGenerator meta = new MetaDataGenerator(path);
			meta.generate();
		}
	}
	
	public MetaDataGenerator(String flvPath)
	{
		path = flvPath;
	}
	
	public void generate()
	{

		try {
			File file = new File(path);
			System.out.println("sssssssss");
			
			if (file.isDirectory())
			{
    			Collection<?> flvFiles = FileUtils.listFiles 
    		    (file, new String[] { "flv" }, true);
    			
    			for (Object o : flvFiles)
    			{
    				File flv = (File)o;
    				
    				saveMetaData(flv);
    			}
			} else {
				saveMetaData(file);
			}
			 
		} catch (Exception e) {
			
		}
		
		
		
	}
	
	private void saveMetaData(File flvFile) throws IOException
	{
		File meta = new File(flvFile.getAbsoluteFile() + ".meta");
		System.out.println(flvFile.getAbsoluteFile());
		if (!meta.isFile())
		{
    		System.out.println("Generating metadata for " + flvFile.getName());
    		
    		FLVReader reader = new FLVReader(flvFile, true);
    		FileKeyFrameMetaCache cache = new FileKeyFrameMetaCache();
    		cache.saveKeyFrameMeta(flvFile, reader.analyzeKeyFrames());
    		
    		System.out.println("Generated metadata for " + 
					flvFile.getName() + " in " + flvFile.getAbsolutePath());
		} else {
			System.out.println("Metadata already generated for " + flvFile.getName());
		}
	}

}
