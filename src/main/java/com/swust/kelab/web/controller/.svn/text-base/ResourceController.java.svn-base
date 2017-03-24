package com.swust.kelab.web.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.swust.kelab.domain.Resource;
import com.swust.kelab.domain.User;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.ResourceService;
import com.swust.kelab.service.VideoServerService;
import com.swust.kelab.utils.CheckCodeUtil;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.utils.FileCopyHandler;
import com.swust.kelab.utils.MD5;
import com.swust.kelab.utils.RemoteIpUtil;
import com.swust.kelab.utils.VideoUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * This Controller contains all course option by student and administrator.<br>
 * Include : students get video address,download document resource.<br>
 * administrator upload resource and create relation between resource and chapter.<br>
 * This Controller uses '/course/' as default namespace.<br>
 * Dependence on {@link com.swust.kelab.service.ResourceService}<br>
 * 
 * @version 1.0
 * @author Easonlian
 * @See com.swust.kelab.service.ResourceService
 */
@Controller()
@RequestMapping("/resource")
public class ResourceController {

	@Autowired
	private LogDBService logService;
	
	private ResourceService resourceService;
	
//	@Autowired
//	private VedioServerService vedioServerService;
	
	@Autowired
	private VideoServerService videoServerService;
	
	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	/**
	 * 查看最近资源列表
	 * @author EasonLian
	 */
	@RequestMapping(value="/viewRecentResourceList.do")
	public JsonAndView viewRecentResourceList() {
		return new JsonAndView().addAllData(resourceService.viewRecentResourceList());
	}
	
	/**
	 * 查询所有的未被分配的资源数据，包含 id,name,type
	 * to visit: /handler/resource/viewResourceListByChapterPage.do
	 * @param request
	 * @param chapId
	 * @param resoId
	 * @param resoName
	 * @param resoType
	 * @author EasonLian
	 */
	@RequestMapping(value="/viewResourceListByChapterPage.do",method=RequestMethod.GET)
	public JsonAndView viewResourceListByChapterPage(
			HttpServletRequest request,
			@RequestParam(value="courId",required=false) Integer courId,
			@RequestParam(value="chapId",required=false) Integer chapId,
			@RequestParam(value="resoId",required=false) Integer resoId,
			@RequestParam(value="resoName",required=false) String resoName,
			@RequestParam(value="resoType",required=false) Integer resoType) {
		JsonAndView jav = new JsonAndView();
		return jav.addAllData(
				resourceService.viewResourceListByChapterPage(
						resoId, resoName, resoType,chapId,courId));
	}
	
	/**
	 * 删除资源数据<br>
	 * @param ids 待删除的id
	 * @return 是否批量删除成功
	 * @author EasonLian
	 */
	@RequestMapping(value="/delResourceByAdmin.do")
	public JsonAndView delResourceByAdmin(
			HttpServletRequest request,
			@RequestParam(value="ids") String ids) {
		JsonAndView jav = new JsonAndView();
		if(resourceService.delResourceByAdmin(ids))
			jav.addData("status", 1);
		logService.insertNewLog(
				request, LogDBService.DELETE_OPERATION, "资源管理", "删除资源");
		return jav;
	}
	
	/**
	 * 待添加的Resource对象--链接<br>
	 * to visit: /handler/resource/addLinkResourceByAdmin.do
	 * @author EasonLian
	 * @param resource 封装的资源对象
	 * @param uploadFile 上传的文件
	 * @param request 请求对象
	 * @return status状态
	 */
	@RequestMapping(value="/addLinkResourceByAdmin.do",method=RequestMethod.POST)
	public JsonAndView addLinkResourceByAdmin(
				HttpServletRequest request,
				@RequestParam(value="resoId",required=false) Integer resoId,
				@RequestParam(value="resoType") Integer resoType,
				@RequestParam(value="resoCourId") Integer resoCourId,
				@RequestParam(value="resoTitle") String resoTitle,
				@RequestParam(value="resoDescribe",required=false) String resoDescribe,
				@RequestParam(value="resoLocation",required=false) String resoLinkAddr) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		Resource reso = new Resource();
		try {
			if(resoType == 1) {
				//	当上传为链接时候，直接保存
				reso.setResoId(resoId);
				reso.setResoLocation(resoLinkAddr);
				/** set params start */
				reso.setResoCourId(resoCourId);						//	3. resoCourId
				reso.setResoType(resoType);							//	4. resoType
				reso.setResoTitle(resoTitle);						//	5. resoTitle
				reso.setResoUserid(user.getUserId());				//	6. userId
				reso.setResoDescribe(resoDescribe);					//	7. resoDescribe
				reso.setResoDownnum(0);								//	9. resoDownnum (default=0)
				reso.setResoStatus(1);
				/** set params end */
				if(resourceService.addResourceByAdmin(reso) != null)
					jav.addData("status", 1);
				else
					jav.addData("status",0);
				
			} else
				jav.addData("status",0);
		} catch(Exception e) {
//			e.printStackTrace();
			System.out.println("添加链接资源错误！！！");
		} finally {
			logService.insertNewLog(
					request, LogDBService.ADD_OPERATION, "链接资源管理", "添加链接资源");
		}
		return jav;
	}
	
	/**
	 * 待添加的Resource对象，包括文档和视频<br>
	 * to visit: /handler/resource/addResourceByAdmin.do
	 * @author EasonLian
	 * @param resource 封装的资源对象
	 * @param uploadFile 上传的文件
	 * @param request 请求对象
	 * @return status状态
	 */
	@RequestMapping(value="/addResourceByAdmin.do",method=RequestMethod.POST)
	public JsonAndView addResourceByAdmin(
				HttpServletRequest request,
				@RequestParam(value="file",required=false) MultipartFile uploadFile,
				@RequestParam(value="resoType") Integer resoType,
				@RequestParam(value="resoCourId") Integer resoCourId,
				@RequestParam(value="resoTitle") String resoTitle,
				@RequestParam(value="resoDescribe",required=false) String resoDescribe) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		Resource reso = new Resource();
		try {
			//	判断上传附件是文档还是视频
			if(resoType == 2) {
				String path = request.getSession().getServletContext().getRealPath("");
				String endWith = null;
				String[] str =  uploadFile.getOriginalFilename().split(",");
				if(str.length != 0)
					endWith = str[str.length-1];
				else
					endWith = ".doc";
				String localFilePath = "/upload/doc_" +
					MD5.getMD5Str(uploadFile.getOriginalFilename()+new Date()+user.getUserId())+endWith;
				File localFile = new File(path+localFilePath);
				//	拷贝文件到指定目录文件中
				FileUtils.copyInputStreamToFile(uploadFile.getInputStream(),localFile);
				reso.setResoLocation(localFilePath);			//	1. resoLocation
				reso.setResoFilename(							//	8. resoFilename
						uploadFile.getOriginalFilename());
				/** set params start */
				reso.setResoCourId(resoCourId);						//	3. resoCourId
				reso.setResoType(2);							//	4. resoType
				reso.setResoTitle(resoTitle);						//	5. resoTitle
				reso.setResoUserid(user.getUserId());				//	6. userId
				reso.setResoDescribe(resoDescribe);					//	7. resoDescribe
				reso.setResoDownnum(0);								//	9. resoDownnum (default=0)
				reso.setResoStatus(1);
				/** set params end */
				if(resourceService.addResourceByAdmin(reso) != null)
					jav.addData("status", 1);
				else
					jav.addData("status",0);
			} else if(resoType == 3) {
				/**
				 * 视频保存处理，另开线程处理，拷贝成功修改那条数据库
				 */
				String localFilePath = "/mnt/iteach/video_" +
					MD5.getMD5Str(uploadFile.getOriginalFilename()+new Date()+user.getUserId()) + ".flv";
				reso.setResoLocation(localFilePath);			//	1. resoLocation
				reso.setResoFilename(							//	8. resoFilename
						uploadFile.getOriginalFilename());
				//	如果上传的文件是视频，则记录视频的时长
				File tempFile = new File(
						request.getSession().getServletContext().
							getRealPath("/upload/temp_"+
								MD5.getMD5Str(uploadFile.getOriginalFilename()+new Date()+user.getUserId())+".Flv")); 
				uploadFile.transferTo(tempFile);
				//	验证视频格式是否正确
				int videoTime =  Integer.valueOf(
						VideoUtil.getVideoDuration(tempFile).intValue());
				if(!uploadFile.getOriginalFilename().toLowerCase().endsWith("flv")
						|| videoTime < 0) {
					jav.addData("status",-1);
					return jav;
				}
				reso.setResoVediotime(videoTime);					//	2. resoVediotimecd
				/** set params start */
				reso.setResoCourId(resoCourId);						//	3. resoCourId
				reso.setResoType(resoType);							//	4. resoType
				reso.setResoTitle(resoTitle);						//	5. resoTitle
				reso.setResoUserid(user.getUserId());				//	6. userId
				reso.setResoDescribe(resoDescribe);					//	7. resoDescribe
				reso.setResoDownnum(0);								//	9. resoDownnum (default=0)
				reso.setResoStatus(1);
				/** set params end */
				//	执行添加，并拷贝到视频服务器
				Integer resoId = resourceService.addResourceByAdmin(reso);
				if(resoId != null) {
					jav.addData("status", 1);
					//	另外线程拷贝文件到指定目录文件中
					FileCopyHandler fch = new FileCopyHandler();
					fch.setResoId(resoId);
					fch.setTempFile(tempFile);
					fch.setLocalFilePath(localFilePath);
					fch.setResoService(resourceService);
					fch.start();
				} else
					jav.addData("status",0);
			}
		} catch(Exception e) {
			System.out.println("添加资源错误！！！");
		} finally {
			String logTarget = null;
			if(resoType == 2)
				logTarget = "添加文档资源";
			else if(resoType == 3)
				logTarget = "添加视频资源";
			else
				logTarget = "-";
			logService.insertNewLog(
					request, LogDBService.ADD_OPERATION, "链接资源管理", logTarget);
		}
		return jav;
	}
	
	/**
	 * 通过资源id和类型查询单条资源<br>
	 * @param resoId
	 * @param resoType
	 * @return
	 */
	@RequestMapping(value="/viewOneResourceByAdmin.do",method=RequestMethod.GET)
	public JsonAndView viewOneResourceByAdmin(
			@RequestParam(value="resoId") Integer resoId,
			@RequestParam(value="resoType") Integer resoType) {
		JsonAndView jav = new JsonAndView();
//	暂时无用
		return jav;
	}
	
	/**
	 * 修改Resource对象<br>
	 * to visit: /handler/resource/modResourceByAdmin.do
	 * @author EasonLian
	 * @param resource 封装的资源对象
	 * @param uploadFile 上传的文件
	 * @param request 请求对象
	 * @return status状态
	 */
	@RequestMapping(value="/modResourceByAdmin.do",method=RequestMethod.POST)
	public JsonAndView modResourceByAdmin(
				HttpServletRequest request,
				@RequestParam(value="resoId") Integer resoId,
				@RequestParam(value="file",required=false) MultipartFile uploadFile,
				@RequestParam(value="resoType") Integer resoType,
				@RequestParam(value="resoCourId") Integer resoCourId,
				@RequestParam(value="resoTitle",required=false) String resoTitle,
				@RequestParam(value="resoDescribe",required=false) String resoDescribe,
				@RequestParam(value="resoLocation",required=false) String resoLocation) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		
		Resource reso = new Resource();
		reso.setResoId(resoId);
		try {
			//	判断上传附件是文档还是视频
			if(resoType == 2) {
				String path = request.getSession().getServletContext().getRealPath("");
				String endWith = null;
				String[] str =  uploadFile.getOriginalFilename().split(",");
				if(str.length != 0)
					endWith = str[str.length-1];
				else
					endWith = ".doc";
				String localFilePath = "/upload/doc_" +
					MD5.getMD5Str(uploadFile.getOriginalFilename()+new Date()+user.getUserId())+endWith;
				File localFile = new File(path+localFilePath);
				//	拷贝文件到指定目录文件中
				FileUtils.copyInputStreamToFile(uploadFile.getInputStream(),localFile);
				reso.setResoLocation(localFilePath);			//	1. resoLocation
				reso.setResoFilename(							//	8. resoFilename
						uploadFile.getOriginalFilename());
				/** set params start */
				reso.setResoCourId(resoCourId);						//	3. resoCourId
				reso.setResoType(2);							//	4. resoType
				reso.setResoTitle(resoTitle);						//	5. resoTitle
				reso.setResoUserid(user.getUserId());				//	6. userId
				reso.setResoDescribe(resoDescribe);					//	7. resoDescribe
				reso.setResoDownnum(0);								//	9. resoDownnum (default=0)
				reso.setResoStatus(1);
				/** set params end */
				if(resourceService.modifyResourceByAdmin(reso,
						request.getSession().getServletContext().getRealPath("/")) != null)
					jav.addData("status", 1);
				else
					jav.addData("status",0);
			} else if(resoType == 3) {
				/**
				 * 视频保存处理，另开线程处理，拷贝成功修改那条数据库
				 */
				File tempFile = null;
				String localFilePath = null;
				if(uploadFile != null
						&& uploadFile.getInputStream() != null) {
					localFilePath = "/mnt/iteach/video_" +
						MD5.getMD5Str(uploadFile.getOriginalFilename()+new Date()+user.getUserId()) + ".flv";
					reso.setResoLocation(localFilePath);			//	1. resoLocation
					reso.setResoFilename(							//	8. resoFilename
							uploadFile.getOriginalFilename());
					//	如果上传的文件是视频，则记录视频的时长
					tempFile = new File(
							request.getSession().getServletContext().
								getRealPath("/upload/temp_"+
										MD5.getMD5Str(uploadFile.getOriginalFilename()+new Date()+user.getUserId())+".Flv")); 
					uploadFile.transferTo(tempFile);
					//	验证视频格式是否正确
					int videoTime =  Integer.valueOf(
							VideoUtil.getVideoDuration(tempFile).intValue());
					if(!uploadFile.getOriginalFilename().toLowerCase().endsWith("flv")
							|| videoTime < 0) {
						jav.addData("status",-1);
						return jav;
					}
					reso.setResoVediotime(videoTime);					//	2. resoVediotimecd
				}
				/** set params start */
				reso.setResoCourId(resoCourId);						//	3. resoCourId
				reso.setResoType(resoType);							//	4. resoType
				reso.setResoTitle(resoTitle);						//	5. resoTitle
				reso.setResoUserid(user.getUserId());				//	6. userId
				reso.setResoDescribe(resoDescribe);					//	7. resoDescribe
				reso.setResoDownnum(0);								//	9. resoDownnum (default=0)
				reso.setResoStatus(1);
				//	修改函数
				String oldPath =resourceService.modifyResourceByAdmin(reso, 
						request.getSession().getServletContext().getRealPath("/"));
				/** set params end */
				//	执行修改，并拷贝到视频服务器
				if(uploadFile != null
						&& uploadFile.getInputStream() != null) {
					if(oldPath != null) {
						jav.addData("status", 1);
						//	另外线程拷贝文件到指定目录文件中
						FileCopyHandler fch = new FileCopyHandler();
						fch.setDeletePath(oldPath);
						fch.setResoId(resoId);
						fch.setTempFile(tempFile);
						fch.setLocalFilePath(localFilePath);
						fch.setResoService(resourceService);
						fch.start();
					} else
						jav.addData("status",0);
				}
			}
//		
//		try {
//			//	判断是否有文件上传
//			if(resoType == 2 
//					|| resoType == 3) {
//				String path = request.getSession().getServletContext().getRealPath("");
//				String localFilePath = "/upload/video_" +
//					MD5.getMD5Str(uploadFile.getOriginalFilename()+new Date()) +
//					"_"+uploadFile.getOriginalFilename();
//				File localFile = new File(path+localFilePath);
//				//	拷贝文件到指定目录文件中
//				FileUtils.copyInputStreamToFile(uploadFile.getInputStream(),localFile);
//				params.put("resoLocation", localFilePath);
//				params.put("resoLocation",uploadFile.getOriginalFilename());
//				//	如果上传的文件是视频，则记录视频的时长
//				if(resoType == 3) {
//					params.put("resoVedioTime",Integer.valueOf(
//							VideoUtil.getVideoDuration(localFile).intValue()));
//				}
//			} else {
//				params.put("resoLocation", resoLocation);
//			}
//			/** set params start */
//			params.put("resoCourId", resoCourId);
//			params.put("resoType", resoType);
//			params.put("resoTitle", resoTitle);
////			reso.setResoUserid(user.getUserId());				//	6. userId
//			params.put("resoDescribe", resoDescribe);
//			/** set params end */
//			if(resourceService.modifyResourceByAdmin(params))
//				jav.addData("status", 1);
//			else
//				jav.addData("status",0);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			String logTarget = null;
			if(resoType == 2)
				logTarget = "添加文档资源";
			else if(resoType == 3)
				logTarget = "添加视频资源";
			else
				logTarget = "-";
			logService.insertNewLog(
					request, LogDBService.UPDATA_OPERATION, "资源管理", logTarget);
		}
		return jav;
	}
	
	/**
	 * 修改Resource对象<br>
	 * to visit: /handler/resource/modResourceByAdminWithoutFile.do
	 * @author EasonLian
	 * @param resource 封装的资源对象
	 * @param uploadFile 上传的文件
	 * @param request 请求对象
	 * @return status状态
	 */
	@RequestMapping(value="/modResourceByAdminWithoutFile.do",method=RequestMethod.POST)
	public JsonAndView modResourceByAdminWithoutFile(
				HttpServletRequest request,
				@RequestParam(value="resoId") Integer resoId,
				@RequestParam(value="resoType") Integer resoType,
				@RequestParam(value="resoCourId") Integer resoCourId,
				@RequestParam(value="resoTitle",required=false) String resoTitle,
				@RequestParam(value="resoDescribe",required=false) String resoDescribe) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		
		Resource reso = new Resource();
		reso.setResoId(resoId);
		try {
			//	判断上传附件是文档还是视频
			reso.setResoLocation(null);
			reso.setResoUserid(user.getUserId());
			if(resoType == 2) {
				/** set params start */
				reso.setResoCourId(resoCourId);	
				reso.setResoTitle(resoTitle);						//	5. resoTitle//	3. resoCourId
				reso.setResoUserid(user.getUserId());				//	6. userId
				reso.setResoDescribe(resoDescribe);					//	7. resoDescribe
				reso.setResoDownnum(0);								//	9. resoDownnum (default=0)
				reso.setResoStatus(1);
				/** set params end */
				if(resourceService.modifyResourceByAdmin(reso,
						request.getSession().getServletContext().getRealPath("/")) != null)
					jav.addData("status", 1);
				else
					jav.addData("status",0);
			} else if(resoType == 3) {
				/** set params start */
				reso.setResoCourId(resoCourId);						//	3. resoCourId
				reso.setResoTitle(resoTitle);						//	5. resoTitle
				reso.setResoUserid(user.getUserId());				//	6. userId
				reso.setResoDescribe(resoDescribe);					//	7. resoDescribe
				reso.setResoDownnum(0);								//	9. resoDownnum (default=0)
				reso.setResoStatus(1);
				//	修改函数
				/** set params end */
				if(resourceService.modifyResourceByAdmin(reso,
						request.getSession().getServletContext().getRealPath("/")) != null)
					jav.addData("status", 1);
				else
					jav.addData("status",0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			String logTarget = null;
			if(resoType == 2)
				logTarget = "添加文档资源";
			else if(resoType == 3)
				logTarget = "添加视频资源";
			else
				logTarget = "-";
			logService.insertNewLog(
					request, LogDBService.UPDATA_OPERATION, "资源管理", logTarget);
		}
		return jav;
	}

	/**
	 * admin页面的资源管理页面基本列表查询，<br>
	 * 以及模糊查询<br>
	 * to visit: /handler/resource/viewResourceListForAdmin.do
	 * @author EasonLian
	 * @param page
	 * @param rows
	 * @param resoName
	 * @param resoType
	 * @return 资源对象数组
	 */
	@RequestMapping(value="/viewResourceListForAdmin.do")
	public JsonAndView viewResourceListForAdmin(
			HttpServletRequest request,
			@RequestParam(value="pageArray") String pages,
			@RequestParam(value="recordPerPage") int rows,
			@RequestParam(value="courId",required=false) Integer courId,
			@RequestParam(value="resoTitle",required=false) String resoTitle,
			@RequestParam(value="resoType",required=false) Integer resoType,
			@RequestParam(value="courName",required=false) String courName,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam(value="keyWord",required=false) String keyWord) {
		JsonAndView jav = new JsonAndView();
		User user = CookieUtil.getCookieUser(request);
		jav.addAllData(resourceService.
				viewResourceListForAdmin(user,
										 courId,
										 resoTitle, 
										 resoType, 
										 courName, 
										 startTime,
										 endTime,
										 keyWord,
										 pages, 
										 rows));
		return jav;
	}
	
	/**
	 * 下载选中的文件，包括视频和文档<br>
	 * to visit : /handler/resource/downloadDocumentResource.do<br>
	 * @author EasonLian
	 * @param HttpServletResponse
	 * @param rid 资源的id
	 */
	@RequestMapping(value="/downloadDocumentResource.do",method=RequestMethod.GET)
	public void downloadDocumnetResource(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("rid") Integer rid,
			@RequestParam(value="rtype") Integer resoType) {
		InputStream is = null;
		OutputStream os = null;
		try {
			Resource resource = resourceService.downloadDocument(rid,resoType);
			//	当资源文件存在是
			if(resource != null) {
				//	保证资源能正常下载
				if(resource.getResoLocation() != null
						&& resource.getResoFilename() != null) {
					String realFileLocation = null;
					File localFile = null;
					if(resoType == 2) {
						realFileLocation = request.getSession().getServletContext().getRealPath(resource.getResoLocation());
					} else if(resoType == 3) {
						realFileLocation = resource.getResoLocation();
					}
					localFile = new File(realFileLocation);
System.out.println("**********************************************\nlocation:"+ realFileLocation);
					is = new FileInputStream(localFile);
					//	设置response头信息，告诉浏览器是文件下载
					response.addHeader("Content-Disposition", "attachment;filename=\"" 
							+ new String(resource.getResoFilename().getBytes("utf-8"),"ISO-8859-1")+"\"");
		            response.addHeader("Content-Length", "" + localFile.length());
		            os = new BufferedOutputStream(response.getOutputStream());
		            response.setContentType("application/octet-stream");
		            byte[] buffer = new byte[1024*5];
		            int len = -1;
		            while((len = is.read(buffer)) != -1) {
		            	os.write(buffer, 0, len);
		            }
		            os.flush();
				}
			}
		} catch (IOException e) {
			System.out.println("download File error!");
			try {
				PrintWriter pw = response.getWriter();
				pw.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN" +
						"http://www.w3.org/TR/html4/loose.dtd\"><html>" +
						"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
				pw.write("<script type='text/javascript'> alert('资源下载失败！');</script>");
				pw.write("</head></html>");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(os != null)
						os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			logService.insertNewLog(
					request, LogDBService.SELECT_OPERATION, "文档、视频资源管理", "下载视频或者文档");
		}
	}
	
	/**
	 * find all the document resource of a course chapter.<br>
	 * to visit	: /handler/resource/viewResourceListByChapter.do<br>
	 * @see com.swust.kelab.service.ResourceService
	 * @author EasonLian
	 * @param javax.servlet.http.HttpServletRequest
	 * @param page 页码
	 * @param rows 每页行数
	 */
	@RequestMapping(value="/viewResourceListByChapter.do",method=RequestMethod.GET)
	public JsonAndView viewResourceListByChapter(HttpServletRequest request,
									  @RequestParam(value="page",required=false) int page,
									  @RequestParam(value="rows",required=false) int rows,
									  @RequestParam("chapId") Integer chapId) {
		return new JsonAndView().addAllData(
				resourceService.viewResourceList(null, null, null, null, chapId, -1, -1));
	}
	
	/**
	 * 查询视频地址<br>
	 * 访问路径：/handler/resource/resource/viewVideoAddress.do<br>
	 * @author EasonLian
	 * @param rid
	 * @return 视频播放地址
	 */
	@RequestMapping(value="/viewVideoAddress.do")
	public JsonAndView viewVideoAddress(
				@RequestParam("rid") Integer rid,
				@RequestParam("courId") Integer courId,
				HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
System.out.println("**********************************************************\n"+RemoteIpUtil.getRemoteIpAddr(request));
		try {
			//	查询的视频对象
			Resource video = resourceService.viewResourceByResoId(rid);
			//	此处是通过学校名匹配，传“”匹配全部，匹配学校的逻辑暂时未定
			String ip=RemoteIpUtil.getRemoteIpAddr(request);
			jav.addAllData(videoServerService.selectByOrder(ip));
			String[] videoRelPath = video.getResoLocation().split("/");
			jav.addData("file",videoRelPath[videoRelPath.length-1]);
			jav.addData("time", video.getResoVediotime());
			jav.addData("describe",video.getResoDescribe());
			//	查询课程封面作为视频封面
			jav.addData("image", "../.." + resourceService.getCourseCoverImgById(
					request.getSession().getServletContext().getRealPath("/"),courId));
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
		return jav;
	}
	
	/**
	 * 查询视频地址<br>
	 * 访问路径：/handler/resource/resource/viewVideoAddressForAdjustment.do<br>
	 * @author EasonLian
	 * @param rid
	 * @return 视频播放地址
	 */
	@RequestMapping(value="/viewVideoAddressForAdjustment.do")
	public JsonAndView viewVideoAddress(HttpServletRequest request) {
		JsonAndView jav = new JsonAndView();
System.out.println("********************************------------------------\n"+RemoteIpUtil.getRemoteIpAddr(request));
		try {
			//	此处是通过学校名匹配，传“”匹配全部，匹配学校的逻辑暂时未定
			String ip=RemoteIpUtil.getRemoteIpAddr(request);
			jav.addAllData(videoServerService.selectByOrder(ip));
			jav.addData("localIp", ip);
			//	查询课程封面作为视频封面
		} catch(Exception e) {
			System.out.println("失败！");
		}
		return jav;
	}
	
	/**
	 * 
	 * @author ZhangXin
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value="/findlist",method=RequestMethod.POST)
	public JsonAndView findResourceList(int courseId) {
		JsonAndView jav = new JsonAndView();
		jav.addData("data", resourceService.findResourceList(courseId));
		return jav;
	}
	
	/**
	 * 视频播放过程中发送验证码
	 * to visit: /handler/resource/getVideoConfirmCode.do
	 * @author Easonlian
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value="/getVideoConfirmCode.do")
	public void getVideoConfirmCode(
			HttpServletRequest request,
			HttpServletResponse response) {
        try {
        	//	设置页面不缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            Object[] objs = CheckCodeUtil.makeCheckCodeImg();
            String code = objs[0].toString();
            BufferedImage img = (BufferedImage) objs[1];
            request.getSession().setAttribute("videoCheckCode", code);
			ImageIO.write(img, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证视频过程中输入的验证码
	 * to visit: /handler/resource/checkVideoCode.do
	 * @author easonlian
	 */
	@RequestMapping(value="/checkVideoCode.do",method=RequestMethod.POST)
	public JsonAndView checkVideoCode(
			HttpServletRequest request,
			@RequestParam(value="inputCode") String inputCode) {
		JsonAndView jav = new JsonAndView();
		try {
			String serverCode = 
					request.getSession().getAttribute("videoCheckCode").toString();
			if(serverCode != null
					&& serverCode.equals(inputCode))
				jav.addData("status", 1);
			else
				jav.addData("status", 0);
		} catch(Exception e) {
			jav.addData("status", 0);
		}
		return jav;
	}
}
